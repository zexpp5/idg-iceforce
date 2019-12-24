
package com.cx.webrtc;

import android.opengl.EGLContext;
import android.util.Log;

import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.imsdk.chat.CXCallListener;
import com.chaoxiang.imsdk.chat.CXCallProcessor;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.superdata.im.constants.CxIMMessageDirect;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.manager.CxSocketManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.CameraEnumerationAndroid;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoSource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebRtcClient {
    /**
     * 视频实时类型
     */
    public static final int VIDEO_MEDIATYPE = 81;
    /**
     * 音频实时类型
     */
    public static final int AUDIO_MEDIATYPE = 82;

    private final static String TAG = WebRtcClient.class.getCanonicalName();
    private final static int MAX_PEER = 2;
    private final CXCallListener callListener;
    private boolean[] endPoints = new boolean[MAX_PEER];
    private PeerConnectionFactory factory;
    private HashMap<String, Peer> peers = new HashMap<>();
    private LinkedList<PeerConnection.IceServer> iceServers = new LinkedList<>();
    private PeerConnectionParameters pcParams;
    private MediaConstraints pcConstraints = new MediaConstraints();
    private MediaStream localMS;
    private VideoSource videoSource;
    private RtcListener mListener;

    /**
     * 是否正在通话中
     */
    private boolean isCalling;

    /**
     * Implement this interface to be notified of events.
     */
    public interface RtcListener {
        /**
         * 准备好进行音视频通话
         */
        void onCallReady();

        void onDisAgreeConnected(String cause);

        /**
         * 无法连接对方
         */
        void onDisConnected(String callId);

        /**
         * 对方正在忙
         */
        void onBusy();

        /**
         * 挂断
         */
        void onHangUp();

        /**
         * 不在线
         */
        void onOffline();

        /**
         * 请求方取消了该次请求
         */
        void onUserExit();

        void onStatusChanged(String newStatus);

        void onLocalStream(MediaStream localStream);

        void onAddRemoteStream(MediaStream remoteStream, int endPoint);

        void onRemoveRemoteStream(int endPoint);

        String getTime();
    }

    private interface Command {
        void execute(String peerId, JSONObject payload) throws JSONException;
    }

    private class CreateOfferCommand implements Command {
        public void execute(String peerId, JSONObject payload) throws JSONException {
            SDLogUtil.debug(TAG, "CreateOfferCommand");
            Peer peer = peers.get(peerId);
            peer.pc.createOffer(peer, pcConstraints);
        }
    }

    private class CreateAnswerCommand implements Command {
        public void execute(String peerId, JSONObject payload) throws JSONException {
            SDLogUtil.debug(TAG, "CreateAnswerCommand");
            Peer peer = peers.get(peerId);
            SessionDescription sdp = new SessionDescription(
                    SessionDescription.Type.fromCanonicalForm(payload.getJSONObject("sdp")
                            .getString("type")),
                    payload.getJSONObject("sdp").getString("sdp")
                    );

            peer.pc.setRemoteDescription(peer, setRemoteDescription(sdp));
            peer.pc.createAnswer(peer, pcConstraints);
        }
    }

    private class SetRemoteSDPCommand implements Command {
        public void execute(String peerId, JSONObject payload) throws JSONException {
            SDLogUtil.debug(TAG, "SetRemoteSDPCommand");
            Peer peer = peers.get(peerId);
            SessionDescription sdp = new SessionDescription(
                    SessionDescription.Type.fromCanonicalForm(payload.getJSONObject("sdp")
                            .getString("type")),
                    payload.getJSONObject("sdp").getString("sdp")
                    );
            peer.pc.setRemoteDescription(peer, setRemoteDescription(sdp));
        }
    }

    private static final String VIDEO_CODEC_VP8 = "VP8";
    private static final String VIDEO_CODEC_VP9 = "VP9";
    private static final String VIDEO_CODEC_H264 = "H264";
    private static final String AUDIO_CODEC_OPUS = "opus";
    private static final String AUDIO_CODEC_ISAC = "ISAC";
    private static final String VIDEO_CODEC_PARAM_START_BITRATE =
            "x-google-start-bitrate";
    private static final String AUDIO_CODEC_PARAM_BITRATE = "maxaveragebitrate";

    public SessionDescription setRemoteDescription(final SessionDescription sdp) {
        String sdpDescription = sdp.description;
        sdpDescription = preferCodec(sdpDescription, VIDEO_CODEC_H264, false);
        if (pcParams.videoStartBitrate > 0) {
            sdpDescription = setStartBitrate(VIDEO_CODEC_VP8, true,
                    sdpDescription, pcParams.videoStartBitrate);
            sdpDescription = setStartBitrate(VIDEO_CODEC_VP9, true,
                    sdpDescription, pcParams.videoStartBitrate);
            sdpDescription = setStartBitrate(VIDEO_CODEC_H264, true,
                    sdpDescription, pcParams.videoStartBitrate);
        }
        if (pcParams.audioStartBitrate > 0) {
            sdpDescription = setStartBitrate(AUDIO_CODEC_OPUS, false,
                    sdpDescription, pcParams.audioStartBitrate);
        }
        Log.d(TAG, "Set remote SDP.");
        return new SessionDescription(
                sdp.type, sdpDescription);
    }

    private static String setStartBitrate(String codec, boolean isVideoCodec,
            String sdpDescription, int bitrateKbps) {
        String[] lines = sdpDescription.split("\r\n");
        int rtpmapLineIndex = -1;
        boolean sdpFormatUpdated = false;
        String codecRtpMap = null;
        // Search for codec rtpmap in format
        // a=rtpmap:<payload type> <encoding name>/<clock rate> [/<encoding
        // parameters>]
        String regex = "^a=rtpmap:(\\d+) " + codec + "(/\\d+)+[\r]?$";
        Pattern codecPattern = Pattern.compile(regex);
        for (int i = 0; i < lines.length; i++) {
            Matcher codecMatcher = codecPattern.matcher(lines[i]);
            if (codecMatcher.matches()) {
                codecRtpMap = codecMatcher.group(1);
                rtpmapLineIndex = i;
                break;
            }
        }
        if (codecRtpMap == null) {
            Log.w(TAG, "No rtpmap for " + codec + " codec");
            return sdpDescription;
        }
        Log.d(TAG, "Found " + codec + " rtpmap " + codecRtpMap
                + " at " + lines[rtpmapLineIndex]);

        // Check if a=fmtp string already exist in remote SDP for this codec and
        // update it with new bitrate parameter.
        regex = "^a=fmtp:" + codecRtpMap + " \\w+=\\d+.*[\r]?$";
        codecPattern = Pattern.compile(regex);
        for (int i = 0; i < lines.length; i++) {
            Matcher codecMatcher = codecPattern.matcher(lines[i]);
            if (codecMatcher.matches()) {
                Log.d(TAG, "Found " + codec + " " + lines[i]);
                if (isVideoCodec) {
                    lines[i] += "; " + VIDEO_CODEC_PARAM_START_BITRATE
                            + "=" + bitrateKbps;
                } else {
                    lines[i] += "; " + AUDIO_CODEC_PARAM_BITRATE
                            + "=" + (bitrateKbps * 1000);
                }
                Log.d(TAG, "Update remote SDP line: " + lines[i]);
                sdpFormatUpdated = true;
                break;
            }
        }

        StringBuilder newSdpDescription = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            newSdpDescription.append(lines[i]).append("\r\n");
            // Append new a=fmtp line if no such line exist for a codec.
            if (!sdpFormatUpdated && i == rtpmapLineIndex) {
                String bitrateSet;
                if (isVideoCodec) {
                    bitrateSet = "a=fmtp:" + codecRtpMap + " "
                            + VIDEO_CODEC_PARAM_START_BITRATE + "=" + bitrateKbps;
                } else {
                    bitrateSet = "a=fmtp:" + codecRtpMap + " "
                            + AUDIO_CODEC_PARAM_BITRATE + "=" + (bitrateKbps * 1000);
                }
                Log.d(TAG, "Add remote SDP line: " + bitrateSet);
                newSdpDescription.append(bitrateSet).append("\r\n");
            }

        }
        return newSdpDescription.toString();
    }

    private class AddIceCandidateCommand implements Command {
        public void execute(String peerId, JSONObject payload) throws JSONException {
            SDLogUtil.debug(TAG, "AddIceCandidateCommand");
            PeerConnection pc = peers.get(peerId).pc;
            if (pc.getRemoteDescription() != null) {
                IceCandidate candidate = new IceCandidate(
                        payload.getJSONObject("candidate").getString("sdpMid"),
                        payload.getJSONObject("candidate").getInt("sdpMLineIndex"),
                        payload.getJSONObject("candidate").getString("candidate")
                        );
                pc.addIceCandidate(candidate);
            }
        }
    }

    /**
     * Send a message through the signaling server
     *
     * @param to id of recipient
     * @param type type of message
     * @param payload payload of message
     * @throws JSONException
     */
    public void sendMessage(String to, String type, JSONObject payload) throws JSONException {
        JSONObject object = new JSONObject();

        JSONObject data = new JSONObject();
        if (type.equals("_ice_candidate") || type.equals("candidate")) {
            object.put("event", "_ice_candidate");
            data.put("candidate", payload);
        } else if (type.equals("_offer") || type.equals("offer")) {
            object.put("event", "_offer");
            data.put("sdp", payload);
        } else if (type.equals("_answer") || type.equals("answer")) {
            object.put("event", "_answer");
            data.put("sdp", payload);
        }
        object.put("data", data);

        CxSocketManager.getInstance().sendVoideOrAudioMsg(to, object.toString());
    }

    private class MessageHandler {
        private HashMap<String, Command> commandMap;

        private MessageHandler() {
            this.commandMap = new HashMap<>();
            commandMap.put("init", new CreateOfferCommand());
            commandMap.put("_offer", new CreateAnswerCommand());
            commandMap.put("_answer", new SetRemoteSDPCommand());
            commandMap.put("_ice_candidate", new AddIceCandidateCommand());
        }

        private void dealProcessor(String from, String msg, int headType) throws Exception {
            // if peer is unknown, try to add him
            SDLogUtil.debug("dealProcessor:" + msg);
            JSONObject data = new JSONObject(msg);

            String type = data.getString("event");
            JSONObject payload = null;
            if (!type.equals("init")) {
                payload = data.getJSONObject("data");
            }
            if (!peers.containsKey(from)) {
                // if MAX_PEER is reach, ignore the call
                int endPoint = findEndPoint();
                if (endPoint != MAX_PEER) {
                    Peer peer = addPeer(from, endPoint);
                    peer.pc.addStream(localMS);
                    commandMap.get(type).execute(from, payload);
                }
            } else {
                commandMap.get(type).execute(from, payload);
            }
        }
    }

    private static String preferCodec(
            String sdpDescription, String codec, boolean isAudio) {
        String[] lines = sdpDescription.split("\r\n");
        int mLineIndex = -1;
        String codecRtpMap = null;
        // a=rtpmap:<payload type> <encoding name>/<clock rate> [/<encoding
        // parameters>]
        String regex = "^a=rtpmap:(\\d+) " + codec + "(/\\d+)+[\r]?$";
        Pattern codecPattern = Pattern.compile(regex);
        String mediaDescription = "m=video ";
        if (isAudio) {
            mediaDescription = "m=audio ";
        }
        for (int i = 0; (i < lines.length)
                && (mLineIndex == -1 || codecRtpMap == null); i++) {
            if (lines[i].startsWith(mediaDescription)) {
                mLineIndex = i;
                continue;
            }
            Matcher codecMatcher = codecPattern.matcher(lines[i]);
            if (codecMatcher.matches()) {
                codecRtpMap = codecMatcher.group(1);
                continue;
            }
        }
        if (mLineIndex == -1) {
            Log.w(TAG, "No " + mediaDescription + " line, so can't prefer " + codec);
            return sdpDescription;
        }
        if (codecRtpMap == null) {
            Log.w(TAG, "No rtpmap for " + codec);
            return sdpDescription;
        }
        Log.d(TAG, "Found " + codec + " rtpmap " + codecRtpMap + ", prefer at "
                + lines[mLineIndex]);
        String[] origMLineParts = lines[mLineIndex].split(" ");
        if (origMLineParts.length > 3) {
            StringBuilder newMLine = new StringBuilder();
            int origPartIndex = 0;
            // Format is: m=<media> <port> <proto> <fmt> ...
            newMLine.append(origMLineParts[origPartIndex++]).append(" ");
            newMLine.append(origMLineParts[origPartIndex++]).append(" ");
            newMLine.append(origMLineParts[origPartIndex++]).append(" ");
            newMLine.append(codecRtpMap);
            for (; origPartIndex < origMLineParts.length; origPartIndex++) {
                if (!origMLineParts[origPartIndex].equals(codecRtpMap)) {
                    newMLine.append(" ").append(origMLineParts[origPartIndex]);
                }
            }
            lines[mLineIndex] = newMLine.toString();
            Log.d(TAG, "Change media description: " + lines[mLineIndex]);
        } else {
            Log.e(TAG, "Wrong SDP media description format: " + lines[mLineIndex]);
        }
        StringBuilder newSdpDescription = new StringBuilder();
        for (String line : lines) {
            newSdpDescription.append(line).append("\r\n");
        }
        return newSdpDescription.toString();
    }

    private class Peer implements SdpObserver, PeerConnection.Observer {
        private PeerConnection pc;
        private String id;
        private int endPoint;

        @Override
        public void onCreateSuccess(final SessionDescription origSdp) {
            // TODO: modify sdp to usesetLocalDescription pcParams prefered
            // codecs
            SDLogUtil.error("onCreateSuccess");
            try {
                SDLogUtil.debug("-->onCreateSuccess");
                String sdpDescription = origSdp.description;

                sdpDescription = preferCodec(sdpDescription, VIDEO_CODEC_H264, false);

                final SessionDescription sdp = new SessionDescription(
                        origSdp.type, sdpDescription);
                JSONObject payload = new JSONObject();
                payload.put("type", origSdp.type.canonicalForm());
                payload.put("sdp", sdpDescription);
                pc.setLocalDescription(Peer.this, sdp);
                sendMessage(id, origSdp.type.canonicalForm(), payload);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSetSuccess() {
            SDLogUtil.error("onSetSuccess");
        }

        @Override
        public void onCreateFailure(String s) {
            SDLogUtil.error("onCreateFailure");
        }

        @Override
        public void onSetFailure(String s) {
            SDLogUtil.error("onSetFailure");
        }

        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            SDLogUtil.error("onSignalingChange" + signalingState);
        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            SDLogUtil.error("onIceConnectionChange:" + iceConnectionState);
            if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED) {
                removePeer(id);
                mListener.onStatusChanged("DISCONNECTED");
            } else if (iceConnectionState == PeerConnection.IceConnectionState.FAILED) {
            }
        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {
            // TODO
            SDLogUtil.error("onIceConnectionReceivingChange");
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            SDLogUtil.error("onIceGatheringChange");
        }

        @Override
        public void onIceCandidate(final IceCandidate candidate) {
            SDLogUtil.error("onIceCandidate");
            try {
                JSONObject payload = new JSONObject();
                payload.put("sdpMLineIndex", candidate.sdpMLineIndex);
                payload.put("sdpMid", candidate.sdpMid);
                payload.put("candidate", candidate.sdp);
                sendMessage(id, "_ice_candidate", payload);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAddStream(MediaStream mediaStream) {
            SDLogUtil.error("onAddStream");
            SDLogUtil.debug(TAG, "onAddStream " + mediaStream.label());
            // remote streams are displayed from 1 to MAX_PEER (0 is
            // localStream)
            mListener.onAddRemoteStream(mediaStream, endPoint + 1);
        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {
            SDLogUtil.error("onRemoveStream");
            SDLogUtil.debug(TAG, "onRemoveStream " + mediaStream.label());
            removePeer(id);
        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {
            SDLogUtil.error("onDataChannel");
        }

        @Override
        public void onRenegotiationNeeded() {
            SDLogUtil.error("onRenegotiationNeeded");
        }

        public Peer(String id, int endPoint) {
            SDLogUtil.debug(TAG, "new Peer: " + id + " " + endPoint);
            PeerConnection.RTCConfiguration rtcConfig =
                    new PeerConnection.RTCConfiguration(iceServers);
            // TCP candidates are only useful when connecting to a server that
            // supports
            // ICE-TCP.
            // rtcConfig.tcpCandidatePolicy =
            // PeerConnection.TcpCandidatePolicy.DISABLED;
            // rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
            // rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
            // Use ECDSA encryption.
            rtcConfig.keyType = PeerConnection.KeyType.ECDSA;

            this.pc = factory.createPeerConnection(rtcConfig, pcConstraints, this);
            this.id = id;
            this.endPoint = endPoint;

            pc.addStream(localMS); // , new MediaConstraints()

            mListener.onStatusChanged("CONNECTING");
        }
    }

    private Peer addPeer(String id, int endPoint) {
        Peer peer = new Peer(id, endPoint);
        peers.put(id, peer);

        endPoints[endPoint] = true;
        return peer;
    }

    private void removePeer(String id) {
        Peer peer = peers.get(id);
        mListener.onRemoveRemoteStream(peer.endPoint);
        peer.pc.close();
        peers.remove(peer.id);
        endPoints[peer.endPoint] = false;
    }

    private MessageHandler handler;

    public WebRtcClient(RtcListener listener, PeerConnectionParameters params,
            EGLContext mEGLcontext) {
        mListener = listener;
        pcParams = params;
        PeerConnectionFactory.initializeFieldTrials(null);
        PeerConnectionFactory.initializeAndroidGlobals(listener, true, true,
                params.videoCodecHwAcceleration);
        factory = new PeerConnectionFactory();
        factory.setVideoHwAccelerationOptions(mEGLcontext);
        // iceServers.add(new
        // PeerConnection.IceServer("stun:stun.l.google.com:19302"));
        // iceServers.add(new PeerConnection.IceServer("stun:stunserver.org"));
        // iceServers.add(new
        // PeerConnection.IceServer("stun:stun.softjoys.com"));
        // iceServers.add(new
        // PeerConnection.IceServer("stun:stun.ideasip.com"));
        iceServers.add(new PeerConnection.IceServer("turn:imxl.myinjoy.cn:3478",
                "ling", "ling1234"));

        // iceServers.add(new
        // PeerConnection.IceServer("turn:104.155.231.43:3478?transport=udp","1456308076:84477854","eyAkrvi5ZC14hiiBzTL/rUExQqw="));
        // iceServers.add(new
        // PeerConnection.IceServer("turn:104.155.231.43:3478?transport=tcp","1456308076:84477854","eyAkrvi5ZC14hiiBzTL/rUExQqw="));
        // iceServers.add(new
        // PeerConnection.IceServer("turn:104.155.231.43:3479?transport=udp","1456308076:84477854","eyAkrvi5ZC14hiiBzTL/rUExQqw="));
        // iceServers.add(new
        // PeerConnection.IceServer("turn:104.155.231.43:3479?transport=tcp","1456308076:84477854","eyAkrvi5ZC14hiiBzTL/rUExQqw="));

        pcConstraints.mandatory
                .add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        if (pcParams.videoCallEnabled) {
            pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo",
                    "true"));
        } else {
            pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo",
                    "false"));
        }
        // pcConstraints.optional.add(new
        // MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
        handler = new MessageHandler();

        CXCallProcessor.getInstance().addGroupChangeListener(
                callListener = new CXCallListener.ICallListener() {

                    @Override
                    public void onAgree(CxMessage cxMessage) {
                        try {
                            JSONObject object = new JSONObject(cxMessage.getBody());
                            if (object.getString(CXCallProcessor.STATUS).equals(
                                    CXCallProcessor.AGREE)) {
                                mListener.onCallReady();
                                isCalling = true;
                            } else if (object.getString(CXCallProcessor.STATUS).equals(
                                    CXCallProcessor.DISAGREE)) {
                                String cause = object.getString("cause");
                                mListener.onDisAgreeConnected(cause);
                                saveMsg(cxMessage.getImMessage().getHeader().getFrom(),
                                        cxMessage.getBody(), CxIMMessageDirect.RECEIVER,
                                        System.currentTimeMillis());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onOffline(CxMessage cxMessage) {
                        saveMsg(cxMessage.getImMessage().getHeader().getTo(), cxMessage.getBody(),
                                CxIMMessageDirect.SEND, System.currentTimeMillis());
                        mListener.onOffline();
                    }

                    @Override
                    public void onHangUp(CxMessage cxMessage) {
                        try {
                            JSONObject object = new JSONObject(cxMessage.getBody());
                            if (isCalling) {
                                object.put("time", mListener.getTime());
                            }
                            saveMsg(cxMessage.getImMessage().getHeader().getFrom(),
                                    object.toString(), CxIMMessageDirect.SEND,
                                    System.currentTimeMillis());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mListener.onHangUp();
                    }

                    @Override
                    public void onBusy(CxMessage cxMessage) {
                        saveMsg(cxMessage.getImMessage().getHeader().getFrom(),
                                cxMessage.getBody(), CxIMMessageDirect.RECEIVER,
                                System.currentTimeMillis());
                        mListener.onBusy();
                    }

                    @Override
                    public void onUserExit(CxMessage cxMessage) {
                        saveMsg(cxMessage.getImMessage().getHeader().getFrom(),
                                cxMessage.getBody(), CxIMMessageDirect.RECEIVER,
                                System.currentTimeMillis());
                        mListener.onUserExit();
                    }

                    @Override
                    public void onWebRtcLink(String from, String msg, int headType) {
                        try {
                            handler.dealProcessor(from, msg, headType);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
    }

    public void offer(String from) {
        try {
            if (!peers.containsKey(from)) {
                // if MAX_PEER is reach, ignore the call
                int endPoint = findEndPoint();
                if (endPoint != MAX_PEER) {
                    Peer peer = addPeer(from, endPoint);
                    peer.pc.addStream(localMS);

                    handler.commandMap.get("init").execute(from, null);

                }
            } else {
                handler.commandMap.get("init").execute(from, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Call this method in Activity.onPause()
     */
    public void onPause() {
        if (videoSource != null)
            videoSource.stop();
    }

    /**
     * Call this method in Activity.onResume()
     */
    public void onResume() {
        if (videoSource != null)
            videoSource.restart();
    }

    /**
     * Call this method in Activity.onDestroy()
     */
    public void onDestroy() {
        for (Peer peer : peers.values()) {
            if (peer.pc != null) {
                peer.pc.close();
                peer.pc = null;
            }
        }
        if (videoSource != null) {
            videoSource.stop();
            videoSource = null;
        }
        if (callListener != null) {
            CXCallProcessor.getInstance().removeGroupChangeListener(callListener);
        }
    }

    private int findEndPoint() {
        for (int i = 0; i < MAX_PEER; i++) {
            if (!endPoints[i])
                return i;
        }
        return MAX_PEER;
    }

    /**
     * Start the client.
     * <p/>
     * Set up the local stream and notify the signaling server. Call this method
     * after onCallReady.
     */
    public void start() {
        setCamera();
    }

    private void setCamera() {
        localMS = factory.createLocalMediaStream("ARDAMS");
        if (pcParams.videoCallEnabled) {
            MediaConstraints videoConstraints = new MediaConstraints();
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxHeight", Integer
                    .toString(pcParams.videoHeight)));
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxWidth", Integer
                    .toString(pcParams.videoWidth)));
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxFrameRate",
                    Integer.toString(pcParams.videoFps)));
            videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("minFrameRate",
                    Integer.toString(pcParams.videoFps)));

            videoSource = factory.createVideoSource(getVideoCapturer(), videoConstraints);
            localMS.addTrack(factory.createVideoTrack("ARDAMSv0", videoSource));
        }

        AudioSource audioSource = factory.createAudioSource(new MediaConstraints());
        localMS.addTrack(factory.createAudioTrack("ARDAMSa0", audioSource));

        mListener.onLocalStream(localMS);
    }

    private VideoCapturer getVideoCapturer() {
        // String cameraDeviceName = CameraEnumerationAndroid.getDeviceName(0);
        String frontCameraDeviceName =
                CameraEnumerationAndroid.getNameOfFrontFacingDevice();
        // if (numberOfCameras > 1 && frontCameraDeviceName != null) {
        // cameraDeviceName = frontCameraDeviceName;
        // }
        return VideoCapturerAndroid.create(frontCameraDeviceName, null);
    }

    /**
     * 拒绝加入本次聊天
     */
    public void refuse(String to) {

        JSONObject object = new JSONObject();
        try {
            JSONObject b;
            if (pcParams.videoCallEnabled) {
                b = Xxxx(object, true);
            } else {
                b = Xxxx(object, false);
            }
            b.put(CXCallProcessor.STATUS, CXCallProcessor.DISAGREE);
            b.put("cause", "refuse");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CxSocketManager.getInstance().sendPushMsg(to, object.toString());
        saveMsg(to, object.toString(), CxIMMessageDirect.SEND, System.currentTimeMillis());
    }

    private static JSONObject Xxxx(JSONObject object, boolean isVideo) throws JSONException {
        // TODO 移除掉
        JSONObject b = new JSONObject();
        if (isVideo) {
            object.put(CXCallProcessor.VIDEO, b);
        } else {
            object.put(CXCallProcessor.AUDIO, b);
        }
        return b;
    }

    /**
     * 接受加入本次聊天
     */
    public void answer(String to) {
        JSONObject object = new JSONObject();
        try {
            JSONObject b;
            if (pcParams.videoCallEnabled) {
                b = Xxxx(object, true);
            } else {
                b = Xxxx(object, false);
            }
            b.put(CXCallProcessor.STATUS, CXCallProcessor.AGREE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CxSocketManager.getInstance().sendPushMsg(to, object.toString());
        mListener.onCallReady();
        isCalling = true;
    }

    /**
     * 发送连接请求
     * @param to
     */
    public void sendReq(String to) {
        JSONObject object = new JSONObject();
        try {
            JSONObject b;
            if (pcParams.videoCallEnabled) {
                b = Xxxx(object, true);
            } else {
                b = Xxxx(object, false);
            }
            b.put(CXCallProcessor.STATUS, CXCallProcessor.REQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CxSocketManager.getInstance().sendPushMsg(to, object.toString());
    }

    /**
     * 发送退出聊天的推送
     *
     * @param to
     */
    public void exit(String to) {
        JSONObject object = new JSONObject();
        try {
            JSONObject b;
            if (pcParams.videoCallEnabled) {
                b = Xxxx(object, true);
            } else {
                b = Xxxx(object, false);
            }

            b.put(CXCallProcessor.STATUS, CXCallProcessor.USER_EXIT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CxSocketManager.getInstance().sendPushMsg(to, object.toString());
        saveMsg(to, object.toString(), CxIMMessageDirect.SEND, System.currentTimeMillis());
    }

    /**
     * 发送正在忙的推送
     *
     * @param to
     */
    public static void sendBusy(String to, boolean isVideo) {
        JSONObject object = new JSONObject();
        int mediaType = 0;
        try {
            JSONObject b;
            if (isVideo) {
                mediaType = VIDEO_MEDIATYPE;
                b = Xxxx(object, true);
            } else {
                mediaType = AUDIO_MEDIATYPE;
                b = Xxxx(object, false);
            }

            b.put(CXCallProcessor.STATUS, CXCallProcessor.BUSY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CxSocketManager.getInstance().sendPushMsg(to, object.toString());
        saveMsg(to, object.toString(), mediaType, CxIMMessageDirect.SEND,
                System.currentTimeMillis());
    }

    /**
     * 发送挂断的推送
     *
     * @param to
     */
    public void handup(String to) {
        JSONObject object = new JSONObject();
        JSONObject b = null;
        try {
            if (pcParams.videoCallEnabled) {
                b = Xxxx(object, true);
            } else {
                b = Xxxx(object, false);
            }

            b.put(CXCallProcessor.STATUS, CXCallProcessor.HANGUP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CxSocketManager.getInstance().sendPushMsg(to, object.toString());

        if (isCalling) {
            try {
                b.put("time", mListener.getTime());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        saveMsg(to, object.toString(), CxIMMessageDirect.SEND, System.currentTimeMillis());
    }

    private static void saveMsg(String to, String object, int mediaType, int direct, long time) {
        String account = SPUtils.get(IMDaoManager.getInstance().getContext(),
                CxSPIMKey.STRING_ACCOUNT, "").toString();
        IMMessage message;
        if (direct == CxIMMessageDirect.RECEIVER) {
            message = CXChatManager.saveAudioOrVideoMsg(account, to, object.toString(), mediaType,
                    direct, time);
        } else {
            message = CXChatManager.saveAudioOrVideoMsg(to, account, object.toString(), mediaType,
                    direct, time);
        }
        IMConversaionManager.getInstance().saveConversation(to, message);
        CXCallProcessor.getInstance().dealNotice(message);
    }

    private void saveMsg(String to, String object, int direct, long time) {
        int mediaType;
        if (pcParams.videoCallEnabled) {
            mediaType = VIDEO_MEDIATYPE;
        } else {
            mediaType = AUDIO_MEDIATYPE;
        }
        saveMsg(to, object, mediaType, direct, time);
    }

}

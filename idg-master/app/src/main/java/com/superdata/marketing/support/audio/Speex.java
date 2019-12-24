
package com.superdata.marketing.support.audio;
import com.chaoxiang.base.utils.SDLogUtil;

public class Speex {

    /*
     * quality 1 : 4kbps (very noticeable artifacts, usually intelligible) 2 :
     * 6kbps (very noticeable artifacts, good intelligibility) 4 : 8kbps
     * (noticeable artifacts sometimes) 6 : 11kpbs (artifacts usually only
     * noticeable with headphones) 8 : 15kbps (artifacts not usually noticeable)
     */
    private static final int DEFAULT_COMPRESSION = 4;

    public Speex() {
    }

    public void init() {
        load();
        open(DEFAULT_COMPRESSION);
        SDLogUtil.debug("AEC INIT " + getAecStatus());
        // if (getAecStatus() == 0) {
        // initEcho(240, 1600);
        // }
        // log.info("AEC INIT " + getAecStatus());
        // if (getAecStatus() == 0) {
        // initEcho(320, 1600);
        // }
        SDLogUtil.debug("speex opened");
    }

    private void load() {
        try {
            System.loadLibrary("speex");
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public native int open(int compression);

    public native int getFrameSize();

    public native int decode(byte encoded[], short lin[], int size);

    public native int encode(short lin[], int offset, byte encoded[], int size);

    public native void close();

    public native void initEcho(int frameSize, int filterLength);

    public native void echoCancellation(short[] rec, short[] play, short[] out);

    public native int echoCancellationEncode(short[] rec, short[] play,
            byte[] encoded);

    public native void destroyEcho();

    public native int getAecStatus();
}

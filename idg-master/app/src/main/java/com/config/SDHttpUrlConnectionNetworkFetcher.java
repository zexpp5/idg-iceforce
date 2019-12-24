package com.config;

import android.content.Context;
import android.net.Uri;

import com.chaoxiang.base.utils.SDLogUtil;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.BaseNetworkFetcher;
import com.facebook.imagepipeline.producers.BaseProducerContextCallbacks;
import com.facebook.imagepipeline.producers.Consumer;
import com.facebook.imagepipeline.producers.FetchState;
import com.facebook.imagepipeline.producers.ProducerContext;
import com.utils.SPUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @descr 图片下载器
 */
public class SDHttpUrlConnectionNetworkFetcher extends BaseNetworkFetcher<FetchState>
{
    private static final int NUM_NETWORK_THREADS = 5;
    private final ExecutorService mExecutorService;
    private Context context;

    public SDHttpUrlConnectionNetworkFetcher(Context context)
    {
        mExecutorService = Executors.newFixedThreadPool(NUM_NETWORK_THREADS);
        this.context = context;
    }

    @Override
    public FetchState createFetchState(Consumer<EncodedImage> consumer, ProducerContext context)
    {
        return new FetchState(consumer, context);
    }

    @Override
    public void fetch(final FetchState fetchState, final Callback callback)
    {
        final String accessToken = getAccessToken();
        final Future<?> future = mExecutorService.submit(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        HttpURLConnection connection = null;
                        Uri uri = fetchState.getUri();
                        String scheme = uri.getScheme();
                        String uriString = fetchState.getUri().toString();
                        while (true)
                        {
                            String nextUriString;
                            String nextScheme;
                            InputStream is;
                            try
                            {
                                URL url = new URL(uriString);
                                SDLogUtil.debug("download image url ==>" + uriString);
                                SDLogUtil.debug("scheme===" + scheme);
                                if (scheme.equalsIgnoreCase("https"))
                                {
                                    trustAllHosts();
                                    HttpsURLConnection https = (HttpsURLConnection) url
                                            .openConnection();
                                    https.setHostnameVerifier(DO_NOT_VERIFY);
                                    connection = https;
                                } else
                                {
                                    connection = (HttpURLConnection) url.openConnection();
                                }
                                connection.setRequestProperty("token", accessToken);
                                SDLogUtil.debug("token==>" + accessToken);
                                nextUriString = connection.getHeaderField("Location");
                                nextScheme = (nextUriString == null) ? null : Uri.parse(nextUriString).getScheme();
                                if (nextUriString == null || nextScheme.equals(scheme))
                                {
                                    is = connection.getInputStream();
                                    callback.onResponse(is, -1);
                                    break;
                                }
                                uriString = nextUriString;
                                scheme = nextScheme;
                            } catch (Exception e)
                            {
                                callback.onFailure(e);
                                break;
                            } finally
                            {
                                if (connection != null)
                                {
                                    connection.disconnect();
                                }
                            }
                        }

                    }
                });
        fetchState.getContext().addCallbacks(
                new BaseProducerContextCallbacks()
                {
                    @Override
                    public void onCancellationRequested()
                    {
                        if (future.cancel(false))
                        {
                            callback.onCancellation();
                        }
                    }
                });
    }

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier()
    {
        @Override
        public boolean verify(String hostname, SSLSession session)
        {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts()
    {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()
        {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException
            {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException
            {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return new java.security.cert.X509Certificate[]{};
            }
        }};

        // Install the all-trusting trust manager
        try
        {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String getAccessToken()
    {
        return SPUtils.get(context, SPUtils.ACCESS_TOKEN, "").toString();
    }
}

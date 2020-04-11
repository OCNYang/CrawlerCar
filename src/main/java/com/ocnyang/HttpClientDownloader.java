package com.ocnyang;

import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.downloader.HttpClientGenerator;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 解决部分SSL高于1.3报错
 */

public class HttpClientDownloader extends us.codecraft.webmagic.downloader.HttpClientDownloader {

    private HttpClientGenerator httpClientGenerator = new MyHttpClientGenerator();

    public static class MyHttpClientGenerator extends HttpClientGenerator {
        private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
            try {
                return new SSLConnectionSocketFactory(createIgnoreVerifySSL(), new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"},
                        null,
                        new DefaultHostnameVerifier()); // 优先绕过安全证书
            } catch (KeyManagementException e) {
                LoggerFactory.getLogger(getClass()).error("ssl connection fail", e);
            } catch (NoSuchAlgorithmException e) {
                LoggerFactory.getLogger(getClass()).error("ssl connection fail", e);
            }
            return SSLConnectionSocketFactory.getSocketFactory();
        }

        private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

            };

            SSLContext sc = SSLContext.getInstance("SSLv3");
            sc.init(null, new TrustManager[]{trustManager}, null);
            return sc;
        }
    }
}

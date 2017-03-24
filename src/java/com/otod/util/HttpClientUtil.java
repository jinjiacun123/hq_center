/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.util;

import java.net.URI;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Administrator
 */
public class HttpClientUtil {

    public static byte[] createHttpRequest(String url, HashMap header) {

        HttpGet httpget = new HttpGet(url);

        byte[] bytes = null;
        Iterator it = header.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, String> entryset = (Entry<String, String>) it.next();
            String key = (String) entryset.getKey();
            String value = (String) entryset.getValue();

            httpget.setHeader(key, value);
        }

        //System.out.println(" - about to get something from " + httpget.getURI());
        HttpClient httpClient = new DefaultHttpClient();
        if (url.indexOf("https") >= 0 || url.indexOf("HTTPS") >= 0) {
            enableSSL(httpClient);
        }

        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5 * 1000);
        // 或者
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8 * 1000);

        try {
            HttpResponse response = httpClient.execute(httpget);
            //System.out.println(" - get executed");
            // get the response body as an array of bytes
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                bytes = EntityUtils.toByteArray(entity);
                //String str = new String(bytes);
                // System.out.println(str);
                //System.out.println(" - " + bytes.length + " bytes read");
            }

            entity = null;
            response = null;
            httpClient = null;
            httpget = null;
            url = null;
            header = null;
        } catch (Exception e) {
            httpget.abort();
            //System.out.println(" - error: " + e);
        }

        return bytes;
    }

    public static byte[] createHttpPostRequest(String url, HashMap header) {

        HttpPost httppost = new HttpPost(url);

        byte[] bytes = null;
        Iterator it = header.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, String> entryset = (Entry<String, String>) it.next();
            String key = (String) entryset.getKey();
            String value = (String) entryset.getValue();

            httppost.setHeader(key, value);
        }

        //System.out.println(" - about to get something from " + httpget.getURI());
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5 * 1000);
        // 或者
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8 * 1000);

        try {
            HttpResponse response = httpClient.execute(httppost);
            //System.out.println(" - get executed");
            // get the response body as an array of bytes
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                bytes = EntityUtils.toByteArray(entity);
                //String str = new String(bytes);
                // System.out.println(str);
                //System.out.println(" - " + bytes.length + " bytes read");
            }

            entity = null;
            response = null;
            httpClient = null;
            httppost = null;
            url = null;
            header = null;
        } catch (Exception e) {
            httppost.abort();
            //System.out.println(" - error: " + e);
        }

        return bytes;
    }

    public static byte[] createHttpPostDataRequest(String url, HashMap header, String postData) {

        HttpPost httppost = new HttpPost(url);

        byte[] bytes = null;
        Iterator it = header.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, String> entryset = (Entry<String, String>) it.next();
            String key = (String) entryset.getKey();
            String value = (String) entryset.getValue();

            httppost.setHeader(key, value);
        }

        //System.out.println(" - about to get something from " + httpget.getURI());
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5 * 1000);
        // 或者
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8 * 1000);
        try {
            httppost.setEntity(new StringEntity(postData));
            HttpResponse response = httpClient.execute(httppost);


            //System.out.println(" - get executed");
            // get the response body as an array of bytes
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                bytes = EntityUtils.toByteArray(entity);
                //String str = new String(bytes);
                // System.out.println(str);
                //System.out.println(" - " + bytes.length + " bytes read");
            }
            entity = null;
            response = null;
            httpClient = null;
            httppost = null;
            header = null;
            url = null;
        } catch (Exception e) {
            httppost.abort();
            //System.out.println(" - error: " + e);
        }

        return bytes;
    }

    public static byte[] createHttpRequestNotURISyntax(String strUrl, HashMap header) {
        HttpGet httpget = null;
        byte[] bytes = null;
        try {
            URL url = new URL(strUrl);
            URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
            httpget = new HttpGet(uri);


            Iterator it = header.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<String, String> entryset = (Entry<String, String>) it.next();
                String key = (String) entryset.getKey();
                String value = (String) entryset.getValue();

                httpget.setHeader(key, value);
            }

            //System.out.println(" - about to get something from " + httpget.getURI());
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5 * 1000);
            // 或者
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8 * 1000);
            HttpResponse response = httpClient.execute(httpget);
            //System.out.println(" - get executed");
            // get the response body as an array of bytes
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                bytes = EntityUtils.toByteArray(entity);
                //String str = new String(bytes);
                // System.out.println(str);
                //System.out.println(" - " + bytes.length + " bytes read");
            }
            entity = null;
            response = null;
            httpClient = null;
        } catch (Exception e) {
            httpget.abort();
            System.out.println(" - error: " + e);
        }

        return bytes;
    }

    private static void enableSSL(HttpClient httpclient) {
        //调用ssl    
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{TruseAllManager}, null);
            SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme https = new Scheme("https", sf, 443);
            httpclient.getConnectionManager().getSchemeRegistry().register(https);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 重写验证方法，取消检测ssl
     */
    private static TrustManager TruseAllManager = new X509TrustManager() {
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub    
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub    
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub    
            return null;
        }
    };
}

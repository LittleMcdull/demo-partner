/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 4:39 PM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** HTTP请求
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 4:39 PM
 */
@Slf4j
public class HttpUtils {

    /**
     * 创建没有证书的SSL链接工厂类
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws KeyManagementException
     */
    public static SSLConnectionSocketFactory getSSLConnectionSocketFactory() throws NoSuchAlgorithmException,
            KeyStoreException, KeyManagementException {
        SSLContextBuilder context = new SSLContextBuilder();
        context.loadTrustMaterial(null, (arg0, arg1) -> true);
        return new SSLConnectionSocketFactory(context.build());
    }

    /**
     * 链接GET请求
     * @param url
     * @return
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String connectGetUrl(String url) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException {
        log.info("=============GET_REQUEST_URL:" + url);
        SSLConnectionSocketFactory sslsf = getSSLConnectionSocketFactory();
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        RequestConfig config = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(5000).build();
        HttpGet httpGet = null;
        CloseableHttpResponse resp = null;
        String jsonString = "";
        try {
            httpGet =	new HttpGet(url);
            httpGet.setConfig(config);
            resp = client.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            jsonString = EntityUtils.toString(entity, "UTF-8");
        } finally {
            if (resp != null) {
                try {
                    resp.close();
                } catch (IOException e) {
                    log.info("关闭HTTPCLIENT输出流异常", e);
                }
            }
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return jsonString;
    }

    /**
     * 链接GET请求(带header)
     * @param url
     * @return
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String connectGetUrlWithHerder(String url,Map<String, String> headers) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException {
        log.info("=============GET_REQUEST_URL:" + url);
        SSLConnectionSocketFactory sslsf = getSSLConnectionSocketFactory();
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        RequestConfig config = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(5000).build();
        HttpGet httpGet = null;
        CloseableHttpResponse resp = null;
        String jsonString = "";
        try {
            httpGet =	new HttpGet(url);
            if (headers != null) {
                Set<String> keys = headers.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                    String key = (String) i.next();
                    httpGet.addHeader(key, headers.get(key));
                }
            }
            httpGet.setConfig(config);
            resp = client.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            jsonString = EntityUtils.toString(entity, "UTF-8");
        } finally {
            try {
                if (httpGet!=null) {
                    httpGet.abort();
                }
                if (resp != null) {
                    resp.close();
                }
                if(client!=null){
                    client.close();
                }
            } catch (Exception e) {
                log.info("关闭HTTPCLIENT异常", e);
            }
        }
        return jsonString;
    }

    /**
     * 链接POST请求xml
     * @param url
     * @param params
     * @return
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String postForm(String url,  List<NameValuePair> params) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException {
        log.info("=============POST_REQUEST_URL:" + url);
        log.info("=============POST_REQUEST_PARAMS:" + params);
        SSLConnectionSocketFactory sslsf = getSSLConnectionSocketFactory();
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        RequestConfig config = RequestConfig.custom().setSocketTimeout(40000).setConnectTimeout(10000).build();
        HttpPost httpPost = null;
        CloseableHttpResponse resp = null;
        httpPost = new HttpPost(url);
        try {
            httpPost.setConfig(config);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            resp = client.execute(httpPost);
            HttpEntity entity = resp.getEntity();
            String resultStr = EntityUtils.toString(entity, "UTF-8");
            log.info("=============POST_REQUEST_CODE:"+resp.getStatusLine().getStatusCode() +",RESULT:"+ resultStr+"");
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return resultStr;
            }
        }catch (IOException e) {
            if (e instanceof org.apache.http.conn.ConnectTimeoutException) {
                log.error("ConnectTimeoutExceptionnnnnnn:", e);
                throw new org.apache.http.conn.ConnectTimeoutException("connect timed out");
            }
            if (e instanceof java.net.SocketTimeoutException) {
                log.error("SocketTimeoutExceptionnnnnnn:", e);
                throw new java.net.SocketTimeoutException("Read timed out");
            }
            log.error("IOEXCptionnnnnn:", e);
            throw new IOException("IOException");
        }  finally {
            try {
                if (httpPost!=null) {
                    httpPost.abort();
                }
                if (resp != null) {
                    resp.close();
                }
                if(client!=null){
                    client.close();
                }
            } catch (Exception e) {
                log.info("关闭HTTPCLIENT异常", e);
            }
        }
        return null;
    }

    /**
     * 链接POST请求xml
     * @param url
     * @param xmlStr
     * @return
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String postXml(String url, String xmlStr) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException {
        log.info("=============POST_REQUEST_URL:" + url);
        log.info("=============POST_REQUEST_PARAMS:" + xmlStr);
        SSLConnectionSocketFactory sslsf = getSSLConnectionSocketFactory();
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        RequestConfig config = RequestConfig.custom().setSocketTimeout(40000).setConnectTimeout(10000).build();
        HttpPost httpPost = null;
        CloseableHttpResponse resp = null;
        httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "text/xml;charset=UTF-8");
        try {
            httpPost.setConfig(config);
            StringEntity params = new StringEntity(xmlStr,"UTF-8");
            httpPost.setEntity(params);
            resp = client.execute(httpPost);
            HttpEntity entity = resp.getEntity();
            String resultStr = EntityUtils.toString(entity, "UTF-8");
            log.info("=============POST_REQUEST_CODE:"+resp.getStatusLine().getStatusCode() +",RESULT:"+ resultStr+"");
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return resultStr;
            }
        }catch (IOException e) {
            if (e instanceof org.apache.http.conn.ConnectTimeoutException) {
                log.error("ConnectTimeoutExceptionnnnnnn:", e);
                throw new org.apache.http.conn.ConnectTimeoutException("connect timed out");
            }
            if (e instanceof java.net.SocketTimeoutException) {
                log.error("SocketTimeoutExceptionnnnnnn:", e);
                throw new java.net.SocketTimeoutException("Read timed out");
            }
            log.error("IOEXCptionnnnnn:", e);
            throw new IOException("IOException");
        }  finally {
            try {
                if (httpPost!=null) {
                    httpPost.abort();
                }
                if (resp != null) {
                    resp.close();
                }
                if(client!=null){
                    client.close();
                }
            } catch (Exception e) {
                log.info("关闭HTTPCLIENT异常", e);
            }
        }
        return null;
    }


    /**
     * 链接POST请求
     * @param url
     * @param request
     * @return
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static JSONObject connectPostUrl(String url, String request, Map<String, String> headers, int readTime) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException {
        log.info("=============POST_REQUEST_URL:" + url);
        log.info("=============POST_REQUEST_PARAMS:" + request);
        SSLConnectionSocketFactory sslsf = getSSLConnectionSocketFactory();
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        RequestConfig config = RequestConfig.custom().setSocketTimeout(readTime).setConnectTimeout(20000).build();
        HttpPost httpPost = null;
        CloseableHttpResponse resp = null;
        JSONObject jObj = new JSONObject();
        httpPost = new HttpPost(url);
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();
                httpPost.addHeader(key, headers.get(key));
            }
        }
        try {
            httpPost.setConfig(config);
            StringEntity params = new StringEntity(request,"UTF-8");
            params.setContentType("application/json");
            httpPost.setEntity(params);
            resp = client.execute(httpPost);
            HttpEntity entity = resp.getEntity();
            String jsonString = EntityUtils.toString(entity, "UTF-8");

            log.info("=============POST_REQUEST_CODE:"+resp.getStatusLine().getStatusCode() +",RESULT:"+ jsonString+"");
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                jObj = JSONObject.parseObject(jsonString);
            }
        }catch (IOException e) {
            if (e instanceof org.apache.http.conn.ConnectTimeoutException) {
                log.error("ConnectTimeoutExceptionnnnnnn:", e);
                throw new org.apache.http.conn.ConnectTimeoutException("connect timed out"+url);
            }
            if (e instanceof java.net.SocketTimeoutException) {
                log.error("SocketTimeoutExceptionnnnnnn:", e);
                throw new java.net.SocketTimeoutException("SocketTimeoutException Read timed out"+url);
            }
            log.error("IOEXCptionnnnnn:", e);
            throw new IOException("IOException");
        }  finally {
            try {
                if (httpPost!=null) {
                    httpPost.abort();
                }
                if (resp != null) {
                    resp.close();
                }
                if(client!=null){
                    client.close();
                }
            } catch (Exception e) {
                log.info("关闭HTTPCLIENT异常", e);
            }
        }
        return jObj;
    }

    /**
     * 链接POST请求 默认响应时间40s
     * @param url
     * @param request
     * @return
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static JSONObject connectPostUrl(String url, String request, Map<String, String> headers) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException {
        return connectPostUrl(url, request, headers, 40000);
    }

    /**
     * 链接POST请求
     * @param url
     * @param request
     * @return
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String connectPostUrlString(String url, String request, Map<String, String> headers) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException {
        log.info("=============POST_REQUEST_URL:" + url);
        log.info("=============POST_REQUEST_PARAMS:" + request);
        SSLConnectionSocketFactory sslsf = getSSLConnectionSocketFactory();
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        RequestConfig config = RequestConfig.custom().setSocketTimeout(40000).setConnectTimeout(20000).build();
        HttpPost httpPost = null;
        CloseableHttpResponse resp = null;
        String jObj = "";
        httpPost = new HttpPost(url);
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();
                httpPost.addHeader(key, headers.get(key));
            }
        }
        try {
            httpPost.setConfig(config);
            StringEntity params = new StringEntity(request,"UTF-8");
            params.setContentType("application/json");
            httpPost.setEntity(params);
            resp = client.execute(httpPost);
            HttpEntity entity = resp.getEntity();
            String jsonString = EntityUtils.toString(entity, "UTF-8");

            log.info("=============POST_REQUEST_CODE:"+resp.getStatusLine().getStatusCode() +",RESULT:"+ jsonString+"");
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                jObj = jsonString;
            }
        }catch (IOException e) {
            if (e instanceof org.apache.http.conn.ConnectTimeoutException) {
                throw new org.apache.http.conn.ConnectTimeoutException("connect timed out:"+url);
            }
            if (e instanceof java.net.SocketTimeoutException) {
                throw new java.net.SocketTimeoutException("Read timed out:"+url);
            }
            log.error("IOEXCptionnnnnn:", e);
            throw new IOException("IOException");
        }  finally {
            try {
                if (httpPost!=null) {
                    httpPost.abort();
                }
                if (resp != null) {
                    resp.close();
                }
                if(client!=null){
                    client.close();
                }
            } catch (Exception e) {
                log.info("关闭HTTPCLIENT异常", e);
            }
        }
        return jObj;
    }

    public static JSONObject connectPostUrl(String url, String request) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException, IOException {
        return connectPostUrl(url, request, null);
    }

    public static String connectPostUrlString(String url, String request) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException, IOException {
        return connectPostUrlString(url, request, null);
    }
    /**
     * 创建链接
     * @param url
     * @return
     * @throws Exception
     */
    public static Map<String,Object> getHttpEntity(String url) throws Exception {
        Map<String,Object> result=new HashMap<>();
        log.info("=============GET_REQUEST_URL:" + url);
        SSLConnectionSocketFactory sslsf = getSSLConnectionSocketFactory();
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        RequestConfig config = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(5000).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);
        CloseableHttpResponse resp = client.execute(httpGet);
        HttpEntity entity=resp.getEntity();
        result.put("entity",entity);
        result.put("resp",resp);
        result.put("httpGet",httpGet);

        return result;
    }

    /**
     * 获取post HttpEntity
     * @param url
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String,Object> postHttpEntity(String url, String request) throws Exception {
        log.info("=============POST_REQUEST_URL:" + url);
        log.debug("=============POST_REQUEST_PARAMS:" + request);
        Map<String,Object> result=new HashMap<>();
        SSLConnectionSocketFactory sslsf = getSSLConnectionSocketFactory();
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        RequestConfig config = RequestConfig.custom().setSocketTimeout(40000).setConnectTimeout(10000).build();
        HttpPost	httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        StringEntity params = new StringEntity(request,"UTF-8");
        params.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(params);
        CloseableHttpResponse resp = client.execute(httpPost);
        HttpEntity entity = resp.getEntity();

        result.put("entity",entity);
        result.put("resp",resp);
        result.put("httpPost",httpPost);

        return result;
    }

    /**
     * 关闭链接
     * @param map
     * @throws Exception
     */
    public static void closeConnection(Map<String,Object> map)throws Exception{
        Object resp = map.get("resp");
        Object httpGet = map.get("httpGet");
        try{
            if (resp!=null){
                CloseableHttpResponse closeableHttpResponse=(CloseableHttpResponse)resp;
                closeableHttpResponse.close();
            }
            if (httpGet != null) {
                HttpGet hget=(HttpGet)httpGet;
                hget.releaseConnection();
            }
        }catch (Exception e){
            log.error("关闭HTTPCLIENT输出流异常",e);
        }
    }
}

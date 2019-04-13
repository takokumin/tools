package cn.tgm.tools.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HttpUtils
 *
 * @author tianguomin
 * @version 1.0
 */
public class HttpUtils {

    /**
     * 日志输出对象
     */
    private static final Logger logger = LogManager.getLogger(HttpUtils.class.getName());

    /**
     * 以GET方式调用接口并返回结果。
     *
     * @param url 接口地址
     * @return 接口返回内容
     * @throws IOException 接口调用异常
     */
    public static String httpGet(String url) throws IOException {

        // 发送请求并接收应答
        return httpReuqest(new HttpGet(url));
    }

    /**
     * 以PUT方式调用接口并返回结果。
     *
     * @param url 接口地址
     * @return 接口返回内容
     * @throws IOException 接口调用异常
     */
    public static String httpPut(String url) throws IOException {

        // 发送请求并接收应答
        return httpReuqest(new HttpPut(url));
    }

    /**
     * 以DELETE方式调用接口并返回结果。
     *
     * @param url 接口地址
     * @return 接口返回内容
     * @throws IOException 接口调用异常
     */
    public static String httpDelete(String url) throws IOException {

        // 发送请求并接收应答
        return httpReuqest(new HttpDelete(url));
    }

    /**
     * 以POST方式调用接口并返回结果。
     *
     * @param url    接口地址
     * @param params 参数列表
     * @return 接口返回内容
     * @throws IOException 接口调用异常
     */
    public static String httpPost(String url, String params) throws IOException {

        return httpPostXml(url, params);
    }

    /**
     * 以POST方式调用接口并返回结果。
     *
     * @param url    接口地址
     * @param params 参数列表
     * @return 接口返回内容
     * @throws IOException 接口调用异常
     */
    public static String httpPost(String url, List<NameValuePair> params) throws IOException {

        return httpPost(url, params, "utf-8");
    }

    /**
     * 以POST方式调用接口并返回结果。
     *
     * @param url     接口地址
     * @param params  参数列表
     * @param charset 参数编码格式
     * @return 接口返回内容
     * @throws IOException 接口调用异常
     */
    public static String httpPost(String url, List<NameValuePair> params, String charset) throws IOException {

        HttpPost httpPost = new HttpPost(url);

        // 请求参数设定
        HttpEntity reqEntity = new UrlEncodedFormEntity(params, charset);
        httpPost.setEntity(reqEntity);

        // 发送请求并接收应答
        return httpReuqest(httpPost);
    }

    /**
     * 以POST方式调用接口并返回结果。
     *
     * @param url    接口地址
     * @param params 参数列表
     * @return 接口返回内容
     * @throws IOException 接口调用异常
     */
    public static String httpPostForm(String url, List<NameValuePair> params) throws IOException {

        HttpPost httpPost = new HttpPost(url);

        // 请求参数设定
        HttpEntity reqEntity = new UrlEncodedFormEntity(params, "utf-8");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(reqEntity);

        // 发送请求并接收应答
        return httpReuqest(httpPost);
    }

    /**
     * 以POST方式调用接口并返回结果。
     *
     * @param url    接口地址
     * @param params 参数列表
     * @return 接口返回内容
     * @throws IOException 接口调用异常
     */
    public static String httpPostXml(String url, String params) throws IOException {

        return httpPost(url, params, "application/xml");
    }

    /**
     * 以POST方式调用接口并返回结果。
     *
     * @param url    接口地址
     * @param params 参数列表
     * @return 接口返回内容
     * @throws IOException 接口调用异常
     */
    public static String httpPostJson(String url, String params) throws IOException {

        return httpPost(url, params, "application/json");
    }

    /**
     * 以POST方式调用接口并返回结果。
     *
     * @param url
     * @param params
     * @param contentType
     * @return
     * @throws IOException
     */
    public static String httpPost(String url, String params, String contentType) throws IOException {

        HttpPost httpPost = new HttpPost(url);

        // 请求参数设定
        StringEntity strEntity = new StringEntity(params);
        strEntity.setContentEncoding("utf-8");
        strEntity.setContentType(contentType);
        httpPost.setEntity(strEntity);

        // 发送请求并接收应答
        return httpReuqest(httpPost);
    }

    /*
     * 调用接口并返回结果。
     *
     * @param url 接口地址
     *
     * @return 接口返回内容
     *
     * @throws IOException 接口调用异常
     */
    private static String httpReuqest(HttpRequestBase httpReuqest) throws IOException {

        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(2000).setConnectTimeout(5000)
                .setSocketTimeout(5000).build();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        String result = "";

        try {
            logger.info("Executing request: " + httpReuqest.getURI());

            if (httpReuqest instanceof HttpPost) {
                InputStream in = ((HttpPost) httpReuqest).getEntity().getContent();
                byte[] bs = new byte[in.available()];
                in.read(bs);
                in.close();
                logger.info("----------params: " + new String(bs, "utf-8"));
            }

            // 发送请求并接收应答
            HttpResponse response = httpclient.execute(httpReuqest);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // 返回结果处理
                result = EntityUtils.toString(entity, "utf-8");
                logger.info("Response content: " + result);
            }
        } finally {
            httpclient.close();
        }

        return result;
    }

}

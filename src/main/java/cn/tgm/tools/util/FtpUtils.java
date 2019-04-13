package cn.tgm.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FtpUtils
 * 
 * @author tianguomin
 * @version 1.0
 */
public class FtpUtils {

    /** Logger */
    private static final Logger logger = LogManager.getLogger(FtpUtils.class.getName());

    private static Map<String, FTPClient> ftpsMap = new HashMap<String, FTPClient>();

    private static Map<String, String> ftpsEncodingMap = new HashMap<String, String>();

    public static final String HOST = "HOST";

    public static final String PORT = "PORT";

    public static final String USERNAME = "USERNAME";

    public static final String PASSWORD = "PASSWORD";

    public static final String PATH = "PATH";

    // FTP数据传输超时时间 1分钟
    public static final Integer FTP_DATA_TRANS_TIME_OUT = 6 * 60000;

    // FTP连接超时时间 1分钟
    public static final Integer FTP_CONNECTION_TIME_OUT = 6 * 60000;

    /**
     * 向FTP服务器上传文件。<br>
     * ftpParam格式如下<br>
     * HOST:ftp服务器主机地址<br>
     * PORT:ftp服务器端口<br>
     * USERNAME:ftp登录账号<br>
     * PASSWORD:ftp登录密码<br>
     * PATH:ftp存储根目录<br>
     * 
     * @param ftpParam
     *            ftp信息
     * @param filename
     *            上传到FTP服务器上的文件名
     * @param file
     *            本地文件(路径+名称)
     * @param dirName
     *            目录名称(如目录不存在，需创建)
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(Map<String, Object> ftpParam, String filename, String file, String dirName)
            throws IOException {

        FTPClient ftpClient = null;
        InputStream input = null;
        boolean success = false;

        try {
            ftpClient = connectServer(ftpParam);

            String path = valueOf(ftpParam.get(PATH));

            if (path != null && path.length() > 0) {
                path = path.replaceAll("\\\\", "/");
                if (path.startsWith("/"))
                    path = path.substring(1);
                ftpClient.makeDirectory(path);
                ftpClient.changeWorkingDirectory(path);
            }

            if (dirName != null && dirName.length() > 0) {
                ftpClient.makeDirectory(dirName);
                ftpClient.changeWorkingDirectory(dirName);
            }

            input = new FileInputStream(new File(file));

            logger.info("开始向[" + valueOf(ftpParam.get(HOST)) + "]上传文件[" + filename + "]...");

            // 文件上传
            boolean result = ftpClient.storeFile(filename, input);

            input.close();
            success = true;

            logger.info("向[" + valueOf(ftpParam.get(HOST)) + "]上传文件[" + filename + "]完毕, Result=" + result);

        } finally {

            try {
                if (input != null) {
                    input.close();
                }

                if (ftpClient != null) {
                    closeConnect(ftpClient);
                }

                logger.info("已断开与[" + valueOf(ftpParam.get(HOST)) + "]ftp服务器的连接。");
            } catch (Exception e) {

            }

        }
        return success;
    }

    /**
     * 连接到服务器(该函数使用以前，要new一个空的FTPClient，作为参数传递给改函数 )
     *
     * @return true 连接服务器成功，false 连接服务器失败
     */
    private static FTPClient connectServer(Map<String, Object> ftpParam) {

        String host = valueOf(ftpParam.get(HOST));
        Integer port = Integer.parseInt(valueOf(ftpParam.get(PORT)));
        String username = valueOf(ftpParam.get(USERNAME));
        String password = valueOf(ftpParam.get(PASSWORD));

        FTPClient ftpClient = ftpsMap.get(host);

        // if(ftpClient != null && ftpClient.isAvailable() &&
        // ftpClient.isConnected()){
        // return ftpClient;
        // }

        if (ftpClient != null) {
            closeConnect(ftpClient);
        }

        ftpClient = new FTPClient();
        int reply;

        try {
            // 编码格式设定
            if (ftpsEncodingMap.get(host) == null) {

                ftpClient.setDefaultPort(port);
                ftpClient.connect(host);

                if (ftpClient.login(username, password)) {

                    if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                        ftpsEncodingMap.put(host, "UTF-8");
                    } else {
                        ftpsEncodingMap.put(host, "GBK");
                    }
                }

                closeConnect(ftpClient);
                ftpClient = new FTPClient();

            }

            logger.info("正在连接ftp服务器[" + host + "]...");
            ftpClient.setControlEncoding(ftpsEncodingMap.get(host));
            ftpClient.setDefaultPort(port);
            ftpClient.connect(host);
            logger.info("正在登录ftp服务器[" + host + "]...");
            ftpClient.login(username, password);
            reply = ftpClient.getReplyCode();
            ftpClient.setDataTimeout(FTP_DATA_TRANS_TIME_OUT);
            ftpClient.setConnectTimeout(FTP_CONNECTION_TIME_OUT);

            if (!FTPReply.isPositiveCompletion(reply)) {

                ftpClient.disconnect();

                logger.error("ftp服务器[" + host + "]拒绝连接！");

                closeConnect(ftpClient);

            } else {

                logger.info("成功连接并登录ftp服务器[" + host + "]。");
                ftpsMap.put(host, ftpClient);

            }

            // FTPClient 编码格式 (上传用)
            String LOCAL_CHARSET = "GBK";
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
            }
            ftpClient.setControlEncoding(LOCAL_CHARSET);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

        } catch (Exception e) {
            closeConnect(ftpClient);
            logger.error("ftp服务器[" + host + "]登录失败！");
        }

        return ftpClient;
    }

    /**
     * 关闭FTP连接
     * 
     * @param ftpClient
     */
    private static void closeConnect(FTPClient ftpClient) {

        try {
            if (ftpClient != null && ftpClient.isConnected()) {

                ftpClient.logout();
                ftpClient.disconnect();

            }
        } catch (Exception e) {
            // do nothing
        } finally {
            ftpClient = null;
        }
    }

    private static String valueOf(Object obj) {

        if (obj == null)
            return "";

        return obj.toString();

    }

}

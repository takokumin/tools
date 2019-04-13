package cn.tgm.tools.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * ZipUtil
 * 
 * @author tianguomin
 * @version 1.0
 */
public class ZipUtils {

    /**
     * 压缩指定目录下的所有文件(包含子目录)。
     * 
     * @param baseDir
     *            压缩文件根目录
     * @param outFile
     *            压缩生成的文件名
     * @exception IOException
     *                文件处理异常
     */
    public static void zipFile(String baseDir, String outFile) throws IOException {

        // 压缩baseDir下所有文件，包括子目录
        List<File> fileList = getSubFiles(new File(baseDir));
        ZipOutputStream zos = null;
        InputStream is = null;

        try {
            zos = new ZipOutputStream(new FileOutputStream(outFile));
            ZipEntry ze = null;
            byte[] bytes = new byte[1024];
            int readLen = 0;

            for (File f : fileList) {
                // 创建一个ZipEntry，并设置Name和其它的一些属性
                ze = new ZipEntry(getAbsFileName(baseDir, f));
                ze.setSize(f.length());
                ze.setTime(f.lastModified());
                // 将ZipEntry加到zos中，再写入实际的文件内容
                zos.putNextEntry(ze);
                is = new BufferedInputStream(new FileInputStream(f));
                while ((readLen = is.read(bytes, 0, 1024)) != -1) {
                    zos.write(bytes, 0, readLen);
                }
            }
        } finally {
            try {
                if (is != null)
                    is.close();

                if (zos != null)
                    zos.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    /**
     * 解压缩指定的文件到当前目录。
     * 
     * @param file
     *            要解压缩的文件
     * 
     * @exception IOException
     *                文件处理异常
     */
    public static void upZipFile(String file) throws IOException {

        byte[] bytes = new byte[1024];
        int length;
        ZipFile zipFile = null;
        OutputStream os = null;
        InputStream is = null;

        try {
            File infile = new File(file);
            zipFile = new ZipFile(infile);
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            ZipEntry zipEntry = null;
            while (enumeration.hasMoreElements()) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                File loadFile = new File(infile.getName(), zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    loadFile.mkdirs();
                } else {
                    if (!loadFile.getParentFile().exists())
                        loadFile.getParentFile().mkdirs();
                    os = new FileOutputStream(loadFile);
                    is = zipFile.getInputStream(zipEntry);
                    while ((length = is.read(bytes)) > 0) {
                        os.write(bytes, 0, length);
                    }

                }
            }
        } finally {
            try {
                if (is != null)
                    is.close();

                if (os != null)
                    os.close();

                if (zipFile != null)
                    zipFile.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    /**
     * 给定根目录，返回另一个文件名的相对路径，用于zip文件中的路径.
     * 
     * @param baseDir
     *            java.lang.String 根目录
     * @param realFileName
     *            java.io.File 实际的文件名
     * @return 相对文件名
     */
    private static String getAbsFileName(String baseDir, File realFileName) {

        File real = realFileName;
        File base = new File(baseDir);
        String ret = real.getName();
        while (true) {
            real = real.getParentFile();
            if (real == null)
                break;
            if (real.equals(base))
                break;
            else {
                ret = real.getName() + "/" + ret;
            }
        }
        return ret;
    }

    /**
     * 取得指定目录下的所有文件列表，包括子目录.
     * 
     * @param baseDir
     *            File 指定的目录
     * @return 包含java.io.File的List
     */
    private static List<File> getSubFiles(File baseDir) {

        List<File> ret = new ArrayList<File>();
        for (File file : baseDir.listFiles()) {
            if (file.isDirectory()) {
                ret.addAll(getSubFiles(file));
            } else {
                ret.add(file);
            }
        }
        return ret;
    }
}

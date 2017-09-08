package com.imitationmafengwo.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.imitationmafengwo.MyApplication;
import com.imitationmafengwo.utils.log.L;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class FileUtil {
    private static final int CONNECT_TIME_OUT = 10 * 1000; // 超时时间
    private static final int READ_TIME_OUT = 30 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    private static final String defineRootName = "Android/data/com.mafengwo";
    private static final String rootName = "mafengwo";
    private static final String download = "download";
    private static final String cache = "cache";
    private static final String images = "images";
    private static final String txt = "txt";
    private static final String albums = "albums";
    public static final String albumsVip = "vipalbums";
    private static final String comis = "comis";
    private static final String tts = "tts";
    private static final String localbook = "localbook";
    private static final String webRoot = "WEB_ROOT";
    public static final String vipdirectory = "vip_id";
    public static final String vipfilename = "vip_tip";
    public static final String debugConfig = "log";

    private static final String snappyDB = "SnappyDB";

    private static FileUtil instance;

    private FileUtil() {
    }

    public synchronized static FileUtil shareInstance() {
        if (instance == null) {
            instance = new FileUtil();
        }
        return instance;
    }

    /**
     * 获取sd卡文件目录
     *
     * @return
     */
    public static File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    public static String getMD5(String content) {
        if (content != null && !"".equals(content)) {
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(content.getBytes());
                return getHashString(digest);

            } catch (NoSuchAlgorithmException e) {
            }
            return "";
        }
        return "";
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder("");
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

    public static boolean isExitSongFromPath(String songPath) {
        if (TextUtils.isEmpty(songPath)) return false;
        File file = new File(songPath);
        return file.exists() && file.isFile();
    }

    /**
     * 读的方法
     *
     * @param path 文件路径
     **/
    public static String readTxtFromFile(String path) {
        String result = "";
        StringBuffer allBuffer = new StringBuffer();
        try {
            //RandomAccessFile raf=new RandomAccessFile(new File("D:\\3\\test.txt"), "r");
            /**
             * model各个参数详解
             * r 代表以只读方式打开指定文件
             * rw 以读写方式打开指定文件
             * rws 读写方式打开，并对内容或元数据都同步写入底层存储设备
             * rwd 读写方式打开，对文件内容的更新同步更新至底层存储设备
             *
             * **/
            RandomAccessFile raf = new RandomAccessFile(path, "r");
            //获取RandomAccessFile对象文件指针的位置，初始位置是0
//			System.out.println("RandomAccessFile文件指针的初始位置:"+raf.getFilePointer());
            raf.seek(0);//移动文件指针位置
            byte[] buff = new byte[1024];//会导致中文乱码
            //用于保存实际读取的字节数
            int hasRead = 0;
            //循环读取
            while ((hasRead = raf.read(buff)) > 0) {
                //打印读取的内容,并将字节转为字符串输入
                allBuffer.append(new String(buff, 0, hasRead));
            }
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allBuffer.toString();

    }

    public static String readPageTxtFromFile(String path) {

        if (path == null) return "";
        File file = new File(path);

        if (file == null || !file.exists()) return "";
        long length = file.length();
        int m_mpBufferLen = (int) length;
        StringBuffer lines = new StringBuffer();
        int m_mbBufEndPos = 0;
        try {
            MappedByteBuffer m_mpBuff = new RandomAccessFile(file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);

            String strParagraph = "";
//			int begin = m_mbBufEndPos;
            while ((m_mbBufEndPos < m_mpBufferLen)) {
                byte[] parabuffer = readParagraphForward(m_mbBufEndPos, m_mpBuff, m_mpBufferLen);
                m_mbBufEndPos += parabuffer.length;
                try {
                    strParagraph = new String(parabuffer, "utf-8");
//					Log.d("xjd", strParagraph);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                lines.append(strParagraph);
            }
            m_mpBuff.clear();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lines.toString();
    }

    /**
     * 向后读取一个段落
     *
     * @param m_mbBufPos
     * @return
     */
    private static byte[] readParagraphForward(int m_mbBufPos, MappedByteBuffer m_mpBuff, int m_mpBufferLen) {
        // TODO Auto-generated method stub
        byte b0;
        int i = m_mbBufPos;
        while (i < m_mpBufferLen) {
            try {
                b0 = m_mpBuff.get(i++);
            } catch (Exception e) {
                break;
            }
            if (b0 == 0x0a) {
                break;
            }
        }
        int nParaSize = i - m_mbBufPos;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            try {
                buf[i] = m_mpBuff.get(m_mbBufPos + i);
            } catch (Exception e) {
                break;
            }
        }
        return buf;
    }

    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strcontent, String filePath,
                                      String fileName) {
        // 生成文件夹之后，再生成文件，不然会出错
        makeRootDirectory(filePath);
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent.replaceAll("(\\r\\n(\\s)*)", "\r\n　　");//匹配前面有0或多个空格的换行回车成换行回车+两个全角空格
        String preBlank = "　　";
        if (strContent.indexOf(preBlank) != 0) {
            strContent = preBlank + strContent;
        }
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                // 删除再创建导致open failed: EBUSY (Device or resource busy)
//				if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
//				file.createNewFile();
            } else {
                deleteFile(file);
            }
            boolean issucc = false;
            if (!file.getParentFile().exists()) {
                synchronized (FileUtil.class) {
                    issucc = file.getParentFile().mkdirs();
                }
            }
            file.createNewFile();

            RandomAccessFile write = null;
            FileChannel channel = null;
            FileLock lock = null;
            write = new RandomAccessFile(file, "rwd");
            channel = write.getChannel();
            while (true) {
                try {
                    lock = channel.lock();//尝试获得文件锁，若文件正在被使用，则一直等待
                    break;
                } catch (Exception e) {
//					L.reportError("writeTxtToFile write::: some other thread is holding the file..."
//							+ e.getLocalizedMessage());
                }
            }
            write.seek(0);//替换原有文件内容
            write.write(strContent.getBytes("utf-8"));
            lock.release();
            channel.close();
            write.close();
//			L.reportError("writeTxtToFile write::: content succ " + strFilePath);
        } catch (Exception e) {
            e.printStackTrace();
//			L.reportError("writeTxtToFile file fail" + strFilePath +" exception="+  e.getLocalizedMessage());
        }
    }


    // 将字符串写入到文本文件中
    public static boolean writeTxtToFile(String strcontent, String filePath,
                                         String fileName, String arttitle) {
        return writeTxtToFile(strcontent, filePath, fileName, arttitle, false);
    }

    // 将字符串写入到文本文件中
    public static boolean writeTxtToFile(String strcontent, String filePath,
                                         String fileName, String arttitle, boolean isvip) {
        // 生成文件夹之后，再生成文件，不然会出错
        makeRootDirectory(filePath);
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent;
        //两种情况，一种是(\r\n  ) 漫画家和时魔者 另一种是(\r\n)战神者和四个女友,怎么匹配两种情况
        if (!isvip) {
            //匹配前面有0或多个空格的换行回车成换行回车+两个全角空格
            strContent = strcontent.replaceAll("(\\r\\n(\\s)*)", "\r\n　　");
            String preBlank = "　　";
            if (strContent.indexOf(preBlank) != 0) {
                strContent = preBlank + strContent;
            }
        }
        strContent = arttitle + "\r\n" + "\r\n" + strContent + "\r\n";

        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                // 删除再创建导致open failed: EBUSY (Device or resource busy)
//				if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
//				file.createNewFile();
            } else {
                deleteFile(file);
            }
            boolean issucc = false;
            if (!file.getParentFile().exists()) {
                synchronized (FileUtil.class) {
                    issucc = file.getParentFile().mkdirs();
                }
            }
            file.createNewFile();

            RandomAccessFile write = null;
            FileChannel channel = null;
            FileLock lock = null;
            write = new RandomAccessFile(file, "rwd");
            channel = write.getChannel();
            while (true) {
                try {
                    lock = channel.lock();//尝试获得文件锁，若文件正在被使用，则一直等待
                    break;
                } catch (Exception e) {
//					L.reportError("writeTxtToFile write::: some other thread is holding the file..."
//							+ e.getLocalizedMessage());
                }
            }
            write.seek(0);//替换原有文件内容
            write.write(strContent.getBytes("utf-8"));
            lock.release();
            channel.close();
            write.close();
//			L.reportError("writeTxtToFile write::: content succ " + strFilePath);
        } catch (Exception e) {
            e.printStackTrace();
//			L.reportError("writeTxtToFile file fail" + strFilePath +" exception="+  e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    /*第一种在文件中插入内容的方法（利用RandomAccessFile类  移动文件指针）*/
    public static void randomInsertWrite(String file, long position, String str) throws IOException {
        File f = new File(file);
        RandomAccessFile rw = new RandomAccessFile(f, "rwd");
        try {
            byte[] b = str.getBytes("utf-8");
            rw.seek(position);
            rw.write(b);//插入内容
        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        } finally {
            //rw.close();
        }
    }

    // 将字符串写入到文本文件中
    public static void writeHtmlToLocalFile(String strcontent, String filePath,
                                            String fileName) {
        // 生成文件夹之后，再生成文件，不然会出错
//		makeFilePath(filePath, fileName);
        String strFilePath = filePath + fileName;
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                // 删除再创建导致open failed: EBUSY (Device or resource busy)
//				if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
//				file.createNewFile();
            } else {
                deleteFile(file);
            }
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            file.createNewFile();

            RandomAccessFile write = null;
            FileChannel channel = null;
            FileLock lock = null;
            write = new RandomAccessFile(file, "rwd");
            channel = write.getChannel();
            while (true) {
                try {
                    lock = channel.lock();//尝试获得文件锁，若文件正在被使用，则一直等待
                    break;
                } catch (Exception e) {
                    System.out.println("write::: some other thread is holding the file...");
                }
            }
            write.seek(0);//替换原有文件内容
            write.write(strcontent.getBytes("utf-8"));
            lock.release();
            channel.close();
            write.close();
            //test
            L.d("write::: content succ ", strFilePath);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    // 将字符串写入到文本文件中
    public static void writeTxtToLocalFile(String strcontent, String filePath,
                                           String fileName) {
        // 生成文件夹之后，再生成文件，不然会出错
//		makeFilePath(filePath, fileName);
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent.replaceAll("(\\r\\n(\\s)*)", "\r\n　　");//匹配前面有0或多个空格的换行回车成换行回车+两个全角空格
        String preBlank = "　　";
        if (strContent.indexOf(preBlank) != 0) {
            strContent = preBlank + strContent;
        }
//		String strContent = strcontent.replaceAll("\r\n", "\r\n　　");
        strContent = "\r\n" + strContent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                // 删除再创建导致open failed: EBUSY (Device or resource busy)
//				if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
//				file.createNewFile();
            } else {
                deleteFile(file);
            }
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            file.createNewFile();

            RandomAccessFile write = null;
            FileChannel channel = null;
            FileLock lock = null;
            write = new RandomAccessFile(file, "rwd");
            channel = write.getChannel();
            while (true) {
                try {
                    lock = channel.lock();//尝试获得文件锁，若文件正在被使用，则一直等待
                    break;
                } catch (Exception e) {
                    System.out.println("write::: some other thread is holding the file...");
                }
            }
            write.seek(0);//替换原有文件内容
            write.write(strContent.getBytes("gbk"));
            lock.release();
            channel.close();
            write.close();
            //test
            L.d("write::: content succ ", strFilePath);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * 上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    @SuppressWarnings({"finally", "unused"})
    public static int uploadFile(File file, String RequestURL) {
        int res = 0;
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        //System.out.println(file.getAbsolutePath() + " 开始上传。。。");
        HttpURLConnection conn = null;
        try {
            URL url = new URL(RequestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIME_OUT);
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);

            CookieSyncManager.createInstance(MyApplication.getApplication());
            CookieManager cookieManager = CookieManager.getInstance();
//			CookieSyncManager.createInstance(context);
            conn.setRequestProperty("cookie", cookieManager.getCookie("sfacg.com"));
            CookieSyncManager.getInstance().sync();
            if (file != null) {
                //System.out.println(file.getAbsolutePath() + " 开始上传。。。not null");
                /**
                 * 当文件不为空时执行上传
                 */
                DataOutputStream dos = new DataOutputStream(
                        conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名
                 */

                sb.append("Content-Disposition: form-data; name=\"pic\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                dos.close();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                res = conn.getResponseCode();
                //System.out.println("response code:" + res);
                if (res == 200) {
                    //System.out.println("request success");
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                    //System.out.println("result : " + result);
                } else {
                    //System.out.println("request error");
                }
            } else {
                //System.out.println(file.getAbsolutePath() + " 开始上传。。。 null");
            }
        } catch (Exception e) {
            //System.out.println(file.getAbsolutePath() + " 上传失败。。。");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                conn = null;
            }
            return res;
        }
    }

    /**
     * 根据文件的uri获取绝对路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByContentResolver(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        String filePath = null;
        if (null == c) {
            throw new IllegalArgumentException(
                    "Query on " + uri + " returns null result.");
        }
        try {
            if ((c.getCount() != 1) || !c.moveToFirst()) {
            } else {
                filePath = c.getString(
                        c.getColumnIndexOrThrow(MediaColumns.DATA));
            }
        } finally {
            c.close();
        }
        return filePath;
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file) {

        long size = 0;
        try {
            if (!file.exists()) return 0;
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;

        return size;
    }

    /**
     * 获取文件夹文件数目
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderNum(File file) {

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if (fileList == null) return 0;
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderNum(fileList[i]);

                } else {
                    size = size + 1;

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;

        return size;
    }


    /**
     * 1、本地书迁移  old： bookshelffiles  直接整个文件夹迁移过去即可
     * 2、在线书迁移  根据数据库转移
     * 3、删除旧目录 bookshelffiles， bookshelfimage， bookshelfdownload
     */
    public static long getMoveFiles() {
        int num = 0;

        File sdDir = null;
        String sdPath = "";
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            sdPath = sdDir.toString();
        }

        String oldLocalPath = sdPath + File.separator + "bookshelffiles" + File.separator;
//    	String oldImagesPath = sdPath + File.separator + "bookshelfimage" + File.separator;
        String oldDownloadPath = sdPath + File.separator + "bookshelfdownload" + File.separator;
        long localNum = getFolderNum(new File(oldLocalPath));
        long imagesNum = 0;//getFolderNum(new File(oldImagesPath)); 图片放在后台最后更新，不显示在界面上
        long txtNum = getFolderNum(new File(oldDownloadPath));

        return localNum + imagesNum + txtNum;
    }


    /**
     * 复制文件夹
     *
     * @param source
     * @param target
     */
    public static void copyDirs(File source, File target) {

        long size = 0;
        try {
            File[] fileList = source.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                String path = target.getAbsolutePath() + File.separator + fileList[i].getName();
                File targetFile = new File(path);
                if (fileList[i].isDirectory()) {
                    makeRootDirectory(path);
                    copyDirs(fileList[i], targetFile);
                } else {
                    makeFilePath(path);
                    nioTransferCopy(fileList[i], targetFile);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 不考虑多线程优化，单线程文件复制最快的方法是(文件越大该方法越有优势，一般比常用方法快30+%):
     *
     * @param source
     * @param target
     */
    public static void nioTransferCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            try {
                inStream = new FileInputStream(source);
                outStream = new FileOutputStream(target);
                in = inStream.getChannel();
                out = outStream.getChannel();
                in.transferTo(0, in.size(), out);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inStream != null) inStream.close();
                if (in != null) in.close();
                if (outStream != null) outStream.close();
                if (out != null) out.close();
            }
        } catch (FileNotFoundException ex) {
            L.e("File not found: " + ex);
        } catch (IOException ex) {
            L.e(ex);
        }

    }

    /**
     * 如果需要监测复制进度，可以用第二快的方法(留意buffer的大小，对速度有很大影响):
     *
     * @param source
     * @param target
     */
    public static void nioBufferCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            try {
                inStream = new FileInputStream(source);
                outStream = new FileOutputStream(target);
                in = inStream.getChannel();
                out = outStream.getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(4096);
                while (in.read(buffer) != -1) {
                    buffer.flip();
                    out.write(buffer);
                    buffer.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inStream != null) inStream.close();
                if (in != null) in.close();
                if (outStream != null) outStream.close();
                if (out != null) out.close();
            }
        } catch (FileNotFoundException ex) {
            L.e("File not found: " + ex);
        } catch (IOException ex) {
            L.e(ex);
        }
    }

    /**
     * 常用的方法1
     *
     * @param source
     * @param target
     */
    public static void customBufferBufferedStreamCopy(File source, File target) {
        InputStream fis = null;
        OutputStream fos = null;
        try {
            try {
                fis = new BufferedInputStream(new FileInputStream(source));
                fos = new BufferedOutputStream(new FileOutputStream(target));
                byte[] buf = new byte[4096];
                int i;
                while ((i = fis.read(buf)) != -1) {
                    fos.write(buf, 0, i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) fis.close();
                if (fos != null) fos.close();
            }
        } catch (FileNotFoundException ex) {
            L.e("File not found: " + ex);
        } catch (IOException ex) {
            L.e(ex);
        }
    }


    /**
     * 常用的方法2
     *
     * @param source
     * @param target
     */
    public static void customBufferStreamCopy(File source, File target) {
        InputStream fis = null;
        OutputStream fos = null;
        try {

            try {
                fis = new FileInputStream(source);
                fos = new FileOutputStream(target);
                byte[] buf = new byte[4096];
                int i;
                while ((i = fis.read(buf)) != -1) {
                    fos.write(buf, 0, i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) fis.close();
                if (fos != null) fos.close();
            }
        } catch (FileNotFoundException ex) {
            L.e("File not found: " + ex);
        } catch (IOException ex) {
            L.e(ex);
        }
    }


    public static String getLocalPath(String bookid, boolean isCach) {
        String path;
        if (isCach) {
            path = new StringBuffer(FileUtil.getSDPath()).
                    append(File.separator).append(rootName).
                    append(File.separator).append(cache).
                    append(File.separator).append(bookid).
                    append(File.separator).append(images).toString();
        } else {
            path = new StringBuffer(FileUtil.getSDPath()).
                    append(File.separator).append(rootName).
                    append(File.separator).append(download).
                    append(File.separator).append(bookid).
                    append(File.separator).append(images).toString();
        }
//		makeFilePath(path);
        return path;
    }

    /**
     * @param path 文件路径
     * @return 文件路径的StatFs对象
     * @throws Exception 路径为空或非法异常抛出
     */
    public static StatFs getStatFs(String path) {
        try {
            return new StatFs(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param stat 文件StatFs对象
     * @return 剩余存储空间的MB数
     */
    public static float calculateSizeInMb(StatFs stat) {
        if (stat != null) {
            float avail = stat.getAvailableBlocks() * 1.0f;
            float blocksize = stat.getBlockSize() * 1.0f / (1024 * 1024.0f);
            return avail * blocksize;
        }
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getAllSize(StatFs stat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long avail = stat.getBlockCountLong();
            long blocksize = stat.getBlockSizeLong();
            return avail * blocksize;
        } else {
            long avail = stat.getBlockCount();
            long blocksize = stat.getBlockSize();
            return avail * blocksize;
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getVavailableSize(StatFs stat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long avail = stat.getAvailableBlocksLong();
            long blocksize = stat.getBlockSizeLong();
            return avail * blocksize;
        } else {
            long avail = stat.getAvailableBlocks();
            long blocksize = stat.getBlockSize();
            return avail * blocksize;
        }
    }


    /**
     * 路径分两组，一组是用户下载的，一组是阅读时产生的缓存
     *
     * @param isCach true代表访问的是缓存，则返回缓存路径。
     *               false代表访问的是下载，返回下载路径。
     * @return
     */
    public static String getTxtPath(String bookid, boolean isCach) {
        String path;
        if (isCach) {
            path = new StringBuffer(FileUtil.getSDPath()).
                    append(File.separator).append(rootName).
                    append(File.separator).append(cache).
                    append(File.separator).append(bookid).
                    append(File.separator).append(txt).
                    append(File.separator).toString();
        } else {
            path = new StringBuffer(FileUtil.getSDPath()).
                    append(File.separator).append(rootName).
                    append(File.separator).append(download).
                    append(File.separator).append(bookid).
                    append(File.separator).append(txt).
                    append(File.separator).toString();
        }
        makeRootDirectory(path);

        return path;
    }

    public static String getInternalTxtPath(String bookid, boolean isCach) {
        String path;
        if (isCach) {
            path = new StringBuffer(FileUtil.getSDPath(false)).
                    append(File.separator).append(rootName).
                    append(File.separator).append(cache).
                    append(File.separator).append(bookid).
                    append(File.separator).append(txt).
                    append(File.separator).toString();
        } else {
            path = new StringBuffer(FileUtil.getSDPath(false)).
                    append(File.separator).append(rootName).
                    append(File.separator).append(download).
                    append(File.separator).append(bookid).
                    append(File.separator).append(txt).
                    append(File.separator).toString();
        }
        makeRootDirectory(path);

        return path;
    }

    public static String getLogPath() {
        String path;
        path = new StringBuffer(FileUtil.getSDPath(true)).
                append(File.separator).append(rootName).
                append(File.separator).append(debugConfig).
                append(File.separator).toString();
        makeRootDirectory(path);
        return path;
    }

    private static boolean openBook(String path) throws IOException {
        if (TextUtils.isEmpty(path)) return false;
        File file = new File(path);
        if (file == null || !file.exists()) return false;
        long length = file.length();
        if (length <= 0) return false;
        MappedByteBuffer m_mpBuff = new RandomAccessFile(file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);

        if (m_mpBuff != null) m_mpBuff.clear();
        else return false;

        return true;
    }

    public static boolean exitFile(String path) {
        if (TextUtils.isEmpty(path)) return false;
        File file = new File(path);
        boolean isExit = file.exists();
        file = null;
        return isExit;
    }

    /**
     * 返回是否是正确的路径
     *
     * @param bookid 小说id
     * @param path   小说对应的路径
     * @return
     */
    public static boolean isCorrectPath(String bookid, String path) {
        if (TextUtils.isEmpty(bookid) || TextUtils.isEmpty(path)) return false;
        return path.contains(File.separator + bookid + File.separator) ||
                path.contains(File.separator + vipdirectory + File.separator);
    }

    /**
     * 获取文件存在的路径，分两组，一组是用户下载的，一组是阅读时产生的缓存
     *
     * @param bookid   小说id
     * @param fileName 文件名
     * @param path     文件路径
     * @return 返回确认后路径
     */
    public static String getExitMarkTxtPath(String bookid, String fileName, String path) {
        if (FileUtil.exitFile(path)) return path;
        return getExitTxtPath(bookid, fileName);
    }

    public static String getExitTxtFile(String bookid, String chaptId) {
        SharedPreferences sp = MyApplication.getApplication().getPreferences();
        boolean isDownUsetf = sp.getBoolean("isUsetf", true);
        String path;
        path = new StringBuffer(FileUtil.getSDPath(isDownUsetf)).
                append(File.separator).append(rootName).
                append(File.separator).append(download).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(bookid).
                append(chaptId).toString();
        if (new File(path).exists()) return path;

        path = new StringBuffer(FileUtil.getSDPath(isDownUsetf)).
                append(File.separator).append(rootName).
                append(File.separator).append(cache).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(bookid).
                append(chaptId).toString();
        if (new File(path).exists()) return path;

        path = new StringBuffer(FileUtil.getSDPath(!isDownUsetf)).
                append(File.separator).append(rootName).
                append(File.separator).append(download).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(bookid).
                append(chaptId).toString();
        if (new File(path).exists()) return path;

        path = new StringBuffer(FileUtil.getSDPath(!isDownUsetf)).
                append(File.separator).append(rootName).
                append(File.separator).append(cache).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(bookid).
                append(chaptId).toString();
        if (new File(path).exists()) return path;

        return "";
    }


    public static boolean exitTxtFile(String bookid, String chaptId) {
        String path;
        String sdPath = FileUtil.getSDPath();
        path = new StringBuffer(sdPath).
                append(File.separator).append(rootName).
                append(File.separator).append(download).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(bookid).
                append(chaptId).toString();
        if (new File(path).exists()) return true;

        path = new StringBuffer(sdPath).
                append(File.separator).append(rootName).
                append(File.separator).append(cache).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(bookid).
                append(chaptId).toString();
        if (new File(path).exists()) return true;

        path = new StringBuffer(sdPath).
                append(File.separator).append(rootName).
                append(File.separator).append(download).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(bookid).
                append(chaptId).toString();
        if (new File(path).exists()) return true;

        path = new StringBuffer(sdPath).
                append(File.separator).append(rootName).
                append(File.separator).append(cache).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(bookid).
                append(chaptId).toString();
        return new File(path).exists();

    }

    public static boolean exitTextFile(String defautlPath, String bookid, String fileName) {
        if (!exitFile(defautlPath)) {
            String path = new StringBuffer(FileUtil.getSDPath(true)).
                    append(File.separator).append(rootName).
                    append(File.separator).append(download).
                    append(File.separator).append(bookid).
                    append(File.separator).append(txt).
                    append(File.separator).append(fileName).toString();
            if (FileUtil.exitFile(path)) return true;

            path = new StringBuffer(FileUtil.getSDPath(false)).
                    append(File.separator).append(rootName).
                    append(File.separator).append(download).
                    append(File.separator).append(bookid).
                    append(File.separator).append(txt).
                    append(File.separator).append(fileName).toString();
            if (FileUtil.exitFile(path)) return true;

            path = new StringBuffer(FileUtil.getSDPath(true)).
                    append(File.separator).append(rootName).
                    append(File.separator).append(cache).
                    append(File.separator).append(bookid).
                    append(File.separator).append(txt).
                    append(File.separator).append(fileName).toString();
            if (FileUtil.exitFile(path)) return true;

            path = new StringBuffer(FileUtil.getSDPath(false)).
                    append(File.separator).append(rootName).
                    append(File.separator).append(cache).
                    append(File.separator).append(bookid).
                    append(File.separator).append(txt).
                    append(File.separator).append(fileName).toString();
            return FileUtil.exitFile(path);
        }
        return true;
    }

    public static String getExitTxtPath(String bookid, String fileName) {
        String path = new StringBuffer(FileUtil.getSDPath(true)).
                append(File.separator).append(rootName).
                append(File.separator).append(download).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(fileName).toString();
        if (FileUtil.exitFile(path)) return path;

        path = new StringBuffer(FileUtil.getSDPath(false)).
                append(File.separator).append(rootName).
                append(File.separator).append(download).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(fileName).toString();
        if (FileUtil.exitFile(path)) return path;

        path = new StringBuffer(FileUtil.getSDPath(true)).
                append(File.separator).append(rootName).
                append(File.separator).append(cache).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(fileName).toString();
        if (FileUtil.exitFile(path)) return path;

        path = new StringBuffer(FileUtil.getSDPath(false)).
                append(File.separator).append(rootName).
                append(File.separator).append(cache).
                append(File.separator).append(bookid).
                append(File.separator).append(txt).
                append(File.separator).append(fileName).toString();
        if (FileUtil.exitFile(path)) return path;

        return path;//到这里拿的是第一个path
    }

    public static String getMydowloadPath(String bookid) {
        String path = new StringBuffer(FileUtil.getSDPath()).
                append(File.separator).append(rootName).
                append(File.separator).append(download).
                append(File.separator).append(bookid).
                append(File.separator).toString();
        makeRootDirectory(path);
        return path;
    }

    public static String getCachPath(boolean isDownUsetf) {
        String path = new StringBuffer(FileUtil.getSDPath(isDownUsetf)).
                append(File.separator).append(rootName).
                append(File.separator).append(cache).
                append(File.separator).toString();//缓存目录
        makeRootDirectory(path);
        return path;
    }

    //    public static String getImagesPath(){
//    	return FileUtil.getSDPath(true) + "/mafengwo/cache/images/";
//    }
    public static String getDownloadPath() {
        String path = new StringBuffer(FileUtil.getSDPath()).
                append(File.separator).append(rootName).
                append(File.separator).append(cache).
                append(File.separator).append(download).
                append(File.separator).toString();//其他下载文件目录，例如apk，头像等
        makeRootDirectory(path);
        return path;
    }

    public static String getDBPath() {
        String path = new StringBuffer(FileUtil.getSDPath())
                .append(File.separator).append(rootName)
                .append(File.separator).append(snappyDB)
                .append(File.separator).toString();
        makeRootDirectory(path);
        return path;
    }

    /**
     * 语音朗读根目录
     *
     * @return
     */
    public static String getTtsRoot() {
        String path = new StringBuffer(FileUtil.getSDPath()).
                append(File.separator).append(rootName).
                append(File.separator).append(tts).
                append(File.separator).toString();
        makeRootDirectory(path);
        return path;
    }

    /**
     * 漫画根目录
     *
     * @return
     */
    public static String getComisRoot() {
        String path = new StringBuffer(FileUtil.getSDPath()).
                append(File.separator).append(rootName).
                append(File.separator).append(comis).
                append(File.separator).toString();
        makeRootDirectory(path);
        return path;
    }

    public static String getComisPath(long comicId) {
        String path = new StringBuffer(FileUtil.getSDPath()).
                append(File.separator).append(rootName).
                append(File.separator).append(comis).
                append(File.separator).append(comicId).
                append(File.separator).toString();
        makeRootDirectory(path);
        return path;
    }

    public static String getComisPath(long comicId, String pathName) {
        String path = new StringBuffer(FileUtil.getSDPath()).
                append(File.separator).append(rootName).
                append(File.separator).append(comis).
                append(File.separator).append(comicId).
                append(File.separator).append(pathName).
                append(File.separator).toString();
        makeRootDirectory(path);
        return path;
    }


    /**
     * 获取音频根目录
     *
     * @return
     */
    public static String getAlbumsPath() {
        String path = new StringBuffer(FileUtil.getSDPath()).
                append(File.separator).append(rootName).
                append(File.separator).append(albums).
                append(File.separator).toString();
        makeRootDirectory(path);
        return path;
    }


    public static String getAlbumsParentPath(boolean isVip) {
        String path = new StringBuffer(FileUtil.getSDPath()).
                append(File.separator).append(rootName).
                append(File.separator).append(isVip?albumsVip:albums)
                .toString();
        makeRootDirectory(path);
        return path;
    }

    public static String getAlbumsPath(long albumId, String songName, boolean isVip) {
        String path = new StringBuffer(getAlbumsParentPath(isVip)).
                append(File.separator).append(albumId).
                append(File.separator).append(songName).toString();
        makeRootDirectory(path);
        return path;
    }

    public static String getLocalbookPath() {
        String path = new StringBuffer(FileUtil.getSDPath()).
                //append(File.separator).
                        append(rootName).
                        append(File.separator).append(localbook).
                        append(File.separator).toString();//本地书目录
        makeRootDirectory(path);
        return path;
    }
    public static String getWebRootPath() {
        String path = new StringBuffer(FileUtil.getSDPath()).
                //append(File.separator).
                        append(rootName).
                        append(File.separator).append(webRoot).
                        append(File.separator).toString();//本地书目录
        makeRootDirectory(path);
        return path;
    }


    public static String getSDPath() {
        SharedPreferences sp = MyApplication.getApplication().getPreferences();
        boolean isUsetf = sp.getBoolean("isUsetf", true);
        return getSDPath(isUsetf);
    }

    private String sdPathWithTF;
    private String sdPathWithoutTF;

    public String getSdPathWithTF() {
        if (sdPathWithTF == null) {
            String path = getTFDir();
            if (!TextUtils.isEmpty(path)) {
                sdPathWithTF = path;
            }
        }
        return sdPathWithTF != null ? sdPathWithTF : "";
    }

    public String getSdPathWithoutTF() {
        if (sdPathWithoutTF == null) {
            File sdDir = null;
            boolean sdCardExist = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
            if (sdCardExist) {
                sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
                sdPathWithoutTF = sdDir.toString();
            } else {
                sdPathWithoutTF = "";
            }
        }
        return sdPathWithoutTF != null ? sdPathWithoutTF : "";
    }

    public static String getSDPath(boolean isUsetf) {
        Context context = MyApplication.getApplication().getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0以上系统目录
            if (isUsetf) {
                return context.getExternalFilesDir(null).getAbsolutePath();
            } else {
                return context.getFilesDir().getAbsolutePath();
            }
        } else {
            if (isUsetf) {
                String path = FileUtil.shareInstance().getSdPathWithTF();
                String rootPath = new StringBuffer(path).append(File.separator).append(defineRootName).
                        append(File.separator).toString();
                File root = new File(path);
                if (root.canWrite()) {
                    return rootPath;
                } else {
                    return context.getExternalFilesDir(null).getAbsolutePath();
                }
            } else {
                String path = FileUtil.shareInstance().getSdPathWithoutTF();
                String rootPath = new StringBuffer(path).append(File.separator).append(defineRootName).
                        append(File.separator).toString();
                File root = new File(path);
                if (root.canWrite()) {
                    return rootPath;
                } else {
                    return context.getFilesDir().getAbsolutePath();
                }
            }
        }
    }


    public static List<String> getExtSDCardPaths() {
        L.i("go in getExtSDCardPaths");
        List<String> paths = new ArrayList<>();

        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        if (extFileStatus.equals(Environment.MEDIA_MOUNTED)
                && extFile.exists() && extFile.isDirectory()
                && extFile.canWrite()) {
            paths.add(extFile.getAbsolutePath());
        }
        try {
            // obtain executed result of command line code of 'mount', to judge
            // whether tfCard exists by the result
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;
            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line
                        .contains("storage"))
                        || line.contains("secure")
                        || line.contains("asec")
                        || line.contains("firmware")
                        || line.contains("shell")
                        || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }
                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }
                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains(File.separator) || mountPath.contains("data")
                        || mountPath.contains("Data")) {
                    continue;
                }
                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite()) {
                    continue;
                }
                boolean equalsToPrimarySD = mountPath.equals(extFile
                        .getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }
                L.i(line);
                File mafengwo = new File(mountPath + File.separator + defineRootName + File.separator
                        + rootName + File.separator);
                if (!mafengwo.exists()) {
                    //有些机型的tf卡生成不了目录
                    if (!mafengwo.mkdirs()) continue;
                }
                if (!mafengwo.canWrite()) continue;
                paths.add(mountPath);
            }
        } catch (IOException e) {
            L.i(e.getLocalizedMessage());
            e.printStackTrace();
        }
        L.i("go in getExtSDCardPaths end");
        return paths;
    }

    private static final String testContent = "版权隐私\n" +
            "联系邮箱：mafengwo@mafengwo.com";

    /**
     * 保存一段文字检查外置SD卡是否能工作
     *
     * @param file
     * @return
     */
    public static boolean checkWriteText(File file) {
        String fileName = "privacy";
        String artTitle = "版权隐私";
        String path = file.getAbsolutePath() + File.separator + "testwrite" + File.separator;
        FileUtil.writeTxtToFile(testContent, path, fileName, artTitle);
        boolean isSucc = false;
        try {
            isSucc = openBook(path + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isSucc;
    }

    /**
     * 检查是否存在SDCard
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public static String getTFDir() {
        List<String> paths = MyApplication.getApplication().getSdkarts();
        if (paths != null && paths.size() > 0) {
            if (paths.size() > 1) return paths.get(1);
            return paths.get(0);
        }
        return "";
    }

    // 生成文件
    public static void makeFilePath(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    synchronized (FileUtil.class) {
                        file.getParentFile().mkdirs();
                    }
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeFilePath(File file) {
        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    synchronized (FileUtil.class) {
                        file.getParentFile().mkdirs();
                    }
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                boolean succ = false;
                synchronized (FileUtil.class) {
                    succ = file.mkdirs();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(long size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0KB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

//	public void saveFile(Bitmap bm, String fileName) throws IOException {
//		File dirFile = new File(FileUtil.getSDPath() + "/" + "bookshelfimage/");
//		if (!dirFile.exists()) {
//			dirFile.mkdir();
//		}
//		File myCaptureFile = new File(FileUtil.getSDPath() + "/" + "bookshelfimage/"
//				+ fileName);
//		BufferedOutputStream bos = new BufferedOutputStream(
//				new FileOutputStream(myCaptureFile));
//		bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//		bos.flush();
//		bos.close();
//	}


    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }

    public static void openFile(File f, Context mainActivity) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        mainActivity.startActivity(intent);
    }

    public static void openAndroidNFile(File f, Context mainActivity) {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE)
                .setDataAndType(FileProvider.getUriForFile(mainActivity,
                        "com.jph.takephoto.fileprovider", f),
                        "application/vnd.android.package-archive")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mainActivity.startActivity(intent);
    }

    /**
     * // 如果是文件，删除
     * // 删除再创建导致open failed: EBUSY (Device or resource busy)
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file == null) return;
        final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
        file.renameTo(to);
        to.delete();
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (!file.exists()) return;
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        deleteFile(file);
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            deleteFile(file);
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}

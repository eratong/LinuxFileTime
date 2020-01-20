package com.example.demo.util;

import com.example.demo.exception.ReceiptProcessException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class FileUtil {
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
	
    private static final int BUF_SIZE = 1024 * 8;

    /**
     * 解压 *.tar.gz文件
     *
     * @param bytes 文件字节数组
     * @param name
     * @return 解压后的字节数组
     */
    public static byte[] unGzFile(byte[] bytes, String name) {
        ArchiveInputStream in;
        BufferedInputStream bis = null;
        if(name.endsWith(".zlib")){
            return ZLibUtils.compress(bytes);
        }
        try {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(
                    bytes));
            in = new ArchiveStreamFactory()
                    .createArchiveInputStream("tar", gis);
            bis = new BufferedInputStream(in);
            TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
            byte[] buffer = new byte[bis.available()];
            while (entry != null) {
                bis.read(buffer, 0, bis.available());
                return buffer;
//                entry = (TarArchiveEntry) in.getNextEntry();
            }
            return buffer;
        } catch (Exception e) {
            log.error("unGzFile fail", e);
            return null;
        } finally {
            IOUtils.closeQuietly(bis);
        }
    }



    public static byte[] readFileToByteArray(final File file) throws IOException {
        FileInputStream in = openInputStream(file);
        FileChannel channel = null;
        FileLock lock = null;
        try {
            channel = in.getChannel();
            lock = channel.lock(0, Long.MAX_VALUE, true);
            final long fileLength = file.length();
            return fileLength > 0 ? IOUtils.toByteArray(in, fileLength) : IOUtils.toByteArray(in);
        } finally {
            if (null != lock) {
                lock.release();
            }
            if (null != channel) {
                channel.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }


    public static void bytes2File(byte[] byteArray, File file) {
        InputStream in = new ByteArrayInputStream(byteArray);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static FileInputStream openInputStream(final File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }
    
    /**
   	 * 先把tar.gz文件转byte[]，再解压，把里面的文件变成byte[]和文件名构成的UNGZ对象
   	 * @param filePath
   	 * @return
   	 */
    public static UNGZ getUNGZ(String filePath) {
		byte[] bytes = null;
		try {
			bytes = readFileToByteArray(new File(filePath));
		} catch (IOException e) {
			log.error("getUNGZ fail:"+filePath,e);
			return null;
		}
		return unGzFile2(bytes);
	}
    
    /**
     * 把tar.gz文件byte[]解压，把里面的文件变成byte[]和文件名构成的UNGZ对象
     * @param bytes 文件字节数组
     * @return 解压后文件 信息
     */
    public static UNGZ unGzFile2(byte[] bytes) {
        ArchiveInputStream in;
        BufferedInputStream bis = null;
        UNGZ ungz = new UNGZ();
        try {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream( bytes ));
            in = new ArchiveStreamFactory().createArchiveInputStream("tar", gis);
            bis = new BufferedInputStream(in);
            TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
            while (entry != null) {
                String fileName = entry.getName();
                int len = (int) entry.getSize();
                byte[] buffer = new byte[len];
                bis.read(buffer, 0, len);
                entry = (TarArchiveEntry) in.getNextEntry();
                if(!fileName.contains(".txt")){
                    ungz.filename = fileName;
                    ungz.data = buffer;
                }else {
                    ungz.textFilename = fileName;
                    ungz.textData = buffer;
                }
            }
            return ungz;
        } catch (Exception e) {
            log.error("",e);
            return null;
        } finally {
            IOUtils.closeQuietly(bis);
        }
    }
    
    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray 字节数组
     * @return hash字符串
     */
    
    public static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
    
    /**
     * 十六进制字符串转为byte数组
     * <p>
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    
    /**
     * byte[] 转为文件
     *
     * @param b          字节数组
     * @param outputFile 目标文件路径
     * @return 文件
     */
    public static File getFileFromBytes(byte[] b,
                                        File outputFile) {
        BufferedOutputStream stream = null;
        try {
            FileOutputStream fstream = new FileOutputStream(outputFile);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            log.error("fail to save file from bytes", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.error("fail to close stream", e);
                }
            }
        }
        return outputFile;
    }
    
    /**
     * 删除剥壳临时目录的文件
     *
     * @param bmpDirList bmp目录目录列表
     */
    public static void deleteTempFile(List<String> bmpDirList) {
        
        // 删除剥壳临时目录文件
        for (String tmpdir : bmpDirList) {
            File file = new File(tmpdir);
            if (file.exists()) {
                file.delete();
            }
        }
    }
    
    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (IOException e) {
            log.error("",e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
        return buffer;
    }
    
    public static boolean customBufferBufferedStreamCopy(File source,
                                                         File target) throws ReceiptProcessException {
        
        int index = target.getPath().lastIndexOf(File.separator);
        String targetPath = target.getPath().substring(0, index);
        File file = new File(targetPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        
        InputStream fis = null;
        OutputStream fos = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(source));
            fos = new BufferedOutputStream(new FileOutputStream(target));
            byte[] buf = new byte[4096];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
            return true;
            
        } catch (Exception e) {
            log.error("",e);
            try {
                UserPrincipal userPrincipal = Files.getOwner(target.toPath());
                Set<PosixFilePermission> posixFilePermissionSet = Files.getPosixFilePermissions(target.toPath());
                StringBuilder sb = new StringBuilder();
                for (PosixFilePermission posixFilePermission : posixFilePermissionSet) {
                    sb.append(posixFilePermission.name()).append(",");
                }
                log.error(target.getPath() + " owner:" + userPrincipal.getName() + ";permissions:" + sb.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            throw new ReceiptProcessException(ReceiptProcessException.BOKE_NO_FILE, source.getName());
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(fis);
        }
    }
    
    /**
     * 使用文件通道的方式复制文件
     *
     * @param s 源文件
     * @param t 复制到的新文件
     */
    
    public static void fileChannelCopy(File s,
                                       File t) {
        
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in;
        FileChannel out;
        
        try {
            
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();// 得到对应的文件通道
            out = fo.getChannel();// 得到对应的文件通道
            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
            
        } catch (IOException e) {
            
            log.error("",e);
            
        } finally {
            IOUtils.closeQuietly(fo);
            IOUtils.closeQuietly(fi);
        }
    }
    
    public static void mergeFiles(String outFile,
                                  String[] files) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        // out.println("Merge " + Arrays.toString(files) + " into " + outFile);
        try {
            fos = new FileOutputStream(outFile);
            FileChannel outChannel = fos.getChannel();
            for (String f : files) {
                fis = new FileInputStream(f);
                FileChannel fc = fis.getChannel();
                ByteBuffer bb = ByteBuffer.allocate(BUF_SIZE);
                while (fc.read(bb) != -1) {
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                IOUtils.closeQuietly(fis);
            }
            //out.println("Merged!! ");
        } catch (IOException ioe) {
            log.error("",ioe);
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(fis);
        }
    }
    
    public static void copyWithLock(File source,
                                    File target) {
        try {
            LockUtil.lock(target);
            try {
                FileUtil.customBufferBufferedStreamCopy(source, target);
            } finally {
                LockUtil.unlock(target);
            }
        } catch (InterruptedException e) {
            log.error("lock file fail",e);
        }
    }

    public static void stringToFile(String str,
                                    File target) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(target);
            osw = new OutputStreamWriter(fos, "gbk");
            osw.write(str);
            osw.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                osw.close();
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void stringToFileCharset(String str,
                                           File target, String charset) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(target);
            osw = new OutputStreamWriter(fos, charset);
            osw.write(str);
            osw.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                osw.close();
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static boolean findStringinFile(File file){
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        try{
            read = new InputStreamReader(new FileInputStream(file),"UTF-8");
            bufferedReader = new BufferedReader(read);
            String line = null;
            boolean val1 = false;
            boolean val2 = false;
            boolean val3 = false;
            while ((line = bufferedReader.readLine()) != null) {
                    //指定字符串判断处
                    if(line.contains("@PJL JOB")){
                        val1 = true;
                    }
                    if(line.contains("PCL")){
                        val2 = true;
                    }
                    if(line.contains("@PJL EOJ")){
                        val3 = true;
                    }
                    if(val1 & val2 & val3){
                        return true;
                    }
                }
                return false;
            }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
                read.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public static UNGZ getUNZLIB(String filePath) {
        String[] split = filePath.split(File.separator);
        log.info(JSONUtil.toJson(split));
        String fileName =  split[split.length - 1];
        byte[] bytes = null;
        try {
            bytes = readFileToByteArray(new File(filePath));
        } catch (IOException e) {
            log.error("getUNZLIB fail:"+filePath,e);
            return null;
        }
        return unZlibFile2(bytes,fileName);
    }

    private static UNGZ unZlibFile2(byte[] bytes, String fileName) {
        UNGZ ungz = new UNGZ();
        byte[] data = ZLibUtils.decompress(bytes);
        ungz.data = data;
        String filename = getFileName(data,fileName);
        ungz.filename = filename;
        return ungz;
    }

    private static String getFileName(byte[] data, String fileName) {
        String DatFileHexString = FileUtil.bytesToHexString(data);
        boolean isPNG = DatFileHexString.startsWith("89504E47");
        if(isPNG)
            return makeFileName(fileName,"png");
        boolean isBMP = DatFileHexString.startsWith("424D");
        if(isBMP)
            return makeFileName(fileName,"bmp");
        return makeFileName(fileName,"dat");
    }
    private static String makeFileName(String filename, String ext){
        return filename.replace("zlib",ext);
    }
}

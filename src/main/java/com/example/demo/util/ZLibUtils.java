package com.example.demo.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

/** 
* ZLib压缩工具 
*/  
public class ZLibUtils {
	
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Unfair\\Desktop\\test.zlib");
		byte[] bytes = file2Bytes(file);
		byte[] bytes2 = decompress(bytes);
		bytes2File(bytes2, "C:\\Users\\Unfair\\Desktop\\test.zlib.dat");
//		byte[] bytes3 = compress(bytes2);
//		bytes2File(bytes3, "C:\\Users\\Unfair\\Desktop\\test.zlib");
	}
	
	public static byte[] file2Bytes(File file) {
		byte[] fileBytes = null;
		try {
			FileInputStream in = new FileInputStream(file);
			long fileLength = file.length();
			fileBytes = IOUtils.toByteArray(in, fileLength);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileBytes;
	}
	
	public static void bytes2File(byte[] byteArray, String targetPath) {
		InputStream in = new ByteArrayInputStream(byteArray);
	    File file = new File(targetPath);
	    
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
	
	/**
	 * 压缩
	 * @param data
	 * @return
	 */
	public static byte[] compress(byte[] data) {
		byte[] result = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DeflaterOutputStream zos = new DeflaterOutputStream(bos);
			zos.write(data);
			zos.close();
			result = bos.toByteArray();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 解压
	 * @param data
	 * @return
	 */
	public static byte[] decompress(byte[] data) {
		byte[] result = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			InflaterOutputStream zos = new InflaterOutputStream(bos);
			zos.write(data);
			zos.close();
			result = bos.toByteArray();
			bos.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
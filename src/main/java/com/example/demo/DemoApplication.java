package com.example.demo;

import com.example.demo.util.DateUtil;
import com.example.demo.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.Date;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
//		try {
//			BasicFileAttributeView basicview =
//                    Files.getFileAttributeView(Paths.get( File.separator+"testfiletime"+ File.separator+"ms134000101_000101614t_2019-07-26T12-35-05.tar.gz"), BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
//
//			System.out.println("创建时间："+DateUtil.get4Ymd(new Date(basicview.readAttributes().creationTime().toMillis())));
//			System.out.println("最后更新时间："+DateUtil.get4Ymd(new Date(basicview.readAttributes().lastModifiedTime().toMillis())));
//			String date = DateUtil.get4Ymd(new Date(basicview.readAttributes().lastAccessTime().toMillis()));
//
//			System.out.println("最后访问时间："+date);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}


		try {

//          移动file到testfiletime/in
			File file=new File(File.separator+"testfiletime"+ File.separator+"ms134000101_000101614t_2019-07-26T12-35-05.tar.gz");
			File newFile=new File(File.separator+"testfiletime"+ File.separator+"in"+File.separator+"ms134000101_000101614t_2019-07-26T12-35-05.tar.gz");
			if(file.exists()){
				FileUtil.customBufferBufferedStreamCopy(file, newFile);
				FileUtils.copyFile(file,newFile);
			}

			BasicFileAttributeView basicview =
					Files.getFileAttributeView(Paths.get( File.separator+"testfiletime"+  File.separator+"in"+File.separator+"ms134000101_000101614t_2019-07-26T12-35-05.tar.gz"), BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);

			System.out.println("复制到in里的-----创建时间："+ DateUtil.get4Ymd(new Date(basicview.readAttributes().creationTime().toMillis())));
			System.out.println("复制到in里的-----最后更新时间："+DateUtil.get4Ymd(new Date(basicview.readAttributes().lastModifiedTime().toMillis())));
			String date = DateUtil.get4Ymd(new Date(basicview.readAttributes().lastAccessTime().toMillis()));

			System.out.println("复制到in里的-----最后访问时间："+date);


		} catch (IOException e) {
			e.printStackTrace();
		}
		SpringApplication.run(DemoApplication.class, args);
	}

}

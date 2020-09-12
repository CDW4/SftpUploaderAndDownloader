package com.sftp.SftpServerConnection;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.sftp.SftpServerConnection.config.UpAndDown;

@SpringBootApplication
public class SftpUploadAndDownload {

	public static void main(String[] args) {
		SpringApplication.run(SftpUploadAndDownload.class, args);
		UpAndDown uAD = new UpAndDown(); 
		
//		//Upload to sftp
//		try {
//			uAD.connect();
//			File file = File.createTempFile("data", ".csv");
//			FileInputStream fis = new FileInputStream(file);
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhms");
//			//File
//			uAD.upload(fis, "/data" + formatter.format(LocalDateTime.now()) + ".csv");
//			uAD.disconnect();
//		} catch (JSchException | IOException | SftpException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//Download And Delete from Sftp
//		try {
//			uAD.connect();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("smh");
//			String uniqueNumbers = formatter.format(LocalDateTime.now());
//			File fileRecieved = File.createTempFile("fileRecieved"+ uniqueNumbers, ".csv");
//			uAD.downloadAndDelete("/data2.txt", new FileOutputStream(fileRecieved));
//			uAD.disconnect();
//			
//			System.out.println("File Recieved: " + fileRecieved.getName());
//		} catch (IOException | JSchException | SftpException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//Download List of files that match 
		try {
			uAD.connect();
			@SuppressWarnings("unchecked")
			List<File> filesFromSftp = uAD.getListOfMatchingFilesFromDirectory("/", "data.*\\.csv");
			//do what you want with list of files
			uAD.disconnect(); 
		} catch (JSchException | SftpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}

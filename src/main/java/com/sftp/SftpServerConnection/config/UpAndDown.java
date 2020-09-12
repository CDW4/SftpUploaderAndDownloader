package com.sftp.SftpServerConnection.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class UpAndDown {
	
  
	  private String host = "127.0.0.1"; 
	  private int port = 2222; 
	  private Session sesh = null;
	  
	  public void connect() throws JSchException {
	  
	  JSch jsch = new JSch();
	  //jsch.setKnownHosts("C:\\Users\\craig\\.ssh\\known_hosts");
	  
	  sesh = jsch.getSession("test", host, port); 
	  sesh.setConfig("StrictHostKeyChecking", "no");
	  sesh.setPassword("password");
	  sesh.connect();
	  
	  System.out.println("connected");
	  
	  }
	  
	  
	  public void disconnect() { 
		  if (sesh != null) { 
			  sesh.disconnect();
			  System.out.println("disconnected"); 
			  } 
		  }
	  
	  
	  public void upload(FileInputStream src, String dst) 
			  throws JSchException, SftpException {
	  Channel uploadChannel = sesh.openChannel("sftp"); 
	  uploadChannel.connect(60 * 1000); 
	  ChannelSftp sftpChannel = (ChannelSftp) uploadChannel; 
	  sftpChannel.put(src, dst);
	  sftpChannel.exit(); 
	  System.out.println("uploaded");
	  
	  }
	  
	  public void download(String src, FileOutputStream dst) 
			  throws JSchException, SftpException { 
		  Channel downloadChannel = sesh.openChannel("sftp");
		  downloadChannel.connect(); 
		  ChannelSftp sftpChannel = (ChannelSftp) downloadChannel;
		  sftpChannel.get(src, dst);
		  sftpChannel.exit();
		  System.out.println("downloaded"); 
		  
	  }
	  
	  public void downloadAndDelete(String src, FileOutputStream dst) 
			  throws JSchException, SftpException { 
		  Channel downloadChannel = sesh.openChannel("sftp");
		  downloadChannel.connect(); 
		  ChannelSftp sftpChannel = (ChannelSftp) downloadChannel;
		  sftpChannel.get(src, dst);
		  sftpChannel.rm(src);
		  sftpChannel.exit();
		  System.out.println("downloaded"); 
		  
	  }
	  
	  public List getListOfMatchingFilesFromDirectory(String directory, String regExString) 
			  throws JSchException, SftpException, IOException {
		  List<File> myListOfFiles = new ArrayList<>(); 
		  File newFile;
		  int counter= 0; 
		  
		  Channel downloadChannel = sesh.openChannel("sftp");
		  downloadChannel.connect(); 
		  ChannelSftp sftpChannel = (ChannelSftp) downloadChannel;
		  @SuppressWarnings("unchecked")
		  List<LsEntry> fileList = sftpChannel.ls(directory);
		  System.out.println("Full File List: " + Arrays.toString(fileList.toArray()));
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("smh-yyyy-dd-MM");
		  String uniqueNumbers = formatter.format(LocalDateTime.now());
		  
		  Pattern regexPattern = Pattern.compile(regExString);
		  for(LsEntry entry : fileList) {
			  System.out.println(entry.getFilename());
			  Matcher matcher = regexPattern.matcher(entry.getFilename());
			  boolean matches = matcher.matches();
			  if (matches) {
				  //download matching files one by one
				  newFile = File.createTempFile("fr"+ uniqueNumbers, ".csv");
				  sftpChannel.get("/" + entry.getFilename(), new FileOutputStream(newFile));
				  //sftpChannel.rm("/" + entry.getFilename());
				  myListOfFiles.add(newFile);
				  counter++;
				  System.out.println("if counter: " + counter);
			  } else {
				  counter++;
				  System.out.println("else counter: " + counter);
				  continue;
			  }
		  }
		  
		  counter = 0; 
		  sftpChannel.exit();
		  
		  System.out.println("Filtered List Returned: " + Arrays.toString(myListOfFiles.toArray()));
		  return myListOfFiles; 
		  
		  
	  }
	  
	  
	  

	 

}

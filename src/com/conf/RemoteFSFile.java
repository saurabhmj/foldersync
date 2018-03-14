package com.conf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * @author saurabh jogalekar
 * 
 *         Class for handling remote files. Uses SFTP to perform the operations.
 *
 *
 */
public class RemoteFSFile extends FSFile {

	private String absPath, relativePath, name, file;
	private boolean isDir = false;
	private long length, lastModified;
	private InputStream ip;
	private OutputStream op;
	private String token;

	public RemoteFSFile(String token, String path, String relPath) {
		file = path + relPath;
		relativePath = relPath;
		absPath = path;
		name = file.split("/")[file.split("/").length - 1];
		this.token = token;
		initializePars(token);

	}

	public RemoteFSFile(String token, String path, String relPath, String name) {
		file = path + "/" + relPath;
		relativePath = relPath;
		absPath = path;
		this.token = token;
		this.name = name;
		initializePars(token);

	}

	/**
	 * @param token
	 * 
	 *            Checks if file exists. If it does, then attributes are extracted.
	 * 
	 * 
	 */
	private void initializePars(String token) {
		ChannelSftp channel = ChannelManager.getInstance().getChannel(token);
		SftpATTRS attrs = null;
		try {
			attrs = channel.stat(file);

			isDir = attrs.isDir();
			length = attrs.getSize();
			lastModified = attrs.getMTime() * 1000L;

		} catch (SftpException e) {

		}

	}

	@Override
	public String getAbsPath() {
		// TODO Auto-generated method stub
		return absPath;
	}

	@Override
	public String getRelativePath() {
		// TODO Auto-generated method stub
		return relativePath;
	}

	@Override
	public InputStream getInputStream() {
		try {
			ChannelSftp channel = ChannelManager.getInstance().getChannel(token);
			String pwd = channel.pwd();
			channel.cd(file.substring(0, file.lastIndexOf("/")));

			ip = channel.get(name);
			channel.cd(pwd);
			return ip;
		} catch (Exception e) {
			ChannelManager.getInstance().closeChannels();
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		try {
			ChannelSftp channel = ChannelManager.getInstance().getChannel(token);
			String pwd = channel.pwd();
			// System.out.println(absPath);
			channel.cd(file.substring(0, file.lastIndexOf("/")));
			op = channel.put(name);
			channel.cd(pwd);
			return op;
		} catch (Exception e) {
			e.printStackTrace();
			ChannelManager.getInstance().closeChannels();
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public void closeInputStream() {
		try {
			ip.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void closeOutputStream() {
		try {
			op.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public boolean isDir() {
		// TODO Auto-generated method stub
		return isDir;
	}

	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return new File(file);
	}

	@Override
	public Date getLastModified() {
		// TODO Auto-generated method stub
		return new Date(lastModified);
	}

	@Override
	public long getLength() {
		// TODO Auto-generated method stub
		return length;
	}

	@Override
	public FSFile[] getChildren() {
		try {
			ChannelSftp channel = ChannelManager.getInstance().getChannel(token);
			String pwd = channel.pwd();
			channel.cd(file);
			Vector<LsEntry> v = channel.ls(channel.pwd());

			List<LsEntry> list = new ArrayList<>(v);
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				LsEntry lsEntry = (LsEntry) iterator.next();
				if (lsEntry.getFilename().equals(".") || lsEntry.getFilename().equals(".."))
					iterator.remove();
			}

			if (list.size() > 0) {
				int i = 0;
				FSFile[] children = new RemoteFSFile[list.size()];
				for (LsEntry entry : list) {
					// String relPath = relativePath.trim().equals("")?name:relativePath+"/"+name;
					// children[i] = new RemoteFSFile(token, absPath,relPath, entry.getFilename());
					children[i] = new RemoteFSFile(token, getAbsPath(), getRelativePath() + "/" + entry.getFilename());
					i++;
				}
				return children;
			} else
				return new FSFile[0];
		} catch (Exception e) {
			e.printStackTrace();
			ChannelManager.getInstance().closeChannels();
			// TODO: handle exception
		}
		return new FSFile[0];
	}

	@Override
	public void setModifiedDate(Date date) {
		try {
			ChannelSftp channel = ChannelManager.getInstance().getChannel(token);

			channel.setMtime(file, (int) (date.getTime() / 1000));

		} catch (Exception e) {
			ChannelManager.getInstance().closeChannels();
			// TODO: handle exception
		}

	}

	@Override
	public boolean mkdir() {
		try {
			ChannelSftp channel = ChannelManager.getInstance().getChannel(token);
			String pwd = channel.pwd();

			channel.cd(file.substring(0, file.lastIndexOf("/")));
			channel.mkdir(name);
			channel.cd(pwd);

		} catch (Exception e) {
			ChannelManager.getInstance().closeChannels();
			// TODO: handle exception
		}
		return false;
	}

}

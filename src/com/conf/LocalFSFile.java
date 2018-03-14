package com.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * @author saurabh jogalekar
 * 
 *         Handles local files for operations.
 */
public class LocalFSFile extends FSFile {

	private File file = null;
	private String absPath, relativePath, name;
	private boolean isDir = false;
	private long length, lastModified;
	private InputStream ip;
	private OutputStream op;

	public LocalFSFile(String path, String relPath) {
		file = new File(path + relPath);
		relativePath = relPath;
		absPath = path;
		isDir = file.isDirectory();
		name = file.getName();
		if (!isDir) {
			lastModified = file.lastModified();
			length = file.length();
		}
	}

	@Override
	public FSFile[] getChildren() {

		String[] strChildren = file.list();

		if (strChildren.length > 0) {
			int i = 0;
			FSFile[] children = new LocalFSFile[strChildren.length];
			for (String child : strChildren) {
				children[i] = new LocalFSFile(getAbsPath(), getRelativePath() + "/" + strChildren[i]);
				i++;
			}

			return children;
		}

		return new FSFile[0];
	}

	@Override
	public String getAbsPath() {

		return absPath;
	}

	@Override
	public String getRelativePath() {

		return relativePath;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public boolean isDir() {

		return isDir;
	}

	@Override
	public File getFile() {

		return file;
	}

	@Override
	public Date getLastModified() {

		return new Date(lastModified);
	}

	@Override
	public long getLength() {

		return length;
	}

	@Override
	public InputStream getInputStream() {
		try {
			ip = new FileInputStream(file);
			return ip;
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		try {
			op = new FileOutputStream(file);
			return op;
		} catch (Exception e) {

		}

		return null;
	}

	@Override
	public void closeInputStream() {
		try {
			ip.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void closeOutputStream() {
		try {
			op.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void setModifiedDate(Date date) {
		file.setLastModified(date.getTime());

	}

	@Override
	public boolean mkdir() {
		if (file.mkdir()) {
			isDir = true;
			return true;
		}
		return false;
	}

}

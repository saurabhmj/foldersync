package com.conf;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * @author saurabh jogalekar
 * 
 *         An encapsulation class for a file. It provides operations and
 *         attributes.
 *
 */
public abstract class FSFile implements Comparable<FSFile> {

	/**
	 * @return absolute path of the file
	 */
	public abstract String getAbsPath();

	/**
	 * @return relative path of the file
	 */
	public abstract String getRelativePath();

	/**
	 * @return File inputStream
	 */
	public abstract InputStream getInputStream();

	/**
	 * @return File outputStream
	 */
	public abstract OutputStream getOutputStream();

	/**
	 * Close the inputstream in the file
	 */
	public abstract void closeInputStream();

	public abstract void closeOutputStream();

	/**
	 * @return Name of file
	 */
	public abstract String getName();

	/**
	 * @return true if directory is successfully created.
	 */
	public abstract boolean mkdir();

	/**
	 * @return whether or not the file is a directory
	 */
	public abstract boolean isDir();

	/**
	 * @param date
	 * 
	 *            Sets the modified date
	 */
	public abstract void setModifiedDate(Date date);

	/**
	 * @return return the File Object for local files, absolute path of File
	 *         otherwise.
	 */
	public abstract File getFile();

	/**
	 * @return the last modified date
	 */
	public abstract Date getLastModified();

	/**
	 * @return length of file
	 */
	public abstract long getLength();

	/**
	 * @return children in the file. If the file is a directory, an array of all the
	 *         files and directories are returned. Returns an empty array otherwise.
	 */
	public abstract FSFile[] getChildren();

	/**
	 * @return directories in the file.
	 * 
	 *         Returns all the files which are directories.
	 */
	public FSFile[] getDirs() {
		FSFile[] children = getChildren();

		if (children.length > 0) {
			int length = 0;
			for (FSFile file : children) {
				if (file.isDir())
					length++;
			}

			FSFile[] dirs = new FSFile[length];
			int i = 0;
			for (FSFile file : children) {
				if (file.isDir()) {
					dirs[i] = file;
					i++;
				}
			}

			return dirs;
		} else
			return new FSFile[0];
	}

	/**
	 * @param ip
	 * @param op
	 *            copies the input stream to output stream.
	 */
	private boolean copy(InputStream ip, OutputStream op) {

		byte[] buffer = new byte[FSConstants.BUFFER_SIZE];
		int size = 0;
		try {
			while (size < getLength()) {
				int len = ip.read(buffer, 0, FSConstants.BUFFER_SIZE);
				op.write(buffer, 0, len);
				size += len;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}

	/**
	 * @param trg
	 *            Copies the contents of the file to target (parameter) file.
	 */
	public boolean copy(FSFile trg) {

		if (isDir()) {
			trg.mkdir();
			return true;
		}

		copy(getInputStream(), trg.getOutputStream());
		closeInputStream();
		trg.closeOutputStream();
		trg.setModifiedDate(getLastModified());
		return true;
	}

	/**
	 * Returns the files among all the children in the file.
	 */
	public FSFile[] getFiles() {
		FSFile[] children = getChildren();

		if (children.length > 0) {
			int length = 0;
			for (FSFile file : children) {
				if (!file.isDir())
					length++;
			}

			FSFile[] dirs = new FSFile[length];
			int i = 0;
			for (FSFile file : children) {
				if (!file.isDir()) {
					dirs[i] = file;
					i++;
				}

			}

			return dirs;
		} else
			return new FSFile[0];
	}

	@Override
	public String toString() {
		return getAbsPath() + getRelativePath() + "  name:" + getName();
	}

	@Override
	public int compareTo(FSFile o) {

		return this.getName().compareTo(o.getName());
	}

	@Override
	public boolean equals(Object obj) {
		return this.compareTo((FSFile) obj) == 0;
	}
}

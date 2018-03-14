package com.conf;

import java.util.List;

/**
 * @author saurabh jogalekar
 *
 *         Utility for synchronizing two folders.
 * 
 *         Copies files to the target location.
 * 
 */
public class SyncUtil {

	private String trg;
	private Scheme targetScheme;
	private String token;
	private List<FSFile> files;

	public void setToken(String token) {
		this.token = token;
	}

	public void setTrg(String trg) {
		this.trg = trg;
	}

	public void setTargetScheme(Scheme targetScheme) {
		this.targetScheme = targetScheme;
	}

	public void setFiles(List<FSFile> files) {
		this.files = files;
	}

	/**
	 * Copies the files into the target location. files, target scheme, and the
	 * target location should be initialized.
	 * 
	 * @return returns true if copying files is successful.
	 */
	public boolean copyToTarget() {
		try {
			if (targetScheme.equals(Scheme.LOCAL)) {
				for (FSFile file : files) {
					FSFile newFile = new LocalFSFile(trg, "/" + file.getRelativePath());
					file.copy(newFile);
					System.out.print(".");
				}
			}
			if (targetScheme.equals(Scheme.REMOTE)) {
				for (FSFile file : files) {
					FSFile newFile = new RemoteFSFile(token, trg, "/" + file.getRelativePath());
					file.copy(newFile);
					System.out.print(".");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			;
			return false;
		}
		return true;
	}

}

package com.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author saurabh Jogalekar
 * 
 *         Utility class for file operations
 */
public class FileUtil {

	private static List<FSFile> toSrc = new ArrayList<>(), toTrg = new ArrayList<>(), collision = new ArrayList<>();;

	public FileUtil() {
		toSrc = new ArrayList<>();
		toTrg = new ArrayList<>();
		collision = new ArrayList<>();
	}

	public static List<FSFile> getToSrc() {
		return toSrc;
	}

	public static List<FSFile> getToTrg() {
		return toTrg;
	}

	public static List<FSFile> getCollision() {
		return collision;
	}

	/**
	 * @param srcDir
	 * @param trgDir
	 * 
	 *            Compares files in the two directories
	 */
	public static void compareDirectories(FSFile srcDir, FSFile trgDir) {

		FSFile[] srcFileList = new FSFile[0];
		FSFile[] tgtFileList = new FSFile[0];
		FSFile[] srcDirectoryList = new FSFile[0];
		FSFile[] tgtDirectoryList = new FSFile[0];

		if (srcDir != null) {
			srcFileList = srcDir.getFiles();
			srcDirectoryList = srcDir.getDirs();
		}

		if (trgDir != null) {
			tgtFileList = trgDir.getFiles();
			tgtDirectoryList = trgDir.getDirs();

		}

		compareFiles(srcFileList, tgtFileList, false);
		compareFiles(srcDirectoryList, tgtDirectoryList, true);
	}

	public static void compareFiles(FSFile[] srcFileList, FSFile[] tgtFileList, boolean isDir) {

		Arrays.sort(srcFileList);
		Arrays.sort(tgtFileList);

		int srcIndex = 0, trgIndex = 0;

		while (srcIndex < srcFileList.length && trgIndex < tgtFileList.length) {

			// Compare the file name
			int comp = srcFileList[srcIndex].compareTo(tgtFileList[trgIndex]);
			if (comp == 0) {
				// If the names match, and it is a file, we compare the last modified date,
				// else we compare the files inside the directory.
				if (isDir) {
					compareDirectories(srcFileList[srcIndex], tgtFileList[trgIndex]);
				} else {
					long diff = Math.abs(srcFileList[srcIndex].getLastModified().getTime()
							- tgtFileList[trgIndex].getLastModified().getTime());
					if (diff > 1000) {
						collision.add(srcFileList[srcIndex]);
						collision.add(tgtFileList[trgIndex]);
					}
				}
				srcIndex++;
				trgIndex++;

			}

			else if (comp < 0) {
				// If the file exists in the source and not in target we add the file to target.
				toTrg.add(srcFileList[srcIndex]);
				if (isDir)
					compareDirectories(srcFileList[srcIndex], null);
				srcIndex++;
			} else if (comp > 0) {
				// If the file exists in target and not in source we add the file to source.
				toSrc.add(tgtFileList[trgIndex]);
				if (isDir)
					compareDirectories(null, tgtFileList[trgIndex]);
				trgIndex++;
			}

		}

		while (trgIndex < tgtFileList.length) {
			toSrc.add(tgtFileList[trgIndex]);
			if (isDir)
				compareDirectories(null, tgtFileList[trgIndex]);
			trgIndex++;
		}

		while (srcIndex < srcFileList.length) {
			toTrg.add(srcFileList[srcIndex]);
			if (isDir)
				compareDirectories(srcFileList[srcIndex], null);

			srcIndex++;
		}

	}

}

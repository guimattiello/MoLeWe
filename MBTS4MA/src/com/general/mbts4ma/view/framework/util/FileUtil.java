package com.general.mbts4ma.view.framework.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

public abstract class FileUtil {

	public static synchronized void writeFile(String content, File file) {
		try {
			FileUtils.writeStringToFile(file, content, Charsets.ISO_8859_1);
		} catch (IOException e) {
		}
	}

	public static synchronized String readFile(File file) {
		if (file == null || !file.exists()) {
			return null;
		}

		try {
			return FileUtils.readFileToString(file, Charsets.ISO_8859_1);
		} catch (IOException e) {
		}

		return null;
	}

	public static synchronized boolean hasFile(File directory, String filename) {
		List<File> files = new LinkedList<File>();

		if (filename != null && !"".equalsIgnoreCase(filename) && directory.isDirectory() && directory.listFiles() != null && directory.listFiles().length > 0) {
			files.addAll(Arrays.asList(directory.listFiles()));

			for (File file : files) {
				if (filename.equals(file.getName())) {
					return true;
				}
			}
		}

		return false;
	}

}

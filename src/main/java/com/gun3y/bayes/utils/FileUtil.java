package com.gun3y.bayes.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;

public class FileUtil {

	public static String ROOT_DIR = ".";

	public static String readFile(String filePath){
		if(!Strings.isNullOrEmpty(filePath)){
			File file = new File(filePath);
			if(file.isFile() && file.exists()){
				try {
					return FileUtils.readFileToString(file, "UTF-8");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
		
	}
	
	public static void createFile(String fileName, String content) {

		if (!Strings.isNullOrEmpty(fileName)) {
			fileName = fileName.replace(":", "").replace("<", "")
					.replace(">", "").replace("*", "").replace("?", "")
					.replace("\"", "").replace("|", "");
			File file = new File(ROOT_DIR + "\\" + fileName);
			try {

				file.getParentFile().mkdirs();

				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fstream = new FileWriter(file.getAbsoluteFile());
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(content);
				// Close the output stream
				out.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}
	
	public static Map<String, List<File>> getSamples(){
		File dir = new File(FileUtil.ROOT_DIR);
		
		Map<String, List<File>> map = new HashMap<String, List<File>>();
		
		if(dir != null && dir.exists() && dir.isDirectory()){
			for (File conceptDirs : dir.listFiles()) {
				if(conceptDirs != null && conceptDirs.exists() && conceptDirs.isDirectory()){
					List<File>  samples= new ArrayList<File>();
					for (File sampleFile : conceptDirs.listFiles()) {
						if(sampleFile != null && sampleFile.exists() && sampleFile.isFile()){
							samples.add(sampleFile);
						}
					}
					map.put(conceptDirs.getName(), samples);
				}
			}
		}
		
		return map;
	}


	public static boolean isValidName(String text) {
		Pattern pattern = Pattern
				.compile(
						"# Match a valid Windows filename (unspecified file system).          \n"
								+ "^                                # Anchor to start of string.        \n"
								+ "(?!                              # Assert filename is not: CON, PRN, \n"
								+ "  (?:                            # AUX, NUL, COM1, COM2, COM3, COM4, \n"
								+ "    CON|PRN|AUX|NUL|             # COM5, COM6, COM7, COM8, COM9,     \n"
								+ "    COM[1-9]|LPT[1-9]            # LPT1, LPT2, LPT3, LPT4, LPT5,     \n"
								+ "  )                              # LPT6, LPT7, LPT8, and LPT9...     \n"
								+ "  (?:\\.[^.]*)?                  # followed by optional extension    \n"
								+ "  $                              # and end of string                 \n"
								+ ")                                # End negative lookahead assertion. \n"
								+ "[^<>:\"/\\\\|?*\\x00-\\x1F]*     # Zero or more valid filename chars.\n"
								+ "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]  # Last char is not a space or dot.  \n"
								+ "$                                # Anchor to end of string.            ",
						Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
								| Pattern.COMMENTS);
		Matcher matcher = pattern.matcher(text);
		boolean isMatch = matcher.matches();
		return isMatch;
	}

}

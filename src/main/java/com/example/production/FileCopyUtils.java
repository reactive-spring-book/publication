package com.example.production;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Log4j2
public abstract class FileCopyUtils {

	public static void copy(InputStream in, OutputStream os) {
		try {
			org.springframework.util.FileCopyUtils.copy(in, os);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void copy(File src, File dst) throws Exception {
		log.info("copying " + src.getAbsolutePath() + " to " + dst.getAbsolutePath());
		if (src.isDirectory()) {
			Assert.isTrue(dst.exists() || dst.mkdirs(),
					dst.getAbsolutePath() + " need to exist");
			String[] files = src.list();
			if (null == files) {
				files = new String[0];
			}
			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dst, file);
				copy(srcFile, destFile);
			}
		}
		else {
			Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

}

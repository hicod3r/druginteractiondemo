package com.google.ce.demos.DrugInteraction;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Compression {

	public static byte[] compress(String str) throws IOException  {

		if (str == null || str.length() == 0) {
			return str.getBytes();
		}

		ByteArrayOutputStream obj = new ByteArrayOutputStream();

		GZIPOutputStream gzip = new GZIPOutputStream(obj);

		gzip.write(str.getBytes("UTF-8"));
		gzip.close();

		return obj.toByteArray();
	}

	public static String decompress(byte[] bytes) {

		try {
			if (bytes == null || bytes.length == 0) {

				return "";
			}

			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));

			BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

			StringBuffer sb = new StringBuffer();

			String line;

			while ((line = bf.readLine()) != null) {
				sb.append(line);

			}

			return sb.toString();

		} catch (Exception ex) {
			return new String(bytes);
		}
	}

}

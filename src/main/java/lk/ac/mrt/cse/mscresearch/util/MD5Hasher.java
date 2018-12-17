package lk.ac.mrt.cse.mscresearch.util;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class MD5Hasher {

	private static final String UTF_8 = "UTF-8";
	private static final Charset CHARSET = Charset.forName(UTF_8);

	public static String md5(String s){
		return md5(s.getBytes(CHARSET));
	}
	
	public static String md5(byte[] s){
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(s);
			return digestToString(digest);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

//	protected static String digestToString(byte[] digest) throws UnsupportedEncodingException {
//		return new String(digest, UTF_8);
//	}
	
	public static String md5(File f){
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			try (InputStream is = Files.newInputStream(Paths.get(f.getAbsolutePath()));
					DigestInputStream dis = new DigestInputStream(is, md)) 
			{
				while(dis.read() != -1);
			}
			byte[] digest = md.digest();
			return digestToString(digest);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String digestToString(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static void main(String[] args){
		File f = new File("hibernate.cfg.xml");
		System.out.println(md5(f));
	}
}

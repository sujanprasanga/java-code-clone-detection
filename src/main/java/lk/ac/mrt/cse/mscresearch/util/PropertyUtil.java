package lk.ac.mrt.cse.mscresearch.util;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyUtil {

	private final Properties properties;
	
	public PropertyUtil(){
		properties = new Properties();
		try(FileInputStream fis = new FileInputStream("ReqularExpressions.properties")){
			properties.load(fis);
		} catch (Exception e) {
			throw new RuntimeException();
		} 
	}
	
	public String getMethodSignatureRegEx(){
		return get("allAccessMods") + get("returnType") + get("methodNameAndParam");
	}
	
	public String getAccessibleMethodRegEx(){
		return get("accessibleMods") + get("returnType") + get("methodNameAndParam");
	}
	
	public String getAbstractMethodRegEx(){
		return get("abstract") + get("returnType") + get("methodNameAndParam");
	}
	
	public String getNativeMethodRegEx(){
		return get("native") + get("returnType") + get("methodNameAndParam");
	}
	
	private String get(String p){
		return properties.getProperty(p);
	}
}

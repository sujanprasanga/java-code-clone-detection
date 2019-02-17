package lk.ac.mrt.cse.mscresearch.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

	private final Properties properties;
	
	public PropertyUtil(){
		properties = new Properties();
		try(InputStream fis = getClass().getClassLoader().getResourceAsStream("META-INF/ReqularExpressions.properties")){
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
	
	public String getRegExForInvoke(){
		return get("invoke");
	}
	
	public String getRegExForConstructorInvoke(){
		return get("invokeConstructor");
	}
	
	private String get(String p){
		return properties.getProperty(p);
	}

	public String getRegExForInvokeDynamic() {
		return get("invokeDynamic");
	}

	public String getRegExForFieldOperations() {
		return get("fieldOp");
	}

	public String getRegExForPrimitiveOperations() {
		return get("primitiveOp");
	}

	public String getRegExForNewArrayOp() {
		return get("newArray");
	}

	public String getRegExForConditionalOperations() {
		return get("conditionals");
	}

	public String getRegExForOtherOperations() {
		return get("other");
	}

	public String getRegExForInstanceOp() {
		return get("instanceOp");
	}

	public String getRegExForReturnOp() {
		return get("return");
	}

	public String getRegExForSwitch() {
		return get("switch");
	}

	public String getRegExForInstructionSplitter() {
		return get("instructionSplitter");
	}

	public String getRegExForSwitchTargets() {
		return get("switchTargets");
	}

	public String getLineNumberTableStartRegEx() {
		return get("lineNumberTable");
	}

	public String getLineNumberTableEntryRegEx() {
		return get("lineNumberTableEntry");
	}
}

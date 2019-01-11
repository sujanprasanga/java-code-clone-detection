package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class AccessibleMethodIdentifier {

	private final PropertyUtil propertyUtil = new PropertyUtil();
	private final Pattern allM = 	Pattern.compile(propertyUtil.getMethodSignatureRegEx());
	private final Pattern accessible = Pattern.compile(propertyUtil.getAccessibleMethodRegEx());
	private final Pattern abstractM = Pattern.compile(propertyUtil.getAbstractMethodRegEx());
	private final Pattern nativeM=Pattern.compile(propertyUtil.getNativeMethodRegEx());
	
	public String extractMethodSignature(String code) {
		 Matcher all = allM.matcher(code);
		 if(!all.find()) {
			 return null;
		 }
		 String signature = code.substring(all.start(), all.end());
		 Matcher a = accessible.matcher(signature);
		 Matcher abs = abstractM.matcher(signature);
		 Matcher n = nativeM.matcher(signature);
		 boolean isAccessible = a.find();
		 boolean isAbstract = abs.find();
		 boolean isNative = n.find();
		 if(isAccessible&& !isAbstract&& !isNative) {
			 return signature;
		 } else {
			 return null;
		 }
	}
}

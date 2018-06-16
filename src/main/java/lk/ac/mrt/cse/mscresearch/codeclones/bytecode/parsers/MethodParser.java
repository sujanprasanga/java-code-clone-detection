package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Instruction;

public class MethodParser {

	private String signature;
	private List<String> localVarTypes = new ArrayList<>();
	private final List<Instruction> instructions = new ArrayList<>();
	
	public void parse(String methodBody){
		System.out.println(methodBody);
		Matcher m = RegularExpressionUtil.getMethodsignatureMatcher(methodBody);
		m.find();
		String[] params = m.group(RegularExpressionUtil.PARAMETER_CG_NAME).split(",");
		signature = methodBody.substring(m.start(), m.end());
		System.out.println(signature);
		parseMethodBody(methodBody.split("\n"), params);
//		buildLocalVarTable(methodBody);
//		System.out.println(signature);
//		System.out.println(localVarTypes);
	}

	private void parseMethodBody(String[] body, String[] params) {
		new MethodBodyParser(body, params).parse();
	}

//	private void buildLocalVarTable(String methodBody) {
//		extractLocalVariablesFromMethodSignature();
//		
//	}
//
//	private void extractLocalVariablesFromMethodSignature() {
//		String[] params = signature.substring(signature.indexOf('(') + 1,signature.indexOf(')')).split(",");
//		for(String param : params){
//			localVarTypes.add(param.trim());
//		}
//	}
}

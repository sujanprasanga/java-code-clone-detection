package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Instruction;

public class MethodParser {

	private static final Logger log = Logger.getLogger(MethodParser.class);
	private final ClassUnderTransform target;
	private String signature;
	private List<String> localVarTypes = new ArrayList<>();
	private final List<Instruction> instructions = new ArrayList<>();
	
	public MethodParser(ClassUnderTransform target) {
		this.target = target;
	}

	public void parse(String methodBody){
		log.debug(methodBody);
		Matcher m = RegularExpressionUtil.getMethodsignatureMatcher(methodBody);
		m.find();
		String[] params = m.group(RegularExpressionUtil.PARAMETER_CG_NAME).split(",");
		signature = methodBody.substring(m.start(), m.end());
		log.debug(signature);
		parseMethodBody(methodBody.split("\n"), params);
//		buildLocalVarTable(methodBody);
//		log.debug(signature);
//		log.debug(localVarTypes);
	}

	private void parseMethodBody(String[] body, String[] params) {
		new MethodBodyParser(target, body, params).parse();
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

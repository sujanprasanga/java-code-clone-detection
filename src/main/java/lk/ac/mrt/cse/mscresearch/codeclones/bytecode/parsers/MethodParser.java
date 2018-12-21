package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Instruction;
import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodIndex;
import lk.ac.mrt.cse.mscresearch.util.MD5Hasher;

public class MethodParser {

	private static final Logger log = Logger.getLogger(MethodParser.class);
	private final ClassUnderTransform target;
	private String signature;
//	private List<String> localVarTypes = new ArrayList<>();
	private List<Instruction> instructions;

	private final Map<Integer, Integer> lineNumberMapping;
	
	private final Set<MethodIndex> methodIndexes = new HashSet<>();
	
	public MethodParser(ClassUnderTransform target, Map<Integer, Integer> lineNumberMapping) {
		this.target = target;
		this.lineNumberMapping = lineNumberMapping;
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
		try{
			instructions = new MethodBodyParser(target, body, params).parse();
			instructions.stream().forEach(i->i.setLinNumber(lineNumberMapping));
			System.out.println(instructions);
		}catch(Exception e){
			e.printStackTrace();//TODO
		}
	}
	
	public Set<MethodIndex> getMethods(){
		MethodIndex m = new MethodIndex();
		String i = "";//TODO
		if(instructions != null){
			i = instructions.toString();
		}
		if(i.length() > 850){
			i = i.substring(0, 850);
		}
		m.setBody(i);
		m.setBodyhash(MD5Hasher.md5(i));
		m.setSignature(signature);
		methodIndexes.add(m);
		return methodIndexes;
	}
}

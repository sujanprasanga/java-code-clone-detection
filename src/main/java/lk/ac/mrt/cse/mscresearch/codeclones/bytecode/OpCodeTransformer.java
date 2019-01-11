package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;
import lk.ac.mrt.cse.mscresearch.util.MD5Hasher;

public class OpCodeTransformer {

	public Set<MethodDTO> transform(String signature, List<OpCode> opcodes, int size){
		Set<MethodDTO> t = new HashSet<>();
		StringBuilder sb = new StringBuilder();
		MethodDTO m = new MethodDTO();
		for(OpCode o : opcodes) {
			sb.append(o.getCode());//TODO
		}
		m.setSignature(signature);
		m.setBody(sb.toString());
		m.setBodyhash(MD5Hasher.md5(m.getBody()));
		m.setSize(size);
		t.add(m);
		return t;
	}
}

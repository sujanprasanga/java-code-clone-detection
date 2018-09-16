package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImmutableUtil<E extends Instruction>{

	private final Map<String,E> immutables = new ConcurrentHashMap<>();
	private final InstructionFactory<E> factory;
	
	public ImmutableUtil(InstructionFactory<E> factory){
		this.factory = factory;
	}
	
	public synchronized E get(String arg){
		if(!immutables.containsKey(arg)){
			immutables.put(arg, factory.create(new InstructionCreateParam(null, arg, null, null)));
		}
		return immutables.get(arg);
	}
	
}

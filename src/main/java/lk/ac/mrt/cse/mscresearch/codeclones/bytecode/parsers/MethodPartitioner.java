package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCodeTransformer;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;

public class MethodPartitioner {

	private static final int MIN_PARTITION_LINE_COUNT = 8;
	
	private final OpCodeTransformer opCodeTransformer = new OpCodeTransformer();

	public Set<MethodDTO> partition(String signature, List<OpCode> opcodes, int size) {
		if(opcodes.isEmpty()) {
			return Collections.emptySet();
		}
		
		Set<MethodDTO> partitions = new HashSet<>();
		try {
		List<int[]> lineRanges = getLineRanges(opcodes);
		
		
		Map<Integer, Integer> starts = new HashMap<>();
		Map<Integer, Integer> ends = new HashMap<>();
		
		for(int[] r : lineRanges) {
			for(int j = 0; j<opcodes.size(); j++) {
				if(r[0] == opcodes.get(j).getLineNumber()) {
					starts.put(r[0], j);
					break;
				}
			}
		}
		
		for(int[] r : lineRanges) {
			for(int j = opcodes.size() -1; j>0; j--) {
				if(r[1] == opcodes.get(j).getLineNumber()) {
					ends.put(r[1], j);
					break;
				}
			}
		}
		
		
		for(int[] r : lineRanges) {
			partitions.addAll(createPartition(signature, opcodes.subList(starts.get(r[0]), ends.get(r[1])), size));
		}
		
//		Map<Integer, Integer> startindexes = getStartIndexes(opcodes, lineRanges);
//		int s = opcodes.size();
//		int li = opcodes.get(0).getLineNumber();
//		int lj = opcodes.get(0).getLineNumber();
//		for(int i=0; i<s; i++) {
//			for(int j=i; j<s; j++) {
//				if( (opcodes.get(j).getLineNumber() > lj || opcodes.get(i).getLineNumber() > li) && 
//				    opcodes.get(j).getLineNumber() - opcodes.get(i).getLineNumber() > MIN_PARTITION_LINE_COUNT) {
//					li = opcodes.get(i).getLineNumber();
//					lj = opcodes.get(j).getLineNumber();
//					partitions.addAll(createPartition(signature, opcodes.subList(i, j), size));
//				}
//			}
//		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return partitions;
	}

	private List<int[]> getLineRanges(List<OpCode> opcodes) {
		Set<Integer> uniqueNumbers = new HashSet<>();
		opcodes.stream().map(OpCode::getLineNumber).forEach(uniqueNumbers::add);
		List<Integer> sorted = uniqueNumbers.stream().sorted().collect(Collectors.toList());
		List<int[]> ranges = new ArrayList<>();
		for(int i=0; i<sorted.size(); i++) {
			for(int j=i; j<sorted.size(); j++) {
				if(sorted.get(j) - sorted.get(i) > MIN_PARTITION_LINE_COUNT && j-i > MIN_PARTITION_LINE_COUNT * 3) {
					ranges.add(new int[] {sorted.get(i), sorted.get(j)});
				}
			}
		}
		return ranges;
	}

	private Set<MethodDTO> createPartition(String signature, List<OpCode> opcodes, int size) {
		return opCodeTransformer.transformForLocalSegment(signature, opcodes, size);
	}
	
}

package lk.ac.mrt.cse.mscresearch.indexer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.DisassembledClass;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.DisassembledMethod;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.FormattedClass;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.FormattedMethod;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.MethodSplitter;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.RefactorableEntity;
import lk.ac.mrt.cse.mscresearch.indexer.formatters.InstructionFormatter;
import lk.ac.mrt.cse.mscresearch.indexer.formatters.LDCNumberRemover;
import lk.ac.mrt.cse.mscresearch.indexer.formatters.LineLableRemover;
import lk.ac.mrt.cse.mscresearch.util.FastExecutor;

public class PreProcessor {
	
	private final static Logger log = Logger.getLogger(PreProcessor.class);
	
	private final ExecutorService executorService = Executors.newCachedThreadPool();
	
	
	public List<InstructionFormatter> formatters = new ArrayList<>();
	
	public PreProcessor(){
		formatters.add(new LineLableRemover());
		formatters.add(new LDCNumberRemover());
	}

	public List<FormattedClass> process(List<DisassembledClass> rowClasses){
		List<FormattedClass> processed = new ArrayList<>(rowClasses.size());
		for(DisassembledClass dissasembled : rowClasses){
			processed.add(process(dissasembled));
		}
		return processed;
	}

	public FormattedClass process(DisassembledClass d) {
		List<DisassembledMethod> rowMethods = d.getMethods();
		List<FormattedMethod> formatted = new ArrayList<>(rowMethods.size());
		for(DisassembledMethod m : rowMethods){
			formatted.add(formatMethod(m));
		}
		return new FormattedClass(d.getClassName(), formatted);
	}

	private FormattedMethod formatMethod(DisassembledMethod m) {
		List<String> rowMethodLines = m.getCode();
		List<String> formatted = new ArrayList<>(rowMethodLines.size());
		Map<Integer, Integer> lineNumberTable = extractLineNumbers(m.getLineNumberTableEntries());
		List<Integer> lables = extractLables(rowMethodLines);
		for(String code : rowMethodLines){
			formatted.add(formatMethodLine(code));
		}
		return new FormattedMethod(m.getClassName(), m.getMethodName(), m.getMethodSignature(), formatted, lables, lineNumberTable);
	}

	private Map<Integer, Integer> extractLineNumbers(List<String> lineNumberTableEntries) {
		Map<Integer, Integer> lineNumberMap = new HashMap<>();
		for(String s : lineNumberTableEntries){
			String[] tmp = s.split(":");
			lineNumberMap.put(Integer.parseInt(tmp[1].trim()), Integer.parseInt(tmp[0].trim()));
		}
		return lineNumberMap;
	}

	private List<Integer> extractLables(List<String> rowMethodLines) {
		List<Integer> lables = new ArrayList<>();
		for(String s : rowMethodLines)
		{
			int colonIndex = s.indexOf(':');
			try{
				lables.add(Integer.parseInt(s.substring(0, colonIndex).trim()));
			}catch(NumberFormatException e){
				log.error(e);
			}
		}
		return lables;
	}

	private String formatMethodLine(String code) {
		String tmp = code;
		for(InstructionFormatter f : formatters){
			tmp = f.format(tmp);
		}
		return tmp;
	}
	
	public List<RefactorableEntity> extractRefatorableEntities(List<FormattedClass> classes){
		final List<RefactorableEntity> e = new ArrayList<>();
		e.addAll(FastExecutor.execute(classes, getExtractRefatorableEntitiesFunction()));
		return e;
		
//		for(FormattedClass fc : classes){
//			for(FormattedMethod fm : fc.getMethods()){
////				e.addAll(extractRefatorableEntities(fm));
//				f.add(executorService.submit(()-> extractRefatorableEntities(fm)));
//			}
//		}
//		for(Future<List<RefactorableEntity>> ff : f){
//			try {
//				e.addAll(ff.get());
//			} catch (Exception e1) {
//				log.error("", e1);
//			} 
//		}
//		return e;
	}
	
	private Function<FormattedClass, Collection<RefactorableEntity>> getExtractRefatorableEntitiesFunction() {
		// TODO Auto-generated method stub
		return (FormattedClass fc)->{
			final List<RefactorableEntity> e = new ArrayList<>();
			e.addAll(extractRefatorableEntities(fc));
			return e;
		};
	}

	public List<RefactorableEntity> extractRefatorableEntities(FormattedClass klass){
		List<RefactorableEntity> e = new ArrayList<>();
			for(FormattedMethod fm : klass.getMethods()){
				e.addAll(extractRefatorableEntities(fm));
		}
		return e;
	}
	
	public List<RefactorableEntity> extractRefatorableEntities(FormattedMethod method){
		return new MethodSplitter(method, 5).split();
	}
	
	public RefactorableEntity covertMethodToRefatorableEntity(FormattedMethod method){
		return new MethodSplitter(method, Integer.MAX_VALUE).convertToRefactorableEntity();
	}

	public List<RefactorableEntity> convertMethodsToRefatorableEntities(List<FormattedClass> f) {
		final List<RefactorableEntity> e = new ArrayList<>();
		e.addAll(FastExecutor.execute(f, getConvertMethodToRefatorableEntitiesFunction()));
		return e;
	}

	private Function<FormattedClass, Collection<RefactorableEntity>> getConvertMethodToRefatorableEntitiesFunction() {
		// TODO Auto-generated method stub
		return (FormattedClass fc)->{
			final List<RefactorableEntity> e = new ArrayList<>();
			for(FormattedMethod m : fc.getMethods())
			{
				try
				{
					e.add(covertMethodToRefatorableEntity(m));
				}
				catch(RuntimeException ex)
				{
					log.debug("error convrting to refactorable entity:" + m.toString(), ex);
				}
			}
			return e;
		};
	}
}

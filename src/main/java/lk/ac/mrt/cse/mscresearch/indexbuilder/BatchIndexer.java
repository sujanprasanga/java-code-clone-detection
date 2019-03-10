package lk.ac.mrt.cse.mscresearch.indexbuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers.ClassParser;
import lk.ac.mrt.cse.mscresearch.remoting.ServerAdaptor;
import lk.ac.mrt.cse.mscresearch.remoting.dto.ClassDTO;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;
import lk.ac.mrt.cse.mscresearch.util.IOUtil;

public class BatchIndexer {

	private static final Logger log = Logger.getLogger(BatchIndexer.class);
	private static final int BATCH_SIZE = 10;
	
	private final IOUtil ioUtil = new IOUtil();
	private final Map<String, String> clazzes;
	private final  String classPath;
	private final Consumer<String> doneListener;
	private final ClassParser classParser = new ClassParser();
	private final ServerAdaptor serverAdapter = new ServerAdaptor();
	
	public BatchIndexer(Map<String, String> clazzes, String classPath, Consumer<String> doneListener) {
		this.clazzes = clazzes;
		this.classPath = classPath;
		this.doneListener = doneListener;
	}
	
	public Set<ClassDTO> index() {
		List<String> classList = clazzes.keySet().stream().collect(Collectors.toList());
		return Lists.partition(classList, BATCH_SIZE).stream()
		                                      .map(this::decompile)
		                                      .map(this::pushToIndexingServer)
		                                      .flatMap(List::stream)
		                                      .collect(Collectors.toSet());
//		List<ClassDTO> decompiled = clazzes.keySet().parallelStream()
//				                                    .map(this::decompileAndindex)
//				                                    .collect(Collectors.toList());
//		
//		return Lists.partition(decompiled, BATCH_SIZE).parallelStream()
//		                                       .map(this::pushToIndexingServer)
//		                                       .flatMap(List::stream)
//		                                       .collect(Collectors.toSet());
	}

	private List<ClassDTO> pushToIndexingServer(List<ClassDTO> classes) {
		List<ClassDTO> list = serverAdapter.indexClasses(classes);
		list.stream().map(ClassDTO::getClassName).forEach(doneListener);
		return list;
	}
	
	private List<ClassDTO> decompile(List<String> classes){
		return classes.parallelStream().map(this::decompile).collect(Collectors.toList());
	}
	
	private ClassDTO decompile(String clazz) {
		String byteCode = ioUtil.disassembleClass(clazz, classPath);
		log.debug("byte code for:" + clazz);
		log.debug(byteCode);
		Set<MethodDTO> methods = classParser.extractMethods(byteCode, clazz);
		ClassDTO dto = new ClassDTO();
		dto.setClassHash(clazzes.get(clazz));
		dto.setClassName(clazz);
		dto.setMethods(methods);
		log.debug("decompiled: " + clazz);
		return dto;
	}
	
}

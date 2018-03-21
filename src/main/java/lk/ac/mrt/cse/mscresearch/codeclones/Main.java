package lk.ac.mrt.cse.mscresearch.codeclones;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.DisassembledClass;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.FormattedClass;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.FormattedMethod;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.RefactorableEntity;
import lk.ac.mrt.cse.mscresearch.indexer.PreProcessor;
import lk.ac.mrt.cse.mscresearch.util.CommandLineUtil;
import lk.ac.mrt.cse.mscresearch.util.FastExecutor;

public class Main {

	static Logger log = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		System.out.println(new Date());
		TargetLibraryProvider libraryProvider = new TargetLibraryProvider();
		log.debug(libraryProvider.getLibraries());
		
		JavaDisassembler javaDisassembler = new JavaDisassembler(libraryProvider, new File("D:\\development\\msc-research\\jarExtracts"));
		javaDisassembler.disassemble();
		List<DisassembledClass> c =  javaDisassembler.getByteCodes();
		PreProcessor p = new PreProcessor();
		List<FormattedClass> f = p.process(c);
		log.debug("################# Code in class path ##################");
		log.debug(f);
		
		System.out.println(new Date());
		String s = new CommandLineUtil().disassembleClass("lk.clones.Example", "D:\\development\\msc-research\\ExampleClasses\\bin");
		log.debug(s);
		DisassembledClass cc = DisassembledClass.create("Example", s);
		log.debug(cc);
		FormattedClass ff = p.process(cc);
		log.debug(ff);
		List<RefactorableEntity> extractRefatorableEntities = p.extractRefatorableEntities(ff);
		System.out.println(new Date());
		log.debug(extractRefatorableEntities);
//		findClones(ff.getMethods().get(0), f);
		Map<RefactorableEntity, List<RefactorableEntity>> map = new HashMap<>();
		List<RefactorableEntity> extractRefatorableEntitiesInClassPath = p.convertMethodsToRefatorableEntities(f);

		for(RefactorableEntity r : extractRefatorableEntities){
			System.out.println(new Date());
			System.out.println(new Date());
			List<RefactorableEntity> clones = findClones(r, extractRefatorableEntitiesInClassPath);
			System.out.println(new Date());
			if(!clones.isEmpty()){
				map.put(r, clones);
			}
		}
		log.debug("clones found:");
		logClones(map);
	}

	private static void logClones(Map<RefactorableEntity, List<RefactorableEntity>> map) {
		for(Entry<RefactorableEntity, List<RefactorableEntity>> entry : map.entrySet()){
			RefactorableEntity key = entry.getKey();
			log.debug("Clones for :" + key.getClassName() + " line: " + key.getLineNumberStart());
			for(RefactorableEntity e : entry.getValue()){
				log.debug(e.getClassName() + "." + e.getMethodName() + "()");
			}
		}
	}

	private static List<RefactorableEntity> findClones(final RefactorableEntity r, List<RefactorableEntity> rr) {
		
		List<RefactorableEntity> clones = new ArrayList<>();
		log.debug("finding clones for :" + r);
		for(RefactorableEntity e : rr){
				if(e.equals(r)){
					log.debug("clone found in " + e);
					clones.add(e);
				}
		}
		log.debug(clones.size() + " clones found!");
		return clones;
	}
}

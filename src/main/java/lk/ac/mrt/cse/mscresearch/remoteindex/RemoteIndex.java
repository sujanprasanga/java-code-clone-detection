package lk.ac.mrt.cse.mscresearch.remoteindex;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.codeclones.Clone;
import lk.ac.mrt.cse.mscresearch.indexbuilder.IndexBuilder;
import lk.ac.mrt.cse.mscresearch.remoting.ServerAdaptor;
import lk.ac.mrt.cse.mscresearch.util.Hashing;

public class RemoteIndex {

	private static final Logger log = Logger.getLogger(RemoteIndex.class);
	
	public static void indexJars(File jar, String hash) {
		try {
			if(!skipJar(jar)) {
				new IndexBuilder().buildIndex(jar, hash);
			} else {
				log.debug(jar.getAbsolutePath() + " skipped");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean skipJar(File jar) {
		String jdkFiles = "C:\\Java\\".toLowerCase();
		return jar.getAbsolutePath().toLowerCase().contains(jdkFiles);
//		return false;
	}

	public static List<Clone> findClones(){
		return null;
	}

	public static void indexDependencies(Set<String> dependencies) {
		if(!dependencies.isEmpty()) {
			log.debug("calculating hashes");
			Map<String, File> files = dependencies.stream().filter(s->s.toLowerCase().endsWith(".jar"))
					                                       .map(File::new)
					                                       .filter(File::exists)
					                                       .collect(Collectors.toMap(Hashing::hash, Function.identity()));
			log.debug("hashes calculated");
			log.debug("checking indexed jar");
			Map<String, Boolean> indexed = new ServerAdaptor().isIndexed(files.keySet());
			log.debug("indexed : " + indexed.toString());
			files.entrySet().stream().filter(e->!indexed.get(e.getKey())).forEach(RemoteIndex::indexJars);
		}
	}
	
	private static void indexJars(Entry<String, File> e) {
		indexJars(e.getValue(), e.getKey());
	}
}

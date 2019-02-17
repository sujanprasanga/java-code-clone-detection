package lk.ac.mrt.cse.mscresearch.remoteindex;

import java.io.File;
import java.util.List;

import lk.ac.mrt.cse.mscresearch.codeclones.Clone;
import lk.ac.mrt.cse.mscresearch.indexbuilder.IndexBuilder;

public class RemoteIndex {

	public static void indexJars(File jar) {
		try {
			new IndexBuilder().buildIndex(jar);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<Clone> findClones(){
		return null;
	}
}

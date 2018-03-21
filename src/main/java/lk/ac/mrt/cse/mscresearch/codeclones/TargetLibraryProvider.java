package lk.ac.mrt.cse.mscresearch.codeclones;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TargetLibraryProvider {

	public List<File> getLibraries()
	{
		String l = "D:\\development\\msc-research\\exampleLibraries\\exampleJar.jar";
		List<File> files = new ArrayList<>();
		files.add(new File(l));
//		files.add(new File("D:\\development\\msc-research\\lib\\commons-exec-1.3\\commons-exec-1.3.jar"));
		return files;
	}
	
}

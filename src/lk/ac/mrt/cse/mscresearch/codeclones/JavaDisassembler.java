package lk.ac.mrt.cse.mscresearch.codeclones;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.DisassembledClass;
import lk.ac.mrt.cse.mscresearch.util.CommandLineUtil;

public class JavaDisassembler {

	private final static Logger log = Logger.getLogger(JavaDisassembler.class);
	
	private final CommandLineUtil commandLineUtil = new CommandLineUtil();
	private final TargetLibraryProvider libraryProvider;
	private final File workspace;
	private final List<DisassembledClass> disassembledByteCodes = new ArrayList<>();
	
	public JavaDisassembler(TargetLibraryProvider libraryProvider, File workspace){
		this.libraryProvider = libraryProvider;
		this.workspace = workspace;
	}
	
	public List<DisassembledClass> getByteCodes(){
		return disassembledByteCodes;
	}
	
	public void disassemble(){
		
		for(File jar : libraryProvider.getLibraries()){
			try {
				extractJar(jar);
				List<String> classNames = commandLineUtil.getClassNamesInJar(jar);
				for(String className : classNames){
					disassembleClass(className);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void disassembleClass(String className) {
		String disassembledCode = commandLineUtil.disassembleClass(className, workspace.getAbsolutePath());
		disassembledByteCodes.add(DisassembledClass.create(className, disassembledCode));
		log.debug(className);
		log.debug(disassembledCode);
	}

	private void extractJar(File jarFile) throws IOException {
		java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile);
		Enumeration<JarEntry> enumEntries = jar.entries();
		while (enumEntries.hasMoreElements()) {
			try{
			    java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
			    java.io.File f = new java.io.File(workspace.getAbsolutePath() + java.io.File.separator + file.getName());
			    createPackagePath(f);
			    if (file.isDirectory()) { // if its a directory, create it
			        f.mkdir();
			        continue;
			    }
			    java.io.InputStream is = jar.getInputStream(file); // get the input stream
			    java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
			    while (is.available() > 0) {  // write contents of 'is' to 'fos'
			        fos.write(is.read());
			    }
			    fos.close();
			    is.close();
			    } catch (Throwable e){
			    	log.error("", e);
			    }
			
		}
		jar.close();
	}
	

	private void createPackagePath(File f) {
		ArrayDeque<File> path = new ArrayDeque<>();
		if(!f.getName().endsWith("class")){
			path.push(f);
		}
		File tmp = f.getParentFile();
		while(tmp != null){
			path.push(tmp);
			tmp = tmp.getParentFile();
		}
		tmp = path.pop();
		while(tmp != null){
			if(!tmp.exists()){
				tmp.mkdir();
			}
			try
			{
			  tmp = path.pop();
			}
			catch(NoSuchElementException e){
				tmp = null;
			}
		}
	}
}

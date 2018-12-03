package lk.ac.mrt.cse.mscresearch.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

public class CommandLineUtil {

	public String disassembleClass(String className, String classpath){
		CommandLine cmdLine = new CommandLine("javap");
		cmdLine.addArgument("-l");
		cmdLine.addArgument("-c");
		cmdLine.addArgument("-classpath");
		cmdLine.addArgument(classpath);
		cmdLine.addArgument(className);
		return excecuteAndGetOutputAsString(cmdLine);
	}
	
	public List<String> getClassNamesInJar(File jar) {
		CommandLine cmdLine = new CommandLine("jar");
		cmdLine.addArgument("-tf");
		cmdLine.addArgument(jar.getAbsolutePath());
		String jarEntries = excecuteAndGetOutputAsString(cmdLine);
		return splitClassNames(jarEntries);
	}
	
	private List<String> splitClassNames(String jarEntries) {
		List<String> classes = new ArrayList<>();
		for(String entry : jarEntries.split("\\n")){
			String lowerCase = entry.trim().toLowerCase();
			if(!lowerCase.startsWith("meta-inf") && lowerCase.endsWith(".class")){
				classes.add(formatClassName(entry));
			}
		}
		return classes;
	}
	
	private String formatClassName(String entry) {
		return entry.substring(0, entry.length() - 7).replaceAll("/", ".");
	}
	
	private String excecuteAndGetOutputAsString(CommandLine cmdLine) {
		try {
			DefaultExecutor executor = new DefaultExecutor();
			executor.setExitValue(0);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PumpStreamHandler streamHandler = new PumpStreamHandler(out);
			executor.setStreamHandler(streamHandler);
			executor.execute(cmdLine);
			return new String(out.toByteArray());
		} catch (ExecuteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}

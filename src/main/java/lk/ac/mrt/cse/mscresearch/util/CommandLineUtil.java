package lk.ac.mrt.cse.mscresearch.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;

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

	public File copy(File src, File dest) throws IOException {
		File copy  = new File(dest, src.getName());
		FileUtils.copyFile(src, copy);
		return copy;
	}
	
	public Map<String, String> unzip(File jar) throws IOException {
		File directory = jar.getParentFile();
        ZipInputStream zis = new ZipInputStream(new FileInputStream(jar));
        ZipEntry zipEntry = zis.getNextEntry();
        Map<String, String> fileHashes = new HashMap<>();
        while (zipEntry != null  ) {
        	if(!zipEntry.isDirectory()){
        		createFile(directory, zis, zipEntry, fileHashes);
        	}else{
        		createDir(directory, zipEntry);
        	}
        	zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
		return fileHashes;
	}

	protected void createFile(File diractory, ZipInputStream zis, ZipEntry zipEntry, Map<String, String> fileHashes) throws IOException, FileNotFoundException {
		byte[] buffer = new byte[1024];
		String name = zipEntry.getName();
		File file = new File(diractory, name);
		try(FileOutputStream fos = new FileOutputStream(file)){
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
		}
		fileHashes.put(formatName(name), MD5Hasher.md5(file));
	}
	
	private String formatName(String name) {
		if(name.endsWith(".class")){
			StringBuilder sb = new StringBuilder(name.replaceAll("/", "."));
			sb.setLength(name.length() - 6);
			return sb.toString();
		}
		return "";
	}

	private void createDir(File directory, ZipEntry zipEntry) {
		File f = new File(directory.getAbsolutePath() + File.separatorChar + zipEntry.getName());
		if(!f.exists()){
			f.mkdirs();
		}
		
	}
}

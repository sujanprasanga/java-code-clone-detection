package lk.ac.mrt.cse.mscresearch.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;

public class IOUtil {

	private static final Pattern hashIndexFileFormat = Pattern.compile("(?<hash>[0-9A-F]{32})=(?<class>.+)");
	
	public String disassembleClass(String className, String classpath){
		CommandLine cmdLine = new CommandLine("javap");
//		cmdLine.addArgument("-l");
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
		if(!copy.exists()){
			FileUtils.copyFile(src, copy);
		}
		return copy;
	}
	
	public Map<String, String> unzip(File jar) throws IOException {
		File directory = jar.getParentFile();
		File unzipMarker = new File(directory, "step.1.unzip.done");
		File fileHashesSaved = new File(directory, "step.1.unzip.hashes");
		if(unzipMarker.exists()) {
			return loadFileHashes(fileHashesSaved);
		}
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
        saveHashes(fileHashesSaved, fileHashes);
        unzipMarker.createNewFile();
		return fileHashes;
	}

	private void saveHashes(File fileHashesSaved, Map<String, String> fileHashes) throws IOException {
		try(FileWriter w = new FileWriter(fileHashesSaved, false)){
			fileHashes.forEach((k,v)-> {
				try {
					w.write(v + "=" + v);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} );
		}
		
	}

	private Map<String, String> loadFileHashes(File fileHashesSaved) throws IOException {
		List<String> s = getStrings(fileHashesSaved.getAbsolutePath());
		Map<String, String> map = new HashMap<>();
		s.stream().forEach(e->{
			Matcher m = hashIndexFileFormat.matcher(e);
			m.find();
			map.put(m.group("class"), m.group("hash"));
		});
		return map;
	}

	protected void createFile(File diractory, ZipInputStream zis, ZipEntry zipEntry, Map<String, String> fileHashes) throws IOException, FileNotFoundException {
		byte[] buffer = new byte[1024];
		String name = zipEntry.getName();
		File file = new File(diractory, name);
		if(file.exists()) {
			return;
		}
		File parent = file.getParentFile();
		if(!parent.exists()) {
			parent.mkdirs();
		}
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

	public List<String> getStrings(File directory, String fileName) throws IOException {
		return getStrings(directory.getAbsolutePath() + File.separatorChar + fileName);
	}
	
	public List<String> getStrings(String filePath) throws IOException {
		if(new File(filePath).exists()) {
			return Files.readAllLines(Paths.get(filePath));
		}
		return Collections.emptyList();
	}
	
	public String getAsString(String fileName) throws IOException {
		List<String> decompiled = Files.readAllLines(Paths.get(fileName));
		StringBuilder sb = new StringBuilder();
		decompiled.stream().forEach(s->sb.append(s).append(","));
		return sb.toString();
	}
	
	public static void main(String[] ar) throws IOException {
		IOUtil ioUtil = new IOUtil();
		String s = ioUtil.getAsString("tmp");
//		System.out.println(s);
		String regEx = "[^\\d,:^ ^#]+";
		Matcher m = Pattern.compile("[a-zA-Z]+\\.[a-zA-Z.$_\\d]+").matcher(s);
		Set<String> flattened = new HashSet<>();
		while(m.find()) {
			flattened.add(s.substring(m.start(), m.end()));
		}
		File parent = new File("D:\\development\\msc-research\\Temp\\7A9E4B32BD67E7E16240D8B169298B8E");
		List<String> current = ioUtil.getStrings(parent, "processed.list");
		current.removeAll(flattened);
		current.stream().forEach(System.out::println);
//		System.out.println(flattened);
		
		try(FileWriter w = new FileWriter(new File(parent.getAbsolutePath() + File.separatorChar + "processed.list"), false)){
			current.stream().forEach(
					ss->{
				try {
					w.write(ss+"\r\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	}}
}

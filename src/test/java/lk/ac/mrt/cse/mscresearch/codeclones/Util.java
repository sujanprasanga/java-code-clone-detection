package lk.ac.mrt.cse.mscresearch.codeclones;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import lk.ac.mrt.cse.mscresearch.util.IOUtil;

public class Util {

	public static String readFile(String name)  throws IOException {
		List<String> decompiled = Files.readAllLines(Paths.get("src\\test\\java\\resources\\" + name));
		StringBuilder sb = new StringBuilder();
		decompiled.stream().forEach(s->sb.append(s).append("\r\n"));
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String f = "D:\\workspace\\personal\\msc-research\\TEst\\bin\\as\\asdas\\asd\\TestCondintionals.class";
		IOUtil ioUtil = new IOUtil();
		System.out.println(ioUtil.disassembleClass("as.asdas.asd.TestCondintionals", "D:\\workspace\\personal\\msc-research\\TEst\\bin\\"));
	}
}

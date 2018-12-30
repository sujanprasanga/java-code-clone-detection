package lk.ac.mrt.cse.mscresearch.codeclones;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Util {

	public static String readFile(String name)  throws IOException {
		List<String> decompiled = Files.readAllLines(Paths.get("src\\test\\java\\resources\\" + name));
		StringBuilder sb = new StringBuilder();
		decompiled.stream().forEach(s->sb.append(s).append("\r\n"));
		return sb.toString();
	}
}

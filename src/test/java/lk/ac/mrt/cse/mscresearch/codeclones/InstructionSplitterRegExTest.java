package lk.ac.mrt.cse.mscresearch.codeclones;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class InstructionSplitterRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForInstructionSplitter());
	
	@Test
	public void testinvoke() throws IOException{
		String s = readFile();
		Matcher matcher = p.matcher(s);
		int i =0;
		while(matcher.find()) {
			i++;
			System.out.println(s.substring(matcher.start(), matcher.end()));
		}
		assertEquals(10, i);
		
	}
	
	protected String readFile() throws IOException {
		List<String> decompiled = Files.readAllLines(Paths.get("src\\test\\java\\resources\\mixOfinstructions"));
		StringBuilder sb = new StringBuilder();
		decompiled.stream().forEach(s->sb.append(s).append('\n'));
		return sb.toString();
	}
}
    
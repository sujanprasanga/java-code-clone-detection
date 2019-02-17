package lk.ac.mrt.cse.mscresearch.localindex;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class IndexUpdaterTest {

	private IndexUpdater underTest;
	private String outPutLocation;
	
	@BeforeMethod
	public void setUp() {
		outPutLocation = System.getProperty("user.dir") + "\\src\\test\\java\\resources\\testOutPutLocation\\";
		underTest = new IndexUpdater("test", Collections.emptySet(), Collections.emptySet(), outPutLocation);
	}
	
	@Test
	public void update() {
		underTest.update();
	}
	
	@Test(dataProvider = "files")
	public void test_detects_all_class_files(String pkg, String className) {
		Set<File> classFiles = underTest.getClassFiles();
		assertTrue(classFiles.contains(new File(outPutLocation + File.separator + pkg + className + ".class")));
		underTest.getClassFiles();
	}
	
	@Test(dataProvider = "files")
	public void test_extracts_fqn(String pkg, String className) {
		File f = new File(outPutLocation + File.separator + pkg + className + ".class");
		String fqn = underTest.toFQN(f);
		System.out.println(fqn);
		assertEquals(fqn, (pkg + className).replaceAll("\\\\", "."));
	}
	
	@DataProvider(name = "files")
	public Object[][] getFiles(){
		return new Object[][] {
{ "lk\\clones\\"           ,"         Invoke$1" .trim()},
{ "lk\\clones\\"           ,"            Other" .trim()},
{ "lk\\clones\\"           ,"           Switch" .trim()},
{ "lk\\clones\\"           ,"       Primitives" .trim()},
{ "lk\\clones\\"           ,"           Invoke" .trim()},
{ "lk\\clones\\sub1\\"     ,"         Field$A$" .trim()},
{ "lk\\clones\\sub1\\"     ,"         Invoke$A" .trim()},
{ "lk\\clones\\sub1\\"     ,"           Return" .trim()},
{ "lk\\clones\\sub1\\"     ,"      Conditinals" .trim()},
{ "lk\\clones\\sub1\\"     ,"    TestException" .trim()},
{ "lk\\clones\\sub1\\sub2\\","          Example".trim()},
{ "lk\\clones\\sub1\\sub2\\","        Field$A2$".trim()},
{ "lk\\clones\\sub1\\sub2\\","       CascadedIf".trim()},
{ "lk\\clones\\sub1\\sub2\\","AllKindsOfMethods".trim()},
{ "lk\\clones\\sub1\\sub2\\","            Field".trim()},
{ "lk\\clones\\sub1\\sub2\\","          SingleM".trim()},
{ "lk\\clones\\sub1\\sub2\\","         Switch$e".trim()},
{ "lk\\clones\\sub1\\sub2\\","         Other$A$".trim()},

		};
	}
}

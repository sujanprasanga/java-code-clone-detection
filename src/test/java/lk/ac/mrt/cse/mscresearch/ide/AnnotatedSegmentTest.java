package lk.ac.mrt.cse.mscresearch.ide;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AnnotatedSegmentTest {

	@Test(dataProvider = "withAccessor")
	public void test_strpAccessor(String testInput, String expected) {
		assertEquals(AnnotatedSegment.stripAccessor(testInput.trim()), expected.trim());
	}
	
	@DataProvider(name = "withAccessor")
	public Object[][] withAccessor(){
		return new Object[][] {
		{"private void setGaussianHeight(float);                                               ","void setGaussianHeight(float);"},
		{"public void unionWith(float, float, float, float, float, float);                     ","void unionWith(float, float, float, float, float, float);"},
		{"longpublic getIfModifiedSince();                                                     ","longpublic getIfModifiedSince();"},
		{"public void setS_crollsOnExpand(boolean);                                            ","void setS_crollsOnExpand(boolean);"},
		{"public static java.util.OptionalDouble empty();                                      ","static java.util.OptionalDouble empty(); "},
		{"public synchronized void unsetDecodeTables();                                        ","synchronized void unsetDecodeTables(); "},
		{"protected static synchronized void unsetDecodeTables();                              ","static synchronized void unsetDecodeTables();"},
		{"public static <K, V> java.util.Map<K, V> copy(java.util.Map<K, V>, java.lang.String);","static <K, V> java.util.Map<K, V> copy(java.util.Map<K, V>, java.lang.String);"},
			
		};
	}
	
	@Test(dataProvider = "withStatic")
	public void test_strpStatic(String testInput, String expected) {
		assertEquals(AnnotatedSegment.stripStatic(testInput.trim()), expected.trim());
	}
	
	@DataProvider(name = "withStatic")
	public Object[][] withStatic(){
		return new Object[][] {
		{"void unionWith(float, float, float, float, float, float);                     ","void unionWith(float, float, float, float, float, float);"},
		{"longpublic getIfModifiedSince();                                              ","longpublic getIfModifiedSince();"},
		{"void setS_crollsOnExpand(boolean);                                            ","void setS_crollsOnExpand(boolean);"},
		{"static java.util.OptionalDouble empty();                                      ","java.util.OptionalDouble empty(); "},
		{"synchronized void unsetDecodeTables();                                        ","synchronized void unsetDecodeTables(); "},
		{"static <K, V> java.util.Map<K, V> copy(java.util.Map<K, V>, java.lang.String);","<K, V> java.util.Map<K, V> copy(java.util.Map<K, V>, java.lang.String);"},
			
		};
	}
	
	@Test(dataProvider = "withFinal")
	public void test_strpFinal(String testInput, String expected) {
		assertEquals(AnnotatedSegment.stripFinal(testInput.trim()), expected.trim());
	}
	
	@DataProvider(name = "withFinal")
	public Object[][] withFinal(){
		return new Object[][] {
		{"void unionWith(float, float, float, float, float, float);                     ","void unionWith(float, float, float, float, float, float);"},
		{"longpublic getIfModifiedSince();                                              ","longpublic getIfModifiedSince();"},
		{"void setS_crollsOnExpand(boolean);                                            ","void setS_crollsOnExpand(boolean);"},
		{"final java.util.OptionalDouble empty();                                      ","java.util.OptionalDouble empty(); "},
		{"synchronized void unsetDecodeTables();                                        ","synchronized void unsetDecodeTables(); "},
		{"final <K, V> java.util.Map<K, V> copy(java.util.Map<K, V>, java.lang.String);","<K, V> java.util.Map<K, V> copy(java.util.Map<K, V>, java.lang.String);"},
			
		};
	}
	
	@Test(dataProvider = "withSynchronized")
	public void test_strpSynchronized(String testInput, String expected) {
		assertEquals(AnnotatedSegment.stripSynchronized(testInput.trim()), expected.trim());
	}
	
	@DataProvider(name = "withSynchronized")
	public Object[][] withSynchronized(){
		return new Object[][] {
		{"void unionWith(float, float, float, float, float, float);                     ","void unionWith(float, float, float, float, float, float);"},
		{"longpublic getIfModifiedSince();                                              ","longpublic getIfModifiedSince();"},
		{"void setS_crollsOnExpand(boolean);                                            ","void setS_crollsOnExpand(boolean);"},
		{"synchronized java.util.OptionalDouble empty();                                      ","java.util.OptionalDouble empty(); "},
		{"synchronized void unsetDecodeTables();                                        ","void unsetDecodeTables(); "},
		{"synchronized <K, V> java.util.Map<K, V> copy(java.util.Map<K, V>, java.lang.String);","<K, V> java.util.Map<K, V> copy(java.util.Map<K, V>, java.lang.String);"},
			
		};
	}
	
	@Test(dataProvider = "withGeneric")
	public void test_strpGeneric(String testInput, String expected) {
		assertEquals(AnnotatedSegment.stripGeneric(testInput.trim()), expected.trim());
	}
	
	@DataProvider(name = "withGeneric")
	public Object[][] withGeneric(){
		return new Object[][] {
{"void unsetDecodeTables();                                                                                                              ", "void unsetDecodeTables();"},
{"<T> void unsetDecodeTables();                                                                                                          ", "void unsetDecodeTables();"},
{"<K, V5> java.util.Map<K, V> merge(java.util.Map<K, V>, java.util.Map<K, V>);                                                           ", "java.util.Map<K, V> merge(java.util.Map<K, V>, java.util.Map<K, V>);"},
{"<T extends java.util.Map<T, T>> void test_formatMethodName();                                                                          ", "void test_formatMethodName();"},
{"<K, V> java.util.Map<K, V> copy(java.util.Map<K, V>, java.lang.String);                                                                ", "java.util.Map<K, V> copy(java.util.Map<K, V>, java.lang.String);"},
{"<T extends java.util.Map<T, T>> java.util.Map<T, T> test_formatMethodName() throws java.lang.NullPointerException, java.io.IOException;", "java.util.Map<T, T> test_formatMethodName() throws java.lang.NullPointerException, java.io.IOException;"},
{"<T extends java.util.Map<T, java.lang.String[]>> void test_formatMethodName() throws java.lang.NullPointerException, java.io.IOException", "void test_formatMethodName() throws java.lang.NullPointerException, java.io.IOException"}
	};
	}
	
	@Test(dataProvider = "names")
	public void test_strpGeneric(String testInput, String returnType, String name, String args) {
		String[] splitNameAndArgs = AnnotatedSegment.splitNameAndArgs(testInput.trim());
		assertEquals(splitNameAndArgs[0], returnType.trim());
		assertEquals(splitNameAndArgs[1], name.trim());
		assertEquals(splitNameAndArgs[2], args.trim());
	}
	
	@DataProvider(name = "names")
	public Object[][] names(){
		return new Object[][] {
{"void registerMediaPlayerForDispose(java.lang.Object, com.sun.media.jfxmedia.MediaPlayer);              ","void","registerMediaPlayerForDispose","java.lang.Object, com.sun.media.jfxmedia.MediaPlayer"},
{"void setGaussianHeight(float);                                                                         ","void","setGaussianHeight","float"},
{"void unionWith(float, float, float, float, float, float);                                              ","void","unionWith","float, float, float, float, float, float"},
{"com.sun.javafx.geom.BaseBounds getInstance(float, float, float, float, float, float);                  ","com.sun.javafx.geom.BaseBounds","getInstance","float, float, float, float, float, float"},
{"javafx.beans.property.ReadOnlyDoubleProperty anchorY5Property();                                       ","javafx.beans.property.ReadOnlyDoubleProperty","anchorY5Property",""},
{"long getIfModifiedSince();                                                                             ","long","getIfModifiedSince",""},
{"java.util.OptionalDouble _empty();                                                                      ","java.util.OptionalDouble","_empty",""},
{"void setDe$stinationBands(int[]);                                                                      ","void","setDe$stinationBands","int[]"},
{"String unsetDecodeTables();                                                                            ","String","unsetDecodeTables",""},
{"String[] unsetDecodeTables();                                                                          ","String[]","unsetDecodeTables",""},
{"java.util.Map<K, V> merge(java.util.Map<K, V>, java.util.Map<K, V>);                                   ","java.util.Map<K, V>","merge","java.util.Map<K, V>, java.util.Map<K, V>"},
{"void test_formatMethodName();                                                                          ","void","test_formatMethodName",""},
{"java.util.Map<K, V> copy(java.util.Map<K, V>, java.lang.String);                                       ","java.util.Map<K, V>","copy","java.util.Map<K, V>, java.lang.String"},
{"java.util.Map<T, T> test_formatMethodName() throws java.lang.NullPointerException, java.io.IOException;","java.util.Map<T, T>","test_formatMethodName",""},
	};
	}
	
	@Test(dataProvider = "typeDef")
	public void test_formatTypeDef(String testInput, String expected) {
		assertEquals(AnnotatedSegment.formatTypeDef(testInput.trim()), expected.trim());
	}
	
	@DataProvider(name = "typeDef")
	public Object[][] typeDef(){
		return new Object[][] {
		{"java.util.Map<T, T>","Map<T, T>"},
		{"java.util.Map<T, java.util.List<T>>","Map<T, List<T>>"},	
		{"java.util.Map<T, java.util.Map<T, T>>","Map<T, Map<T, T>>"},	
		{"void","void"},
		{"", ""},
		{"java.util.Map", "Map"},
		{"Map<T>", "Map<T>"},
		};
	}
	
	@Test
	public void test_formatMethodName() {
		String a = "public static <K, V> java.util.HasMap<K, V> copy(java.util.Map<K, V>, java.lang.String);";
		String e = "copy(Map<K, V>, String) : HasMap<K, V>";
		assertEquals(AnnotatedSegment.formatMethodName(a), e);
	}
	
	@Test
	public void test_formatMethodName2() {
		String a = "public static <K, V> java.util.HasMap<K, V> copy(java.util.Map<K, V>, int[][]);";
		String e = "copy(Map<K, V>, int[][]) : HasMap<K, V>";
		assertEquals(AnnotatedSegment.formatMethodName(a), e);
	}
}

package lk.ac.mrt.cse.mscresearch.codeclones;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.annotations.DataProvider;

public class NonMatchingDataProvider {

	public final static String DATA_PROVIDER_NAME = "nonMatching";
	
	public static Set<Class<?>> classesWithData = new HashSet<>();
	static {
//		classesWithData.add(ConstructorDecodeRegExTest.class);
		classesWithData.add(FieldOpDecodeRegExTest.class);
		classesWithData.add(InvokeDecodeRegExTest.class);
		classesWithData.add(NewArrayDecodeRegExTest.class);
		classesWithData.add(PrimitiveOpDecodeRegExTest.class);
		classesWithData.add(InvokeDynamicRegExTest.class);
		classesWithData.add(ConditionalOpDecodeRegExTest.class);
		classesWithData.add(OtherOpDecodeRegExTest.class);
		classesWithData.add(InstanceRegExTest.class);
		classesWithData.add(ReturnRegExTest.class);
		classesWithData.add(SwitchOpcodeRegExTest.class);
	}
	
	private final Object[][] code;
	
	public Object[][] getCode() {
		return code;
	}

	public NonMatchingDataProvider(Class<?> c) {
		if(classesWithData.contains(c)) {
			code = extractData(classesWithData.stream().filter(k-> !k.equals(c)).collect(Collectors.toSet()));
		} else {
			throw new RuntimeException(c.getCanonicalName() + " not added!");
		}
	}

	private Object[][] extractData(Set<Class<?>> filtered) {
		List<Object>  data = filtered.stream().map((this::extractData)).flatMap(Stream::of).collect(Collectors.toList());
		Object[][] d = new Object[data.size()][];
		for(int i=0; i<d.length; i++) {
			d[i] = new Object[] {data.get(i)};
		}
		return d;
	}
	
	private Object[] extractData(Class<?> c){
		try {
			Object o = c.newInstance();
			return Stream.of(c.getMethods()).filter(this::isDataProviderMethod)
			                                .map(this.invoker(o))
											.flatMap(Stream::of)
											.map(a->a[0])
											.collect(Collectors.toSet())
											.toArray(new Object[0]);
		}  catch (Exception e) {
			throw new RuntimeException("Error invoking methods in " + c.toString(), e);
		}
	}
	
	private boolean isDataProviderMethod(Method m) {
		DataProvider annotation = m.getAnnotation(DataProvider.class);
		return annotation != null && !annotation.name().equals(DATA_PROVIDER_NAME);
	}
	
	private Function<Method, Object[][]> invoker(Object o){
		return m->{
		try {
			m.setAccessible(true);
			Object[][] invoke = (Object[][])m.invoke(o);
			return invoke;
		} catch (Exception e) {
			throw new RuntimeException("Error invoking " + m.toString(), e);
		}};
	}
}

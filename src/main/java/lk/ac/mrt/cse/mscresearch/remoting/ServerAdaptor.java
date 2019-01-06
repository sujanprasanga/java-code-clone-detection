package lk.ac.mrt.cse.mscresearch.remoting;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lk.ac.mrt.cse.mscresearch.remoting.dto.ClassDTO;
import lk.ac.mrt.cse.mscresearch.remoting.dto.JarDTO;

public class ServerAdaptor {

	public boolean isJarIndexed(String hash) {
		return lookupByteCodePersistor().isJarIndexed(hash);
	}
	
	
	private static ByteCodePersistor lookupByteCodePersistor(){
        final Hashtable<Object,Object> jndiProperties = new Hashtable<Object,Object>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,  "org.wildfly.naming.client.WildFlyInitialContextFactory");
                jndiProperties.put(Context.PROVIDER_URL,"http-remoting://localhost:9080");
        
		try {
			Context context = new InitialContext(jndiProperties);
			return (ByteCodePersistor) context
					.lookup("ejb:/java-code-clone-prevention-server/ByteCodePersistorEjb!lk.ac.mrt.cse.mscresearch.remoting.ByteCodePersistor");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
    }

	public void indexJar(JarDTO jarDTO) {
		lookupByteCodePersistor().indexJar(jarDTO);
	}


	public Map<String, ClassDTO> getIndexedClasses(Map<String, String> fileHashes) {
		return lookupByteCodePersistor().getIndexedClasses(new HashSet<>(fileHashes.values()));
	}


	public ClassDTO indexClass(ClassDTO classDTO) {
		return lookupByteCodePersistor().indexClass(classDTO);
	}
}

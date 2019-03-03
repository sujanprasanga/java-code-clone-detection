package lk.ac.mrt.cse.mscresearch.remoting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.remoting.dto.ClassDTO;
import lk.ac.mrt.cse.mscresearch.remoting.dto.JarDTO;

public class ServerAdaptor {

	private static final Logger log = Logger.getLogger(ServerAdaptor.class);
	
	public boolean isJarIndexed(String hash) {
		try {
			return lookupByteCodePersistor().isJarIndexed(hash);
		}
		catch(Exception e) {
			log.error("", e);
			return false;
		}
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
		try {
			lookupByteCodePersistor().indexJar(jarDTO);
		}
		catch(Exception e) {
			log.error("", e);
		}
	}


	public Map<String, ClassDTO> getIndexedClasses(Map<String, String> fileHashes) {
		try {
			return lookupByteCodePersistor().getIndexedClasses(new HashSet<>(fileHashes.values()));
		}
		catch(Exception e) {
			log.error("", e);
			return Collections.emptyMap();
		}
	}


	public ClassDTO indexClass(ClassDTO classDTO) {
		return lookupByteCodePersistor().indexClass(classDTO);
	}
	
	public List<ClassDTO> indexClasses(List<ClassDTO> classDTO) {
		try {
			return lookupByteCodePersistor().indexClasses(classDTO);
		} catch(Exception e) {
			String classNames = classDTO.stream().map(ClassDTO::getClassName).collect(Collectors.toList()).toString();
			log.error("Could not index :" + classNames, e);
			return Collections.emptyList();
		}
	}

	public Map<String, Boolean> isIndexed(Set<String> jarHashes) {
		try {
			return lookupByteCodePersistor().isIndexed(new HashSet<>(jarHashes));
		}
		catch(Exception e) {
			log.error("", e);
			return Collections.emptyMap();
		}
	}
}

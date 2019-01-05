package lk.ac.mrt.cse.mscresearch;

import java.io.File;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lk.ac.mrt.cse.mscresearch.remoting.ByteCodePersistor;
import lk.ac.mrt.cse.mscresearch.remoting.dto.JarDTO;

public class Remoting {
	public static void main(String[] args) throws Exception {
		
//		File f = new File("D:\\development\\tools\\spring-framework-5.1.3.RELEASE-dist\\spring-framework-5.1.3.RELEASE\\libs");
//		for(File j :f.listFiles())
//		{
//			System.out.println("    <resource-root path=\""+ j.getName() +"\"/>");
//		}
		ByteCodePersistor calculator = lookupCalculatorEJB();
                  
 
 
        Set<JarDTO> totalMoney = calculator.getIndexedJars(Collections.emptySet());
        System.out.println("Money plus interests " + totalMoney);
 
        JarDTO dto = new JarDTO();
        dto.setArtifact("artifact");
        dto.setJarHash("hassdhajasdas");
        dto.setName("name");
        calculator.index(dto);
    }
 
 
    private static ByteCodePersistor lookupCalculatorEJB() throws NamingException {
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,  "org.wildfly.naming.client.WildFlyInitialContextFactory");
                jndiProperties.put(Context.PROVIDER_URL,"http-remoting://localhost:9080");
        final Context context = new InitialContext(jndiProperties);
 
        return (ByteCodePersistor) context
                .lookup("ejb:/java-code-clone-prevention-server/ByteCodePersistorEjb!lk.ac.mrt.cse.mscresearch.remoting.ByteCodePersistor");
    }
}

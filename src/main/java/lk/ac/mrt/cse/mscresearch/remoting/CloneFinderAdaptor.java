package lk.ac.mrt.cse.mscresearch.remoting;

import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lk.ac.mrt.cse.mscresearch.codeclones.Clone;
import lk.ac.mrt.cse.mscresearch.codeclones.CodeFragmentData;

public class CloneFinderAdaptor {

	public List<Clone> find(List<CodeFragmentData> codeFragment){
		return lookupCloneFinder().find(codeFragment);
	}
	
	private static CloneFinder lookupCloneFinder() {
        final Hashtable<Object,Object> jndiProperties = new Hashtable<Object,Object>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,  "org.wildfly.naming.client.WildFlyInitialContextFactory");
                jndiProperties.put(Context.PROVIDER_URL,"http-remoting://localhost:9080");
        
		try {
			Context context = new InitialContext(jndiProperties);
			return (CloneFinder) context
					.lookup("ejb:/java-code-clone-prevention-server/CloneFinderEjb!lk.ac.mrt.cse.mscresearch.remoting.CloneFinder");
//			.lookup("ejb:/java-code-clone-prevention-server/ByteCodePersistorEjb!lk.ac.mrt.cse.mscresearch.remoting.ByteCodePersistor");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
}

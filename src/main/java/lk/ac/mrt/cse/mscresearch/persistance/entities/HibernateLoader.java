package lk.ac.mrt.cse.mscresearch.persistance.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateLoader {
	
	private static final SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
			.addAnnotatedClass(JarIndex.class)
			.addAnnotatedClass(ClassIndex.class)
			.addAnnotatedClass(MethodIndex.class)
			                                    .buildSessionFactory();
	
	public static Session getSession(){
		return factory.getCurrentSession();
	}
}

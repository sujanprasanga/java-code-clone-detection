package lk.ac.mrt.cse.mscresearch.persistance;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transaction;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import lk.ac.mrt.cse.mscresearch.persistance.dao.JarDAO;
import lk.ac.mrt.cse.mscresearch.persistance.entities.ClassIndex;
import lk.ac.mrt.cse.mscresearch.persistance.entities.JarIndex;
import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodIndex;

public class JDBCConnection {

	public static void main(String[] s) throws SQLException, NoSuchAlgorithmException{
//		String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=byte-code-indexes;user=sujan;password=123";
//		Connection connection = DriverManager.getConnection(connectionUrl);
//		connection.close();
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
				.addAnnotatedClass(JarIndex.class)
				.addAnnotatedClass(ClassIndex.class)
				.addAnnotatedClass(MethodIndex.class)
				                                    .buildSessionFactory();
		
		Session se = factory.getCurrentSession();
		
		JarIndex ji = new JarIndex();
		ji.setArtifact("artifact");
		ji.setName("test");
		ji.setJarHash("jar".hashCode()+"");
//		
//		ClassIndex ci = new ClassIndex();
//		ci.setClassName("className");
//		ci.setJar(ji);
//		
//		MethodIndex mi = new MethodIndex();
//		mi.setBody("body");
//		mi.setBodyhash(new String(MessageDigest.getInstance("MD5").digest("body".getBytes())));
//		mi.setClassIndex(ci);
//		mi.setPluginid(6);
//		
//		MethodIndex mi2 = new MethodIndex();
//		mi2.setBody("body2");
//		mi2.setBodyhash(new String(MessageDigest.getInstance("MD5").digest("body2".getBytes())));
//		mi2.setClassIndex(ci);
//		mi2.setPluginid(6);
		
		org.hibernate.Transaction transaction = se.beginTransaction();
		
		JarDAO d = new JarDAO(se);
//		d.save(ji);
		JarIndex ji2 = d.getByHashOf("104987                          ").get(0);
		System.out.println(ji2.getName());
//		EntityManager em = se.getEntityManagerFactory().createEntityManager();
		
//		em.persist(mi);
//		EntityTransaction transaction = em.getTransaction();
		//if(transaction.isActive())
		transaction.commit();
		
//		se.persist(mi);
//		se.getTransaction().commit();
//		
//		se.beginTransaction();
//		se.persist(mi2);
//		se.getTransaction().commit();
	}
}

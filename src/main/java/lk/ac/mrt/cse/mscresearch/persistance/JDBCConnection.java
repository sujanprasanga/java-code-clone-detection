package lk.ac.mrt.cse.mscresearch.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class JDBCConnection {

	public static void main(String[] s) throws SQLException{
//		String connectionUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=byte-code-indexes;user=sujan;password=123";
//		Connection connection = DriverManager.getConnection(connectionUrl);
//		connection.close();
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
				                                    .addAnnotatedClass(TestEntity.class)
				                                    .buildSessionFactory();
		
		Session se = factory.getCurrentSession();
		TestEntity t = new TestEntity();
		t.setName(897987);
		se.beginTransaction();
		se.persist(t);
		se.getTransaction().commit();
	}
}

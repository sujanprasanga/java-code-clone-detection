package lk.ac.mrt.cse.mscresearch.persistance.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="class_index")
public class ClassIndex implements EntityId {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="primaryKey")
	private int primaryKey;
	
	@JoinColumn(name="jar_index")
	@ManyToOne(targetEntity=JarIndex.class,fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private JarIndex jar;
	
	@Column(name="class_name")
	private String className;

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public JarIndex getJar() {
		return jar;
	}

	public void setJar(JarIndex jar) {
		this.jar = jar;
	}
}

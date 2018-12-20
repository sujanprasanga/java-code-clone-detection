package lk.ac.mrt.cse.mscresearch.persistance.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="class_index")
public class ClassIndex implements EntityId {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="primaryKey")
	private int primaryKey;
	
	@ManyToMany(mappedBy = "classes")
	private Set<JarIndex> jars = new HashSet<>();
	
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

	public Set<JarIndex> getJars() {
		return jars;
	}

	public void setJar(Set<JarIndex> jars) {
		this.jars = jars;
	}
}

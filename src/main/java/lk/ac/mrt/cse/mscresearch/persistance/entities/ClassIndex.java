package lk.ac.mrt.cse.mscresearch.persistance.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	
	@Column(name="class_md5_hash")
	private String classHash;
	
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "class_method_mapping", 
        joinColumns = { @JoinColumn(name = "class_key") }, 
        inverseJoinColumns = { @JoinColumn(name = "method_key") }
    )
	private Set<MethodIndex> methods = new HashSet<>();

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

	public String getClassHash() {
		return classHash;
	}

	public void setClassHash(String classHash) {
		this.classHash = classHash;
	}

	public Set<JarIndex> getJars() {
		return jars;
	}

	public void setJar(Set<JarIndex> jars) {
		this.jars = jars;
	}

	public Set<MethodIndex> getMethods() {
		return methods;
	}

	public void setMethods(Set<MethodIndex> methods) {
		this.methods = methods;
	}
}

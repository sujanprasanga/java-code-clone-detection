package lk.ac.mrt.cse.mscresearch.persistance.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="jar_index")
public class JarIndex  implements EntityId  {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="primaryKey")
	private int primaryKey;
	
	@Column(name="jar_name")
	private String name;
	
	@Column(name="jar_md5_hash")
	private String jarHash;
	
	@Column(name="artifact")
	private String artifact;

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJarHash() {
		return jarHash;
	}

	public void setJarHash(String jarHash) {
		this.jarHash = jarHash;
	}

	public String getArtifact() {
		return artifact;
	}

	public void setArtifact(String artifact) {
		this.artifact = artifact;
	}
	
	
}

package lk.ac.mrt.cse.mscresearch.persistance.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="method_index")
public class MethodIndex  implements EntityId {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="primaryKey")
	private int primaryKey;
	
	@Column(name="body")
	private String body;
	
	@Column(name="bodyhash")
	private String bodyhash;
	
	@Column(name="pluginid")
	private int pluginid;
	
	@Column(name="methodsignature")
	private String signature;
	
	@ManyToMany(mappedBy = "methods")
	private Set<ClassIndex> classes = new HashSet<>();

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBodyhash() {
		return bodyhash;
	}

	public void setBodyhash(String bodyhash) {
		this.bodyhash = bodyhash;
	}

	public int getPluginid() {
		return pluginid;
	}

	public void setPluginid(int pluginid) {
		this.pluginid = pluginid;
	}

	public Set<ClassIndex> getClasses() {
		return classes;
	}

	public void setClasses(Set<ClassIndex> classes) {
		this.classes = classes;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}

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
@Table(name="method_index")
public class MethodIndex  implements EntityId {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="primaryKey")
	private int primaryKey;
	
	@JoinColumn(name="class_index")
	@ManyToOne(targetEntity=ClassIndex.class,fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private ClassIndex classIndex;
	
	@Column(name="body")
	private String body;
	
	@Column(name="bodyhash")
	private String bodyhash;
	
	@Column(name="pluginid")
	private int pluginid;

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

	public ClassIndex getClassIndex() {
		return classIndex;
	}

	public void setClassIndex(ClassIndex classIndex) {
		this.classIndex = classIndex;
	}
}

package lk.ac.mrt.cse.mscresearch.persistance.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="method_body")
public class MethodBody implements EntityId  {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="primaryKey")
	private int primaryKey;
	
	@Column(name="body")
	private String body;

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
}

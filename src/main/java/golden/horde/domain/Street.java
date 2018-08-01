package golden.horde.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Entity
@Table(name = "V_STREET_FULL")
@Data
public class Street {

	@Id
	@Column(name = "street_id")
	private Integer id;

	@Column(name = "is_valid")
	private Integer isValid;

	@Column(name = "street_name_rus")
	private String rusName;

	@Column(name = "type_name_rus")
	private String rusType;

	@Column(name = "street_name_ukr")
	private String ukrName;

	@Column(name = "type_name_ukr")
	private String ukrType;

	@Column(name = "previous_id")
	private Integer previousId;

	@Column(name = "previous_id_too")
	private Integer previousAnotherId;

	@Override
	public String toString() {
		return "Street [id=" + id + ", rusName=" + rusName + ", rusType=" + rusType + "]";
	}

	
	// Getters & Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public String getRusName() {
		return rusName;
	}

	public void setRusName(String rusName) {
		this.rusName = rusName;
	}

	public String getRusType() {
		return rusType;
	}

	public void setRusType(String rusType) {
		this.rusType = rusType;
	}

	public String getUkrName() {
		return ukrName;
	}

	public void setUkrName(String ukrName) {
		this.ukrName = ukrName;
	}

	public String getUkrType() {
		return ukrType;
	}

	public void setUkrType(String ukrType) {
		this.ukrType = ukrType;
	}

	public Integer getPreviousId() {
		return previousId;
	}

	public void setPreviousId(Integer previousId) {
		this.previousId = previousId;
	}

	public Integer getPreviousAnotherId() {
		return previousAnotherId;
	}

	public void setPreviousAnotherId(Integer previousAnotherId) {
		this.previousAnotherId = previousAnotherId;
	}
	

}

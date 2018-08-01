package golden.horde.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ADDRESSES")
@Data
public class Address {
	
	@Id
	@Column(name = "address_id")
	private Integer id;
	
	@Column(name = "street_id")
	private Integer streetId;

	@Column(name = "house_number")
	private Integer number;

	@Column(name = "house_suffix")
	private String suffix;

	// Getters & Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStreetId() {
		return streetId;
	}

	public void setStreetId(Integer streetId) {
		this.streetId = streetId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	
}

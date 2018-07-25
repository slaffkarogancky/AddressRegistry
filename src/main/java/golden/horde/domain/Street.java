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
	

}

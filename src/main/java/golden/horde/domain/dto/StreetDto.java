package golden.horde.domain.dto;

import java.util.Set;
import java.util.TreeSet;

import golden.horde.service.StreetInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor 
public class StreetDto {
	
	private String nameRus;
	private String typeRus;
	private String nameUkr;
	private String typeUkr;
	private String formerNameRus = "";
	private String formerTypeRus = "";
	private String formerNameUkr = "";
	private String formerTypeUkr = "";
	private Set<String> houses = new TreeSet<>();
	
	public StreetDto(StreetInfo street) {
		if (street.successor != null) {
			this.nameRus = street.successor.rusName;
			this.typeRus = street.successor.rusType;
			this.nameUkr = street.successor.ukrName;
			this.typeUkr = street.successor.ukrType;
			this.formerNameRus = street.rusName;
			this.formerTypeRus = street.rusType;
			this.formerNameUkr = street.ukrName;
			this.formerTypeUkr = street.ukrType;
		}		
		else {
			this.nameRus = street.rusName;
			this.typeRus = street.rusType;
			this.nameUkr = street.ukrName;
			this.typeUkr = street.ukrType;
		}
	}
}

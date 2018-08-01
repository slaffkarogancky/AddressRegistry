package golden.horde.domain.dto;

import java.util.Set;
import java.util.TreeSet;

import golden.horde.service.StreetInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data @NoArgsConstructor @AllArgsConstructor 
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

	public String getNameRus() {
		return nameRus;
	}

	public void setNameRus(String nameRus) {
		this.nameRus = nameRus;
	}

	public String getTypeRus() {
		return typeRus;
	}

	public void setTypeRus(String typeRus) {
		this.typeRus = typeRus;
	}

	public String getNameUkr() {
		return nameUkr;
	}

	public void setNameUkr(String nameUkr) {
		this.nameUkr = nameUkr;
	}

	public String getTypeUkr() {
		return typeUkr;
	}

	public void setTypeUkr(String typeUkr) {
		this.typeUkr = typeUkr;
	}

	public String getFormerNameRus() {
		return formerNameRus;
	}

	public void setFormerNameRus(String formerNameRus) {
		this.formerNameRus = formerNameRus;
	}

	public String getFormerTypeRus() {
		return formerTypeRus;
	}

	public void setFormerTypeRus(String formerTypeRus) {
		this.formerTypeRus = formerTypeRus;
	}

	public String getFormerNameUkr() {
		return formerNameUkr;
	}

	public void setFormerNameUkr(String formerNameUkr) {
		this.formerNameUkr = formerNameUkr;
	}

	public String getFormerTypeUkr() {
		return formerTypeUkr;
	}

	public void setFormerTypeUkr(String formerTypeUkr) {
		this.formerTypeUkr = formerTypeUkr;
	}

	public Set<String> getHouses() {
		return houses;
	}

	public void setHouses(Set<String> houses) {
		this.houses = houses;
	}
	
	
}

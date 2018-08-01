package golden.horde.domain.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

//@Data @NoArgsConstructor  
public class AddressSearchResult {

	private String initialStr;
	
	private String maybeStr;
	
	private List<StreetDto> streets;

	public AddressSearchResult(String initialStr,  String maybeStr, List<StreetDto> streets) {
		super();
		this.initialStr = initialStr;
		this.maybeStr = maybeStr;
		this.streets = streets;
	}

	public String getInitialStr() {
		return initialStr;
	}

	public void setInitialStr(String initialStr) {
		this.initialStr = initialStr;
	}

	public String getMaybeStr() {
		return maybeStr;
	}

	public void setMaybeStr(String maybeStr) {
		this.maybeStr = maybeStr;
	}

	public List<StreetDto> getStreets() {
		return streets;
	}

	public void setStreets(List<StreetDto> streets) {
		this.streets = streets;
	}
	
	
	
}

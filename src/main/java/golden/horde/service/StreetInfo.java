package golden.horde.service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import golden.horde.domain.Street;
import golden.horde.utils.UsefulUtils;

public class StreetInfo {

	public final int id;
	public final int previousId;
	public final int previousAnotherId;
	public final boolean isValid;
	public final String rusName;
	public final String ukrName;
	public final String rusType;
	public final String ukrType;
	public final String[] rusParts;
	public final String[] ukrParts;
	public final String[] allParts;
	public final String firstRusLetter;
	public final String firstUkrLetter;
	public StreetInfo successor; // действующая улица-правопреемник	
	public Set<String> adresses;
	
	@Override
	public String toString() {
		return "StreetInfo [id=" + id + ", rusParts=" + Arrays.toString(rusParts) + ", firstRusLetter=" + firstRusLetter + "]";
	}

	public StreetInfo(Street src) {
		this.id = src.getId();
		this.previousId = src.getPreviousId();
		this.previousAnotherId = src.getPreviousAnotherId();
		this.isValid = src.getIsValid() == 1;
		this.rusName = src.getRusName().trim();
		this.ukrName = src.getUkrName().trim();
		this.rusType = src.getRusType().trim();
		this.ukrType = src.getUkrType().trim();
		this.rusParts = splitStreetName(this.rusName); 
		this.ukrParts = splitStreetName(this.ukrName);
		this.firstRusLetter = rusParts[0].substring(0, 1);
		this.firstUkrLetter = ukrParts[0].substring(0, 1);
		this.allParts = Stream.of(this.rusParts, this.ukrParts).flatMap(Stream::of).distinct().toArray(String[]::new);
	}
	
	private String[] splitStreetName(String name) {
		String[] splitted = name.toLowerCase().split("[\\s-()]");
		return Arrays.stream(splitted).filter(StreetInfo::isPartValid).toArray(String[]::new);
	}
	
	private static boolean isPartValid(String part) {
		part = UsefulUtils.trim(part.trim(), ".");	
		return (part.length() > 1) && (!(part.equals("ст") || part.equals("пос") || part.equals("сел") || part.equals("пгт")));
	}

	
	// Getters & Setters
	public StreetInfo getSuccessor() {
		return successor;
	}

	public void setSuccessor(StreetInfo successor) {
		this.successor = successor;
	}

	public Set<String> getAdresses() {
		return adresses;
	}

	public void setAdresses(Set<String> adresses) {
		this.adresses = adresses;
	}

	public int getId() {
		return id;
	}

	public int getPreviousId() {
		return previousId;
	}

	public int getPreviousAnotherId() {
		return previousAnotherId;
	}

	public boolean isValid() {
		return isValid;
	}

	public String getRusName() {
		return rusName;
	}

	public String getUkrName() {
		return ukrName;
	}

	public String getRusType() {
		return rusType;
	}

	public String getUkrType() {
		return ukrType;
	}

	public String[] getRusParts() {
		return rusParts;
	}

	public String[] getUkrParts() {
		return ukrParts;
	}

	public String[] getAllParts() {
		return allParts;
	}

	public String getFirstRusLetter() {
		return firstRusLetter;
	}

	public String getFirstUkrLetter() {
		return firstUkrLetter;
	}		
}


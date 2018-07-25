package golden.horde.matcher;

public class EditionalPrescription {

	public String route;
	public int distance;

	EditionalPrescription(int distance, String route) {
		this.distance = distance;
		this.route = route;
	}

	@Override
	public String toString() {
		return "EditionalPrescription [route=" + route + ", distance=" + distance + "]";
	}
	
}

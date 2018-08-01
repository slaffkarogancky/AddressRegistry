package golden.horde.utils;

public class UsefulUtils {

	public static String trim(String source, String unwantedSymbol) {
		if (unwantedSymbol.length() > 1)
			throw new IllegalArgumentException("The length of unwantedSymbol must be equals 1");
		while (source.startsWith(unwantedSymbol)) {
			source = source.substring(1, source.length());
		}
		while (source.endsWith(unwantedSymbol)) {
			source = source.substring(0, source.length() - 1);
		}
		return source;
	}
	
	public static String checkTypos(String source) {
		if (source == null) {
			return null;
		}
		String result = source;
		char[] canbe = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
		char[] mustbe = "йцукенгшщзфывапролдячсмитьЙЦУКЕНГШЩЗФЫВАПРОЛДЯЧСМИТЬ".toCharArray();
		for(int i = 0; i < canbe.length; i++) {
			result = result.replace(canbe[i], mustbe[i]);
		}
		return result;
	}

	private UsefulUtils() {
		super();
	}
}

package golden.horde.matcher;

public class LevenshteinMatcher {

	public static EditionalPrescription compare(String S1, String S2) {
		int m = S1.length(), n = S2.length();
		int[][] D = new int[m + 1][n + 1];
		char[][] P = new char[m + 1][n + 1];

		// Базовые значения
		for (int i = 0; i <= m; i++) {
			D[i][0] = i;
			P[i][0] = 'D';
		}
		for (int i = 0; i <= n; i++) {
			D[0][i] = i;
			P[0][i] = 'I';
		}

		for (int i = 1; i <= m; i++)
			for (int j = 1; j <= n; j++) {
				int cost = (S1.charAt(i - 1) != S2.charAt(j - 1)) ? 1 : 0;
				if(D[i][j - 1] < D[i - 1][j] && D[i][j - 1] < D[i - 1][j - 1] + cost) {
					//Вставка
					D[i][j] = D[i][j - 1] + 1;
					P[i][j] = 'I';
				}
				else if(D[i - 1][j] < D[i - 1][j - 1] + cost) {
					//Удаление
					D[i][j] = D[i - 1][j] + 1;
					P[i][j] = 'D';
				}
				else {
					//Замена или отсутствие операции
					D[i][j] = D[i - 1][j - 1] + cost;
					P[i][j] = (cost == 1) ? 'R' : 'M';
				}
			}

		//Восстановление предписания
		StringBuilder route = new StringBuilder("");
		int i = m, j = n;
		do {
			char c = P[i][j];
			route.append(c);
			if(c == 'R' || c == 'M') {
				i --;
				j --;
			}
			else if(c == 'D') {
				i --;
			}
			else {
				j --;
			}
		} while((i != 0) || (j != 0));
		return new EditionalPrescription(D[m][n], route.reverse().toString());
	}
}

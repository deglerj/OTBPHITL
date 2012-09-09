package org.jd.otbphitl.client;

public class Arrays {

	public static boolean[][] copyOf(final boolean[][] source) {
		final int iMax = source.length;
		final int jMax = source[0].length;

		final boolean[][] copy = new boolean[iMax][jMax];

		for (int i = 0; i < iMax; i++) {
			for (int j = 0; j < jMax; j++) {
				copy[i][j] = source[i][j];
			}
		}

		return copy;
	}

	private Arrays() {
		// Utility class, no instances allowed
	}

}

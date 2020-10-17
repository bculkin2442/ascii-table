package com.bethecoder.ascii_table.test;

import java.text.NumberFormat;
import java.util.Scanner;

@SuppressWarnings("javadoc")
public class ChainMatrix {
	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);

		String[] parts = scn.nextLine().trim().split(" ");

		int[] dims = new int[parts.length];

		for (int i = 0; i < parts.length; i++) {
			dims[i] = Integer.parseInt(parts[i]);
		}

		doChainMultiply(dims);

		scn.close();
	}

	private static void doChainMultiply(int[] dims) {
		int n = dims.length;

		int[][] m = new int[n][n];
		int[][] s = new int[n][n];

		/*
		 * m[i,j] = Minimum number of scalar multiplications needed to
		 * compute the matrix A[i]A[i+1]...A[j] = A[i..j] where dimension
		 * of A[i] is p[i-1] x p[i]
		 */

		// cost is zero when multiplying one matrix.
		for (int i = 1; i < n; i++) {
			m[i][i] = 0;
		}

		for (int chainLength = 2; chainLength < n; chainLength++) {
			for (int i = 1; i < n - chainLength + 1; i++) {
				int j = i + chainLength - 1;

				if (j == n) {
					continue;
				}

				m[i][j] = Integer.MAX_VALUE;

				for (int k = i; k <= j - 1; k++) {
					int cost = m[i][k] + m[k + 1][j]
							+ dims[i - 1] * dims[k] * dims[j];

					if (cost < m[i][j]) {
						m[i][j] = cost;
						s[i][j] = k;
					}
				}
			}
		}

		NumberFormat format = NumberFormat.getIntegerInstance();

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.println("m[" + i + ", " + j + "] = "
						+ format.format(m[i][j]));
			}
		}

		System.out.println();

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.println("s[" + i + ", " + j + "] = "
						+ format.format(s[i][j]));
			}
		}

		printOptimal(s, 1, n - 1);
	}

	private static void printOptimal(int[][] s, int i, int j) {
		if (i == j) {
			System.out.print(Character.toString((char) ('A' + (i - 1))));
		} else {
			System.out.print("(");

			printOptimal(s, i, s[i][j]);
			printOptimal(s, s[i][j] + 1, j);

			System.out.print(")");
		}
	}
}

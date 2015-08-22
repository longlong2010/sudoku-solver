import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

class Sudoku {
	private int[][] matrix;
	final int SIZE = 9;

	public Sudoku() {
		matrix = new int[SIZE][SIZE];
	}

	public Sudoku(Sudoku s) {
		this();
		int size = SIZE;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				set(i, j, s.get(i, j));
			}
		}
	}

	public void set(int i, int j, int v) {
		matrix[i][j] = v;
	}

	public int get(int i, int j) {
		return matrix[i][j];
	}

	private boolean calculate() {
		int size = SIZE;
		int n;
		do {
			n = 0;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (get(i, j) != 0) {
						continue;
					}
					ArrayList<Integer> nums = getAvailNums(i, j);
					int s = nums.size();
					if (s == 1) {
						int v = nums.get(0);
						set(i, j, v);
						n++;
					}
					if (s == 0) {
						return false;
					}
				}
			}
		} while (n > 0);
		return true;
	}

	public boolean solve() {
		int size = SIZE;
		if (!calculate()) {
			return false;
		}
		int n;
		do {
			n = 0;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (get(i, j) != 0) {
						n++;
						continue;
					}
					ArrayList<Integer> nums = getAvailNums(i, j);
					int s = nums.size();
					if (s == 0) {
						return false;
					}
					for (int k = 0; k < s; k++) {
						Sudoku ns = new Sudoku(this);
						ns.set(i, j, nums.get(k));
						if (ns.solve()) {
							for (int p = 0; p < size; p++) {
								for (int q = 0; q < size; q++) {
									set(p, q, ns.get(p, q));
								}
							}
							return true;
						}
					}
					return false;
				}
			}
		} while (n != size * size);
		return true;
	}

	public ArrayList<Integer> getAvailNums(int i, int j) {
		int size = SIZE;
		ArrayList<Integer> nums = getNums();
		for (int k = 0; k < size; k++) {
			int index;
			int v1 = matrix[i][k];
			if ((index = nums.indexOf(v1)) != -1) {
				nums.remove(index);
			}
			int v2 = matrix[k][j];
			if ((index = nums.indexOf(v2)) != -1) {
				nums.remove(index);
			}
		}

		int ssize = (int) Math.sqrt(size);

		int m = i / ssize;
		int n = j / ssize;
		for (int p = m * ssize; p < (m + 1) * ssize; p++) {
			for (int q = n * ssize; q < (n + 1) * ssize; q++) {
				int index;
				int v = matrix[p][q];
				if ((index = nums.indexOf(v)) != -1) {
					nums.remove(index);
				}
			}
		}
		return nums;
	}

	private ArrayList<Integer> getNums() {
		int size = SIZE;
		ArrayList<Integer> nums = new ArrayList<Integer>();
		for (int k = 0; k < size; k++) {
			nums.add(k + 1);
		}
		return nums;
	}

	public void load(String file) {
		int size = SIZE;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
			for (int i = 0; i < size; i++) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				Scanner in = new Scanner(line);
				for (int j = 0; j < size; j++) {
					if (!in.hasNext()) {
						break;
					}
					matrix[i][j] = in.nextInt();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void print() {
		int size = SIZE;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				System.out.printf("%d ", matrix[i][j]);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Sudoku s = new Sudoku();
		s.load(args[0]);
		s.print();
		System.out.println();

		s.solve();
		s.print();
	}
}

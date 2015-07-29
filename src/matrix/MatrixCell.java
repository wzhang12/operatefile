package matrix;

public class MatrixCell implements Comparable<MatrixCell> {
	
	private final String word1, word2;
	private final int count;

	public MatrixCell(String s1, String s2, int i) {
		word1 = s1;
		word2 = s2;
		count = i;
	}

	// 为了降序排列，输出与正常情况相反
	public int compareTo(MatrixCell o) {
		return o.count - count;
	}
	
	public String toString() {
		return word1 + "\t" + word2 + "\t" + count;
	}
	
}

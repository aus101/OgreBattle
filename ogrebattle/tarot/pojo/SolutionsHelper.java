package ogrebattle.tarot.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolutionsHelper {
	private int[] starting;
	private int[] comparableValues;
	
	/**
	 * Work around Java arrays' incompatibility with equals and hashcode to play well with sets
	 * @param starting the answers, with zeroes indicating which to iterate all combinations for
	 */
	public SolutionsHelper(int[] starting) {
		this.starting = new int[Tarot.DECK_SIZE];
		System.arraycopy(starting, 0, this.starting, 0, starting.length);
	}
	
	/**
	 * Always set comparableValues, boolean is a dummy variable. Use array constructor if you don't need sorted order.
	 * @param comparableValues can be true or false, will still be created
	 * @param starting the answers, with zeroes indicating which to iterate all combinations for
	 */
	public SolutionsHelper(boolean comparableValues, int[] starting) {
		this.starting = new int[Tarot.DECK_SIZE];
		System.arraycopy(starting, 0, this.starting, 0, starting.length);
		setComparableValues();
	}
	
	private void setComparableValues() {
		int[] temp = new int[Tarot.DECK_SIZE];
		int tempIndex = 0;
		for(int i=0; i<Tarot.DECK_SIZE; i++) {
			if(starting[i] == 0) {
				temp[tempIndex] = starting[i];
				tempIndex++;
			}
		}
		comparableValues = new int[tempIndex];//number of zeroes found
		System.arraycopy(temp, 0, this.starting, 0, tempIndex);
	}
	
	/**
	 * @return the number of zeroes in the starting array
	 */
	public int zeroes() {
		if (comparableValues != null)
			return comparableValues.length;
		else {
			int count = 0;
			for(int i : starting) {
				if (i == 0)
					count++;
			}
			return count;
		}
	}
	
	public int[] getSolution() {
		return starting;
	}
	
	public int[] getComparableValues() {
		if (comparableValues != null) {
			return comparableValues;
		} else {
			System.err.println("No comparable values, use boolean constructor. Returning empty array instead of null.");
			return new int[0];
		}
	}
	
	//auto-boxing required so must loop, also a deep copy
	public List<Integer> getSolutionList() {
		List<Integer> solutions = new ArrayList<>(starting.length);
		for(int i : starting) {
			solutions.add(i);
		}
		return solutions;
	}

	@Override
	public int hashCode() {
		return 29 + Arrays.hashCode(starting);
	}

	/**
	 * Considered equal if starting arrays are identical, regardless of comparableValues being initialized or not 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolutionsHelper other = (SolutionsHelper) obj;
		return Arrays.equals(starting, other.starting);
	}
}

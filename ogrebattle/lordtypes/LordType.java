package ogrebattle.lordtypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ogrebattle.tarot.pojo.Tarot;

public abstract class LordType {
	protected TreeSet<int[]> solutions;
	protected boolean INIT = false;
	
	public Set<int[]> getSolutionsSet() {
		 return solutions;
	}
	
	public List<int[]> getSolutionsList() {
		return new ArrayList<int[]>(solutions);
		 //return Arrays.asList(returnAllSolutionsSet().toArray(new int[0][0]));
	}
	
	public List<int[]> getSolutionsListDeepCopy() {
		List<int[]> temp = new ArrayList<int[]>(solutions.size());
		for(int[] s : solutions) {
			int[] toAdd = new int[s.length];//22 but let's not hardcore it
			System.arraycopy(s, 0, toAdd, 0, s.length);
			temp.add(toAdd);
		}
		return temp;
	}

	public void printSolutions() {
		System.out.println("Count: " + solutions.size());//18 expected for IceCloud and 8 for Ianuki
		for (int[] lhs : solutions) {
			System.out.print("{");
			for (int i = 0; i < lhs.length - 1; i++) {
				System.out.print(lhs[i] + ",");
			}
			System.out.println(lhs[lhs.length - 1] + "};");
		}
	}
	
	protected static int countDifferences(int[] found, int[] BASE) {
		int count = 0;
		for(int i=0; i<Tarot.DECK_SIZE; i++)
		{
			if(BASE[i] != found[i])
				count++;
		}
		return count;
	}
	
	protected void countDifferences(Set<int[]> solution,  int[] BASE) {
		for (int[] answers : solution) {
			int diff = countDifferences(answers, BASE);
			if (diff > 0) {
				if (diff == 1) {
					System.out.println(diff + " Difference");
				} else {
					System.out.println(diff + " Differences");
				}
				System.out.print("{");
				for (int i = 0; i < answers.length - 1; i++) {
					System.out.print(answers[i] + ",");
				}
				System.out.println(answers[answers.length - 1] + "};" + System.lineSeparator());
			}
		}
	}
	
	protected int countOnes(int[] solution) {
		int count = 0;
		for(int i=0; i<Tarot.DECK_SIZE; i++)
		{
			if(solution[i] == 1)
				count++;
		}
		return count;
	}
	
	/**
	 * First choice for Tarot questions is desirable in speedrunning due to
	 * 3 frames + human reaction time needed to switch to answer 2 or 3.
	 */
	protected void countOnes(Set<int[]> solution) {
	for(int[] answers : solution) {
		int ones = countOnes(answers);
		if (ones > 0) {
			System.out.println("Ones: " + ones);
				System.out.print("{");
				for(int i=0; i<answers.length-1; i++) {
					System.out.print(answers[i]+",");
				}
				System.out.println(answers[answers.length-1]+"};" + System.lineSeparator());
			}
		}
	}
}

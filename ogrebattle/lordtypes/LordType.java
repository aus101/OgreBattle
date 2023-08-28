package ogrebattle.lordtypes;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import ogrebattle.tarot.pojo.Tarot;

public abstract class LordType {
	protected static int[] BASE = new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	protected static final int CARDS = Tarot.values().length;//22
	
	public abstract Set<int[]> returnAllSolutionsSet();
	
	//can use abstract method since implementation is guaranteed by extending classes
	public List<int[]> returnAllSolutionsList() {
		 //amazing how screwy converting a set of ints to a list of ints is with Java's own API
		 return Arrays.asList(returnAllSolutionsSet().toArray(new int[0][0]));
	}
	
	//can use abstract method since implementation is guaranteed by extending classes
	public void printSolutions() {
		Set<int[]> solutions = this.returnAllSolutionsSet();
		System.out.println("Count: " + solutions.size());// 56 expected
		for (int[] lhs : solutions) {
			System.out.print("{");
			for (int i = 0; i < lhs.length - 1; i++) {
				System.out.print(lhs[i] + ",");
			}
			System.out.println(lhs[lhs.length - 1] + "};");
		}
	}
	
	protected int conutDifferences(int[] found) {
		int count = 0;
		for(int i=0; i<CARDS; i++)
		{
			if(BASE[i] != found[i])
				count++;
		}
		return count;
	}
	
	protected int countOnes(int[] solution) {
		int count = 0;
		for(int i=0; i<CARDS; i++)
		{
			if(solution[i] == 1)
				count++;
		}
		return count;
	}
	
	public void countDifferences(Set<int[]> solution) {
		for (int[] answers : solution) {
			int diff = conutDifferences(answers);
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
	
	/**
	 * First choice for Tarot questions is desirable in speedrunning due to
	 * 3 frames + human reaction time needed to switch to answer 2 or 3.
	 */
	public void countOnes(Set<int[]> solution) {
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

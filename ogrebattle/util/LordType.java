package ogrebattle.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import ogrebattle.tarot.Tarot;

public abstract class LordType {
	protected static int[] BASE = new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	protected static final int CARDS = Tarot.values().length;//22
	
	public abstract Set<int[]> returnAllSolutionsSet();
	
	//can use abstract method since implementation is guaranteed by extending classes
	public List<int[]> returnAllSolutionsList() {
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
	
	protected int countChanges(int[] found) {
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
	
	public void countChanges(Set<int[]> solution) {
		for (int[] answers : solution) {
			int changes = countChanges(answers);
			if (changes > 0) {
				if (changes == 1) {
					System.out.println(changes + " Change");
				} else {
					System.out.println(changes + " Changes");
				}
				System.out.print("{");
				for (int i = 0; i < answers.length - 1; i++) {
					System.out.print(answers[i] + ",");
				}
				System.out.println(answers[answers.length - 1] + "};" + System.lineSeparator());
			}
		}
	}

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

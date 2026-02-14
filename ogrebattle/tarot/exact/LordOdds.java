package ogrebattle.tarot.exact;

import static ogrebattle.tarot.pojo.LORD.*;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import ogrebattle.printer.Util;
import ogrebattle.tarot.generator.AllPossibleHands;
import ogrebattle.tarot.pojo.LORD;
import ogrebattle.tarot.pojo.Tarot;
import ogrebattle.tarot.pojo.TarotAnswers;
import ogrebattle.tarot.pojo.TarotQuestions;
import ogrebattle.tarot.pojo.TarotQuestionsSFC;
import ogrebattle.tarot.pojo.TarotSorting.IntArrayComparator;

public class LordOdds {
	private static final TarotQuestions[] TAROT_LORD = TarotQuestions.values();
	private static final TarotQuestionsSFC[] TAROT_LORD_SFC = TarotQuestionsSFC.values();
	private boolean originalSFC = true;
	private boolean iterate = false;
	private TarotAnswers tempAnswers;
	private Set<int[]> possibleSolutions;
	private AllPossibleHands handsGenerator;
	
	private LordOdds(){}
	
	private void initialize(int numberOfCards) {
		long start = System.nanoTime();
		possibleSolutions = new TreeSet<int[]>(new IntArrayComparator());
		handsGenerator = new AllPossibleHands(numberOfCards, true);// unsorted (true) is faster, NUMBER_OF_CARDS should be 6 or 7
		long end = System.nanoTime();

		System.out.println((double) (end - start) / Util.NANOSECONDS_IN_1_SECOND + " seconds to generate all "
			+ handsGenerator.size() + " hands drawing " + numberOfCards + " cards from the deck");
	}
	
	public static LordOdds generateAnd() {
		return new LordOdds();//does not initialize, force a chain
	}
	
	public LordOdds useOriginalSFCReleasePoints() {
		this.originalSFC = true;
		return this;
	}
	
	public LordOdds useEnglishAndLaterReleasesPoints() {
		this.originalSFC = false;
		return this;
	}
	
	public LordOdds andInitializeWith6CardHands() {
		initialize(6);
		return this;
	}
	
	public LordOdds andInitializeWith7CardHands() {
		initialize(7);
		return this;
	}
	
	public void clearSolutions() {
		possibleSolutions.clear();
	}
	
	public Set<int[]> getPossibleSolutions() {
		return possibleSolutions;
	}
	
	public void addSolution(int[] solution) {
		possibleSolutions.add(solution);
	}

	private class OrderHolder {
		int correct = 0;
		int[] first = new int[4];
		int[] second = new int[4];

		OrderHolder(int[] test, int[] order, int loops, AllPossibleHands handsGenerator) {
			Set<TreeSet<Tarot>> all_hands = handsGenerator.returnAllHandsSet();
			for (TreeSet<Tarot> hand : all_hands) {		
				int[] indices = findHighestSecondHighest(trackResults(test, hand));
				int highestIndex = indices[0];
				int secondHighestIndex = indices[1];
				first[highestIndex]++;
				second[secondHighestIndex]++;

				if (order.length == 1) {
					if (highestIndex == order[0]) {
						correct++;
					}
				}
				else if (highestIndex == order[0] && secondHighestIndex == order[1]) {
					correct++;
				}
			}
		}
		
		/**
		 * @param tracker the list of Tarot card answers<br>
		 * @return the highest and second highest Lord types with [0] being highest and [1] the second highest<br>
		 * where 0 = Ianuki, 1 = Phantom, 2 = Ice Cloud, 3 = Thunder<br>
		 * accounts for tie breaking order of Ianuki > Phantom > Ice Cloud, Thunder<br>
		 */
	    //cleaner ways to do this but overhead of n*log(n) for small n would take longer
		private int[] findHighestSecondHighest(int[] tracker) {
			int ianukiScore = tracker[0]; int phantomScore = tracker[1]; int icecloudScore = tracker[2]; int thunderScore = tracker[3];
			int highestIndex = IANUKI.O; int secondHighestIndex;//default for faster execution
			int highestScore = ianukiScore; int secondHighestScore;
			
			//highest
			if (phantomScore > highestScore) {
				highestIndex = PHANTOM.O;
				highestScore = phantomScore;
			} 
			if (icecloudScore > highestScore) {
				highestIndex = ICE_CLOUD.O;
				highestScore = icecloudScore;
			}
			if (thunderScore > highestScore) {
				highestIndex = THUNDER.O;
				highestScore = thunderScore;
			}
			//second highest
/*			if (highestIndex != IANUKI.O) {
			secondHighestIndex = IANUKI.O;
			secondHighestScore = ianukiScore;
		}
		if (highestIndex != PHANTOM.O && phantomScore > secondHighestScore) {
			secondHighestIndex = PHANTOM.O;
			secondHighestScore = phantomScore;
		}   in theory, below implementation is faster */
			if ((highestIndex == IANUKI.O) || (highestIndex != PHANTOM.O && phantomScore > ianukiScore)) {
				secondHighestIndex = PHANTOM.O;
				secondHighestScore = phantomScore;
			} else {
				secondHighestIndex = IANUKI.O;
				secondHighestScore = ianukiScore;
			}
			if (highestIndex != ICE_CLOUD.O && icecloudScore > secondHighestScore) {
				secondHighestIndex = ICE_CLOUD.O;
				secondHighestScore = icecloudScore;
			}
			if (highestIndex != THUNDER.O && thunderScore > secondHighestScore) {
				secondHighestIndex = THUNDER.O;
			}
			return new int[]{highestIndex, secondHighestIndex};
		}
			
		/**
		 * Where the real work is done of comparing Tarot card hands
		 * @param test the answer set to test against all hands
		 * @param hand the hand to compare
		 * @return point totals for all lords
		 */
		private int[] trackResults(int[] test, TreeSet<Tarot> hand) {
			final int[] tracker = new int[4];//4 Lord types
			int i = 0;
			for (Tarot card : hand) {
				i++;
				if (i == 7) continue;//bonus card does not affect Lord type
				int drawn = card.ordinal();
				int chosen = test[drawn];
				int[] temp;
				if(!originalSFC) {
					temp = TarotQuestions.getValues(TAROT_LORD[drawn], chosen);
				} else {
					temp = TarotQuestionsSFC.getValues(TAROT_LORD_SFC[drawn], chosen);
				}
				for (int k = 0; k < 4; k++) {
					tracker[k] += temp[k];
				}
			}
			return tracker;
		}
	}
	
	public void searchFor1sEntry(int[] solution, int givenRecord, LORD... lordOrder) {
		
		searchFor1sEntry(solution, givenRecord, Util.lordToInt(lordOrder));
	}
	
	public void searchFor1sEntry(TarotAnswers solution) {
		searchFor1sEntry(solution.getAnswers(), solution.getRecord(), solution.getDesiredLord());
	}
	
	/**
	 * Go "backwards" from checking the solution's count by changing each answer that isn't 1 to 1<br>
	 * Report the solution set from changing one answer from a 2 or 3 to 1 that has the smallest decrease in chosen Lord types<br>
	 * Iterates over a max 22 loops if no current answer is 1<br>
	 * @param solution the answer set to iterate over and test changing values
	 * @param givenRecord the number of correct Lord types for that answer set
	 * @param order the desired Lord types, first and optional second choice supported
	 */
	public void searchFor1sEntry(int[] solution, int givenRecord, int... order) {
		Set<int[]> possibleSolutions = new TreeSet<int[]>(new IntArrayComparator());
		Tarot[] values = Tarot.values();
		possibleSolutions.add(solution); 
		final int[] test = new int[Tarot.DECK_SIZE];
		int[] maxSolution = new int[Tarot.DECK_SIZE];
		int maxRecord = 0;
		int currentRecord = 0;
		System.arraycopy(solution, 0, test, 0, Tarot.DECK_SIZE);//deep copy
		int originalAnswer = 0;
		int tarot = 0;
		int changes = 0;
		final int loops = Tarot.DECK_SIZE;
		
		System.out.println(System.lineSeparator() + "Starting: " + givenRecord);
		Util.printAnswers(test);
		
		for (int i=0; i<loops; i++) {
			if (test[i] == 1) continue;// || (i == ordinal)) //if 1 or a card not to consider			
			changes++;
			originalAnswer = test[i];
			test[i] = 1;

			OrderHolder holder = new OrderHolder(test, order, loops, handsGenerator);

			if (holder.correct < currentRecord) {
				//do nothing
			} else if (holder.correct == currentRecord) {
				if (possibleSolutions.add(test)) {//added as a new solution versus a repeat
					System.out.println(System.lineSeparator() + "Alternate for same count of " + currentRecord + " Changing " + values[i]);
					Util.printAnswers(test);
				} else {
					System.out.println("Count of " + holder.correct + " equals current record of " + currentRecord);
					Util.printAnswers(test);
				}
			} else {//(correct > currentRecord) and can change this card
				System.out.println(System.lineSeparator() + "New Record: " + holder.correct + " Changing " + values[i]);
				currentRecord = holder.correct;
				tarot = i;
				Util.printAnswers(test);
				if (holder.correct > maxRecord) {
					maxRecord = holder.correct;
					System.arraycopy(test, 0, maxSolution, 0, Tarot.DECK_SIZE);//deep copy, necessary due to rotation on test
				}
			}
			test[i] = originalAnswer;//switch back to avoid another full array copy	
		}
		if (changes > 0) {
			if (maxRecord > 0) {
				System.out.println(System.lineSeparator() + "Max Record: " + maxRecord);
				Util.printAnswers(maxSolution);
			}
			System.out.print(System.lineSeparator()+"Max Record " + currentRecord + getPercent(currentRecord, handsGenerator.size()) + "is "); 
			if (givenRecord > currentRecord) {
				System.out.println((givenRecord - currentRecord) + " less than original record of " + givenRecord + getPercent(givenRecord, handsGenerator.size()));
				iterate = false;
			} else if (givenRecord < currentRecord) {
				System.out.println((currentRecord - givenRecord) + " greater than original record of " + givenRecord  + getPercent(givenRecord, handsGenerator.size()));	
				tempAnswers = new TarotAnswers(currentRecord, maxSolution, order);
			} else {
				System.out.println("somehow equal to the starting record");
				iterate = false;
			}
			System.out.println(values[tarot] + " at index " + (tarot) + " changed from " + test[tarot]+ " for " + (Tarot.DECK_SIZE - changes) + " total 1's");
			System.out.println(System.lineSeparator() + "Original Answers: ");
			Util.printAnswers(test);
		} else System.out.println(System.lineSeparator()+"Every answer is 1 to begin with, nothing to do");
	}
	
	/**
	 * Continuous calling <code>searchForImprovement</code> by updating <code>tempAnswers</code> on each loop with the answer set<br>
	 * containing the most matches. Continues until an improvement in matches is not made. Some optimal answer sets can result from<br>
	 * not choosing the most amount of matches to iterate down a different path. The paths may converge on the same ultimate set of<br>
	 * answers or result in a set with lower matches. The limitation is iterating by changing 1 answer every which way instead of 2.<br>
	 */
	public void continuousSearchForImprovement() {
		this.iterate = true;
		while (this.iterate)
			this.searchForImprovement();
	}
	
	/**
	 * Helper method for {@link #continuousSearchForImprovement()}
	 * @param solution deep copied to <code>tempAnswers<code>
	 */
	public void continuousSearchForImprovement(TarotAnswers solution) {
		tempAnswers = new TarotAnswers(true, solution);//deep copy
		this.iterate = true;
		while (this.iterate)
			this.searchForImprovement();
	}
	
	public void searchForImprovement() {
		searchForImprovement(tempAnswers.getRecord(), true, tempAnswers.getAnswers(), tempAnswers.getDesiredLord());
	}
		
	public void searchForImprovement(TarotAnswers solution) {
		searchForImprovement(solution.getRecord(), true, solution.getAnswers(), solution.getDesiredLord());
	}
	
	public void searchForImprovement(int[] answers, int... order) {
		searchForImprovement(0, true, answers, order);
	}
	
	public void countMatches(int[] answers, int... order) {
		searchForImprovement(0, false, answers, order);
	}
	
	public void countMatches(TarotAnswers solution) {
		searchForImprovement(0, false, solution.getAnswers(), solution.getDesiredLord());
	}
	
	public void countMatches(TarotAnswers solution, int... order) {
		searchForImprovement(0, false, solution.getAnswers(), order);
	}
	
	public void searchForImprovement(int givenRecord, int[] solution, LORD... lordOrder) {
		searchForImprovement(givenRecord, true, solution, Util.lordToInt(lordOrder));
	}
	
	public void searchForImprovement(int givenRecord, boolean iterate, int[] solution, int... order) {
		if (order == null || order.length == 0) {//downside of using varangs is order becomes optional
			System.err.println(System.lineSeparator() + "Must provide 1 or 2 Lord types to count matches for");
			return;
		}
		Set<int[]> possibleSolutions = new TreeSet<int[]>(new IntArrayComparator());
		possibleSolutions.add(solution);
		final int[] test = new int[Tarot.DECK_SIZE];
		int[] maxSolution = new int[Tarot.DECK_SIZE];
		int maxRecord = 0;
		int currentRecord = givenRecord;
		System.arraycopy(solution, 0, test, 0, Tarot.DECK_SIZE);//deep copy
		final int loops = (iterate) ? Tarot.DECK_SIZE * 2 : 1;//1 loop if not iterating
		final String startPercent = getPercent(givenRecord, handsGenerator.size());
		for (int i=0; i<loops; i++) {
			if(iterate) {
				if (i < Tarot.DECK_SIZE)
					rotateUp(test, i);
				else
					rotateDown(test, i - Tarot.DECK_SIZE);
			}
			OrderHolder holder = new OrderHolder(test, order, loops, handsGenerator);
			if (holder.correct > maxRecord) {
				maxRecord = holder.correct;
			}
			final String holderPercent = getPercent(holder.correct, handsGenerator.size());

			if (!iterate && holder.correct < currentRecord) {
				System.out.println(System.lineSeparator() + "Count of " + holder.correct + holderPercent + "is worse than current record of "
						+ currentRecord + startPercent);
				Util.printAnswers(test);
			}
			if (holder.correct == currentRecord) {
				if (possibleSolutions.add(test)) {//added as a new solution versus a repeat
					System.out.println(System.lineSeparator() + "Alternate: same record of " + currentRecord + startPercent);
					Util.printAnswers(test);
				} else if(!iterate) {
					System.out.println("Count of " + holder.correct + holderPercent + "equals current record of " + givenRecord);
				}
			} else if (holder.correct > currentRecord) {
				if (iterate) {
					currentRecord = holder.correct;
//					if (holder.correct >= givenRecord)
//					System.out.println(System.lineSeparator() + "New Record: " + currentRecord + holderPercent + "up from " + givenRecord + startPercent);
//					Util.printAnswers(test);
				} else {
					if (holder.correct >= givenRecord)
					System.out.println(System.lineSeparator() + "The Record: " + holder.correct + holderPercent);
					Util.printAnswers(test);
				}
				System.arraycopy(test, 0, maxSolution, 0, Tarot.DECK_SIZE);// deep copy, necessary due to rotating test
			}
			//switch back to avoid another full array copy
			if (iterate) {
				if (i < Tarot.DECK_SIZE)
					rotateDown(test, i);
				else
					rotateUp(test, i - Tarot.DECK_SIZE);
			} else {
				printLordTypes(holder);
			}
		}
		//end of for (int i=0; i<loops; i++) {
		if (iterate) {
			if (currentRecord == givenRecord) {
				this.iterate = false;//can't find a better solution so stop looking
				System.out.println(System.lineSeparator() + "No solutions found that exceed " + givenRecord + startPercent + "printed above");
				if (maxRecord < currentRecord) {
					System.out.println("Max Matches " +  maxRecord + getPercent(maxRecord, handsGenerator.size()) + "so not printing, iteration stopped");
				}
			} else {
				System.out.println(System.lineSeparator() + "Max Record: " + maxRecord + getPercent(maxRecord, handsGenerator.size())
					+ "up from " + givenRecord + getPercent(givenRecord, handsGenerator.size()));
				tempAnswers = new TarotAnswers(maxRecord, maxSolution, order);
				Util.printAnswers(tempAnswers.getAnswers());
			}
		} else {
			if (givenRecord > 0) {
				System.out.println(System.lineSeparator() + "Starting Answers: " +  givenRecord + startPercent);
				Util.printAnswers(test);
			}
		}
	}
	
	public void justCount(int[] solution) {
		System.out.println("No order given so defaulting to Ianuki for Record count. The count for each Lord type will be correct.");
		fastCount(false, 0, solution, IANUKI.O);
	}
	
	public void justCount(int[] solution, int... order) {
		fastCount(false, 0, solution, order);
	}
	
	public void justCount(boolean silent, int[] solution, int... order) {
		fastCount(silent, 0, solution, order);
	}
	
	public void justCount(int[] solution, LORD... lordOrder) {
		fastCount(false, 0, solution, Util.lordToInt(lordOrder));
	}
	
	public void justCount(int givenRecord, int[] solution, LORD... lordOrder) {
		fastCount(false, givenRecord, solution, Util.lordToInt(lordOrder));
	}
	
	public void justCount(int givenRecord, int[] solution, int... order) {
		fastCount(false, givenRecord, solution, order);
	}
	
	public void justCount(TarotAnswers solution) {
		fastCount(false, solution.getRecord(), solution.getAnswers(), solution.getDesiredLord());
	}
	
	public void justCount(boolean silent, TarotAnswers solution) {
		fastCount(silent, solution.getRecord(), solution.getAnswers(), solution.getDesiredLord());
	}
	
	public void justCount(boolean silent, int[] solution, LORD... lordOrder) {
		fastCount(silent, 0, solution, Util.lordToInt(lordOrder));
	}
	
	public void justCount(boolean silent, int givenRecord, int[] solution, LORD... lordOrder) {
		fastCount(silent, givenRecord, solution, Util.lordToInt(lordOrder));
	}
	
	//deliberately do not want to update givenRecord to enable finding more improvements
	public void fastCount(boolean silent, int givenRecord, int[] solution, int... order) {
		boolean altSolutionOrNewRecord = false;
		OrderHolder holder = new OrderHolder(solution, order, 1, handsGenerator);//1 loop since only checking 1 solution
		//final String holderPercent = getPercent(holder.correct, handsGenerator.size()); may as well save a microsecond
		if (holder.correct < givenRecord) {//check most likely outcome first
			if (!silent) {
			System.out.println(System.lineSeparator() + "Count of " + holder.correct + getPercent(holder.correct, handsGenerator.size())
					+ "is worse than current record of " + givenRecord + getPercent(givenRecord, handsGenerator.size()));
			}
		} else if (holder.correct == givenRecord) {//second most likely outcome
			if (possibleSolutions.add(Arrays.copyOf(solution, Tarot.DECK_SIZE))) {//must add deep copy or mutability will add duplicates
				System.out.println("Count matches given record " + givenRecord + getPercent(givenRecord, handsGenerator.size()));
				altSolutionOrNewRecord = true;
			} else if (!silent) {
				System.out.println("Count of " + holder.correct + getPercent(holder.correct, handsGenerator.size())
					+ "is a repeat from an existing solution");
			}
		} else if (possibleSolutions.add(Arrays.copyOf(solution, Tarot.DECK_SIZE))) {// must add deep copy or mutability will add duplicates
			if (givenRecord > 0) {// by process of elimination, holder.corrent is greater than
				System.out.println("New Record: " + holder.correct + getPercent(holder.correct, handsGenerator.size())
						+ "up from " + givenRecord + getPercent(givenRecord, handsGenerator.size()));
				altSolutionOrNewRecord = true;
			} else if (!silent) {//deliberately choosing not to print this case if on silent mode
				System.out.println("The Record: " + holder.correct + getPercent(holder.correct, handsGenerator.size()));
			}
		}

		if (altSolutionOrNewRecord) {
			Util.printAnswers(solution);
			printLordTypes(holder);
			altSolutionOrNewRecord = false;
		}
	}
	
	private void printLordTypes(OrderHolder holder) {//private class, needs to stay here unless array is extracted
		System.out.println(System.lineSeparator() + "Highest Lord Type");
		Util.printIndex(holder.first);
		System.out.println("Second Highest Lord Type");
		Util.printIndex(holder.second);	
	}
	
	//use 2 decimal places and round up but do not round to 100%, numerator must equal denominator
	private String getPercent(int numerator, int denominator) {
		return new StringBuilder(" (").append(Util.percentCalc(numerator, denominator, 2)).append(") ").toString();
	}
	
	private void rotateUp(int[] answers, int slot) {
		int value;
		
		if (answers[slot] == 1)
			value = 2;
		else if (answers[slot] == 2)
			value = 3;
		else
			value = 1;
		
		answers[slot] = value;				
	}
	
	private void rotateDown(int[] answers, int slot) {
		int value;
		
		if (answers[slot] == 1)
			value = 3;
		else if (answers[slot] == 2)
			value = 1;
		else
			value = 2;
		
		answers[slot] = value;				
	}
}
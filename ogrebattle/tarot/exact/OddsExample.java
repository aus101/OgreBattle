package ogrebattle.tarot.exact;

import static ogrebattle.util.TarotComparators.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ogrebattle.tarot.Tarot;
import ogrebattle.tarot.TarotLord;
import ogrebattle.util.Ianuki;
import ogrebattle.util.IceCloud;

/**
 * Not really intended to be clean, reusable code. Rather to show how to use AllPossibleHands to derive optimal solutions.
 * The commented out code shows how to iterate through Ianuki or Ice Cloud that have more than one optimal set of Tarot answers.
 * The rotateUp and rotateDown code check all 44 combinations from changing 1 Tarot card answer for odds improvement.
 * More Ice Cloud solutions could theoretically exist but would be at least 3 Tarot card changes from all solutions given.
 * Perhaps the best extension would be finding optimal solutions for any Lord being the second highest total.<br>
 */
public class OddsExample {
	public final static int NANOSECONDS_IN_1_SECOND = 1_000_000_000;
	//for Ice Cloud iterations since Fortune, Justice and Devil answers are constant for all 65 solutions
	private final static int[] validCards = new int[] {0,1,2,3,4,5,6,7,8,11,12,13,15,16,17,18,19,20,21};
	public final static int deckSize = Tarot.values().length;
	private final static int Ianuki=0; final static int Phantom=1; final static int Ice_Cloud=2; final static int Thunder=3;
	
	public final static List<int[]> answersIanukiAll9 = new Ianuki().returnAllSolutionsList();     // max ianuki,         74603 out of 74613 99.99%
	public final static int[] answersPhantom =       {3,3,3,1,2,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3};// max phantom,        74137 out of 74613 99.36%	
	public final static List<int[]> answersIceCloudAll65 = new IceCloud().returnAllSolutionsList();// max ice cloud,      74613 out of 74613 100%
	public final static int[] answersThunder =       {2,2,1,2,2,1,3,3,2,2,3,2,2,3,1,2,3,1,1,2,3,1};// max thunder,        74003 out of 74613 99.18%
	public final static int[] phantomIceCloud =      {3,2,3,1,1,1,1,3,3,2,1,1,2,2,3,1,1,1,2,2,1,3};// phantom, ice cloud, 45848 out of 74613 61.45%
	public final static int[] ianukiIceCloud =       {1,1,2,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,3,2,2};// ianuki, ice cloud,  60171 out of 74613 80.64%		
	public final static int[] answersFastest =       {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};// phantom is most likely
	
	private AllPossibleHands handsGenerator;
	
	public OddsExample() {
		long start = System.nanoTime();
		handsGenerator = new AllPossibleHands(6, true);// unsorted (true) is faster
		long end = System.nanoTime();

		System.out.println((double) (end - start) / NANOSECONDS_IN_1_SECOND + " seconds to execute for "
				+ handsGenerator.size() + " hands");
	}
	
	public static void main(String[] args) {
		OddsExample e = new OddsExample();
		e.searchForImprovement(ianukiIceCloud, 60171, false, Ianuki, Ice_Cloud);
		e.searchForImprovement(answersThunder, 74003, true, Thunder);
		System.out.println();
		e.printRandomHands(3);
		System.out.println();
		System.out.println("Phantom Lord optimal answers for 99.36% chance for in-game and alphabetical order:");
		e.printAnswersByTarot(answersPhantom);
		System.out.println();
		e.printAnswersByTarotAlphabetical(answersPhantom);
		System.out.println();
		e.verifyOdds();//compare to ogrebattle.tarot.simulate.OddsExample.java
	}
	
	public void searchForImprovement(int[] solution, int record, boolean iterate, int... order) {	
//		Ianuki ianuki = new Ianuki();
//		Set<int[]> ianukiSolutions = ianuki.returnAllSolutionsSet();
		Set<int[]> possibleSolutions = new TreeSet<int[]>(new IntArrayComparator());
		possibleSolutions.add(solution);
		final int found = possibleSolutions.size();
		TarotLord[] tarotLord = TarotLord.values();
		
//		Iterator<int[]> it = ianukiSolutions.iterator();
//		while(it.hasNext()) {
		final int[] test = new int[deckSize];
		
		System.arraycopy(solution, 0, test, 0, deckSize);// deep copy
		
		int loops = validCards.length * 2;
		if (!iterate) {
			loops = 1;
		}

		for (int i=0; i<loops; i++) {
			int currentRecord = record;

			if(iterate) {
				if (i < deckSize)
					rotateUp(test, i);
				else
					rotateDown(test, i - deckSize);
			}
			
			int correct = 0;
			int[] first = new int[4];
			int[] second = new int[4];

			Set<TreeSet<Tarot>> all_hands = handsGenerator.returnAllHandsSet();
			for (TreeSet<Tarot> hand : all_hands) {
				int[] tracker = new int[4];
				for (Tarot card : hand) {
					int drawn = card.ordinal();
					int chosen = test[drawn];
					int[] temp = TarotLord.getValues(tarotLord[drawn], chosen);
					for (int k = 0; k < 4; k++) {
						tracker[k] += temp[k];
					}
				}

				int[] indices = findHighestSecondHighest(tracker);
				int highestIndex = indices[0];
				int secondHighestIndex = indices[1];
				first[highestIndex]++;
				second[secondHighestIndex]++;

				if(order.length == 1) {
					if (highestIndex == order[0]) {
						correct++;
					}	
				}// else {
				 //	break;//ICE CLOUD ONLY! where 100% success from any 6 cards is possible
				 //}
				else if (highestIndex == order[0] && secondHighestIndex == order[1]) {
					correct++;
				}
			}

			if ((!iterate) && (correct < currentRecord)) {
				System.out.println("Count of " + correct + " is worse than current record of " + record);
				printAnswers(test);
			}
			if (correct == currentRecord) {
				int before = possibleSolutions.size();
				possibleSolutions.add(test);
				if (possibleSolutions.size() > before) {
					System.out.println(System.lineSeparator() + "Alternate for same count of " + currentRecord);
					printAnswers(test);
				} else if(!iterate) {
					System.out.println("Count of " + correct + " equals current record of " + record);
				}
			} else if (correct > currentRecord) {
				currentRecord = correct;
				System.out.println(System.lineSeparator() + "New Record: " + currentRecord + " up from " + record);
				printAnswers(test);
			}
			// switch back to avoid another full array copy		
			if (iterate) {
				if (i < deckSize)
					rotateDown(test, i);
				else
					rotateUp(test, i - deckSize);
			} else {
				System.out.println(System.lineSeparator()+"Highest Lord Type");
				printIndex(first);
				System.out.println(System.lineSeparator()+"Second Highest Lord Type");
				printIndex(second);
				System.out.println();
			}
		}
		if (found == possibleSolutions.size()) {
			if (iterate) {
				System.out.println("No new solutions found. Existing solution(s) for " + record + " counts likely optimal.");
				printAnswers(test);
			}
		}
	}
//	}
	
	/**
	 * 
	 * @param tracker the list of Tarot card answers<br>
	 * @return the highest and second highest Lord types with [0] being highest and [1] the second highest<br>
	 * where 0 = Ianuki, 1 = Phantom, 2 = Ice Cloud, 3 = Thunder<br>
	 * accounts for tie breaking order of Ianuki > Phantom > Ice Cloud, Thunder<br>
	 */
    //cleaner ways to do this but overhead of n*log(n) would take longer
	public static int[] findHighestSecondHighest(int[] tracker) {
		int ianuki = tracker[0]; int phantom = tracker[1]; int icecloud = tracker[2]; int thunder = tracker[3];
		int highestIndex = 0; int secondHighestIndex = 0;
		int highestScore = ianuki; int secondHighestScore = 0;
		
		//highest
		if (phantom > highestScore) {
			highestIndex = 1;
			highestScore = phantom;
		}
		if (icecloud > highestScore) {
			highestIndex = 2;
			highestScore = icecloud;
		}
		if (thunder > highestScore) {
			highestIndex = 3;
			highestScore = thunder;
		}
		//second highest
		if (highestIndex != 0) {
			secondHighestIndex = 0;
			secondHighestScore = tracker[0];
		}
		if (highestIndex != 1 && tracker[1] > secondHighestScore) {
			secondHighestIndex = 1;
			secondHighestScore = tracker[1];
		}
		if (highestIndex != 2 && tracker[2] > secondHighestScore) {
			secondHighestIndex = 2;
			secondHighestScore = tracker[2];
		}
		if (highestIndex != 3 && tracker[3] > secondHighestScore) {
			secondHighestIndex = 3;
			secondHighestScore = tracker[3];
		}
		return new int[]{highestIndex, secondHighestIndex};
	}
	
	public void printIndex(int[] answers) {
		System.out.println("Ianuki:    " + answers[0]);
		System.out.println("Phantom:   " + answers[1]);
		System.out.println("Ice Cloud: " + answers[2]);
		System.out.println("Thunder:   " + answers[3]);
	}
	

	public void printAnswers(int[] answers) {
		System.out.print("{");
		for(int i=0; i<deckSize-1; i++) {
			System.out.print(answers[i] + ",");
		}
		System.out.print(answers[deckSize-1]+"};"+System.lineSeparator());
	}
	
	public void printAnswersByTarot(int[] answers) {
		Tarot[] values = Tarot.values();
		for(int i=0; i<values.length; i++) {
//			System.out.println(//pad left
//					String.format("%" + 13 + "." + 13 + "s", new String(values[i]+": " + answers[i])));
			System.out.println(//pad right
			String.format("%-" + 12 + "." + 12 + "s", String.valueOf(values[i])) + ": " + answers[i]);				
		}
	}
	
	public void printAnswersByTarotAlphabetical(int[] answers) {
		Tarot[] values = Tarot.values();
		Arrays.sort(values, new AlphabeticalComparator());
		for(int i=0; i<values.length; i++) {
			System.out.println(//pad right
			String.format("%-" + 12 + "." + 12 + "s", String.valueOf(values[i])) + ": " + answers[values[i].ordinal()]);			
		}
	}
	
	public void verifyOdds() {
		final int combin = 170544;//COMBIN(22,7)
		AllPossibleHands sevenCards = new AllPossibleHands(7, true);
		System.out.println("Odds of 1 specific card in opening hand of 7:");
		printPercent(sevenCards.countContains(Tarot.Fool), combin, 4);
		System.out.println();
		System.out.println("Odds of at least 1 of 3 specific cards in opening hand of 7:");
		printPercent(sevenCards.countContainsAny(Tarot.Devil, Tarot.Chariot, Tarot.Hermit), combin, 4);
		System.out.println();
		System.out.println("Odds of Fool and at least 1 of 3 other specific cards in opening hand of 7:");
		printPercent(sevenCards.countContainsAndContainsAny(Tarot.Fool,
				Tarot.Devil, Tarot.Chariot, Tarot.Hermit), combin, 4);
	}
	
	/**
	 * Exact odds of hand combinations using BigDecimal to prevent floating point error.<br>
	 * Compare to simulated odds in <code>ogrebattle.tarot.simulate.OddsExample.java<code>.<br>
	 * @param successes hands that matched given criteria
	 * @param totalHands total hands possible that were iterated through
	 * @param precision decimal places, with rounding on last decimal
	 */
	private static void printPercent(int successes, int totalHands, int precision) {
		System.out.println(successes + " / " + totalHands);
		BigDecimal num = new BigDecimal(successes);
		BigDecimal denom = new BigDecimal(totalHands);
		System.out.println(num.multiply(new BigDecimal(100)).divide(denom, precision, RoundingMode.HALF_UP) + "%");
	}

	public void printRandomHand() {
		System.out.println(handsGenerator.returnRandomHandInList());
	}
	
	public void printRandomHands(int hands) {
		for(int i=0; i<hands; i++)
			System.out.println(handsGenerator.returnRandomHandInList());
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

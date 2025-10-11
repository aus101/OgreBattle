package ogrebattle.tarot.exact;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ogrebattle.lordtypes.Ianuki;
import ogrebattle.lordtypes.IceCloud;
import ogrebattle.tarot.pojo.LORD;
import ogrebattle.tarot.pojo.Tarot;
import ogrebattle.tarot.pojo.TarotQuestions;

/**
 * Not really intended to be clean, reusable code. Rather to show how to use AllPossibleHands to derive optimal solutions.
 * The commented out code shows how to iterate through Ianuki or Ice Cloud that have more than one optimal set of Tarot answers.
 * The rotateUp and rotateDown code check all 44 combinations from changing 1 Tarot card answer for odds improvement.
 * More Ice Cloud solutions could theoretically exist but would be at least 3 Tarot card changes from all solutions given.
 * Perhaps the best extension would be finding optimal solutions for any Lord being the second highest total.<br>
 * <br>
 * <b>These point values are for all releases except the original Super Famicom release, which has the wrong values for 20 cards and reworked ones for 2.</b>
 * Can do a Replace All of 'TarotQuestions' for 'TarotQuestionsSFC' for original SFC release points.<br>
 * The Nintendo Power flash cart release on Super Famicom uses these values. Need to confirm Japanese Virtual Console release values.
 */
public class OddsExample {
	public final static int NANOSECONDS_IN_1_SECOND = 1_000_000_000;
	//for Ice Cloud iterations since Fortune, Justice and Devil answers are constant for all 65 solutions
	private final static int[] validCards = new int[] {0,1,2,3,4,5,6,7,8,11,12,13,15,16,17,18,19,20,21};
	public final static int deckSize = Tarot.values().length;

	//just get the first solution for Ianuki and Ice Cloud, can iterate through and see them all if you want
	final static List<int[]> answersIanukiAll8 =     new Ianuki().returnAllSolutionsList();              // max ianuki, 8 solutions      74603 out of 74613 99.99%
	public final static int[] answersPhantom =       {3,3,3,1,2,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3};// max phantom,                 74137 out of 74613 99.36%	
	public final static List<int[]> answersIceCloudAll18 = new IceCloud().returnAllSolutionsList();      // max ice cloud, 18+ solutions 74613 out of 74613 100%
	public final static int[] answersThunder =       {2,2,1,2,2,1,3,3,2,2,3,2,2,3,1,2,3,1,1,2,3,1};// max thunder,                 74003 out of 74613 99.18%
	
	public final static int[] answersFastest =       {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};// phantom most likely 30953 41.48%, ianuki second most likely 12717 17.04%
	
	public final static int[] ianukiIceCloud =       {1,1,2,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,3,2,2};// ianuki 1st & ice cloud 2nd,   60171 out of 74613 80.64%
	public final static int[] answersIanukiThunder = {1,1,1,1,2,3,3,2,2,1,2,2,3,1,2,2,1,3,2,1,3,2};// ianuki 1st & thunder second,  74137 out of 74613 99.36%
	public final static int[] phantomIceCloud =      {3,2,3,1,1,1,1,3,3,2,1,1,2,2,3,1,1,1,2,2,1,3};// phantom 1st & ice cloud 2nd,  45848 out of 74613 61.45%

	private AllPossibleHands handsGenerator;
	
	public OddsExample() {
		System.out.println(answersIanukiAll8.iterator().next()[3]);
		long start = System.nanoTime();
		handsGenerator = new AllPossibleHands(6, true);// unsorted (true) is faster
		long end = System.nanoTime();

		System.out.println((double) (end - start) / NANOSECONDS_IN_1_SECOND + " seconds to execute for "
				+ handsGenerator.size() + " hands");
	}
	
	public static void main(String[] args) {
		OddsExample e = new OddsExample();
		
		e.searchForImprovement(ianukiIceCloud, 60171, false, LORD.IANUKI.O, LORD.ICE_CLOUD.O);
		e.searchForImprovement(answersThunder, 74003, true, LORD.THUNDER.O);
		
		System.out.println("Phantom Lord optimal answers for 99.36% chance for in-game and alphabetical order:");
		e.printAnswersByTarot(answersPhantom);
		
		System.out.println();
		e.printAnswersByTarotAlphabetical(answersPhantom);
		System.out.println();
		
		System.out.println();
		System.out.println("Print 3 Random Hands");
		e.printRandomHands(3);
		System.out.println();
		
		e.verifyOdds();//compare to ogrebattle.tarot.simulate.OddsExample.java
	}
	
	/**
	 * Print the Tarot card question answers for the most likely possibility of getting the Lord type you want.<br>
	 * Support for highest and second highest point totals. Default is using the point values in every release but the original Super Famicom.<br>
	 * @param solution answers in array form from Magician to World
	 * @param record current record for most correct results, used for iterate = true
	 * @param iterate look for an improvement by changing each question's answer 1 at a time for 1 level of iteration
	 * @param order 1st Lord type for highest total, 2nd Lord type, if given, for 2nd highest total, 3rd highest not supported
	 */
	public void searchForImprovement(int[] solution, int record, boolean iterate, int... order) {	
		Set<int[]> possibleSolutions = new TreeSet<int[]>(new IntArrayComparator());
		possibleSolutions.add(solution);
		final int found = possibleSolutions.size();
		TarotQuestions[] tarotLord = TarotQuestions.values();
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
					int[] temp = TarotQuestions.getValues(tarotLord[drawn], chosen);
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
				}
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
				possibleSolutions.add(test);
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
/*
0.5865753 seconds to execute for 74613 hands
Count of 60171 equals current record of 60171

Highest Lord Type
Ianuki:    69698
Phantom:   0
Ice Cloud: 4909
Thunder:   6

Second Highest Lord Type
Ianuki:    4915
Phantom:   2398
Ice Cloud: 60171
Thunder:   7129


New Record: 74009 up from 74003
{2,2,3,2,2,1,3,3,2,2,3,2,2,3,1,2,3,1,1,2,3,1};

Print 3 Random Hands
[Priestess, Hierophant, Hermit, Justice, Temperance, Devil]
[Hierophant, Hanged_Man, Temperance, Tower, Star, Judgment]
[Magician, Hierophant, Hermit, Tower, Star, Moon]

Phantom Lord optimal answers for 99.36% chance for in-game and alphabetical order:
Magician    : 3
Priestess   : 3
Empress     : 3
Emperor     : 1
Hierophant  : 2
Lovers      : 3
Chariot     : 1
Strength    : 3
Hermit      : 1
Fortune     : 2
Justice     : 1
Hanged_Man  : 1
Death       : 3
Temperance  : 2
Devil       : 1
Tower       : 1
Star        : 3
Moon        : 1
Sun         : 1
Judgment    : 2
Fool        : 1
World       : 3

Chariot     : 1
Death       : 3
Devil       : 1
Emperor     : 1
Empress     : 3
Fool        : 1
Fortune     : 2
Hanged_Man  : 1
Hermit      : 1
Hierophant  : 2
Judgment    : 2
Justice     : 1
Lovers      : 3
Magician    : 3
Moon        : 1
Priestess   : 3
Star        : 3
Strength    : 3
Sun         : 1
Temperance  : 2
Tower       : 1
World       : 3

Odds of 1 specific card in opening hand of 7:
54264 / 170544
31.8182%

Odds of at least 1 of 3 specific cards in opening hand of 7:
120156 / 170544
70.4545%

Odds of Fool and at least 1 of 3 other specific cards in opening hand of 7:
35700 / 170544
20.9330%
*/

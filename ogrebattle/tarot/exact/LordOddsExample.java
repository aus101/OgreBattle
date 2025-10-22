package ogrebattle.tarot.exact;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.Set;
import java.util.TreeSet;
import ogrebattle.printer.Util;
//static imports to reduce clutter
import static ogrebattle.tarot.pojo.LORD.IANUKI;
import static ogrebattle.tarot.pojo.LORD.PHANTOM;
import static ogrebattle.tarot.pojo.LORD.ICE_CLOUD;
import static ogrebattle.tarot.pojo.LORD.THUNDER;

import ogrebattle.tarot.pojo.LORD;
import ogrebattle.tarot.pojo.Tarot;
import ogrebattle.tarot.pojo.TarotAnswers;
import ogrebattle.tarot.pojo.TarotQuestions;
import ogrebattle.tarot.pojo.TarotQuestionsSFC;

/**
 * Not really intended to be clean, reusable code. Rather to show how to use AllPossibleHands to derive optimal solutions.
 * The commented out code shows how to iterate through Ianuki or Ice Cloud that have more than one optimal set of Tarot answers.
 * The rotateUp and rotateDown code check all 44 combinations from changing 1 Tarot card answer for odds improvement.
 * More Ice Cloud solutions could theoretically exist but would be at least 3 Tarot card changes from all solutions given.
 * Perhaps the best extension would be finding optimal solutions for any Lord being the second highest total.<br>
 */
public class LordOddsExample {
	private static final TarotQuestions[] TAROT_LORD = TarotQuestions.values();
	private static final TarotQuestionsSFC[] TAROT_LORD_SFC = TarotQuestionsSFC.values();
	private static boolean isInitialized = false;
	private final int NUMBER_OF_CARDS = 6;//6 or 7 cards work just fine, bonus card not answered for points
	private final boolean ORIGINAL_SFC_QUESTIONS = false;
	private boolean iterate = false;
	private TarotAnswers tempAnswers;
	private AllPossibleHands handsGenerator;
		
	public static final TarotAnswers ianuki = new TarotAnswers(74603, Util.IanukiBaseDeepCopy(), IANUKI);//max Ianuki, 74603 out of 74613                                                 99.99%
	public static final TarotAnswers phantom = new TarotAnswers(74137, new int[]{3,3,3,1,2,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3}, PHANTOM);//max Phantom                                     98.74%
	public static final TarotAnswers iceCloud = new TarotAnswers(74613, Util.IceCloudBaseDeepCopy(), ICE_CLOUD);//max Ice Cloud                                                           100%
	public static final TarotAnswers thunder = new TarotAnswers(74009, new int[]{2,2,3,2,2,1,3,3,2,2,3,2,2,3,1,2,3,1,1,2,3,1}, THUNDER);//max Thunder                                     99.19%
	
	public static final TarotAnswers ianukiIceCloud = new TarotAnswers(60171, new int[]{1,1,2,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,3,2,2}, IANUKI, ICE_CLOUD);//Ianuki 1st, Ice Cloud 2nd      80.64%
	public static final TarotAnswers phantomIceCloud = new TarotAnswers(44814, new int[]{3,2,3,1,1,1,1,3,3,3,1,1,2,2,3,1,1,1,2,2,1,3}, PHANTOM, ICE_CLOUD);//Phantom 1st, Ice Cloud 2nd   58.45%

	public static final TarotAnswers ianukiSFC =   new TarotAnswers(74535, new int[]{2,1,1,2,3,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2,2}, IANUKI);//max Ianuki                                   99.90%
	public static final TarotAnswers phantomSFC = new TarotAnswers(74386, new int[]{3,3,1,1,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3,2}, PHANTOM);//max Phantom                                  99.70%
	public static final TarotAnswers iceCloudSFC = new TarotAnswers(74613, Util.IceCloudSFCBaseDeepCopy(), ICE_CLOUD);//max Ice Cloud                                                     100%
	public static final TarotAnswers thunderSFC = new TarotAnswers(72839, new int[]{2,2,2,2,3,3,3,1,2,2,2,2,3,1,2,3,1,1,2,3,1,3}, THUNDER);//max Thunder                                  97.62%

	public static final TarotAnswers ianukiIceCloudSFC = new TarotAnswers(56723, new int[]{2,1,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,1,2,2,1}, THUNDER);//Ianuki 1st, Ice Cloud 2nd             76.02%
	public static final TarotAnswers phantomIceCloudSFC = new TarotAnswers(44814, new int[]{3,3,1,1,1,1,3,3,3,1,1,2,2,3,1,1,1,1,2,1,3,1}, THUNDER);//Phantom 1st, Ice CLoud 2nd           60.06%
	
	//Phantom most likely with 30953 out of 74603 (41.48%) and 31077 (41.65%) in Original SFC Release
	public static final TarotAnswers all1s = new TarotAnswers(30953, new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, PHANTOM);
	
	public static void main(String[] args) {
		LordOddsExample e = new LordOddsExample();
		System.out.println(System.lineSeparator() + "searchFor1sEntry(ianuki) start");
		e.searchFor1sEntry(ianuki);
		System.out.println(System.lineSeparator() + "print answers for Phantom Lord that work 98.74% of the time");
		Util.printAnswersByTarot(phantom);
		System.out.println(System.lineSeparator() + "countMatches(all1s) start");
		e.countMatches(all1s);
		System.out.println(System.lineSeparator() + "searchForImprovement(all1s) start for Phantom Lord");
		e.searchForImprovement(all1s);
		
//		System.out.println(System.lineSeparator() + "continuousSearchForImprovement(new TarotAnswers(0, true, all1s.getAnswers(), PHANTOM, ICE_CLOUD)) start");
//		e.continuousSearchForImprovement(new TarotAnswers(0, true, al.getAnswers(), PHANTOM, ICE_CLOUD));//generate phantomIceCloud solution set up from nothing! 	
//		Util.printAnswersByGroup(e.tempAnswers);//answers for ianukiIceCloud grouped for speedrunning!
	}
	
	public LordOddsExample() {
		if (!isInitialized) {
			isInitialized = true;
			long start = System.nanoTime();
			handsGenerator = new AllPossibleHands(NUMBER_OF_CARDS, true);// unsorted (true) is faster, NUMBER_OF_CARDS should be 6 or 7
			long end = System.nanoTime();
	
			System.out.println((double) (end - start) / Util.NANOSECONDS_IN_1_SECOND + " seconds to generate all "
					+ handsGenerator.size() + " hands drawing " + NUMBER_OF_CARDS + " cards from the deck");
		}
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
			
		private int[] trackResults(int[] test, TreeSet<Tarot> hand) {
			final int[] tracker = new int[4]; //4 Lord Types
			int i = 0;
			for (Tarot card : hand) {
				i++;
				if (i == 7) continue;//bonus card does not affect Lord Type
				int drawn = card.ordinal();
				int chosen = test[drawn];
				int[] temp;
				if(!ORIGINAL_SFC_QUESTIONS) {
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
	
	public void searchFor1sEntry(TarotAnswers solution) {
		searchFor1sEntry(solution.getAnswers(), solution.getRecord(), solution.getDesiredLord());
	}
	
	/**
	 * Go "backwards" from checking the solution's count by changing each answer that isn't 1 to 1<br>
	 * Report the solution set from changing one answer from a 2 or 3 to 1 that has the smallest decrease in chosen lord types<br>
	 * Iterates over a max 22 loops if no current answer is 1<br>
	 * @param solution the answer set to iterate over and test changing values
	 * @param givenRecord the number of correct lord types for that answer set
	 * @param order the desired lord types, first and optional second choice supported
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
				int before = possibleSolutions.size();
				possibleSolutions.add(test);
				if (possibleSolutions.size() > before) {
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
		searchForImprovement( solution.getRecord(), true, solution.getAnswers(), solution.getDesiredLord());
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
		int[] order = null;
		if (lordOrder != null) {
			order = new int[lordOrder.length];
			for(int i=0; i<lordOrder.length; i++) {
				order[i] = lordOrder[i].O;
			}
		}
		searchForImprovement(givenRecord, true, solution, order);
	}
	
	public void searchForImprovement(int givenRecord, boolean iterate, int[] solution, int... order) {
		if (order == null || order.length == 0) {//downside of using varangs is order becomes optional
			System.err.println(System.lineSeparator() + "Must provide 1 or 2 lord types to count matches for");
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

			if ((!iterate) && (holder.correct < currentRecord)) {
				System.out.println(System.lineSeparator() + "Count of " + holder.correct + holderPercent + "is worse than current record of "
						+ currentRecord + startPercent);
				Util.printAnswers(test);
			}
			if (holder.correct == currentRecord) {
				int before = possibleSolutions.size();
				possibleSolutions.add(test);
				if (possibleSolutions.size() > before) {
					System.out.println(System.lineSeparator() + "Alternate: same record of " + currentRecord);
					Util.printAnswers(test);
				} else if(!iterate) {
					System.out.println("Count of " + holder.correct + holderPercent + "equals current record of " + givenRecord);
				}
			} else if (holder.correct > currentRecord) {
				if (iterate) {
					currentRecord = holder.correct;
					if (holder.correct >= givenRecord)
					System.out.println(System.lineSeparator() + "New Record: " + currentRecord + holderPercent + "up from " + givenRecord + startPercent);
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
				System.out.println(System.lineSeparator() + "Highest Lord Type");
				Util.printIndex(holder.first);
				System.out.println("Second Highest Lord Type");
				Util.printIndex(holder.second);
			}
		}
		if (iterate) {
			if (currentRecord == givenRecord) {
				this.iterate = false;//can't find a better solution so stop looking
				System.out.println(System.lineSeparator() + "No solutions found that exceed " + givenRecord + startPercent + "printed above");
				if (maxRecord < currentRecord) {
					System.out.println("Max Matches " +  maxRecord + getPercent(maxRecord, handsGenerator.size()) + "so not printing, iteration stopped");
				}
			} else {
				System.out.println(System.lineSeparator() + "Max Record: " + maxRecord + getPercent(maxRecord, handsGenerator.size()));
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
/*
0.5544093 seconds to generate all 74613 hands drawing 6 cards from the deck

1.4234258 seconds to generate all 74613 hands drawing 6 cards from the deck

searchFor1sEntry(ianuki) start

Starting: 74603
{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};

New Record: 74468 Changing Empress
{1,1,1,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};

New Record: 74512 Changing Fool
{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,1,2};

Max Record: 74512
{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,1,2};

Max Record 74512 (99.86%) is 91 less than original record of 74603 (99.99%) 
Fool at index 20 changed from 2 for 7 total 1's

Original Answers: 
{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};

print answers for Phantom Lord that work 98.74% of the time
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
HangedMan   : 1
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

countMatches(all1s) start

The Record: 30953 (41.48%) 
{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

Highest Lord Type
Ianuki:    12717
Phantom:   30953
Ice Cloud: 21335
Thunder:   9608

Second Highest Lord Type
Ianuki:    18152
Phantom:   19297
Ice Cloud: 16729
Thunder:   20435


searchForImprovement(all1s) start for Phantom Lord

New Record: 32583 (43.67%) up from 30953 (41.48%) 

New Record: 34322 (46.00%) up from 30953 (41.48%) 

New Record: 38765 (51.95%) up from 30953 (41.48%) 

New Record: 40518 (54.30%) up from 30953 (41.48%) 

New Record: 41410 (55.50%) up from 30953 (41.48%) 

Max Record: 41410 (55.50%) 
{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,3};
*/
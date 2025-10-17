package ogrebattle.tarot.exact;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.Set;
import java.util.TreeSet;
import ogrebattle.lordtypes.Ianuki;
import ogrebattle.lordtypes.IanukiSFC;
import ogrebattle.lordtypes.IceCloud;
import ogrebattle.lordtypes.IceCloudSFC;
import ogrebattle.printer.Util;
//static imports to reduce clutter
import static ogrebattle.tarot.pojo.LORD.IANUKI;
import static ogrebattle.tarot.pojo.LORD.PHANTOM;
import static ogrebattle.tarot.pojo.LORD.ICE_CLOUD;
import static ogrebattle.tarot.pojo.LORD.THUNDER;

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
	public final static int NANOSECONDS_IN_1_SECOND = 1_000_000_000;
	public final static int DECK_SIZE = Tarot.values().length;
	private static final TarotQuestions[] TAROT_LORD = TarotQuestions.values();
	private static final TarotQuestionsSFC[] TAROT_LORD_SFC = TarotQuestionsSFC.values();
	private static final int NUMBER_OF_CARDS = 6;//6 or 7 cards work just fine, bonus card not answered for points
	private static final boolean ORIGINAL_SFC_QUESTIONS = false;
	private static boolean isInitialized = false;
	private AllPossibleHands handsGenerator;
		
	public static final TarotAnswers ianuki = new TarotAnswers(Ianuki.getBaseDeepCopy(), new int[]{IANUKI.O}, 74603);//max Ianuki, 74603 out of 74613                                                  99.99%
	public static final TarotAnswers phantom = new TarotAnswers(new int[]{3,3,3,1,2,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3}, new int[]{PHANTOM.O}, 74137);//max Phantom                                     98.74%
	public static final TarotAnswers iceCloud = new TarotAnswers(IceCloud.getBaseDeepCopy(), new int[]{ICE_CLOUD.O}, 74613);//max Ice Cloud                                                            100%
	public static final TarotAnswers thunder = new TarotAnswers(new int[]{2,2,3,2,2,1,3,3,2,2,3,2,2,3,1,2,3,1,1,2,3,1}, new int[]{THUNDER.O}, 74009);//max Thunder                                     99.19%
	
	public static final TarotAnswers ianukiIceCloud = new TarotAnswers(new int[]{1,1,2,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,3,2,2}, new int[]{IANUKI.O, ICE_CLOUD.O}, 60171);//Ianuki 1st, Ice Cloud 2nd    80.64%
	public static final TarotAnswers phantomIceCloud = new TarotAnswers(new int[]{3,2,3,1,1,1,1,3,3,3,1,1,2,2,3,1,1,1,2,2,1,3}, new int[]{PHANTOM.O, ICE_CLOUD.O}, 43612);//Phantom 1st, Ice Cloud 2nd 58.45%

	public static final TarotAnswers ianukiSFC =   new TarotAnswers(IanukiSFC.getBaseDeepCopy(), new int[]{IANUKI.O}, 74506);//max Ianuki                                                              99.86%
	public static final TarotAnswers phantomSFC = new TarotAnswers(new int[]{3,3,1,1,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3,2}, new int[]{PHANTOM.O}, 74386);//max Phantom                                  99.70%
	public static final TarotAnswers iceCloudSFC = new TarotAnswers(IceCloudSFC.getBaseDeepCopy(), new int[]{ICE_CLOUD.O}, 74613);//max Ice Cloud                                                      100%
	public static final TarotAnswers thunderSFC = new TarotAnswers(new int[]{2,2,2,2,3,3,3,1,2,2,2,2,3,1,2,3,1,1,2,3,1,3}, new int[]{THUNDER.O}, 72839);//max Thunder                                  97.62%

	public static final TarotAnswers ianukiIceCloudSFC = new TarotAnswers(new int[]{2,1,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,1,2,2,1}, new int[]{THUNDER.O}, 56723);//Ianuki 1st, Ice Cloud 2nd             76.02%
	public static final TarotAnswers phantomIceCloudSFC = new TarotAnswers(new int[]{3,3,1,1,1,1,3,3,3,1,1,2,2,3,1,1,1,1,2,1,3,1}, new int[]{THUNDER.O}, 44814);//Phantom 1st, Ice CLoud 2nd           60.06%
	
	//Phantom most likely 41.48% and 31077 counts for 41.65% in Original SFC Release
	public static final TarotAnswers all1s = new TarotAnswers(new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, new int[]{PHANTOM.O}, 30953);
	
	private boolean iterate = true;
	private TarotAnswers tempAnswers = new TarotAnswers(all1s.getAnswers(), new int[]{IANUKI.O, ICE_CLOUD.O}, 0, true);
	
	public static void main(String[] args) {
		LordOddsExample e = new LordOddsExample();
//      e.searchFor1sEntry(ianuki);
//		e.searchForImprovement(all1s.getAnswers(), new int[]{IANUKI.O, ICE_CLOUD.O});
		e.countRecord(ianuki);
		
		System.out.println();
		Util.printAnswersByTarot(ianuki);
		//Util.printAnswersByGroup(ianuki);
		//Util.printAnswersByTarotAlphabetical(ianuki);
		
		while (e.iterate)
			e.searchForImprovement();//generate phantomIceCloud solution set up from nothing! 
		
		Util.printAnswersByGroup(e.tempAnswers);//answers for ianukiIceCloud grouped for speedrunning!

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
	 * @param record the number of correct lord types for that answer set
	 * @param order the desired lord types, first and optional second choice supported
	 */
	public void searchFor1sEntry(int[] solution, int record, int... order) {
		Set<int[]> possibleSolutions = new TreeSet<int[]>(new IntArrayComparator());
		Tarot[] values = Tarot.values();
		possibleSolutions.add(solution);
		//final int ordinal = doNotChange.ordinal()-1; 
		final int[] test = new int[DECK_SIZE];
		int[] maxSolution = new int[DECK_SIZE];
		int maxRecord = 0;
		int currentRecord = 0;
		System.arraycopy(solution, 0, test, 0, DECK_SIZE);// deep copy
		int originalAnswer = 0;
		int tarot = 0;
		int changes = 0;
		final int loops = DECK_SIZE;
		
		System.out.println(System.lineSeparator()+"Original: " + record);
		Util.printAnswers(test, DECK_SIZE);
		
		for (int i=0; i<loops; i++) {
			if (test[i] == 1) continue;// || (i == ordinal)) //if 1 or the preserved card
			
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
					Util.printAnswers(test, DECK_SIZE);
				} else {
					System.out.println("Count of " + holder.correct + " equals current record of " + currentRecord);
					Util.printAnswers(test, DECK_SIZE);
				}
			} else {//(correct > currentRecord) and can change this card
				System.out.println(System.lineSeparator() + "New Record: " + holder.correct + " Changing " + values[i]);
				currentRecord = holder.correct;
				tarot = i;
				Util.printAnswers(test, DECK_SIZE);
				if (holder.correct > maxRecord) {
					maxRecord = holder.correct;
					System.arraycopy(test, 0, maxSolution, 0, DECK_SIZE);//deep copy, necessary due to rotation on test
				}
			}
			test[i] = originalAnswer;// switch back to avoid another full array copy	
		}
		if (changes > 0) {
			if (maxRecord > 0) {
				System.out.println(System.lineSeparator() + "Max Record: " + maxRecord);
				Util.printAnswers(maxSolution, DECK_SIZE);
			}
			System.out.print(System.lineSeparator()+"Max Record " + currentRecord + getPercent(currentRecord, handsGenerator.size()) + "is "); 
			if (record > currentRecord) {
				System.out.println((record - currentRecord) + " less than starting record of " + record + getPercent(record, handsGenerator.size()));
				iterate = false;
			} else if (record < currentRecord) {
				System.out.println((currentRecord - record) + " greater than starting record of " + record  + getPercent(record, handsGenerator.size()));	
				tempAnswers = new TarotAnswers(maxSolution, tempAnswers.getDesiredLord(), currentRecord);
			} else {
				System.out.println(" somehow equal to the starting record");
				iterate = false;
			}
			System.out.println(values[tarot] + " at Position " + (tarot+1) + " changed from " + test[tarot]+ " for " + (DECK_SIZE - changes) + " total 1's");
			System.out.println(System.lineSeparator()+"Original Answers: ");
			Util.printAnswers(test, DECK_SIZE);
		} else System.out.println(System.lineSeparator()+"Every answer is 1 to begin with, nothing to do");
	}
	
	public void searchForImprovement() {
		searchForImprovement(tempAnswers.getAnswers(), tempAnswers.getRecord(), true, tempAnswers.getDesiredLord());
	}
	
	public void searchForImprovement(TarotAnswers solution, boolean iterate) {
		searchForImprovement(solution.getAnswers(), solution.getRecord(), iterate, solution.getDesiredLord());
	}
	
	public void searchForImprovement(TarotAnswers solution) {
		searchForImprovement(solution.getAnswers(), solution.getRecord(), this.iterate, solution.getDesiredLord());
	}
	
	public void searchForImprovement(int[] answers, int... order) {
		searchForImprovement(answers, 0, true, order);
	}
	
	public void countRecord(int[] answers, int... order) {
		searchForImprovement(answers, 0, false, order);
	}
	
	public void countRecord(TarotAnswers solution) {
		searchForImprovement(solution.getAnswers(), 0, false, solution.getDesiredLord());
	}
	
	public void searchForImprovement(int[] solution, int givenRecord, boolean iterate, int... order) {	
		Set<int[]> possibleSolutions = new TreeSet<int[]>(new IntArrayComparator());
		possibleSolutions.add(solution);
		final int[] test = new int[DECK_SIZE];
		int[] maxSolution = new int[DECK_SIZE];
		int maxRecord = 0;
		int currentRecord = givenRecord;
		System.arraycopy(solution, 0, test, 0, DECK_SIZE);//deep copy
		final int loops = (iterate) ? DECK_SIZE * 2 : 1;//1 loop if not iterating
		final String startPercent = getPercent(givenRecord, handsGenerator.size());
		for (int i=0; i<loops; i++) {
			if(iterate) {
				if (i < DECK_SIZE)
					rotateUp(test, i);
				else
					rotateDown(test, i - DECK_SIZE);
			}
			OrderHolder holder = new OrderHolder(test, order, loops, handsGenerator);
			if (holder.correct > maxRecord) {
				maxRecord = holder.correct;
			}
			final String holderPercent = getPercent(holder.correct, handsGenerator.size());

			if ((!iterate) && (holder.correct < currentRecord)) {
				System.out.println(System.lineSeparator() + "Count of " + holder.correct + holderPercent + "is worse than current record of "
						+ currentRecord + startPercent);
				Util.printAnswers(test, DECK_SIZE);
			}
			if (holder.correct == currentRecord) {
				int before = possibleSolutions.size();
				possibleSolutions.add(test);
				if (possibleSolutions.size() > before) {
					System.out.println(System.lineSeparator() + "Alternate: same record of " + currentRecord);
					Util.printAnswers(test, DECK_SIZE);
				} else if(!iterate) {
					System.out.println("Count of " + holder.correct + holderPercent + "equals current record of " + givenRecord);
				}
			} else if (holder.correct > currentRecord) {
				if (iterate) {
					currentRecord = holder.correct;
					System.out.println(System.lineSeparator() + "New Record: " + currentRecord + holderPercent + "up from " + givenRecord + startPercent);
				} else {
					System.out.println(System.lineSeparator() + "The Record: " + holder.correct + holderPercent);
				}
				Util.printAnswers(test, DECK_SIZE);
				System.arraycopy(test, 0, maxSolution, 0, DECK_SIZE);// deep copy, necessary due to rotating test
			}
			//switch back to avoid another full array copy
			if (iterate) {
				if (i < DECK_SIZE)
					rotateDown(test, i);
				else
					rotateUp(test, i - DECK_SIZE);
			} else {
				System.out.println(System.lineSeparator() + "Highest Lord Type");
				Util.printIndex(holder.first);
				System.out.println("Second Highest Lord Type");
				Util.printIndex(holder.second);
			}
		}
		if (!iterate) {
			System.out.println("Original Answers: ");
		} else if (currentRecord == givenRecord) {
			this.iterate = false;//can't find a better solution so stop looking
			System.out.println(System.lineSeparator() + "No solutions found that exceed " + givenRecord + startPercent);
			if (maxRecord < currentRecord) {
				System.out.println(System.lineSeparator() + "Actual matches found: " +  maxRecord + getPercent(maxRecord, handsGenerator.size())
						+ "are less than the provided " + givenRecord);
			}
		} else {
			System.out.println(System.lineSeparator() + "Max Record: " + maxRecord + getPercent(maxRecord, handsGenerator.size()));
			System.out.println(System.lineSeparator() + "Original Answers: ");
			tempAnswers = new TarotAnswers(maxSolution, tempAnswers.getDesiredLord(), maxRecord);
		} 
		Util.printAnswers(test, DECK_SIZE);
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
0.5711245 seconds to generate all 74613 hands drawing 6 cards from the deck

No better solutions found. Existing solution(s) for 74603 (99.99%) likely optimal.
{1,1,2,1,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2};

Magician    : 1
Priestess   : 1
Empress     : 2
Emperor     : 1
Hierophant  : 2
Lovers      : 2
Chariot     : 3
Strength    : 2
Hermit      : 2
Fortune     : 1
Justice     : 2
HangedMan   : 2
Death       : 3
Temperance  : 1
Devil       : 2
Tower       : 1
Star        : 2
Moon        : 3
Sun         : 2
Judgment    : 1
Fool        : 2
World       : 2
*/

package ogrebattle.tarot.exact;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.Set;
import java.util.TreeSet;
import ogrebattle.lordtypes.Ianuki;
import ogrebattle.lordtypes.IceCloud;
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
	private static final int NUMBER_OF_CARDS = 6;
	private static final boolean ORIGINAL_SFC_QUESTIONS = false;
	private static boolean isInitialized = false;
	
	public static TarotAnswers ianuki = new TarotAnswers(Ianuki.getBaseDeepCopy(), new int[]{IANUKI.O}, 74603);//max Ianuki, 74603 out of 74613                    99.99%
	public static TarotAnswers phantom = new TarotAnswers(new int[]{3,3,3,1,2,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3}, new int[]{PHANTOM.O}, 74137);//max Phantom       99.36%
	public static TarotAnswers iceCloud = new TarotAnswers(IceCloud.getBaseDeepCopy(), new int[]{ICE_CLOUD.O}, 74613);//max Ice Cloud                              100%	
	public static TarotAnswers thunder = new TarotAnswers(new int[]{2,2,1,2,2,1,3,3,2,2,3,2,2,3,1,2,3,1,1,2,3,1}, new int[]{THUNDER.O}, 74003);//max Thunder                                     99.18%
	public static TarotAnswers ianukiIceCloud = new TarotAnswers(new int[]{1,1,2,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,3,2,2}, new int[]{IANUKI.O, ICE_CLOUD.O}, 60171);//Ianuki 1st, Ice Cloud 2nd    93.41%
	public static TarotAnswers phantomIceCloud = new TarotAnswers(new int[]{3,2,3,1,1,1,1,3,3,3,1,1,2,2,3,1,1,1,2,2,1,3}, new int[]{PHANTOM.O, ICE_CLOUD.O}, 43612);//Phantom 1st, Ice Cloud 2nd 61.45%
	
	public static TarotAnswers ianukiSFC = new TarotAnswers(new int[]{2,2,3,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2,2}, new int[]{IANUKI.O}, 74506);//max Thunder                                     99.86%	
	public static TarotAnswers phantomSFC = new TarotAnswers(new int[]{3,3,1,1,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3,2}, new int[]{PHANTOM.O}, 74386);//max Thunder                                   99.70%
	public static TarotAnswers iceCloudSFC = new TarotAnswers(new int[]{3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,1}, new int[]{ICE_CLOUD.O}, 74613);//max Thunder                                100%
	public static TarotAnswers thunderSFC = new TarotAnswers(new int[]{2,2,2,2,3,3,3,1,2,2,2,2,3,1,2,3,1,1,2,3,1,3}, new int[]{THUNDER.O}, 72839);//max Thunder                                   97.62%
	public static TarotAnswers all1s = new TarotAnswers(new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, new int[]{PHANTOM.O}, 30953);//Phantom most likely 41.48%, 41.65% Original Release
	                                                
	private AllPossibleHands handsGenerator;
	
	public static void main(String[] args) {
		LordOddsExample e = new LordOddsExample();
		final boolean iterate = false;
		
		//e.searchFor1sEntry(ianuki);
		e.searchForImprovement(ianuki, iterate);
		Util.printAnswersByTarot(ianuki.getAnswers());
		//Util.printAnswersByTarotAlphabetical(tarotAnswers);
	}
	
	public LordOddsExample() {
		if (!isInitialized) {
			isInitialized = true;
			long start = System.nanoTime();
			handsGenerator = new AllPossibleHands(NUMBER_OF_CARDS, true);// unsorted (true) is faster, NUMBER OF CARDS should be 6 or 7
			long end = System.nanoTime();
	
			System.out.println((double) (end - start) / Util.NANOSECONDS_IN_1_SECOND + " seconds to execute for "
					+ handsGenerator.size() + " hands");
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
//			if (highestIndex != IANUKI.O) {
//			secondHighestIndex = IANUKI.O;
//			secondHighestScore = ianukiScore;
//		}
//		if (highestIndex != PHANTOM.O && phantomScore > secondHighestScore) {
//			secondHighestIndex = PHANTOM.O;
//			secondHighestScore = phantomScore;
//		}
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
	
	public void searchFor1sEntry(TarotAnswers answers) {
		searchFor1sEntry(answers.getAnswers(), answers.getRecord(), answers.getDesiredLord());
	}
	
	public void searchFor1sEntry(int[] solution, int record, int... order) {
		Set<int[]> possibleSolutions = new TreeSet<int[]>(new IntArrayComparator());
		possibleSolutions.add(solution);
		final int[] test = new int[DECK_SIZE];
		//final int ordinal = doNotChange.ordinal()-1; 
		int currentRecord = 0;
		System.arraycopy(solution, 0, test, 0, DECK_SIZE);// deep copy
		int originalAnswer = 0;
		int tarot = 0;
		final int loops = DECK_SIZE;
		
		System.out.println(System.lineSeparator()+"Original :");
		Util.printAnswers(test, DECK_SIZE);
		
		for (int i=0; i<loops; i++) {
			if (test[i] == 1) {// || (i == ordinal)) {//if 1 or the preserved card
				continue;
			}
			originalAnswer = test[i];
			test[i] = 1;

			OrderHolder holder = new OrderHolder(test, order, loops, handsGenerator);

			if (holder.correct < currentRecord) {
				//do nothing
			} else if (holder.correct == currentRecord) {
				int before = possibleSolutions.size();
				possibleSolutions.add(test);
				if (possibleSolutions.size() > before) {
					System.out.println(System.lineSeparator() + "Alternate for same count of " + currentRecord);
					Util.printAnswers(test, DECK_SIZE);
				} else {
					System.out.println("Count of " + holder.correct + " equals current record of " + currentRecord);
					Util.printAnswers(test, DECK_SIZE);
				}
			} else {//(correct > currentRecord) and can change this card
				System.out.println(System.lineSeparator() + "New Record: " + holder.correct + " up from " + currentRecord);
				currentRecord = holder.correct;
				tarot = i;
				Util.printAnswers(test, DECK_SIZE);
			}
			// switch back to avoid another full array copy	
			test[i] = originalAnswer;
		}
		int count = 0;
		for(int i : test) {
			if(i == 1) {
				count++;
			}
		}
		System.out.print(System.lineSeparator()+"Current record " + currentRecord + " is "); 
		if (record > currentRecord) {
			System.out.println((record - currentRecord) + " less than starting record of " + record);
		} else if (record < currentRecord) {
			System.out.println((currentRecord - record) + " greater than starting record of " + record);			
		} else {
			System.out.println(" somehow equal to the starting record");
		}
		System.out.println("Question " + (tarot+1) + " changed from " + test[tarot]+ " for " + count + " total 1's");
		System.out.println(System.lineSeparator() + "Original Answers: ");
		Util.printAnswers(test, DECK_SIZE);
	}
	
	public void searchForImprovement(TarotAnswers answers, boolean iterate) {
		searchForImprovement(answers.getAnswers(), answers.getRecord(), iterate, answers.getDesiredLord());
	}
	
	public void searchForImprovement(int[] solution, int record, boolean iterate, int... order) {	
		Set<int[]> possibleSolutions = new TreeSet<int[]>(new IntArrayComparator());
		possibleSolutions.add(solution);
		final int[] test = new int[DECK_SIZE];
		boolean newRecord = false;
		int currentRecord = 0;
		System.arraycopy(solution, 0, test, 0, DECK_SIZE);//deep copy
		final int loops = (iterate) ? DECK_SIZE * 2 : 1;//1 loop if not iterating
		for (int i=0; i<loops; i++) {
			currentRecord = record;
			if(iterate) {
				if (i < DECK_SIZE)
					rotateUp(test, i);
				else
					rotateDown(test, i - DECK_SIZE);
			}

			OrderHolder holder = new OrderHolder(test, order, loops, handsGenerator);

			if ((!iterate) && (holder.correct < currentRecord)) {
				System.out.println("Count of " + holder.correct + " is worse than current record of " + record);
				Util.printAnswers(test, DECK_SIZE);
			}
			if (holder.correct == currentRecord) {
				int before = possibleSolutions.size();
				possibleSolutions.add(test);
				if (possibleSolutions.size() > before) {
					System.out.println(System.lineSeparator() + "Alternate for same count of " + currentRecord);
					Util.printAnswers(test, DECK_SIZE);
				} else if(!iterate) {
					System.out.println("Count of " + holder.correct + " equals current record of " + record);
				}
			} else if (holder.correct > currentRecord) {
				newRecord = true;
				currentRecord = holder.correct;
				System.out.println(System.lineSeparator() + "New Record: " + currentRecord + " up from " + record);
				Util.printAnswers(test, DECK_SIZE);
			}
			//switch back to avoid another full array copy		
			if (iterate) {
				if (i < DECK_SIZE)
					rotateDown(test, i);
				else
					rotateUp(test, i - DECK_SIZE);
			} else {
				System.out.println(System.lineSeparator()+"Highest Lord Type");
				Util.printIndex(holder.first);
				System.out.println("Second Highest Lord Type");
				Util.printIndex(holder.second);
			}
		}
		if (!newRecord) {
			if (iterate) {
				System.out.println("No new solutions found. Existing solution(s) for " + record + " counts likely optimal.");
				Util.printAnswers(test, DECK_SIZE);
			}
		}
		System.out.println(System.lineSeparator() + "Original Answers: ");
		Util.printAnswers(test, DECK_SIZE);
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
0.5756881 seconds to execute for 74613 hands
Count of 74603 equals current record of 74603

Highest Lord Type
Ianuki:    74603
Phantom:   0
Ice Cloud: 0
Thunder:   10

Second Highest Lord Type
Ianuki:    10
Phantom:   13223
Ice Cloud: 32923
Thunder:   28457


Original Answers: 
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

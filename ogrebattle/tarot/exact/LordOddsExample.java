package ogrebattle.tarot.exact;

import static ogrebattle.tarot.pojo.TarotSorting.*;

import java.util.Set;
import java.util.TreeSet;
import ogrebattle.lordtypes.Ianuki;
import ogrebattle.lordtypes.IceCloud;
import ogrebattle.printer.Util;
import ogrebattle.tarot.pojo.Tarot;
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
	public final static int IANUKI=0; final static int PHANTOM=1; final static int ICE_CLOUD=2; final static int THUNDER=3;
	private static final TarotQuestions[] TAROT_LORD = TarotQuestions.values();
	private static final TarotQuestionsSFC[] TAROT_LORD_SFC = TarotQuestionsSFC.values();
	private static final boolean ORIGINAL_SFC_QUESTIONS = true;
	private static boolean isInitialized = false;
	
	//public final static List<int[]> answersIanukiAll9 = new Ianuki().returnAllSolutionsList();
	//public final static List<int[]> answersIceCloudAll65 = new IceCloud().returnAllSolutionsList();
	
	public final static int[] answersIanuki =     Ianuki.getBASE();                               //max ianuki          74603 out of 74613 99.99%
	public final static int[] answersPhantom =    {3,3,3,1,2,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3};  //max phantom         74137 out of 74613 99.36%	
	public final static int[] answersIceCloud =   IceCloud.getBASE();                             //max ice cloud       74613 out of 74613 100%
	public final static int[] answersThunder =    {2,2,1,2,2,1,3,3,2,2,3,2,2,3,1,2,3,1,1,2,3,1};  //max thunder         74003 out of 74613 99.18%
	
	public final static int[] ianukiIceCloud =    {1,1,2,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,3,2,2};  //ianuki, ice cloud   69698 out of 74613 93.41%
	public final static int[] phantomIceCloud =   {3,2,3,1,1,1,1,3,3,2,1,1,2,2,3,1,1,1,2,2,1,3};  //phantom, ice cloud  45848 out of 74613 61.45%
	
	public final static int[] sfcIanuki =         {1,2,3,2,2,3,2,2,1,2,2,3,1,2,1,2,3,2,1,2,2,2};  //ianuki most likely 74506 out of 74613 99.86%
	public final static int[] sfcPhantom =        {3,3,1,1,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3,2};  //phantom most likely 74386 out of 74613 99.70%
	public final static int[] sfcIceCloud =       {3,1,3,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2,1};  //ice cloud most likely 74613 out of 74613 100% 9
	public final static int[] sfcThunder =        {2,2,2,2,3,3,3,1,2,2,2,2,3,1,2,3,1,1,2,3,1,3};  //thunder most likely 72839 out of 74613 97.62%
	
	public final static int[] all1s =             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};  //phantom most likely 30953 out of 74613 41.48%
	                                                
	private AllPossibleHands handsGenerator;
	
	public static void main(String[] args) {
		LordOddsExample e = new LordOddsExample();
		final int[] tarotAnswers = sfcPhantom;
		final int[] desiredLord = new int[]{PHANTOM};
		final int record = 74386;
		final boolean iterate = false;
		
		//e.searchFor1sEntry(tarotAnswers, record, desiredLord);
		e.searchForImprovement(tarotAnswers, record, iterate, desiredLord);
		System.out.println();
		Util.printAnswersByTarot(tarotAnswers);
		//Util.printAnswersByTarotAlphabetical(tarotAnswers);
	}
	
	public LordOddsExample() {
		if (!isInitialized) {
			isInitialized = true;
			long start = System.nanoTime();
			handsGenerator = new AllPossibleHands(6, true);// unsorted (true) is faster
			long end = System.nanoTime();
	
			System.out.println((double) (end - start) / NANOSECONDS_IN_1_SECOND + " seconds to execute for "
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
				} // else {
					// break;//ICE CLOUD ONLY! where 100% success from any 6 cards is possible
					// }
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
			int ianuki = tracker[0]; int phantom = tracker[1]; int icecloud = tracker[2]; int thunder = tracker[3];
			int highestIndex = IANUKI; int secondHighestIndex = -1;//default to Ianuki
			int highestScore = ianuki; int secondHighestScore = 0;
			
			//highest
			if (phantom > highestScore) {
				highestIndex = PHANTOM;
				highestScore = phantom;
			}
			if (icecloud > highestScore) {
				highestIndex = ICE_CLOUD;
				highestScore = icecloud;
			}
			if (thunder > highestScore) {
				highestIndex = THUNDER;
				highestScore = thunder;
			}
			//second highest
			if (highestIndex != IANUKI) {
				secondHighestIndex = IANUKI;
				secondHighestScore = tracker[0];
			}
			if (highestIndex != PHANTOM && tracker[1] > secondHighestScore) {
				secondHighestIndex = PHANTOM;
				secondHighestScore = tracker[1];
			}
			if (highestIndex != ICE_CLOUD && tracker[2] > secondHighestScore) {
				secondHighestIndex = ICE_CLOUD;
				secondHighestScore = tracker[2];
			}
			if (highestIndex != THUNDER && tracker[3] > secondHighestScore) {
				secondHighestIndex = THUNDER;
				secondHighestScore = tracker[3];
			}
			return new int[]{highestIndex, secondHighestIndex};
		}
		
		private int[] trackResults(int[] test, TreeSet<Tarot> hand) {
			final int[] tracker = new int[4]; //4 Lord Types
			for (Tarot card : hand) {
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
	
	public void searchForImprovement(int[] solution, int record, boolean iterate, int... order) {	
//		Ianuki ianuki = new Ianuki();
//		Set<int[]> ianukiSolutions = ianuki.returnAllSolutionsSet();
		Set<int[]> possibleSolutions = new TreeSet<int[]>(new IntArrayComparator());
		possibleSolutions.add(solution);
//		final int found = possibleSolutions.size();
		
//		Iterator<int[]> it = ianukiSolutions.iterator();
//		while(it.hasNext()) {
		final int[] test = new int[DECK_SIZE];
		boolean newRecord = false;
		int currentRecord = 0;
		System.arraycopy(solution, 0, test, 0, DECK_SIZE);// deep copy
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
				System.out.println(System.lineSeparator()+"Second Highest Lord Type");
				Util.printIndex(holder.second);
				System.out.println();
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
//	}
	
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
0.6649104 seconds to execute for 74613 hands
Count of 9801 equals current record of 9801

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


Original Answers: 
{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

Chariot     : 1
Death       : 1
Devil       : 1
Emperor     : 1
Empress     : 1
Fool        : 1
Fortune     : 1
Hanged_Man  : 1
Hermit      : 1
Hierophant  : 1
Judgment    : 1
Justice     : 1
Lovers      : 1
Magician    : 1
Moon        : 1
Priestess   : 1
Star        : 1
Strength    : 1
Sun         : 1
Temperance  : 1
Tower       : 1
World       : 1
*/

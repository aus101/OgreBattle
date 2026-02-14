package ogrebattle.tarot.exact;

import java.util.Arrays;
import ogrebattle.printer.Util;
//static import to reduce clutter
import static ogrebattle.tarot.pojo.LORD.*;

import ogrebattle.tarot.pojo.TarotAnswers;

/**
 * Not really intended to be clean, reusable code. Rather to show how to use {@link ogrebattle.tarot.generator.AllPossibleHands} to derive optimal solutions.
 * The commented out code shows how to iterate through Ianuki or Ice Cloud that have more than one optimal set of Tarot answers.
 * The rotateUp and rotateDown code check all 44 combinations from changing 1 Tarot card answer for odds improvement.
 * More Ice Cloud solutions could theoretically exist but would be at least 3 Tarot card changes from all solutions given.
 * Perhaps the best extension would be finding optimal solutions for any Lord being the second highest total.<br>
 */

public class LordOddsExample {
	public static final TarotAnswers ianuki = new TarotAnswers(74603, Util.IanukiBaseDeepCopy(), IANUKI);//max Ianuki, 74603 out of 74613                                                  99.99%
	public static final TarotAnswers phantom = new TarotAnswers(73671, new int[]{3,3,3,1,2,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3}, PHANTOM);//max Phantom   was 74137?                         98.74%
	public static final TarotAnswers iceCloud = new TarotAnswers(74613, Util.IceCloudBaseDeepCopy(), ICE_CLOUD);//max Ice Cloud                                                            100%
	public static final TarotAnswers thunder = new TarotAnswers(74009, new int[]{2,2,3,2,2,1,3,3,2,2,3,2,2,3,1,2,3,1,1,2,3,1}, THUNDER);//max Thunder                                      99.19%
	
	public static final TarotAnswers ianukiIceCloud = new TarotAnswers(60171, new int[]{1,1,2,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,3,2,2}, IANUKI, ICE_CLOUD);//Ianuki 1st, IceCloud 2nd        80.64%
	public static final TarotAnswers phantomIceCloud = new TarotAnswers(43612, new int[]{3,2,3,1,1,1,1,3,3,3,1,1,2,2,3,1,1,1,2,2,1,3}, PHANTOM, ICE_CLOUD);//Phantom 1st, IceCloud 2nd     58.45%
	
	public static final TarotAnswers ianukiPhantomA = new TarotAnswers(46334, new int[]{1,1,2,1,2,3,1,2,1,1,3,1,3,1,1,1,2,3,2,1,2,2}, IANUKI, PHANTOM);//Ianuki 1st, Phantom 2nd           62.10%  
	public static final TarotAnswers iceCloudPhantomA = new TarotAnswers(57633, new int[]{3,3,3,3,1,1,2,3,3,3,1,3,1,2,3,3,1,1,2,2,1,2}, ICE_CLOUD, PHANTOM);//IceCloud 1st, Phantom 2nd    77.24%  

	public static final TarotAnswers ianukiSFC =   new TarotAnswers(74535, Util.IanukiSFCBaseDeepCopy(), IANUKI);//max Ianuki                                                              99.90%
	public static final TarotAnswers phantomSFC = new TarotAnswers(74386, new int[]{3,3,1,1,3,1,3,1,2,1,1,3,2,1,1,3,1,1,2,1,3,2}, PHANTOM);//max Phantom                                   99.70%
	public static final TarotAnswers iceCloudSFC = new TarotAnswers(74613, Util.IceCloudSFCBaseDeepCopy(), ICE_CLOUD);//max Ice Cloud                                                      100%
	public static final TarotAnswers thunderSFC = new TarotAnswers(72839, new int[]{2,2,2,2,3,3,3,1,2,2,2,2,3,1,2,3,1,1,2,3,1,3}, THUNDER);//max Thunder                                   97.62%

	public static final TarotAnswers ianukiIceCloudSFC = new TarotAnswers(56723, new int[]{2,1,3,3,2,2,1,2,1,2,2,3,1,2,1,2,3,2,1,2,2,1}, IANUKI, ICE_CLOUD);//Ianuki 1st, IceCloud 2nd     76.02%
	public static final TarotAnswers phantomIceCloudSFC = new TarotAnswers(45058, new int[]{3,3,3,1,1,1,3,3,2,1,1,2,2,3,1,3,1,2,2,1,3,1}, PHANTOM, ICE_CLOUD);//Phantom 1st, Ice Loud 2nd  60.39%
	
	public static final TarotAnswers ianukiPhantomSFCA = new TarotAnswers(52114, new int[]{1,1,1,2,3,3,2,1,1,3,3,3,1,1,1,2,3,2,1,2,2,2}, IANUKI, PHANTOM);//Ianuki 1st, Phantom 2nd        69.85%  
	public static final TarotAnswers iceCloudPhantomSFCA = new TarotAnswers(56359, new int[]{3,1,3,1,1,2,3,3,3,1,3,1,2,3,3,1,1,3,2,1,3,1}, ICE_CLOUD, PHANTOM);//IceCloud 1st, Phantom 2nd 75.54%  
	
	//Phantom most likely with 30953 out of 74613 (41.48%) and 31077 (41.65%) in Original SFC Release
	public static final TarotAnswers all1s = new TarotAnswers(30953, new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, PHANTOM);

	public static void main(String[] args) {
		start();
	}
	
	public static void start() {
		LordOdds odds = LordOdds.generateAnd().useEnglishAndLaterReleasesPoints().andInitializeWith6CardHands();
	
		System.out.println("Confirm an answer set for Ice Cloud works 100% of the time by entering it manually:");
		odds.justCount(new int[]{3,1,2,2,1,1,2,1,3,3,1,3,1,1,3,3,1,2,3,3,3,2}, ICE_CLOUD);
		
		System.out.println(System.lineSeparator() + "Print answers for Phantom Lord that work 98.74% of the time");
		Util.printAnswersByTarot(phantom);
		
		System.out.println("Count of each lord type by choosing all 1's for the 6 Tarot card questions:");
		System.out.println("countMatches(all1s)");
		odds.countMatches(all1s);
		
		System.out.println("Now change a single answer to a 2 or 3 that improves the odds of getting Phantom Lord the most:");
		System.out.println("searchForImprovement(all1s) start for Phantom Lord");
		odds.searchForImprovement(all1s);
		
		System.out.println("Programmatically generate the Phantom Lord with Ice Cloud second highest total answers, phantomIceCloud:");
		System.out.println("continuousSearchForImprovement(new TarotAnswers(0, true, all1s.getAnswers(), PHANTOM, ICE_CLOUD))");
		odds.continuousSearchForImprovement(new TarotAnswers(0, true, all1s.getAnswers(), PHANTOM, ICE_CLOUD));//generate the phantomIceCloud solution set up from nothing! 	
/*		
		int temp1[] = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		int temp2[] = {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2};
		int temp3[] = {3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3};		
		System.out.println("temp1");
		odds.continuousSearchForImprovement(new TarotAnswers(0, true, temp1, IANUKI, ICE_CLOUD));
		System.out.println("temp2");
		odds.continuousSearchForImprovement(new TarotAnswers(0, true, temp2, IANUKI, ICE_CLOUD));
		System.out.println("temp3");
    	odds.continuousSearchForImprovement(new TarotAnswers(0, true, temp3, IANUKI, ICE_CLOUD));
    	
		arrayCompare(ianukiIceCloudSFC.getAnswers(), new int[] {3,2,3,3,2,3,3,3,1,3,3,3,1,2,3,2,3,3,3,3,2,1});
*/	
/*
		long startT = System.nanoTime();
		odds.getPossibleSolutions().clear();
		odds.addSolution(ianukiIceCloudSFC.getAnswers());
		int[] starting = new int[]{0,0,3,3,2,0,0,0,1,0,0,3,1,2,0,2,3,0,0,0,2,1};
		int[] indices  = getIndices(starting);
		int[][] iterate = ogrebattle.tarot.generator.TernaryGenerator.littleEndianGenerator(indices.length, 1);//avoid warning on import	
		
		for (int i=0; i<iterate.length; i++) {//number of columns = number of solutions
			for (int j=0; j<indices.length; j++) {
				starting[indices[j]] = iterate[i][j]; 
			}
			odds.fastCount(true, ianukiIceCloudSFC.getRecord(), starting, ianukiIceCloudSFC.getDesiredLord());
		}
		long endT = System.nanoTime();
		
		System.out.println("DONE IN " + Util.nanoToMinutesSeconds((endT - startT)));
*/
	}

	/**
	 * Print a new answer set that has 0 for each answer that is different between the two and the shared value if not,<br>
	 * then print the zero indices as a new array, then count the number of zeroes, i.e. the count different answers<br>
	 * @param solution the current answer set
	 * @param proposal the comparison answer set
	 */
	//package default on purpose
	static void arrayCompare(int[] solution, int[] proposal) {
		int zeroes = 0;
		StringBuilder sb = new StringBuilder("{");
		StringBuilder z0 = new StringBuilder("{");
		for (int i = 0; i < solution.length; i++) {
			if (solution[i] == proposal[i]) {
				sb.append(solution[i]).append(",");
			} else {
				sb.append("0").append(",");
				z0.append(i).append(",");
				zeroes++;
			}
		}
		sb.replace(sb.length() - 1, sb.length(), "}");
		System.out.println(sb.toString());
		z0.replace(z0.length() - 1, z0.length(), "}");
		z0.append(System.lineSeparator()).append("ZEROES: ").append(zeroes).append(System.lineSeparator());
		System.out.println(z0.toString());
	}

	//package default on purpose
	static int[] getIndices(int[] proposal) {
		int zeroes=0;
		int[] temp = new int[proposal.length];
		for(int i=0; i<proposal.length; i++) {
			if(proposal[i] == 0) {
				temp[zeroes] = i;
				zeroes++;
			}
		}
		return Arrays.copyOf(temp, zeroes);
	}
}
/*
0.5627753 seconds to generate all 74613 hands drawing 6 cards from the deck
Confirm an answer set for Ice Cloud works 100% of the time by entering it manually:
The Record: 74613 (100%) 

Print answers for Phantom Lord that work 98.74% of the time
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

Count of each lord type by choosing all 1's for the 6 Tarot card questions:
countMatches(all1s)

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

Now change a single answer to a 2 or 3 that improves the odds of getting Phantom Lord the most:
searchForImprovement(all1s) start for Phantom Lord

Max Record: 41410 (55.50%) up from 30953 (41.48%) 
{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,3};

Programmatically generate the Phantom Lord with Ice Cloud second highest total answers, phantomIceCloud:
continuousSearchForImprovement(new TarotAnswers(0, true, all1s.getAnswers(), PHANTOM, ICE_CLOUD))

Max Record: 14985 (20.08%) up from 0 (0.00%) 
{1,1,1,1,1,1,1,1,1,1,1,1,1,2,1,1,1,1,1,1,1,1};


Max Record: 21173 (28.38%) up from 14985 (20.08%) 
{1,1,1,1,1,1,1,1,1,1,1,1,1,2,1,1,1,1,1,1,1,3};


Max Record: 26510 (35.53%) up from 21173 (28.38%) 
{1,1,1,1,1,1,1,1,1,3,1,1,1,2,1,1,1,1,1,1,1,3};


Max Record: 31114 (41.70%) up from 26510 (35.53%) 
{3,1,1,1,1,1,1,1,1,3,1,1,1,2,1,1,1,1,1,1,1,3};


Max Record: 33774 (45.27%) up from 31114 (41.70%) 
{3,1,1,1,1,1,1,1,3,3,1,1,1,2,1,1,1,1,1,1,1,3};


Max Record: 36242 (48.57%) up from 33774 (45.27%) 
{3,1,3,1,1,1,1,1,3,3,1,1,1,2,1,1,1,1,1,1,1,3};


Max Record: 38456 (51.54%) up from 36242 (48.57%) 
{3,1,3,1,1,1,1,3,3,3,1,1,1,2,1,1,1,1,1,1,1,3};


Max Record: 40282 (53.99%) up from 38456 (51.54%) 
{3,3,3,1,1,1,1,3,3,3,1,1,1,2,1,1,1,1,1,1,1,3};


Max Record: 41004 (54.96%) up from 40282 (53.99%) 
{3,3,3,1,1,1,1,3,3,3,1,1,1,2,3,1,1,1,1,1,1,3};


Max Record: 42267 (56.65%) up from 41004 (54.96%) 
{3,3,3,1,1,1,1,3,3,3,1,1,2,2,3,1,1,1,1,1,1,3};


Max Record: 42588 (57.08%) up from 42267 (56.65%) 
{3,3,3,1,1,1,1,3,3,3,1,1,2,2,3,1,1,1,1,2,1,3};


Max Record: 42957 (57.57%) up from 42588 (57.08%) 
{3,3,3,1,1,1,1,3,3,3,1,1,2,2,3,1,1,1,2,2,1,3};


Max Record: 43612 (58.45%) up from 42957 (57.57%) 
{3,2,3,1,1,1,1,3,3,3,1,1,2,2,3,1,1,1,2,2,1,3};


No solutions found that exceed 43612 (58.45%) printed above
Max Matches 43292 (58.02%) so not printing, iteration stopped
*/
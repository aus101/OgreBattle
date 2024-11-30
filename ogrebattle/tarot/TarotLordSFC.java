package ogrebattle.tarot;
/**
 * Note: Ianuki is called Sonic Blade in the PlayStation release. These point values are correct for all releases.<br>
 * Order is answer 1, 2, 3, 4 for Ianuki, Phantom, Ice Cloud, Thunder.<br>
 * since that is the order the game stores the Lord values and therefore the order used to break ties.<br>
 * i.e. First answer Poison for Magician gives 2 points to Ianuki, 4 points to Phantom, 1 point to Ice Cloud and 4 points to Thunder.<br>
 */
public enum TarotLordSFC {
	Magician(new int[][]{{2,4,1,4}, {3,2,1,5}, {1,5,2,2}}),//same as all other releases 
	Priestess(new int[][]{{2,1,5,1}, {3,2,1,4}, {1,4,3,5}}), 
	Empress(new int[][]{{5,4,2,4}, {3,1,4,5}, {4,2,5,1}}),
	
	Emperor(new int[][]{{3,2,5,2}, {5,3,1,4}, {4,1,4,3}}),
	Hierophant(new int[][]{{1,2,4,2}, {5,2,5,3}, {4,5,1,4}}),
	Lovers(new int[][]{{2,5,1,4}, {3,1,5,3}, {5,4,2,5}}),
	
	Chariot(new int[][]{{3,1,5,1}, {5,3,1,2}, {4,5,4,5}}),
	Strength(new int[][]{{3,5,1,4}, {4,1,3,3}, {2,3,4,1}}),
	Hermit(new int[][]{{5,1,2,1}, {1,5,2,4}, {2,4,5,3}}),
	
	Fortune(new int[][]{{1,4,4,2}, {4,1,2,5}, {2,3,1,4}}),
	Justice(new int[][]{{1,5,1,3}, {4,2,2,4}, {3,3,4,4}}),
	HangedMan(new int[][]{{1,2,5,2}, {1,3,2,4}, {5,4,1,4}}),
	
  	Death(new int[][]{{5,1,5,1}, {1,5,4,2}, {3,3,2,5}}),
    Temperance(new int[][]{{4,5,1,5}, {5,1,2,3}, {1,2,5,1}}),      
    Devil(new int[][]{{3,5,3,1}, {4,4,2,5}, {1,2,5,2}}),
    
    Tower(new int[][]{{1,2,3,2}, {3,1,2,1}, {2,3,1,3}}), 
    Star(new int[][]{{1,3,2,3}, {2,1,3,2}, {3,1,1,1}}), 
    Moon(new int[][]{{1,4,2,4}, {3,2,3,1}, {2,1,4,2}}),
    
    Sun(new int[][]{{2,1,1,1}, {1,3,2,3}, {3,2,3,2}}),
    Judgment(new int[][]{{3,4,4,1}, {4,3,2,2}, {2,1,3,3}}), 
    Fool(new int[][]{{1,1,2,5}, {4,2,4,2}, {2,5,2,1}}),
    World(new int[][]{{15,12,15,8}, {15,14,0,8}, {0,3,15,8}});//much higher values than all other releases
    
	private int[][] values = new int[4][3];
	
    TarotLordSFC(int [][] values) {
        this.values = values;
    }
    
    public int[][] getAllValues() {
    	return values;
    }
    
    public static int[][] getValues(TarotLordSFC tarot) {
    	return tarot.values;
    }
    
    public static int[] getValues(TarotLordSFC tarot, int answer) {
    	if (answer < 1 || answer > 3) {
    		throw new ArrayIndexOutOfBoundsException("question must be 1, 2 or 3 for first, second or third answer");
    	}
    	return tarot.values[answer-1];
    }
    
    public int[] getValues(int answer) {
    	return this.values[answer-1];
    }
}


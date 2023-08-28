package ogrebattle.tarot.pojo;
/**
 * Order is answer 1, 2, 3, 4 for Ianuki, Phantom, Ice Cloud, Thunder<br>
 * since that is the order the game stores the Lord values and therefore the order used to break ties.<br>
 * i.e. First answer Poison for Magician gives 2 points to Ianuki, 4 points to Phantom, 1 point to Ice Cloud and 4 points to Thunder<br>
 */
public enum TarotQuestions {
	Magician(new int[][]{{2,4,1,4}, {3,2,1,5}, {1,5,2,2}}), 
	Priestess(new int[][]{{5,1,5,2}, {3,1,4,2}, {2,1,5,1}}), 
	Empress(new int[][]{{3,2,1,4}, {4,1,4,1}, {1,4,5,3}}),  
	
	Emperor(new int[][]{{5,4,2,4}, {3,1,4,5}, {4,2,5,1}}), 
	Hierophant(new int[][]{{3,2,5,2}, {5,3,1,4}, {4,1,4,3}}), 
	Lovers(new int[][]{{1,2,4,2}, {5,2,5,3}, {4,5,1,4}}), 
	
	Chariot(new int[][]{{2,5,1,4}, {3,1,5,3}, {5,4,2,5}}), 
	Strength(new int[][]{{3,1,5,1}, {5,3,1,2}, {4,5,4,5}}), 
	Hermit(new int[][]{{3,5,1,4}, {4,1,3,3}, {2,3,4,1}}), 
	
	Fortune(new int[][]{{5,1,2,1}, {1,5,2,4}, {2,4,5,3}}), 
	Justice(new int[][]{{1,4,4,2}, {4,1,2,5}, {2,3,1,4}}), 
	HangedMan(new int[][]{{1,5,1,3}, {4,2,2,4}, {3,3,4,4}}),
	
	Death(new int[][]{{1,2,5,2}, {1,3,2,4}, {5,4,1,4}}), 
	Temperance(new int[][]{{5,1,5,1}, {1,5,4,2}, {3,3,2,5}}), 
	Devil(new int[][]{{4,5,1,5}, {5,1,2,3}, {1,2,5,1}}),  
    
	Tower(new int[][]{{3,5,3,1}, {4,4,2,5}, {1,2,5,2}}), 
	Star(new int[][]{{1,2,3,2}, {3,1,2,1}, {2,3,1,3}}), 
	Moon(new int[][]{{1,3,2,3}, {2,1,3,2}, {3,1,1,1}}), 
    
	Sun(new int[][]{{1,4,2,4}, {3,2,3,1}, {2,1,4,2}}), 
	Judgment(new int[][]{{2,1,1,1}, {1,3,2,3}, {3,2,3,2}}), 
	Fool(new int[][]{{3,4,4,1}, {4,3,2,2}, {2,1,3,3}}), 
	World(new int[][]{{1,1,2,5}, {4,2,4,2}, {2,5,2,1}});
    
	private int[][] values = new int[4][3];
	
    TarotQuestions(int [][] values) {
        this.values = values;
    }
    
    public int[][] getAllValues() {
    	return values;
    }
    
    public static int[][] getValues(TarotQuestions tarot) {
    	return tarot.values;
    }
    
    public static int[] getValues(TarotQuestions tarot, int answer) {
    	if (answer < 1 || answer > 3) {
    		throw new ArrayIndexOutOfBoundsException("question must be 1, 2 or 3 for first, second or third answer");
    	}
    	return tarot.values[answer-1];
    }
    
    public int[] getValues(int answer) {
    	return this.values[answer-1];
    }
}


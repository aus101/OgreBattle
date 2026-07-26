package ogrebattle.tarot.exact;

public class TarotCardGenerator {
	//the parameters of final objects can still be modified
	final byte[] lookupTable = new byte[112];//11C6 for last card, $11C8 + $6C =  $1234
	//final BytePair[] lookupTableObject = new BytePair[56];
	static int OFFSET = 0;
	static final int LAST_CARD_ADDRESS = 0;//$11C6
	static final int RNG_ADDRESS = 2;//$11C8
	static final int ITER = 2;//Y Register uses INY, INY
	static final int RNG_CALL_LIMIT = 55;//6E divided by 2
	static final int LOWER_SHUFFLER_ADDRESS = 62;//$1206 - $11C8 = $3E = 62, goes up to + $2E = $1234 = end of lookup table
	static final int LOWER_SHUFFLER_LIMIT = 48;//$30	
	static final int UPPER_SHUFFLER_ADDRESS = -30;//$1198 - $11C8 = -30 in base 10 but INDEX is at 30 when second loop starts
	static final int UPPER_SHUFFLER_LIMIT = 110;//$CE
}

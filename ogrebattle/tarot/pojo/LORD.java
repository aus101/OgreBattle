package ogrebattle.tarot.pojo;

public enum LORD {
	IANUKI(0), PHANTOM(1), ICE_CLOUD(2), THUNDER(3);//0, 1, 2, 3 ordinal ints but being explicit
	
	public final int O;
	private LORD(int ordinal) {
		O = ordinal;
	}
}

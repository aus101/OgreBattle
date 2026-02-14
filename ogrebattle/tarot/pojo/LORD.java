package ogrebattle.tarot.pojo;

public enum LORD {
	IANUKI(0), PHANTOM(1), ICE_CLOUD(2), THUNDER(3);//equivalent to <ENUM>.ordinal() but being explicit
	
	public final int O;
	private LORD(int ordinal) {
		O = ordinal;
	}
	
	public static LORD reverse(int o) {
		LORD reverse;
		switch (o)//Java 12 can return in the switch and not need breaks but let's not force Java 12+
		{
		    case 0: reverse = IANUKI;
		        break;
		    case 1: reverse = PHANTOM;
		        break;
		    case 2: reverse = ICE_CLOUD;
		    	break;
		    case 3: reverse = THUNDER;
		    	break;
		    default: reverse = null;
		}
		return reverse;
	}
}
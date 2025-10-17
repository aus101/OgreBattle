package ogrebattle.tarot.pojo;

public class TarotAnswers {
	protected int[] answers;
	protected int[] desiredLord;
	protected int record;
	
	public TarotAnswers(int[] answers, int... desiredLord) {
		this.answers = answers;
		this.desiredLord = desiredLord;
		this.record = 0;
	}
	
	public TarotAnswers(int[] answers, LORD... desiredLord) {
		this.answers = answers;
		this.desiredLord(desiredLord);
		this.record = 0;
	}
	
	public TarotAnswers(boolean deepCopyArrays, int[] answers, int... desiredLord) {
		System.arraycopy(answers, 0, this.answers, 0, answers.length);
		System.arraycopy(desiredLord, 0, this.desiredLord, 0, desiredLord.length);
		this.record = 0;
	}
	
	public TarotAnswers(boolean deepCopyArrays, int[] answers, LORD... desiredLord) {
		System.arraycopy(answers, 0, this.answers, 0, answers.length);
		this.desiredLord(desiredLord);
		this.record = 0;
	}
	
	public TarotAnswers(int record, int[] answers, int... desiredLord) {
		this.answers = answers;
		this.desiredLord = desiredLord;
		this.record = record;
	}
	
	public TarotAnswers(int record, int[] answers, LORD... desiredLord) {
		this.answers = answers;
		this.desiredLord(desiredLord);
		this.record = record;
	}
	
	public TarotAnswers(int record, boolean deepCopyArrays, int[] answers, int... desiredLord) {
		this.answers = new int[answers.length];
		this.desiredLord = new int[desiredLord.length];
		System.arraycopy(answers, 0, this.answers, 0, answers.length);
		System.arraycopy(desiredLord, 0, this.desiredLord, 0, desiredLord.length);
		this.record = record;
	}
	
	public TarotAnswers(int record, boolean deepCopyArrays, int[] answers, LORD... desiredLord) {
		this.answers = new int[answers.length];
		this.desiredLord = new int[desiredLord.length];
		System.arraycopy(answers, 0, this.answers, 0, answers.length);
		this.desiredLord(desiredLord);
		this.record = record;
	}
	
	private void desiredLord(LORD[] desiredLord) {//effectively a deep copy
		this.desiredLord = new int[desiredLord.length];
		for(int i=0; i<desiredLord.length; i++) {
			this.desiredLord[i] = desiredLord[i].O;
		}
	}

	public int[] getAnswers() {
		return answers;
	}

	public int[] getDesiredLord() {
		return desiredLord;
	}

	public int getRecord() {
		return record;
	}

	public void setRecord(int record) {
		this.record = record;
	}
}

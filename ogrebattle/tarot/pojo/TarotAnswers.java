package ogrebattle.tarot.pojo;

public class TarotAnswers {
	protected int[] answers;
	protected int[] desiredLord;
	protected int record;
	
	public TarotAnswers(int[] answers, int[] desiredLord) {
		this.answers = answers;
		this.desiredLord = desiredLord;
		this.record = 0;
	}
	
	public TarotAnswers(int[] answers, int[] desiredLord, boolean deepCopyArrays) {
		System.arraycopy(answers, 0, this.answers, 0, answers.length);
		System.arraycopy(desiredLord, 0, this.desiredLord, 0, desiredLord.length);
		this.record = 0;
	}
	
	public TarotAnswers(int[] answers, int[] desiredLord, int record) {
		this.answers = answers;
		this.desiredLord = desiredLord;
		this.record = record;
	}
	
	public TarotAnswers(int[] answers, int[] desiredLord, int record, boolean deepCopyArrays) {
		System.arraycopy(answers, 0, this.answers, 0, answers.length);
		System.arraycopy(desiredLord, 0, this.desiredLord, 0, desiredLord.length);
		this.record = record;
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

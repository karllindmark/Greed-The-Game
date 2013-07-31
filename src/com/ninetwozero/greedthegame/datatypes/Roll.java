package com.ninetwozero.greedthegame.datatypes;

public class Roll {
	private final int mScore;
	private final String mText;
	
	public Roll(final int score, final String text) {
		mScore = score;
		mText = text;
	}
	
	public int getScore() {
		return mScore;
	}
	
	public String getText() {
		return mText;
	}
}

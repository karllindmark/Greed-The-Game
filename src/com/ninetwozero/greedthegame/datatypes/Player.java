package com.ninetwozero.greedthegame.datatypes;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {
	private final String mName;
	private int mScore;
	private int mRollCount;
	private int mRoundCount;
	
	public Player(final String name) {
		mName = name;
	}
	
	public Player(final String name, final int score, final int rollCount, final int roundCount) {
		mName = name;
		mScore = score;
		mRollCount = rollCount;
		mRoundCount = roundCount;
	}
	
	public Player(final Parcel in) {
		mName = in.readString();
		mScore = in.readInt();
		mRollCount = in.readInt();
		mRoundCount = in.readInt();
	}
	
	public String getName() {
		return mName;
	}
	
	public int getScore() {
		return mScore;
	}
	
	public int getRollCount() {
		return mRollCount;
	}
	
	public int getRoundCount() {
		return mRoundCount;
	}
	
	public void updateScore(final int addition) {
		mScore += addition;
	}

	public void incrementRoll() {
		mRollCount++;
	}
	
	public void incrementRound() {
		mRoundCount++;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeInt(mScore);
		dest.writeInt(mRoundCount);
		dest.writeInt(mRollCount);
	}
	
    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}

package com.ninetwozero.greedthegame.datatypes;

import java.util.Random;

public class Die {
    protected Side mSideFacingUp;
    protected State mState;
	
    public enum Side {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), UNKNOWN(-1);

        private final int mValue;
        Side(final int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
        
        public static Side fromValue(final int value) {
        	for( Side side : Side.values() ) {
        		if( side.mValue == value ) {
        			return side;
        		}
        	}
        	throw new IllegalArgumentException("Value not found.");
        }
    }
    
    public enum State {
    	NORMAL, SELECTED, LOCKED;
    }
    
    public Die(final int value) {
    	mSideFacingUp = Side.fromValue(value);
    	mState = State.NORMAL;
    }

    public Die(final Side side) {
    	mSideFacingUp = side;
    	mState = State.NORMAL;
    }
        
    public Die() {
        roll();
    }
    
    public Side getSide() {
    	return mSideFacingUp;
    }

    public int getValue() {
        return mSideFacingUp.getValue();
    }
    
    public void roll() {
    	final Random generator = new Random();
        final Side[] sides = Side.values();
        final int randomIndex = generator.nextInt(sides.length-1);
        
        mSideFacingUp = sides[randomIndex];	
        mState = State.NORMAL;
    }
    
    public State getState() {
    	return mState;
    }
    
    public void setState(final State state) {
    	mState = state;
    }
    
    public boolean isLocked() {
    	return mState == State.LOCKED;
    }
    
    public boolean isSelected() {
    	return mState == State.SELECTED;
    }

    public boolean isNormal() {
    	return mState == State.NORMAL;
    }
    
    public boolean isSelectable() {
    	return State.LOCKED != mState && Side.UNKNOWN != mSideFacingUp;
    }
    
    public void set(final int state, final int side) {
    	mState = State.values()[state];
    	mSideFacingUp = Side.fromValue(side);
    }
        
    public void reset() {
    	mSideFacingUp = Side.UNKNOWN;
    	mState = State.NORMAL;
    }
    
    @Override
    public String toString() {
    	return "Die[mSideFacingUp: " + mSideFacingUp + "; mState:" + mState + "]";
    }
}

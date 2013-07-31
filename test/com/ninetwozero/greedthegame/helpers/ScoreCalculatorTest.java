package com.ninetwozero.greedthegame.helpers;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ninetwozero.greedthegame.datatypes.Die;

public class ScoreCalculatorTest {
	final ScoreCalculator mCalculator = new ScoreCalculator();

	@Test
    public void testIfInitializationWorks() {
        Assert.assertNotNull("ScoreCalculator is null", new ScoreCalculator());
    }
	
	@Test
	public void testIfOneToSixIsAStraight() {
		List<Die> dice = getOneToSixList();
		Assert.assertTrue(mCalculator.isStraight(dice));
	}
	
	@Test
	public void testIfTwoToSixIsAStraight() {
		List<Die> dice = new ArrayList<Die>();
		for( int i = 2; 6 >= i; i++ ) {
			dice.add( new Die(i) );
		}
		Assert.assertFalse(mCalculator.isStraight(dice));
	}

	@Test
	public void testIfThreeOnesEqualATriplet() {
		List<Die> dice = new ArrayList<Die>();
		dice.add(new Die(1));
		dice.add(new Die(1));
		dice.add(new Die(1));
		dice.add(new Die(2));
		dice.add(new Die(3));
		dice.add(new Die(4));
		
		Assert.assertTrue(mCalculator.hasTripletWithOnes(dice));
	}
	
	@Test
	public void testThatAStraightGives1000Points() {
		List<Die> dice = getOneToSixList();
		Assert.assertEquals(1000, mCalculator.getScoreForRoll(dice, false).getScore());
	}
	
	@Test
	public void testThatOneToSixIsDetected() {
		List<Die> dice = getOneToSixList();
		for( int i = 1; 6 >= i; i++ ) {
			Assert.assertTrue(mCalculator.hasValue(i, dice));			
		}
	}
	
	@Test
	public void testThatTripletsWithOnlyOnesAreScoringCorrectly() {
		List<Die> dice = new ArrayList<Die>();
		dice.add(new Die(1));
		dice.add(new Die(1));
		dice.add(new Die(1));
		dice.add(new Die(1));
		dice.add(new Die(1));
		dice.add(new Die(1));
		Assert.assertEquals(2000, mCalculator.getScoreForRoll(dice, false).getScore());
	}
	
	@Test
	public void testThatWeGetTheCorrectScoresForOneOneAndTwoFives() {
		List<Die> dice = new ArrayList<Die>();
		dice.add(new Die(1));
		dice.add(new Die(3));
		dice.add(new Die(4));
		dice.add(new Die(5));
		dice.add(new Die(6));
		dice.add(new Die(5));
		Assert.assertEquals(200, mCalculator.getScoreForRoll(dice, false).getScore());
	}
	
	@Test
	public void testThatWeGetTheCorrectScoresForAScrambledStraight() {
		List<Die> dice = new ArrayList<Die>();
		dice.add(new Die(5));
		dice.add(new Die(3));
		dice.add(new Die(1));
		dice.add(new Die(6));
		dice.add(new Die(2));
		dice.add(new Die(4));
		Assert.assertEquals(1000, mCalculator.getScoreForRoll(dice, false).getScore());
	}
	
	@Test
	public void testThatWeGetTheCorrectScoreForFourOnes() {
		List<Die> dice = new ArrayList<Die>();
		dice.add(new Die(1));
		dice.add(new Die(1));
		dice.add(new Die(1));
		dice.add(new Die(1));
		dice.add(new Die(6));
		dice.add(new Die(6));
		Assert.assertEquals(1100, mCalculator.getScoreForRoll(dice, false).getScore());		
	}
	
	@Test
	public void testThatWeGetTheCorrectScoreForFourFives() {
		List<Die> dice = new ArrayList<Die>();
		dice.add(new Die(5));
		dice.add(new Die(5));
		dice.add(new Die(5));
		dice.add(new Die(5));
		dice.add(new Die(6));
		dice.add(new Die(6));
		Assert.assertEquals(550, mCalculator.getScoreForRoll(dice, false).getScore());		
	}
	
	private List<Die> getOneToSixList() {
		List<Die> dice = new ArrayList<Die>();
		for( int i = 1; 6 >= i; i++ ) {
			dice.add( new Die(i) );
		}
		return dice;
	}
}

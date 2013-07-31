package com.ninetwozero.greedthegame.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninetwozero.greedthegame.R;
import com.ninetwozero.greedthegame.abstractions.AbstractActivity;
import com.ninetwozero.greedthegame.datatypes.Die;
import com.ninetwozero.greedthegame.datatypes.Die.State;
import com.ninetwozero.greedthegame.datatypes.Player;
import com.ninetwozero.greedthegame.datatypes.Roll;
import com.ninetwozero.greedthegame.helpers.DiceImages;
import com.ninetwozero.greedthegame.helpers.ScoreCalculator;

public class GameActivity extends AbstractActivity {
	
	// Constants
	public static final String INTENT_SAVE_LIMIT = "save_limit";
	public static final String INTENT_WIN_SCORE = "win_score";
	public static final String INTENT_PLAYERS = "players";
	
	private static final String STATE_SAVE_LIMIT = "state_save_limit";
	private static final String STATE_WIN_SCORE = "state_win_score";
	private static final String STATE_PLAYERS = "state_players";
	private static final String STATE_CURRENT_PLAYER = "state_current_player";
	private static final String STATE_CURRENT_ROUND_SCORE = "state_current_round_score";
	private static final String STATE_CURRENT_DICE_STATES = "state_current_dice_states";
	private static final String STATE_CURRENT_DICE_VALUES = "state_current_dice_values";
	private static final String STATE_CURRENT_ROUND_COMBO = "state_current_round_combo";
	private static final String STATE_CURRENT_ROUND_ROLL_COUNT = "state_current_round_roll_count";
	private static final String STATE_CURRENT_STATUS_TEXT = "state_current_status_text";
	private static final String STATE_CURRENT_STATUS_TEXT_COLOR = "state_current_status_text_color";
	
	public static final int COLOR_ERROR = 0x88FF0000;
	public static final int COLOR_LIGHTER_ERROR = 0x44FF0000;
	public static final int COLOR_OK = 0x8800FF00;
	private static final int COLOR_NORMAL = 0xFF000000;
	
    public static final int DEFAULT_SAVE_LIMIT = 300;
    public static final int DEFAULT_WIN_SCORE = 10000;

    // Regular attributes
	private final ScoreCalculator mCalculator = new ScoreCalculator();
	
    private final List<ImageView> mDiceViews = new ArrayList<ImageView>(6);
	private final int[] mDiceIdArray = {
		R.id.dice_1, R.id.dice_2, R.id.dice_3, 
		R.id.dice_4, R.id.dice_5, R.id.dice_6
	};	
	
	private Button mSaveButton;
	private Button mRollButton;
	private TextView mComboMessageText;
	private TextView mStatusTextView;

	private int mCurrentPlayerIndex;
	private int mPlayerCount;
	private int mRollCounter;
	private int mTemporaryRoundScore;
	private int mCurrentRoundScore;
	private StringBuilder mCurrentComboMessage = new StringBuilder(48);
	private String mCurrentStatusText;
	private int mCurrentStatusTextColor;
	
	private List<Player> mPlayers = new ArrayList<Player>(2);
	private List<View> mPlayerViews = new ArrayList<View>(2);
	private int mRoundSaveLimit;
	private int mGameWinLimit;
	private List<Die> mDice = new ArrayList<Die>(6);
	
	@Override
	public void onCreate(final Bundle state) {
		super.onCreate(state);		
		setContentView(R.layout.activity_game);
		setup(state);
	}	
	
	@Override
	protected void onSaveInstanceState(Bundle out) {
		final int numDice = mDice.size();
		final int[] diceStates = new int[numDice];
		final int[] diceValues = new int[numDice];
		for( int i = 0; i < numDice; i++ ) {
			diceStates[i] = mDice.get(i).getState().ordinal();
			diceValues[i] = mDice.get(i).getValue();
		}
		
		out.putParcelableArrayList(STATE_PLAYERS, (ArrayList<Player>) mPlayers);
		out.putInt(STATE_WIN_SCORE, mGameWinLimit);
		out.putInt(STATE_SAVE_LIMIT, mRoundSaveLimit);
		out.putInt(STATE_CURRENT_PLAYER, mCurrentPlayerIndex);
		out.putInt(STATE_CURRENT_ROUND_SCORE, mCurrentRoundScore);
		out.putIntArray(STATE_CURRENT_DICE_STATES, diceStates);
		out.putIntArray(STATE_CURRENT_DICE_VALUES, diceValues);
		out.putString(STATE_CURRENT_ROUND_COMBO, mCurrentComboMessage.toString());		
		out.putInt(STATE_CURRENT_ROUND_ROLL_COUNT, mRollCounter);
		out.putString(STATE_CURRENT_STATUS_TEXT, mCurrentStatusText);
		out.putInt(STATE_CURRENT_STATUS_TEXT_COLOR, mCurrentStatusTextColor);
		
		super.onSaveInstanceState(out);
	}
	
	private void setup(final Bundle state) {
		final Bundle data = state == null? getIntent().getExtras() : state;
		setupData(data);
		setupDice(data);
		setupButtons(data);
		setupTextViews(data);
		
		if( state != null ) {
			setupStateSpecifics(data);
		}
	}

	private void setupData(final Bundle state) {
		setupPlayers(state);
		mRoundSaveLimit = state.getInt(INTENT_SAVE_LIMIT, state.getInt(STATE_SAVE_LIMIT, DEFAULT_SAVE_LIMIT));
		mGameWinLimit = state.getInt(INTENT_WIN_SCORE, state.getInt(STATE_WIN_SCORE, DEFAULT_WIN_SCORE));
		mCurrentPlayerIndex = state.getInt(STATE_CURRENT_PLAYER, 0);
	}

	private void setupPlayers(final Bundle state) {
		if( state.containsKey(INTENT_PLAYERS) ) {
			final String[] playerNames = state.getStringArray(INTENT_PLAYERS);		
			mPlayerCount = playerNames.length;
			for( int i = 0; i < mPlayerCount; i++ ) {
				mPlayers.add( new Player(playerNames[i]) );		
			}
		} else if( state.containsKey(STATE_PLAYERS)) {
			mPlayers = state.getParcelableArrayList(STATE_PLAYERS);
			mPlayerCount = mPlayers.size();
		} else {
			showToast("No players selected.");
			finish();
		}
	}
	
	private void setupDice(final Bundle state) {
		mDice.clear();

        final OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Die die = (Die) view.getTag();
                if( null == die || !die.isSelectable() ) {
                    return;
                }

	            view.setSelected(!view.isSelected());
	            if( view.isSelected() ) {
	            	die.setState(State.SELECTED);
	            } else {
	            	die.setState(State.NORMAL);
	            }
	            doPreliminaryScoreCalculation();
            }

            private void doPreliminaryScoreCalculation() {
                final Roll roll = mCalculator.getScoreForRoll(mDice, true);
                final int score = roll.getScore() + mTemporaryRoundScore;
               
                if( mRoundSaveLimit <= score ) {
                	mSaveButton.setEnabled(true);
                } else {
                	mSaveButton.setEnabled(false);
                }
                updateComboMessageText(roll.getText());
            }
        };

		for( int resource : mDiceIdArray ) {
			final ImageView diceView = (ImageView) findViewById(resource);
			final Die die = new Die();
			
			diceView.setImageResource(state.getInt(String.valueOf(resource), R.drawable.dice_unknown));
			diceView.setOnClickListener(onClickListener);
			diceView.setTag(die);
			
			mDiceViews.add(diceView);			
			mDice.add(die);
		}
	}
	
	private void setupButtons(final Bundle state) {
		setupSaveButton();
		setupRollButton();
	}

	private void setupSaveButton() {
		mSaveButton = (Button) findViewById(R.id.button_save);
		mSaveButton.setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(final View view) {
					final Player player = mPlayers.get(mCurrentPlayerIndex);
					final View wrap = mPlayerViews.get(mCurrentPlayerIndex);
					final int score = mCurrentRoundScore + mCalculator.getScoreForRoll(mDice, true).getScore();
					
					player.updateScore(score);
					resetForNewRound();
					
					setText(wrap, R.id.text_player_score, player.getScore());
					setText(wrap, R.id.text_player_round_count, player.getRoundCount());

					if( player.getScore() >= mGameWinLimit ) {
						showToast("A winner is " + player.getName());
						
						Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
						intent = intent.putExtra(ScoreActivity.INTENT_PLAYER, player.getName());
						intent = intent.putExtra(ScoreActivity.INTENT_SCORE, player.getScore());
						intent = intent.putExtra(ScoreActivity.INTENT_ROLLS, player.getRollCount());
						intent = intent.putExtra(ScoreActivity.INTENT_ROUNDS, player.getRoundCount());
						intent = intent.putExtra(ScoreActivity.INTENT_ROOT_OPTIONS, getIntent().getExtras());
						startActivity(intent);
						finish();
					} else {
						showStatus("Storing " + score + " points", COLOR_OK);
					}
				}
			}
		);
	}
	
	private void setupRollButton() {
		mRollButton = (Button) findViewById(R.id.button_roll);
		mRollButton.setOnClickListener(
			new OnClickListener() {
				private boolean mUserHasSelected;
				private boolean mUserScoredAllDice;
				
				@Override
				public void onClick(final View view) {
                   doBeforeRoll();
                   doRoll();
                   handleRollOutcome();
                   doAfterRole();
                }
				
				private void doBeforeRoll() {
					final Player player = mPlayers.get(mCurrentPlayerIndex);
					player.incrementRoll();

					setText(mPlayerViews.get(mCurrentPlayerIndex), R.id.text_player_roll_count, player.getRollCount());
					updateRollButton(R.string.text_rolling, false);
                    showStatus("", COLOR_NORMAL);
				}
				
				private void doRoll() {
					int selectionCounter = 0;
					for( int i = 0, max = mDice.size(); i < max; i++ ) {
						final Die die = mDice.get(i);
						if( die.isNormal() ) {
							rollDie(die, i);
						} else {
							die.setState(State.LOCKED);
                            selectionCounter++;
						}
					}
					mUserHasSelected = selectionCounter > 0;
					mUserScoredAllDice = selectionCounter == mDice.size();
					mRollCounter++;
				}
				
				private void handleRollOutcome() {
                   final int potentialScore = mCalculator.getScoreForRoll(mDice, false).getScore();
                   final boolean isPotentiallyAbleToSave = checkIfPossibleToSaveRoll(potentialScore);
                   final int selectedScore = mCalculator.getScoreForRoll(mDice, true).getScore();
                   final boolean hasSelectedEnoughToSave = checkIfPossibleToSaveRoll(selectedScore);
                   final boolean hasTheSameScoreAsDuringThePreviousRoll = selectedScore == mTemporaryRoundScore;
                   final boolean willProbablyHaveTheSameScore = (potentialScore - selectedScore) == mTemporaryRoundScore;

                   if( 0 == selectedScore && 1 < mRollCounter ) {
                	   showStatus(R.string.text_roll_no_selection, COLOR_ERROR);
                	   resetForNewRound();
                   } else if( !isPotentiallyAbleToSave ) {
                       showStatus(String.format(getString(R.string.text_roll_below_limit), mRoundSaveLimit, potentialScore), COLOR_ERROR);
                	   resetForNewRound();
                   } else if( mUserHasSelected && !hasSelectedEnoughToSave ) {
                	   showStatus(R.string.text_roll_under_limit, COLOR_ERROR);
                	   resetForNewRound();
                   } else if( mUserHasSelected && ( hasTheSameScoreAsDuringThePreviousRoll || willProbablyHaveTheSameScore ) ) {
						showStatus(String.format(getString(R.string.text_roll_greedy), getCurrentRoundPoints()), COLOR_ERROR);
						resetForNewRound();
                   } else {
                	   if( mUserScoredAllDice ) {
                		   showStatus(R.string.text_roll_scored_all, COLOR_OK);
                		   resetForCurrentRound();
                	   } else {
                		   showStatus(R.string.text_roll_dare_to_roll, COLOR_OK);
                	   }
                	   doScoreCalculation();
                   }
				}
				
				private void doAfterRole() {
                    updateRollButton(R.string.text_roll, true);
                    mStatusTextView.setVisibility(View.VISIBLE);
				}
				
				private boolean checkIfPossibleToSaveRoll(final int value) {
					return ( value >= mRoundSaveLimit || (value > 0 && mCurrentRoundScore > 0) );
				}

                private void doScoreCalculation() {
                    final Roll roll = mCalculator.getScoreForRoll(mDice, true);
                    mTemporaryRoundScore = roll.getScore();
                    updateComboMessageText(roll.getText());
                }

                private void updateRollButton(final int text, final boolean enabled) {
                    mRollButton.setEnabled(enabled);
                    mRollButton.setText(text);
                }
			}
		);
	}

	
	private void setupTextViews(final Bundle state) {
		mPlayerViews.add(findViewById(R.id.wrap_player1));
		mPlayerViews.add(findViewById(R.id.wrap_player2));
		
		for( int i = 0; i < mPlayerCount; i++ ) {
			final Player player = mPlayers.get(i);
			final View wrap = mPlayerViews.get(i);
			
			setText(wrap, R.id.text_player_name, player.getName());
			setText(wrap, R.id.text_player_score, player.getScore());
			setText(wrap, R.id.text_player_round_count, player.getRoundCount());
			setText(wrap, R.id.text_player_roll_count, player.getRollCount());

			if( i == mCurrentPlayerIndex ) {
				setTextColor(wrap, R.id.text_player_name, COLOR_OK);
			}
			
			wrap.setVisibility(View.VISIBLE);
		}
		
		mComboMessageText = (TextView) findViewById(R.id.text_combo_message);
		mStatusTextView = (TextView) findViewById(R.id.text_status);
		
		updateComboMessageText(state.getString(String.valueOf(R.id.text_combo_message)));
	}
	
	private void updateComboMessageText(final String text) {
		if(null != text) {
			if( mCurrentComboMessage.length() == 0 ) {
				mComboMessageText.setText(text);
			} else {
				mComboMessageText.setText(mCurrentComboMessage + " + " + text);
			}
		}
	}

	private void setupStateSpecifics(final Bundle data) {
		mCurrentRoundScore = data.getInt(STATE_CURRENT_ROUND_SCORE, 0);
		mCurrentComboMessage.append(data.getString(STATE_CURRENT_ROUND_COMBO));
		mRollCounter = data.getInt(STATE_CURRENT_ROUND_ROLL_COUNT);
		mCurrentStatusText = data.getString(STATE_CURRENT_STATUS_TEXT);
		mCurrentStatusTextColor = data.getInt(STATE_CURRENT_STATUS_TEXT_COLOR);
		
		setupDiceFromState(data);		
		setupDisplayedInformationFromState();
	}

	private void setupDisplayedInformationFromState() {
		final Roll currentRoll = mCalculator.getScoreForRoll(mDice, true);
		final int possibleScore = mCurrentRoundScore + currentRoll.getScore();
		
		updateComboMessageText(currentRoll.getText());
		mSaveButton.setEnabled(possibleScore >= mRoundSaveLimit);
		showStatus(mCurrentStatusText, mCurrentStatusTextColor);
	}

	private void setupDiceFromState(final Bundle data) {
		final int[] diceStates = data.getIntArray(STATE_CURRENT_DICE_STATES);
		final int[] diceValues = data.getIntArray(STATE_CURRENT_DICE_VALUES);
		for( int i = 0, max = mDice.size(); i < max; i++ ) {
			final ImageView imageView = mDiceViews.get(i);
			final Die die = mDice.get(i); 
			
			die.set(diceStates[i], diceValues[i]);
			imageView.setImageResource(DiceImages.getFromValue(die.getValue()));
			imageView.setEnabled(!die.isLocked());
			imageView.setSelected(!die.isNormal());
		}
	}
	
	private void rollAllDice() {
		for( int i = 0, max = mDice.size(); i < max; i++ ) {
			rollDie(mDice.get(i), i);
		}
	}

	private void rollDie(final Die die, final int index) {
		die.roll();
		mDiceViews.get(index).setImageResource(DiceImages.getFromValue(die.getValue()));
		mDiceViews.get(index).setTag(die);
	}
	
	private void resetDice() {
		for( Die die : mDice ) {
			die.reset();
		}
		
		for( View v : mDiceViews ) {
			v.setSelected(false);
		}
	}
	
	private int getCurrentRoundPoints() {
		return mCurrentRoundScore + mCalculator.getScoreForRoll(mDice, true).getScore();
	}
	
	private void resetForCurrentRound() {
		final Roll roll = mCalculator.getScoreForRoll(mDice, true);
		mCurrentRoundScore += roll.getScore();
		mCurrentComboMessage.append(roll.getText());
		resetDice();
		rollAllDice();
	}

	private void resetForNewRound() {
		final Player currentPlayer = mPlayers.get(mCurrentPlayerIndex);
		currentPlayer.incrementRound();
		resetDice();
		
		final View wrapCurrentUser = mPlayerViews.get(mCurrentPlayerIndex);
		setText(wrapCurrentUser, R.id.text_player_round_count, currentPlayer.getRoundCount());	
		setTextColor(wrapCurrentUser, R.id.text_player_name, COLOR_NORMAL);
		
		mTemporaryRoundScore = 0;
		mCurrentRoundScore = 0;
		mCurrentPlayerIndex = ((0 == mCurrentPlayerIndex) && (2 == mPlayerCount))? 1 : 0;
		mCurrentComboMessage.setLength(0);
		mRollCounter = 0;
		
		mSaveButton.setEnabled(false);
		setTextColor(mPlayerViews.get(mCurrentPlayerIndex), R.id.text_player_name, COLOR_OK);
		updateComboMessageText(getString(R.string.text_none));
	}
	
	private void showStatus(final int stringResource, final int color) {
		showStatus(getString(stringResource), color);
	}
	
	private void showStatus(final String text, final int color) {
		mCurrentStatusText = text;
		mCurrentStatusTextColor = color;
		
		if( "".equals(mCurrentStatusText) ) {
			mStatusTextView.setVisibility(View.GONE);
		} else {
			mStatusTextView.setVisibility(View.VISIBLE);
			mStatusTextView.setTextColor(color);
		}
		mStatusTextView.setText(mCurrentStatusText);
	}
}

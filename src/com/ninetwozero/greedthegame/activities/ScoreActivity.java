package com.ninetwozero.greedthegame.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ninetwozero.greedthegame.R;

public class ScoreActivity extends Activity {

	protected static final String INTENT_PLAYER = "playerName";
	protected static final String INTENT_SCORE = "playerScore";
	protected static final String INTENT_ROLLS = "numRolls";
	protected static final String INTENT_ROUNDS = "numRounds";
	protected static final String INTENT_ROOT_OPTIONS = "rootOptions";

	private String mPlayer;
	private int mScore;
	private int mRoundCount;
	private int mRollCount;
	private Bundle mRootIntent;
	
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);		
		setContentView(R.layout.activity_score);		
		setup();
	}

	private void setup() {
		setupDataFromIntent(getIntent());
		setupLayout();
	}
	
	private void setupDataFromIntent(final Intent intent) {
		mPlayer = intent.getStringExtra(INTENT_PLAYER);
		mScore = intent.getIntExtra(INTENT_SCORE, 0);
		mRoundCount = intent.getIntExtra(INTENT_ROUNDS, 0);
		mRollCount = intent.getIntExtra(INTENT_ROLLS, 0);
		mRootIntent = intent.getBundleExtra(INTENT_ROOT_OPTIONS);
	}
	
	private void setupLayout() {
		((TextView) findViewById(R.id.text_congratulations)).setText(
			Html.fromHtml(String.format(getString(R.string.text_win_info), mPlayer))
		);
		((TextView) findViewById(R.id.text_status)).setText(
			Html.fromHtml(String.format(getString(R.string.text_win_score), mRoundCount, mRollCount, mScore))
		);
		findViewById(R.id.button_restart).setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(final View view) {
					startActivity(new Intent(ScoreActivity.this, GameActivity.class).putExtras(mRootIntent));
					finish();
				}
			}
		);
		findViewById(R.id.button_quit).setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(final View view) {
					finish();
				}
			}
		);
	}
	
}

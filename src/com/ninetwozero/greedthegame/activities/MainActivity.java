package com.ninetwozero.greedthegame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.ninetwozero.greedthegame.R;
import com.ninetwozero.greedthegame.abstractions.AbstractActivity;

public class MainActivity extends AbstractActivity {
	private CheckBox mAdvancedCheckbox;
	private Button mStartButton;
	private EditText mGameScoreField;
	private EditText mGameSaveLimitField;
	private EditText mPlayer1Field;
	private EditText mPlayer2Field;
	private View mWrapAdvancedOptions;
		
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);		
		setContentView(R.layout.activity_main);		
		setup(state);		
	}
	
	private void setup(final Bundle state) {
		setupViews();
		setupStartButton();
		setupAdvancedOptions();
	}

	private void setupViews() {
		mStartButton = (Button) findViewById(R.id.button_start);
		mGameScoreField = (EditText) findViewById(R.id.field_win_score);
		mGameSaveLimitField = (EditText) findViewById(R.id.field_save_limit);
        mPlayer1Field = (EditText) findViewById(R.id.field_player1);
        mPlayer2Field = (EditText) findViewById(R.id.field_player2);
        mAdvancedCheckbox = (CheckBox) findViewById(R.id.checkbox);
        mWrapAdvancedOptions = findViewById(R.id.wrap_advanced);
	}

	private void setupAdvancedOptions() {
		mAdvancedCheckbox.setOnCheckedChangeListener(
			new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					mWrapAdvancedOptions.setVisibility(isChecked? View.VISIBLE : View.GONE );					
				}
			}
		);
	}

	private void setupStartButton() {
		mStartButton.setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(final View view) {
                    final String player1 = mPlayer1Field.getText().toString();
                    final String player2 = mPlayer2Field.getText().toString();
                    final int winScore = Integer.parseInt( 0 + mGameScoreField.getText().toString());
                    final int saveLimit = Integer.parseInt( 0 + mGameSaveLimitField.getText().toString());
                    final int numberOfPlayers = player2 != null && 0 == player2.length() ? 1 : 2;
                    final String[] players = new String[numberOfPlayers];
                    final Bundle options = new Bundle();
                    
                    if( player1 != null && 0 == player1.length() ) {
                        mPlayer1Field.setError(getString(R.string.text_error_name));
                        showToast(R.string.text_form_error);
                        return;
                    }

                    players[0] = player1;
                    if(2 == players.length) {
                        players[1] = player2;
                    }
                    options.putStringArray(GameActivity.INTENT_PLAYERS, players);
                    
                    if( winScore > 0 ) {
                    	options.putInt(GameActivity.INTENT_WIN_SCORE, winScore);
                    }
                    
                    if( saveLimit > 0 ) {
                    	options.putInt(GameActivity.INTENT_SAVE_LIMIT, saveLimit);
                    }
                    
                    startGame(options);
              	}
			}
		);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if( item.getItemId() == R.id.action_rules ) {
			startActivity( new Intent(getApplicationContext(), RuleActivity.class) );
		}
		return true;
	}

	private void startGame(final Bundle options) {
		final Intent intent = new Intent(this, GameActivity.class);
		intent.putExtras(options);
		startActivity( intent );
	}	
}

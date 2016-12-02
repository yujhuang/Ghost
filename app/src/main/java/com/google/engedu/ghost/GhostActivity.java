package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn.";
    private static final String USER_TURN = "Your turn.";
    private FastDictionary dictionary;
    private boolean userTurn = false,addToStart = false;
    private Random random = new Random();
    private TextView GhostText, gameStatus;
    private Button swi,chal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        swi = (Button)findViewById(R.id.btn_switch);
        chal = (Button)findViewById(R.id.btn_challenge);
        swi.setEnabled(true);
        chal.setEnabled(true);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event ) {
        char keyPressed = (char) event.getUnicodeChar();
        if(keyPressed < 97 && keyPressed > 122) {
            Toast.makeText(GhostActivity.this,"Enter from a-z only.",Toast.LENGTH_SHORT).show();
            return super.onKeyUp(keyCode,event);
        }else {
            String currentText = GhostText.getText().toString();
            if(addToStart) {
                currentText = (keyPressed + currentText).toLowerCase();
            }else {
                currentText = (currentText+keyPressed).toLowerCase();
            }
            GhostText.setText(currentText);
            gameStatus.setText(COMPUTER_TURN);
            computerTurn();
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     *
     */
    public void onStart(View view) {
        userTurn = random.nextBoolean();
        GhostText = (TextView) findViewById(R.id.ghostText);
        GhostText.setText("");
        gameStatus = (TextView) findViewById(R.id.gameStatus);
        swi.setEnabled(true);
        chal.setEnabled(true);
        if (userTurn) {
            if(addToStart) {
                gameStatus.setText(USER_TURN+" Insert from start.");
            }else {
                gameStatus.setText(USER_TURN+" Insert at the end.");
            }

        } else {
            gameStatus.setText(COMPUTER_TURN);
            computerTurn();
        }
    }
    public void handleChallenge(View view) {
        String currentText=GhostText.getText().toString();

        if(currentText.length()>=4 && dictionary.isWord(currentText)){
            Toast.makeText(GhostActivity.this,"No word is prefixed with" + currentText,Toast.LENGTH_SHORT).show();
            gameStatus.setText("You win!");
            swi.setEnabled(false);
            chal.setEnabled(false);
        }else{
            String word= dictionary.getAnyWordStartingWith(currentText);
            if(word!=null){
                Toast.makeText(GhostActivity.this,"Current ghost text includes prefix "+word,Toast.LENGTH_SHORT).show();
                swi.setEnabled(false);
                chal.setEnabled(false);
            }else{
                Toast.makeText(GhostActivity.this,"No word is prefixed with "+ word,Toast.LENGTH_SHORT).show();
                gameStatus.setText("You win!");
                swi.setEnabled(false);
                chal.setEnabled(false);
            }
        }
    }
    public void handleSwitch(View view) {
        if(addToStart) {

            addToStart = false;
        }else {
             addToStart = true;
        }
        if(addToStart) {
            gameStatus.setText(USER_TURN+" Insert from start.");
        }else {
            gameStatus.setText(USER_TURN+" Insert at the end.");
        }

    }

    private void computerTurn() {

        // Do computer turn stuff then make it the user's turn again
        String currentText = GhostText.getText().toString();
        if(currentText.length()>=4) {
             if(dictionary.isWord(currentText)) {
                 Toast.makeText(GhostActivity.this, currentText + " is a valid word!", Toast.LENGTH_SHORT).show();
                 gameStatus.setText("Computer Wins!");
                 swi.setEnabled(false);
                 chal.setEnabled(false);
             } else {
                 String roll = dictionary.getAnyWordStartingWith(currentText);
                 if(roll == null) {
                     Toast.makeText(GhostActivity.this,currentText + " is not a prefix!",Toast.LENGTH_SHORT).show();
                     gameStatus.setText("Computer Wins!");
                     swi.setEnabled(false);
                     chal.setEnabled(false);
                 }else {
                     currentText = currentText+ roll.charAt(currentText.length());
                     GhostText.setText(currentText);
                     userTurn = true;
                     if(addToStart) {
                         gameStatus.setText(USER_TURN+" Insert from start.");
                     }else {
                         gameStatus.setText(USER_TURN+" Insert at the end.");
                     }
                 }
            }
        }else {
            String roll = dictionary.getAnyWordStartingWith(currentText);
            if (roll == null) {
                Toast.makeText(GhostActivity.this, currentText + " is a valid word!", Toast.LENGTH_SHORT).show();
                gameStatus.setText("Computer Wins!");
                swi.setEnabled(false);
                chal.setEnabled(false);
            } else {
                currentText = currentText + roll.charAt(currentText.length());
                GhostText.setText(currentText);
                userTurn = true;
                if(addToStart) {
                    gameStatus.setText(USER_TURN+" Insert from start.");
                }else {
                    gameStatus.setText(USER_TURN+" Insert at the end.");
                }
            }

        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString("WORD_FRAGMENT", GhostText.getText().toString());
        savedInstanceState.putString("GAME_STATUS", gameStatus.getText().toString());
        savedInstanceState.putBoolean("SWITCH", addToStart);
        Log.d("Ghost 2",savedInstanceState.getString("WORD_FRAGMENT"));
        Log.d("Status 2",savedInstanceState.getString("GAME_STATUS"));

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore value of members from saved state

            Log.d("Ghost 1",savedInstanceState.getString("WORD_FRAGMENT"));
            Log.d("Status 1",savedInstanceState.getString("GAME_STATUS"));
            GhostText.setText(savedInstanceState.getString("WORD_FRAGMENT"));
            gameStatus.setText(savedInstanceState.getString("GAME_STATUS"));
            addToStart = savedInstanceState.getBoolean("SWITCH");
        }
    }
}

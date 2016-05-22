package fr.esgi.jamal.timesup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    public Vector<Player> players;

    private void hideVirtualKeyboard(IBinder token)
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(token, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private void addPlayer(final BaseAdapter adapter)
    {
        final EditText playerEdit = (EditText)findViewById(R.id.nameInput);
        if(playerEdit.getText().length() != 0)
        {
            players.add(new Player(playerEdit.getText().toString()));
            adapter.notifyDataSetChanged();
            playerEdit.getText().clear();
            playerEdit.clearFocus();
            hideVirtualKeyboard(playerEdit.getWindowToken());
        }
        else
        {
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
            playerEdit.startAnimation(anim);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context ctx = this;
        players = new Vector<Player>();

        final ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, players);
        ListView myList = (ListView)findViewById(R.id.playerList);
        myList.setAdapter(adapter);

        EditText playerEdit = (EditText)findViewById(R.id.nameInput);
        hideVirtualKeyboard(playerEdit.getWindowToken());
        playerEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER)
                {
                    addPlayer(adapter);
                    return true;
                }
                return false;
            }
        });

        Button btn = (Button)findViewById(R.id.addButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayer(adapter);
            }
        });

        Button runBtn = (Button)findViewById(R.id.runButton);
        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(players.size() > 0)
                {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra("playerSize", players.size());
                    for(int i = 0; i < players.size(); ++i)
                    {
                        intent.putExtra("player" + i, players.get(i));
                    }
                    startActivity(intent);
                }
            }
        });
    }
}

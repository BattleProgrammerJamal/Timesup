package fr.esgi.jamal.timesup;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

public class GameActivity extends AppCompatActivity {
    private class GameCard
    {
        public int      id;
        public String   name;

        public GameCard(int _id, final String _name)
        {
            id = _id;
            name = _name;
        }

        public String toString()
        {
            return name;
        }
    }

    private void hideVirtualKeyboard(IBinder token)
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(token, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private void ValidateResponse()
    {
        EditText responseEdit = (EditText)findViewById(R.id.responseEdit);
        if(responseEdit.getText().toString() != "")
        {
            if(cards.get(currentCardIndex).name.compareToIgnoreCase(responseEdit.getText().toString()) == 0)
            {
                Player p = players.get(currentPlayerIndex);
                p.setLastScore(p.getLastScore() + 1);
                TextView messageView = (TextView)findViewById(R.id.messageTextView);
                messageView.setVisibility(View.VISIBLE);
                messageView.setText("Bonne réponse ! ");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    public void run()
                    {
                        TextView messageView = (TextView)findViewById(R.id.messageTextView);
                        messageView.setVisibility(View.GONE);
                    }
                }, 1000);
            }
            else
            {
                TextView messageView = (TextView)findViewById(R.id.messageTextView);
                messageView.setVisibility(View.VISIBLE);
                messageView.setText("Mauvaise réponse ! ");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    public void run()
                    {
                        TextView messageView = (TextView)findViewById(R.id.messageTextView);
                        messageView.setVisibility(View.GONE);
                    }
                }, 1000);
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

            if(currentCardIndex < cards.size() - 1)
            {
                ++currentCardIndex;
                ImageView iView = (ImageView)findViewById(R.id.currentImage);
                iView.setImageResource(cards.get(currentCardIndex).id);
                Toast t = Toast.makeText(getApplicationContext(), players.get(currentPlayerIndex).getName() + ", à ton tour ! ", Toast.LENGTH_LONG);
                t.show();
            }
            else
            {
                running = false;
                if(players.size() > 0)
                {
                    Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                    intent.putExtra("playerSize", players.size());
                    intent.putExtra("cardSize", cards.size());
                    for(int i = 0; i < players.size(); ++i)
                    {
                        intent.putExtra("player" + i, players.get(i));
                    }
                    startActivity(intent);
                }
            }

            time = 0.0f;
            responseEdit.getText().clear();
            hideVirtualKeyboard(responseEdit.getWindowToken());
            responseEdit.clearFocus();
        }
    }

    private Vector<Player>      players;
    private Vector<GameCard>    cards;
    private int                 currentCardIndex;
    private int                 currentPlayerIndex;
    private float               time;
    private int                 maximumTime;
    private boolean             running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        cards = new Vector<GameCard>();
        cards.add(new GameCard(R.mipmap.arya, "arya"));
        cards.add(new GameCard(R.mipmap.bran, "bran"));
        cards.add(new GameCard(R.mipmap.danaerys, "danaerys"));
        cards.add(new GameCard(R.mipmap.ed, "ned"));
        cards.add(new GameCard(R.mipmap.jon, "jon"));
        cards.add(new GameCard(R.mipmap.sansa, "sansa"));
        cards.add(new GameCard(R.mipmap.tyrion, "tyrion"));
        cards.add(new GameCard(R.mipmap.tywin, "tywin"));

        Intent intent = getIntent();
        players = new Vector<Player>();
        for(int i = 0; i < intent.getIntExtra("playerSize", 0); ++i)
        {
            Player player = (Player)intent.getParcelableExtra("player" + i);
            player.setLastScore(0);
            players.add(player);
        }
        maximumTime = 15;

        currentPlayerIndex = 0;
        currentCardIndex = 0;
        ImageView iView = (ImageView)findViewById(R.id.currentImage);
        iView.setImageResource(cards.get(currentCardIndex).id);

        Toast t = Toast.makeText(getApplicationContext(), players.get(currentPlayerIndex).getName() + ", à ton tour ! ", Toast.LENGTH_LONG);
        t.show();

        final Handler handler = new Handler();
        running = true;
        handler.postDelayed(new Runnable(){
            public void run()
            {
                time += 1.0f;
                ProgressBar timebar = (ProgressBar)findViewById(R.id.timerProgressBar);
                int v = (int)((time * 100.0f) / maximumTime);

                if(time > maximumTime)
                {
                    ValidateResponse();
                    time = 0.0f;
                }
                else
                {
                    timebar.setProgress(v);
                }

                if(running) { handler.postDelayed(this, 1000); }
            }
        }, 1000);

        final EditText responseEdit = (EditText)findViewById(R.id.responseEdit);
        hideVirtualKeyboard(responseEdit.getWindowToken());
        responseEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER))
                {
                    ValidateResponse();
                    return true;
                }
                return false;
            }
        });

        Button respondBtn = (Button)findViewById(R.id.responseBtn);
        respondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateResponse();
            }
        });
    }
}

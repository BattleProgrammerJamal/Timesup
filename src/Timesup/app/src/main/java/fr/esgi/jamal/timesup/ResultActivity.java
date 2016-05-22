package fr.esgi.jamal.timesup;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ResultActivity extends AppCompatActivity {
    class PlayerResult
    {
        private Player player;

        public PlayerResult(final Player player)
        {
            this.player = player;
        }

        public String toString()
        {
            return player.getName() + " : " + player.getLastScore() + " / " + cardSize;
        }
    }

    private Vector<PlayerResult>        players;
    private int                         cardSize;
    private GLSurfaceView               glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        players = new Vector<PlayerResult>();
        cardSize = intent.getIntExtra("cardSize", 0);
        Player bestPlayer = null;
        int bestScore = -1;
        for(int i = 0; i < intent.getIntExtra("playerSize", 0); ++i)
        {
            Player player = (Player)intent.getParcelableExtra("player" + i);
            if(player.getLastScore() > bestScore)
            {
                bestPlayer = player;
                bestScore = player.getLastScore();
            }
            players.add(new PlayerResult(player));
        }

        final ArrayAdapter<PlayerResult> adapter = new ArrayAdapter<PlayerResult>(this, android.R.layout.simple_list_item_1, players);
        ListView myList = (ListView)findViewById(R.id.playerResultsList);
        myList.setAdapter(adapter);

        TextView winnerTextView = (TextView)findViewById(R.id.winnerTextView);
        if(bestPlayer != null)
        {
            winnerTextView.setText("Victoire pour " + bestPlayer.toString() + " ! ");
        }

        Button restartBtn = (Button)findViewById(R.id.restartBtn);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
            }
        });
    }
}

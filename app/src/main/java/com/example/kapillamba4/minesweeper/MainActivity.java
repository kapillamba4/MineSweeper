package com.example.kapillamba4.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int size, score = 0, flaggedMines = 0, playerScore = 0;
    private Patch[][] field;
    private Patch face;
    private LinearLayout mLinearLayout;
    private TextView textView;
    private LinearLayout rows[];
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLinearLayout = (LinearLayout) findViewById(R.id.root);
        sharedPreferences = getSharedPreferences("MineSweeper", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Intent i = getIntent();
        Bundle b = i.getExtras();
        String name = b.getString("username");
        String difficulty = b.getString("difficulty");
        if(difficulty.equals("Easy")) {
            size = 5;
        } else if(difficulty.equals("Medium")) {
            size = 9;
        } else {
            size = 15;
        }

        setUpField();
    }

    public void reset() {
        setUpField();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.reset) {
            reset();
        }

        return true;
    }

    public boolean isOutOfBound(int r, int c) {
        if(r < 0 || r >= size || c < 0 || c >= size) {
            return true;
        }

        return false;
    }

    public void add(int r, int c) {
        if(isOutOfBound(r, c)) {
            return;
        }

        field[r][c].incrementSurroundingMines();
    }

    public void setUpField() {
        flaggedMines = 0;
        score = 0;
        playerScore = 0;
        field = new Patch[size][size];
        rows = new LinearLayout[size];
        mLinearLayout.removeAllViews();

        // header
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) (size*0.2f));
        headerParams.setMargins(2, 2, 2, 2);
        header.setWeightSum((float) size);
        header.setGravity(Gravity.CENTER);
        headerParams.gravity = Gravity.CENTER;
        header.setLayoutParams(headerParams);


        final LinearLayout.LayoutParams scoreParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, (size)*0.2f);
        scoreParams.setMargins(0, 2, 0, 2);
        textView = new TextView(this);
        textView.setText("000");
        textView.setPadding(0, 0, 0, 0);
        textView.setMaxLines(1);
        header.addView(textView);


        LinearLayout.LayoutParams faceParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, (size*0.2f));
        faceParams.setMargins(0, 2, 0, 2);
        face = new Patch(this, 0, 0);
        face.setLayoutParams(faceParams);
        face.setPadding(0, 0, 0, 0);
        face.setImageResource(R.drawable.face);
        face.setScaleType(ImageView.ScaleType.FIT_XY);
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        header.addView(face);
        mLinearLayout.addView(header);

        // for each row
        for(int i = 0; i < size; i++) {
            rows[i] = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1f);
            params.setMargins(0, 0, 0, 0);
            rows[i].setLayoutParams(params);
            rows[i].setWeightSum((float) size);
            rows[i].setOrientation(LinearLayout.HORIZONTAL);
            mLinearLayout.addView(rows[i]);
        }

        // for each Patch on minefield
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                field[i][j] = new Patch(this, i, j);
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                params.setMargins(0, 0, 0, 0);
                field[i][j].setLayoutParams(params);
                field[i][j].setOnClickListener(this);
                field[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Patch patch = (Patch) v;
                        patch.flagThis();
                        if(patch.isFlagged() && patch.isItMine()) {
                            ++flaggedMines;
                        } else if(!patch.isFlagged() && patch.isItMine()) {
                            --flaggedMines;
                        }

                        if(flaggedMines+score == size*size) {
                            Toast.makeText(MainActivity.this, "You Won!!!!!!!!!", Toast.LENGTH_SHORT).show();
                            setUpField();
                        }

                        return true;
                    }
                });

                field[i][j].setImageResource(R.drawable.tile);
                field[i][j].setScaleType(ImageView.ScaleType.FIT_XY);
                field[i][j].setPadding(0, 0, 0, 0);
                rows[i].addView(field[i][j]);
            }
        }

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(field[i][j].isItMine()) {
                    add(i-1, j);
                    add(i-1, j-1);
                    add(i-1, j+1);
                    add(i, j-1);
                    add(i, j+1);
                    add(i+1, j);
                    add(i+1, j-1);
                    add(i+1, j+1);
                }
            }
        }

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                field[i][j].setUnderThePatch();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Patch patch = (Patch) v;

        if(patch.isFlagged()) {
            return;
        }

        if(patch.isItMine()) {
            patch.setImageResource(R.drawable.red_mine);
            gameOver();
        } else {
            revealSurrouding(patch.getRow(), patch.getCol());
            score += patch.revealPatch();
            playerScore += patch.getSurroundingMineCount();
        }
    }

    public void revealSurrouding(int r, int c) {
        if(isOutOfBound(r, c) || field[r][c].isRevealed() || field[r][c].isItMine()) {
            return;
        }

        if(field[r][c].getSurroundingMineCount() > 0) {
            score += field[r][c].revealPatch();
            return;
        }

        score += field[r][c].revealPatch();
        revealSurrouding(r-1, c);
        revealSurrouding(r-1, c-1);
        revealSurrouding(r-1, c+1);
        revealSurrouding(r, c-1);
        revealSurrouding(r, c+1);
        revealSurrouding(r+1, c);
        revealSurrouding(r+1, c-1);
        revealSurrouding(r+1, c+1);
    }

    public void gameOver() {
        Toast.makeText(this, "Game Over", LENGTH_SHORT).show();
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                field[i][j].revealPatch();
            }
        }
    }
}

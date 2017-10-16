package com.example.kapillamba4.minesweeper;

import android.content.Context;
import android.widget.ImageButton;

/**
 * Created by kapil on 29/8/17.
 */

public class Patch extends ImageButton {
    private boolean isExplored;
    private boolean isMine;
    private boolean isFlagged;
    private int surroundingMines;
    private int underThePatch;
    private int row;
    private int col;

    public boolean getRandomBoolean() {
        return Math.random() >= 0.8;
    }

    public void incrementSurroundingMines() {
        ++surroundingMines;
    }

    public Patch(Context context, int r, int c) {
        super(context);
        isExplored = false;
        isMine = getRandomBoolean();
        isFlagged = false;
        surroundingMines = 0;
        row = r;
        col = c;
    }

    public int getSurroundingMineCount() {
        return surroundingMines;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isRevealed() {
        return isExplored;
    }

    public void setUnderThePatch() {
        if (isMine) {
            underThePatch = R.drawable.mine;
        } else if (surroundingMines == 0) {
            underThePatch = R.drawable.revealed_tile;
        } else if (surroundingMines == 1) {
            underThePatch = R.drawable.one;
        } else if (surroundingMines == 2) {
            underThePatch = R.drawable.two;
        } else if (surroundingMines == 3) {
            underThePatch = R.drawable.three;
        } else if (surroundingMines == 4) {
            underThePatch = R.drawable.four;
        } else if (surroundingMines == 5) {
            underThePatch = R.drawable.five;
        } else if (surroundingMines == 6) {
            underThePatch = R.drawable.six;
        } else if (surroundingMines == 7) {
            underThePatch = R.drawable.seven;
        } else if (surroundingMines == 8) {
            underThePatch = R.drawable.eight;
        }
    }

    boolean isItMine() {
        return isMine;
    }

    int revealPatch() {
        if(!isFlagged && !isExplored) {
            this.setImageResource(underThePatch);
            isExplored = true;
            return 1;
        } else {
            return 0;
        }
    }

    boolean isFlagged() {
        return isFlagged;
    }

    void flagThis() {
        if (!isExplored) {
            if(isFlagged) {
                isFlagged = false;
                this.setImageResource(R.drawable.tile);
            } else {
                isFlagged = true;
                this.setImageResource(R.drawable.flagged_tile);
            }
        }
    }


}

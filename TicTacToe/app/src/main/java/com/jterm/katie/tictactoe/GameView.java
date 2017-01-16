/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jterm.katie.tictactoe;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
    private static final String TAG = "GameView";

    public interface CellTouchListener {
        void onCellTouched(int x, int y);
    }

    private CellTouchListener mTouchListener;
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Game mGame;
    private int mTextSize;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStrokeWidth(10);

        mTextSize = getResources().getDimensionPixelSize(R.dimen.game_text_size);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "touch event!");
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    return true;
                } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
                    if (mTouchListener != null) {
                        int cellSize = getWidth() / 3;
                        int x = (int) (motionEvent.getX() / cellSize);
                        int y = (int) (motionEvent.getY() / cellSize);
                        mTouchListener.onCellTouched(x, y);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void setCellTouchListener(CellTouchListener listener) {
        mTouchListener = listener;
    }

    public void clearCellTouchListener(CellTouchListener listener) {
        mTouchListener = null;
    }

    public void updateGame(Game game) {
        mGame = game;
        postInvalidate();
    }

    public Game getGame() {
        return mGame;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec == 0 || heightMeasureSpec == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        int size = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cellSize = getWidth() / 3;
        canvas.drawLine(cellSize, 0, cellSize, getHeight(), mLinePaint);
        canvas.drawLine(cellSize * 2, 0, cellSize * 2, getHeight(), mLinePaint);
        canvas.drawLine(0, cellSize, getWidth(), cellSize, mLinePaint);
        canvas.drawLine(0, cellSize * 2, getWidth(), cellSize * 2, mLinePaint);

        if (mGame == null) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int move = mGame.getBoard().get(i).get(j);
                if (move == Game.FIRST_PLAYER) {
                    canvas.drawText("X", cellSize * i + cellSize / 2, (int) (
                            cellSize * j + cellSize / 2 + mTextSize / 2.5), mTextPaint);
                } else if (move == Game.SECOND_PLAYER) {
                    canvas.drawText("O", cellSize * i + cellSize / 2, (int) (
                            cellSize * j + cellSize / 2 + mTextSize / 2.5), mTextPaint);
                }
            }
        }

    }
}

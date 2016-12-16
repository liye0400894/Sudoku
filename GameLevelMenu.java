package com.laboratory.sudoku;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Li on 2016/11/30.
 */
public class GameLevelMenu extends MenuLayout{

    public GameLevelMenu(Context context) {
        super(context);
        activity = (MainActivity)context;

        menuWidth = 0;
        menuHeight = 0;

        numOfButton = 2; // 2 button by default
        verticalEdge = 0;
        horizontalEdge = 0;
        smallRectWidth = 0;
        smallRectHeigh = 0;
    }

    public GameLevelMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (MainActivity)context;

        menuWidth = 0;
        menuHeight = 0;

        numOfButton = 2; // 2 button by default

        verticalEdge = 0;
        horizontalEdge = 0;
        smallRectWidth = 0;
        smallRectHeigh = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        int myAction = event.getAction();
        int button = -1;
        if (myAction == MotionEvent.ACTION_DOWN) {
            button = findButton(touchX, touchY);
        }

        // easy - hard level new game
        if (button>=0 && button<numOfButton) {
            activity.getHandler().sendEmptyMessage(button+5);
        }

        // go back to puzzle layout
        if (button == -1){
            activity.getHandler().sendEmptyMessage(4);
        }

        return super.onTouchEvent(event);
    }
}

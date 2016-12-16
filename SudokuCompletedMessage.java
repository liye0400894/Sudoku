package com.laboratory.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by Li on 2016/11/23.
 */
public class SudokuCompletedMessage extends View {
    private MainActivity activity;

    public SudokuCompletedMessage(Context context) {
        super(context);
        activity = (MainActivity)context;
    }

    public SudokuCompletedMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (MainActivity)context;
    }

    //rivate void SetViewSizeAndPosition(){
    //    DisplayMetrics dm = getResources().getDisplayMetrics();
    //    int screenWidth = dm.widthPixels;;
    //    int screenHeight = dm.heightPixels;

        //FrameLayout.LayoutParams layoutParameters = (FrameLayout.LayoutParams)getLayoutParams();
        //layoutParameters.width = screenWidth*2/3;
        //layoutParameters.height = screenHeight*2/3;
        //layoutParameters.setMargins( (screenWidth - layoutParameters.width)/2, (screenHeight - layoutParameters.height)/2,
        //        (screenWidth - layoutParameters.width)/2 + layoutParameters.width, (screenHeight - layoutParameters.height)/2 + layoutParameters.height);

        //setLayoutParams(layoutParameters);
    //}

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Log.d("Interface", "onDraw() called");
        //SetViewSizeAndPosition();
        drawMessageBackground(canvas);
        drawMessageText(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("Interface", "Switching from congratulation dialog to menu");
        activity.getHandler().sendEmptyMessage(3);

        return super.onTouchEvent(event);
    }

    private void drawMessageBackground(Canvas canvas){

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(getResources().getColor(R.color.shudu_background));

        float dialogWidth = getWidth()*2/3;
        float dialogHeight = getHeight()*2/3;
        canvas.drawRect((getWidth()-dialogWidth)/2, (getHeight()-dialogHeight)/2, (getWidth()-dialogWidth)/2 + dialogWidth, (getHeight()-dialogHeight)/2 + dialogHeight, backgroundPaint);
    }

    private void drawMessageText(Canvas canvas){
        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(100*0.75f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        //FrameLayout.LayoutParams layoutParameters = (FrameLayout.LayoutParams)getLayoutParams();
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float x = getWidth()/2;
        float y = getHeight()/2 - (fm.ascent + fm.descent)/2;

        canvas.drawText( "You win!", x, y, textPaint);
    }
}

package com.laboratory.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Li on 2016/11/22.
 */
public class MenuLayout extends View {
    protected MainActivity activity;

    protected int menuWidth;
    protected int menuHeight;

    protected int numOfButton;
    protected String[] menuText;

    protected float verticalEdge;
    protected float smallRectWidth;
    protected float horizontalEdge;
    protected float smallRectHeigh;

    public MenuLayout(Context context) {
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

    public MenuLayout(Context context, AttributeSet attrs) {
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

    public void setNumOfButton(int number){
        if (number < 1) {
            return;
        }

        numOfButton = number;
        setMenuSizeAndPosition();
    }

    public void setMenuText(String[] text){
        if (text.length < numOfButton){
            return;
        }
        menuText = new String[numOfButton];

        for (int i = 0; i< numOfButton; i++){
            menuText[i] = text[i];
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
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

        if (button>=0 && button<numOfButton) {
            if (button == 0){ // reset puzzle
                activity.getHandler().sendEmptyMessage(10);
                Log.d("Interface", "Reset on keypad is pressed");
            }
            if (button == 1) { // save puzzle progress
                activity.getHandler().sendEmptyMessage(12);
            }
            if (button == 2){ // call menu to select new game level
                activity.getHandler().sendEmptyMessage(14);
            }
        }

        // go back to puzzle layout
        if (button == -1){
            activity.getHandler().sendEmptyMessage(4);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Log.d("Interface", "onDraw() called");
        setMenuSizeAndPosition();
        drawMenu(canvas);
        drawMenuText(canvas);
    }

    private void setMenuSizeAndPosition(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;;
        int screenHeight = dm.heightPixels;

        //FrameLayout.LayoutParams layoutParameters = (FrameLayout.LayoutParams)getLayoutParams();
        //layoutParameters.width = screenWidth*2/3;
        //layoutParameters.height = screenHeight*2/3;
        //layoutParameters.setMargins( (screenWidth - layoutParameters.width)/2, (screenHeight - layoutParameters.height)/2,
        //        (screenWidth - layoutParameters.width)/2 + layoutParameters.width, (screenHeight - layoutParameters.height)/2 + layoutParameters.height);

        //setLayoutParams(layoutParameters);
        menuWidth = screenWidth*2/3;
        menuHeight = screenHeight*2/3;

        verticalEdge = 5;
        horizontalEdge = 5;
        smallRectWidth = menuWidth - verticalEdge *2;
        smallRectHeigh = (menuHeight - 5* horizontalEdge) / numOfButton;
    }

    private void drawMenu(Canvas canvas){
        // draw large rect
        Paint menuPaint = new Paint();
        menuPaint.setColor(getResources().getColor(R.color.shudu_background));

        float menuStartPositionX = (getWidth() - menuWidth)/2;
        float menuStartPositionY = (getHeight() - menuHeight)/2;
        canvas.drawRect(menuStartPositionX, menuStartPositionY, menuStartPositionX + menuWidth, menuStartPositionY + menuHeight, menuPaint);

        // draw 4 small rects
        menuPaint.setColor(getResources().getColor(R.color.shudu_light));

        for (int rectCount = 0; rectCount < numOfButton; rectCount ++){
            canvas.drawRect(menuStartPositionX + verticalEdge, menuStartPositionY + horizontalEdge +rectCount*(horizontalEdge + smallRectHeigh),
                    menuStartPositionX + smallRectWidth + verticalEdge, menuStartPositionY + (rectCount+1)*(horizontalEdge + smallRectHeigh), menuPaint);
        }

    }

    private void drawMenuText(Canvas canvas){
        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(100*0.75f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = textPaint.getFontMetrics();
        //String[] menuText = new String[numOfButton];
        //menuText[0] = new String("Easy");
        //menuText[1] = new String("Normal");
        //menuText[2] = new String("Hard");


        float menuStartPositionY = (getHeight() - menuHeight)/2;
        for (int textCount = 0; textCount < numOfButton; textCount ++){
            float x = getWidth()/2;
            float y = menuStartPositionY + horizontalEdge +textCount*(horizontalEdge + smallRectHeigh)+ smallRectHeigh /2 - (fm.ascent + fm.descent)/2;

            canvas.drawText( menuText[textCount], x, y, textPaint);
        }

    }

    protected int findButton(float xCoord, float yCoord){

        float menuStartPositionX = (getWidth() - menuWidth)/2;
        float menuStartPositionY = (getHeight() - menuHeight)/2;
        if ((xCoord>menuStartPositionX) && (xCoord<(menuStartPositionX + menuWidth))){
            int temp = (int)((yCoord - menuStartPositionY)/(smallRectHeigh + horizontalEdge));
            return temp;
        }

        return -1;

    }

}

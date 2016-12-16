package com.laboratory.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Li on 2016/11/16.
 * Draw the layout of sudoku and controls the numbers shown on it
 */
public class SudokuLayout extends View {

    private PuzzleUIData UIData;
    private float cellWidth; // we want square cells

    //private PuzzleController m_Puzzle; // sudoku puzzle
    private PuzzleHighlightor highlightedPosition; // class to check if a cell is checked, used to draw highlighted cell

    private MainActivity activity;

    private String[] keyPadText;
    protected int numOfKeyWithText;

    class SingleData {
        private int positionX;
        private int positionY;
        private int value;

        public int getPositionX(){
            return positionX;
        }

        public int getPositionY(){
            return positionY;
        }

        public int getValue(){
            return value;
        }

        public void setPositionX(int x){
            positionX = x;
        }

        public void setPositionY(int y){
            positionY = y;
        }

        public void setValue(int v){
            value = v;
        }
    }

    class PuzzleUIData{
        ArrayList<SingleData> correctList;
        ArrayList<SingleData> wrongList;
        ArrayList<SingleData> defaultList;

        public PuzzleUIData(){
            correctList = new ArrayList(81);
            wrongList = new ArrayList(81);
            defaultList = new ArrayList(81);
        }

        public void resetAllData(){
            correctList.clear();
            wrongList.clear();
            defaultList.clear();
        }

        public void addToCorrectInputList(int x, int y, int value){
            SingleData newData = new SingleData();
            newData.setPositionX(x);
            newData.setPositionY(y);
            newData.setValue(value);

            correctList.add(newData);
        }

        public void addToWrongInputList(int x, int y, int value){
            SingleData newData = new SingleData();
            newData.setPositionX(x);
            newData.setPositionY(y);
            newData.setValue(value);

            wrongList.add(newData);
        }

        public void addToDefaultList(int x, int y, int value){
            SingleData newData = new SingleData();
            newData.setPositionX(x);
            newData.setPositionY(y);
            newData.setValue(value);

            defaultList.add(newData);
        }
    }

    public SudokuLayout(Context context) {
        super(context);

        cellWidth = 1;

        //m_Puzzle = new PuzzleController();
        highlightedPosition = new PuzzleHighlightor();
        UIData = new PuzzleUIData();
        activity = (MainActivity)context;
        numOfKeyWithText = 3; // 3 keys with text by default
    }

    public SudokuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        cellWidth = 1;

        //m_Puzzle = new PuzzleController();
        highlightedPosition = new PuzzleHighlightor();
        UIData = new PuzzleUIData();
        activity = (MainActivity)context;
        numOfKeyWithText = 3; // 3 keys with text by default
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Log.d("Interface", "onDraw() called");
        drawCell(canvas);
        drawText(canvas);
        drawKeypad(canvas);
        drawKeypadText(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        int myAction = event.getAction();
        int[] cellPosition = new int[2]; // array to store position of cell
        int key = -1;
        if (myAction == MotionEvent.ACTION_DOWN) {
            touchCell(touchX, touchY, cellPosition); // check which cell is touched
            key = touchKeypad(touchX, touchY); // check which keypad button is touched
        }

        // find out which cell is selected
        int[] checkedCellPosition = new int[2];
        checkedCellPosition[0] = -1; checkedCellPosition[1] = -1;
        highlightedPosition.getHighlightedCell(checkedCellPosition);

        // highlight a cell
        if ((cellPosition[0]>=0)&&(cellPosition[0]<=9)&&(cellPosition[1]>=0)&&(cellPosition[1]<=9)){
            Message touchMsg = Message.obtain();
            touchMsg.what = 11;
            touchMsg.arg1 = cellPosition[0];
            touchMsg.arg2 = cellPosition[1];
            activity.getHandler().sendMessage(touchMsg);
        }

        // if one of the cell is highlighted and the keypad is pressed,
        // test if it is number or function button (erase, reset, menu)
        // if it is number, store the number on key to user puzzle
        if ((checkedCellPosition[0] != -1)&&(checkedCellPosition[1] != -1)&&(key!=-1)){
                if (key == 1) { // erase
                    // store erase position in message and send
                    Message msg = Message.obtain();
                    msg.what = 9;
                    msg.arg1 = checkedCellPosition[0];
                    msg.arg2 = checkedCellPosition[1];
                    activity.getHandler().sendMessage(msg);
                    //m_Puzzle.userEraseValue(checkedCellPosition[0], checkedCellPosition[1]);
                    Log.d("Interface", "Erase on keypad is pressed");
                } else if (key == 9) { // call menu
                    Log.d("Interface", "Menu on keypad is pressed");
                    activity.getHandler().sendEmptyMessage(1);
                }else if (key == 5) { // give hint at highlighted spot
                    Message msg = Message.obtain();
                    msg.what = 13;
                    msg.arg1 = checkedCellPosition[0];
                    msg.arg2 = checkedCellPosition[1];
                    activity.getHandler().sendMessage(msg);
                    Log.d("Interface", "Hint on keypad is pressed");
                } else { // fill number to puzzle
                    // decide number to be filled
                    switch (key) {
                        case 2:
                            key = 1;
                            break;
                        case 3:
                            key = 2;
                            break;
                        case 4:
                            key = 3;
                            break;
                        case 6:
                            key = 4;
                            break;
                        case 7:
                            key = 5;
                            break;
                        case 8:
                            key = 6;
                            break;
                        case 10:
                            key = 7;
                            break;
                        case 11:
                            key = 8;
                            break;
                        case 12:
                            key = 9;
                            break;
                        default:
                    }
                    Log.d("Interface", "Number on keypad is pressed");
                    Log.d("Interface", ((Integer) key).toString());
                    //m_Puzzle.userFillValue(checkedCellPosition[0], checkedCellPosition[1], key);
                    Message msg = Message.obtain();
                    msg.what = 8;
                    msg.arg1 = checkedCellPosition[0];
                    msg.arg2 = checkedCellPosition[1];
                    msg.obj = key;
                    activity.getHandler().sendMessage(msg);
                    key = -1;
                }
        }else if (key == 9) { // call menu when no cell is highlighted
                Log.d("Interface", "Menu on keypad is pressed");
                activity.getHandler().sendEmptyMessage(1);
        }

        // refresh view
        this.invalidate();

        return super.onTouchEvent(event);
        //return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w <= h){
            this.cellWidth = w / 9f;
        }else{
            this.cellWidth = h / 9f;
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void drawCell(Canvas canvas){

        // draw background
        Paint backgroundPaint = new Paint();

        // draw background according to cell status
        for (int rowCounter = 0; rowCounter < 9; rowCounter++){
            for (int columnCounter = 0; columnCounter < 9; columnCounter++){

                if (highlightedPosition.isHighlighted(rowCounter, columnCounter) == true){
                    backgroundPaint.setColor(getResources().getColor(R.color.shudu_light));
                }else {
                    backgroundPaint.setColor(getResources().getColor(R.color.shudu_background));
                }

                canvas.drawRect(rowCounter*cellWidth, columnCounter*cellWidth, (rowCounter+1)*cellWidth, (columnCounter+1)*cellWidth, backgroundPaint);

            }
        }


        // draw lines
        Paint darkPaint = new Paint();
        darkPaint.setColor(getResources().getColor(R.color.shudu_dark));

        Paint hilitePaint = new Paint();
        hilitePaint.setColor(getResources().getColor(R.color.shudu_hilite));

        Paint lightPaint = new Paint();
        lightPaint.setColor(getResources().getColor(R.color.shudu_light));

        // draw normal lines
        for(int i = 0 ; i <= 9 ; i++){
            canvas.drawLine(0,               i*cellWidth,        getWidth(),        i*cellWidth,     darkPaint); // horizontal line
            //canvas.drawLine(0,               i*m_CellWidth + 1,    getWidth(),        i*m_CellWidth + 1, hilitePaint);
            canvas.drawLine(i*cellWidth,     0,                  i*cellWidth,     9*cellWidth,       darkPaint); // vertical line
            //canvas.drawLine(i*m_CellWidth + 1, 0,                  i*m_CellWidth + 1, 9*m_CellWidth ,      hilitePaint);
        }

        // draw thick lines to separate each 3x3 block
        darkPaint.setStrokeWidth((float) 4.0);
        canvas.drawLine( 0, 3*cellWidth, 9*cellWidth, 3*cellWidth, darkPaint);
        canvas.drawLine( 0, 6*cellWidth, 9*cellWidth, 6*cellWidth, darkPaint);
        canvas.drawLine( 3*cellWidth, 0, 3*cellWidth, 9*cellWidth, darkPaint);
        canvas.drawLine( 6*cellWidth, 0, 6*cellWidth, 9*cellWidth, darkPaint);
    }

    private void drawText(Canvas canvas){
        Paint numberPaint = new Paint();
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setTextSize(cellWidth*0.75f);
        numberPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = numberPaint.getFontMetrics();
        float x = cellWidth / 2;
        float y = cellWidth/2 - (fm.ascent + fm.descent)/2;

        // draw all default text
        for (int i = UIData.defaultList.size() - 1; i >= 0; i--){
            numberPaint.setColor(getResources().getColor(R.color.shudu_text_default));
            String tempString = ((Integer) UIData.defaultList.get(i).getValue()).toString();
            canvas.drawText( tempString, UIData.defaultList.get(i).getPositionX()*cellWidth + x, UIData.defaultList.get(i).positionY*cellWidth + y, numberPaint);
        }
        for (int i = UIData.correctList.size() - 1; i >= 0; i--){
            numberPaint.setColor(getResources().getColor(R.color.shudu_text_correct));
            String tempString = ((Integer) UIData.correctList.get(i).getValue()).toString();
            canvas.drawText( tempString, UIData.correctList.get(i).getPositionX()*cellWidth + x, UIData.correctList.get(i).positionY*cellWidth + y, numberPaint);
        }
        for (int i = UIData.wrongList.size() - 1; i >= 0; i--){
            numberPaint.setColor(getResources().getColor(R.color.shudu_text_wrong));
            String tempString = ((Integer) UIData.wrongList.get(i).getValue()).toString();
            canvas.drawText( tempString, UIData.wrongList.get(i).getPositionX()*cellWidth + x, UIData.wrongList.get(i).positionY*cellWidth + y, numberPaint);
        }

    }

    // keypad layout:
    // erase 1 2 3
    // reset 4 5 6
    // menu  7 8 9
    private void drawKeypad(Canvas canvas){
        Paint keypadPaint = new Paint();
        keypadPaint.setColor(getResources().getColor(R.color.shudu_hilite));

        float keypadHeight = (getHeight() - 9*cellWidth)/3;
        float keypadWidth = getWidth()/4;


        // draw pad
        for (int keyRow = 0; keyRow < 3; keyRow ++){
            for (int keyColumn = 0; keyColumn < 4; keyColumn ++){
                canvas.drawRect(keyColumn*keypadWidth+1, 9*cellWidth + keyRow*keypadHeight+1, (keyColumn+1)*keypadWidth-1, 9*cellWidth + (keyRow+1)*keypadHeight-1, keypadPaint);
                //canvas.drawRoundRect();
            }
        }

        // draw menu button
        //canvas.drawRect(1, 9*m_CellWidth + keypadHeight+1, keypadWidth-1, 9*m_CellWidth + 3*keypadHeight-1, keypadPaint);

        //draw text for keypad
        keypadPaint.setStyle(Paint.Style.STROKE);
        keypadPaint.setTextSize(cellWidth*0.75f);
        keypadPaint.setTextAlign(Paint.Align.CENTER);
        keypadPaint.setColor(getResources().getColor(R.color.shudu_dark));

        Paint.FontMetrics fm = keypadPaint.getFontMetrics();
        float x = keypadWidth / 2;
        float y = keypadHeight/2 - (fm.ascent + fm.descent)/2;

        int keypadText = 1;
        for (int keyRow = 0; keyRow < 3; keyRow ++){
            for (int keyColumn = 1; keyColumn < 4; keyColumn ++){

                String tempString = ((Integer)keypadText).toString();

                canvas.drawText( tempString, keyColumn*keypadWidth + x, 9*cellWidth + keyRow*keypadHeight + y, keypadPaint);

                keypadText ++;
            }
        }

    }

    public void drawKeypadText(Canvas canvas){
        Paint keypadTextPaint = new Paint();
        keypadTextPaint.setStyle(Paint.Style.STROKE);
        keypadTextPaint.setTextSize(cellWidth*0.50f);
        keypadTextPaint.setTextAlign(Paint.Align.CENTER);
        keypadTextPaint.setColor(getResources().getColor(R.color.shudu_dark));

        Paint.FontMetrics fm = keypadTextPaint.getFontMetrics();
        float keypadWidth = 9*cellWidth/4;
        float keypadHeight = (getHeight() - 9*cellWidth)/3;

        float x = keypadWidth / 2;
        float y = keypadHeight / 2 - (fm.ascent + fm.descent) / 2;

        //canvas.drawText( "Erase", x, 9*cellWidth + 0 * keypadHeight + y, keypadTextPaint);
        //canvas.drawText( "Hint", x, 9*cellWidth + 1 * keypadHeight + y, keypadTextPaint); // TODO: show number of hints left (i.e. hint 3/5 means 3 hints out of 5 left for user to use)
        //canvas.drawText( "Menu",  x, 9*cellWidth + 2 * keypadHeight + y, keypadTextPaint);
        canvas.drawText( keyPadText[0], x, 9*cellWidth + 0 * keypadHeight + y, keypadTextPaint);
        canvas.drawText( keyPadText[1], x, 9*cellWidth + 1 * keypadHeight + y, keypadTextPaint);
        canvas.drawText( keyPadText[2],  x, 9*cellWidth + 2 * keypadHeight + y, keypadTextPaint);
    }

    public void setSudokuLayoutText(String[] text){
        if (text.length < numOfKeyWithText){
            return;
        }
        keyPadText = new String[numOfKeyWithText];

        for (int i = 0; i< numOfKeyWithText; i++){
            keyPadText[i] = text[i];
        }
    }

    public void touchCell(float x, float y, int[] out_cellPosition) {

        int row = (int)(x/cellWidth);
        int column = (int)(y/cellWidth);

        if ((column<0) || (column > 8)){
            column = -1;
        }
        if ((row<0) || (row > 8)){
            row = -1;
        }

        out_cellPosition[0] = column;
        out_cellPosition[1] = row;

        return;
    }

    public int touchKeypad(float x, float y) {

        float keypadWidth = getWidth()/4;
        float keypadHeight = (getHeight() - 9*cellWidth)/3;

        int column = (int)(x/keypadWidth);

        float yCoord = y - 9*cellWidth;
        int row = (int)((y - 9*cellWidth)/keypadHeight);
        if ((yCoord < 0)&&(row == 0)){
            row = -1;
        }
        Log.d("Interface", ((Float)(y - 9*cellWidth)).toString());
        Log.d("Interface", ((Float)x).toString());
        Log.d("Interface", ((Integer)row).toString());
        Log.d("Interface", ((Integer)column).toString());

        if ((row<0) || (row > 3)||(column<0) || (column > 3)){
            return -1;
        }

        int out_key = row * 4 + column + 1;

        //Log.d("Interface", "Keypad touched");
        //Log.d("Interface", ((Integer)out_key).toString());

        return out_key;
    }

    public void hightHightCell(int x, int y){
        highlightedPosition.highlightPosition(x, y);
    }

    public void resetAllData(){
        UIData.resetAllData();
        highlightedPosition.resetHighLight();
    }

    public void addToCorrectInputList(int x, int y, int value){
        UIData.addToCorrectInputList(x, y, value);
    }

    public void addToWrongInputList(int x, int y, int value){
        UIData.addToWrongInputList(x, y, value);
    }

    public void addToDefaultList(int x, int y, int value){
        UIData.addToDefaultList(x, y, value);
    }
}

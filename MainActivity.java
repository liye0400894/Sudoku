package com.laboratory.sudoku;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private PuzzleController puzzleController;
    private PuzzleUI puzzleUI;
    private FileController file;

    private Handler m_UIHandler = new Handler(){

        public void handleMessage(Message msg) {
            Log.d("Main activity", "Message received");
            if(msg.what == 1){ // message from puzzle to call menu
                Log.d("Main activity", "From puzzle to menu");
                setContentView(puzzleUI.menuView);
            }else if(msg.what == 2){ // message from puzzle to call congratulation dialog
                Log.d("Main activity", "From puzzle to dialog");
                setContentView(puzzleUI.messageView);
            }else if(msg.what == 3){ // message from congratulation dialog to go to menu
                Log.d("Main activity", "From dialog to menu");
                setContentView(puzzleUI.menuView);
            }else if (msg.what == 4){ // message from menu to go back to puzzle
                Log.d("Main activity", "From menu to puzzle");
                setContentView(puzzleUI.puzzleView);
            }else if(msg.what >= 5 && msg.what <=7){ // message from menu to start new puzzle

                // generate new puzzle according to message
                // 5 - easy; 6 - normal; 7 - hard;
                int newPuzzleLevel = msg.what - 4;
                Log.d("Main activity", "About to:generate new puzzle");
                Log.d("Main activity", ((Integer)newPuzzleLevel).toString());

                // reset UI data
                puzzleUI.resetUIData();
                // reset old user input
                puzzleController.puzzlePlayer.resetAllUserInput();
                puzzleController.puzzlePlayer.setHintToUse(5);
                generatePuzzle(newPuzzleLevel);
                fillUIData();
                fillMenuData();

                setContentView(puzzleUI.puzzleView);
            }else if (msg.what == 8){ // fill in value
                // fill value into game data
                int value = (int)msg.obj;
                puzzleController.puzzlePlayer.userFillValue(msg.arg2, msg.arg1, value);

                // refresh UI data
                puzzleUI.resetUIData();

                fillUIData();
                puzzleUI.puzzleView.invalidate();

                // if puzzle is resolved, show congratulation dialog
                if (puzzleController.puzzlePlayer.isResolutionCorrect() == true){
                    setContentView(puzzleUI.messageView);
                }

            }else if (msg.what == 9){
                // erase value in game data
                puzzleController.puzzlePlayer.userEraseValue(msg.arg2, msg.arg1);

                // refresh UI data
                puzzleUI.resetUIData();
                fillUIData();

                puzzleUI.puzzleView.invalidate();
                //m_Puzzle.userEraseValue(checkedCellPosition[0], checkedCellPosition[1]);
            }else if (msg.what == 10){ // reset puzzle
                //m_Puzzle.resetAllUserInput();
                puzzleController.puzzlePlayer.resetAllUserInput();
                puzzleUI.resetUIData();
                fillUIData();
                puzzleUI.puzzleView.invalidate();
                setContentView(puzzleUI.puzzleView);
            }else if (msg.what == 11){
                // if a cell is not default, highlight it
                if (puzzleController.puzzlePlayer.isDefault(msg.arg2, msg.arg1) != true){
                    puzzleUI.hightHightCell(msg.arg2, msg.arg1);
                    puzzleUI.puzzleView.invalidate();
                }
            } else if (msg.what == 12){ // save puzzle progress
                SavePuzzleProgress();
            } else if (msg.what == 13) { // give hint to user
                if (puzzleController.puzzlePlayer.isDefault(msg.arg2, msg.arg1) != true){
                    puzzleController.puzzlePlayer.GiveHintAtPosition(msg.arg2, msg.arg1);
                    puzzleUI.resetUIData();
                    fillUIData();
                    fillMenuData();
                    puzzleUI.puzzleView.invalidate();
                }
                Log.d("Main activity", "About to give hint to user");
            } else if (msg.what == 14){ // call menu to select new game level
                setContentView(puzzleUI.gameLevelChoiceView);
            }

        }
    };

    public Handler getHandler(){
        return m_UIHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        puzzleController = new PuzzleController();
        puzzleController.puzzlePlayer.setHintToUse(5);
        // check if a saved puzzle exist, if so, load the saved progress
        file = new FileController();
        boolean fileExist = ReadFile();
        if (fileExist == false){
            puzzleController.puzzleGenerator.generateSudokuPuzzle();
            //puzzleController.puzzlePlayer.setHintToUse(5);
        }

        puzzleUI = new PuzzleUI(this);

        fillUIData();
        fillMenuData();

        setContentView(puzzleUI.puzzleView);
    }

    private void fillUIData(){

        // fill default values to UI
        for (int x = 0; x < 9; x ++){
            for (int y = 0; y < 9; y ++){
                if (puzzleController.puzzlePlayer.isDefault(y, x) == true){
                    puzzleUI.puzzleView.addToDefaultList(y, x, puzzleController.puzzlePlayer.getDefaultValue(y, x));
                }

            }
        }

        // test user input values, if it is invalid, fill it to wrong value list
        // otherwise fill it to correct value list (a wrong value could be valid)
        for (int x = 0; x < 9; x ++){
            for (int y = 0; y < 9; y ++){
                if (puzzleController.puzzlePlayer.validateValueAtCell(y, x) == true){
                    puzzleUI.puzzleView.addToCorrectInputList(y, x, puzzleController.puzzlePlayer.getUserInputValue(y, x));
                }else{
                    if (puzzleController.puzzlePlayer.getUserInputValue(y, x) != 0){
                        puzzleUI.puzzleView.addToWrongInputList(y, x, puzzleController.puzzlePlayer.getUserInputValue(y, x));
                    }
                }
            }
        }

    }

    private void fillMenuData(){
        puzzleUI.menuView.setNumOfButton(3);

        String[] text = new String[3];
        text[0] = new String("Reset Puzzle");
        text[1] = new String("Save Puzzle");
        text[2] = new String("New Game");

        puzzleUI.menuView.setMenuText(text);

        puzzleUI.gameLevelChoiceView.setNumOfButton(3);
        String[] textChoice = new String[3];
        textChoice[0] = new String("Easy");
        textChoice[1] = new String("Normal");
        textChoice[2] = new String("Hard");

        puzzleUI.gameLevelChoiceView.setMenuText(textChoice);

        String[] keyPadText = new String[3];
        keyPadText[0] = new String("Erase");

        //keyPadText[1] = new String("Hint5/5");
        CalculateHintText(keyPadText);

        keyPadText[2] = new String("Menu");

        puzzleUI.puzzleView.setSudokuLayoutText(keyPadText);
    }

    private void CalculateHintText(String[] text){

        int maxHint = puzzleController.puzzlePlayer.getMaxHint();
        int leftHint = puzzleController.puzzlePlayer.getNumOfHint();
        String hintText = "Hint(" + ((Integer)leftHint).toString() + "/" + ((Integer)maxHint).toString() + ")";

        text[1] = hintText;
    }

    private void generatePuzzle(int level){
        puzzleController.puzzleGenerator.setPuzzleLevel(level);
        puzzleController.puzzleGenerator.generateSudokuPuzzle();
    }

    // attempt to read saved puzzle progress, return true if read successfully
    // and return false if failed to read
    private boolean ReadFile(){
        // get file path
        File recordFileDir = this.getFilesDir();

        ArrayList<String> dataList = new ArrayList();
        boolean success = file.ReadFromFile(recordFileDir.toString(),
                dataList);

        if (success == true) {
            // fill file data to puzzle
            FillDataToPuzzle(dataList);

            return true;
        }

        return false;
    }

    private void SavePuzzleProgress(){
        // get file path
        File recordFileDir = this.getFilesDir();

        // get data to be written
        ArrayList<String> dataList = new ArrayList();
        GetPuzzleData(dataList);

        // write data to file
        file.WriteToFile(recordFileDir.toString(), dataList);
    }

    private void GetPuzzleData(ArrayList<String> data){
        puzzleController.puzzlePlayer.getAllPuzzleResolution(data);
        puzzleController.puzzlePlayer.getAllDefaultData(data);
        puzzleController.puzzlePlayer.getAllUserInput(data);
        puzzleController.puzzlePlayer.getHintData(data);
    }

    private void FillDataToPuzzle(ArrayList<String> data){
        int n = 0;

        // fill in resolution data
        for (int x = 0; x < 9; x ++){
            for (int y = 0; y < 9; y ++){
                //int i = Integer.parseInt(dataList.get(0));
                puzzleController.puzzlePlayer.setPuzzleResolutionValue(x, y, Integer.parseInt(data.get(n)));
                n ++;
            }
        }

        // fill in default data
        for (int x = 0; x < 9; x ++){
            for (int y = 0; y < 9; y ++){
                //int i = Integer.parseInt(dataList.get(0));
                puzzleController.puzzlePlayer.setDefaultValue(x, y, Integer.parseInt(data.get(n)));
                n ++;
            }
        }

        // fill in user input data
        for (int x = 0; x < 9; x ++){
            for (int y = 0; y < 9; y ++){
                //int i = Integer.parseInt(dataList.get(0));
                puzzleController.puzzlePlayer.setUserInputValue(x, y, Integer.parseInt(data.get(n)));
                n ++;
            }
        }

        puzzleController.puzzlePlayer.setHintToUse(Integer.parseInt(data.get(n)));
    }

}







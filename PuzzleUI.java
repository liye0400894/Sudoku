package com.laboratory.sudoku;

/**
 * Created by Li on 2016/11/25.
 */
public class PuzzleUI {
    public SudokuLayout puzzleView;
    public MenuLayout menuView;
    public GameLevelMenu gameLevelChoiceView;
    public SudokuCompletedMessage messageView;

    public PuzzleUI(MainActivity activity){
        initView(activity);
    }

    private void initView(MainActivity activity){
        puzzleView = new SudokuLayout(activity);
        menuView = new MenuLayout(activity);
        gameLevelChoiceView = new GameLevelMenu(activity);
        messageView = new SudokuCompletedMessage(activity);
    }

    public void resetUIData(){
        puzzleView.resetAllData();
    }

    public void hightHightCell(int x, int y){
        puzzleView.hightHightCell(x, y);
    }
}

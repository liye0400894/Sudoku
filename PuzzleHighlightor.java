package com.laboratory.sudoku;

/**
 * Created by Li on 2016/11/18.
 * class to store if one of cell is highlighted
 */
public class PuzzleHighlightor {


    // array to store check/uncheck status of all cells
    // 0 - cell is not highlighted; 1 - cell is highlighted
    private int highlightedPositionX;
    private int highlightedPositionY;
    //private int[][] highlightPuzzle =
           //{{0, 0, 0, 0, 0, 0, 0, 0, 0},
            //{0, 0, 0, 0, 0, 0, 0, 0, 0},
            //{0, 0, 0, 0, 0, 0, 0, 0, 0},
            //{0, 0, 0, 0, 0, 0, 0, 0, 0},
            //{0, 0, 0, 0, 0, 0, 0, 0, 0},
            //{0, 0, 0, 0, 0, 0, 0, 0, 0},
            //{0, 0, 0, 0, 0, 0, 0, 0, 0},
            //{0, 0, 0, 0, 0, 0, 0, 0, 0},
            //{0, 0, 0, 0, 0, 0, 0, 0, 0}};

    public PuzzleHighlightor(){
        highlightedPositionX = -1;
        highlightedPositionY = -1;
    }

    public boolean isHighlighted(int row, int column){
        //if ((row<0) || (row > 8)){
        //    return false;
        //}
        //if ((column<0) || (column > 8)){
        //    return false;
        ///}

        //if (highlightPuzzle[row][column] == 0){
        //    return false;
        //}

        if ((row == highlightedPositionX) && (column == highlightedPositionY)){
            return true;
        }

        return false;
    }

    public void highlightPosition(int row, int column){
        if ((row<0) || (row > 8)){
            return;
        }
        if ((column<0) || (column > 8)){
            return;
        }

        highlightedPositionX = row;
        highlightedPositionY = column;

    }

    public void getHighlightedCell(int[] position){
        position[0] = highlightedPositionY;
        position[1] = highlightedPositionX;
    }

    public void resetHighLight(){
        highlightedPositionX = -1;
        highlightedPositionY = -1;
    }
}

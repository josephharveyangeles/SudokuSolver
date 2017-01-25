/*
 *  Copyright (c) 2014 Nokia. All rights reserved.
 *
 *  Revision History:
 *
 *  DATE/AUTHOR          COMMENT
 *  ---------------------------------------------------------------------
 *  Oct 8, 2014/Joseph.Angeles                            
 */
package com.josephharveyangeles.ss;

import java.util.ArrayList;

/**
 * TODO:Write class description
 * @author <a HREF="mailto:yourMail@nsn.com">Your Name</a>
 *
 */
public class SudokuState
{

    /**
     * 
     */
    private int[][] grid = new int[ 9 ][ 9 ];

    private ArrayList<Integer> choices;

    private Cell theBlankCell;

    public SudokuState( int[][] board, ArrayList<Integer> choices, Cell theCell )
    {
        //this.grid = board;
        copyGrid( board );
        this.choices = choices;
        this.theBlankCell = new Cell( theCell );
        //this.theBlankCell = theCell;
    }

    public int[][] getGrid()
    {
        return this.grid;
    }

    public ArrayList<Integer> getChoices()
    {
        return this.choices;
    }

    public Cell getCell()
    {
        return this.theBlankCell;
    }

    private void copyGrid( int[][] oldGrid )
    {
        for( int i = 0; i < oldGrid.length; i++ )
            for( int j = 0; j < oldGrid[i].length; j++ )
                grid[i][j] = oldGrid[i][j];
    }

}

/*
 *  Copyright (c) 2014 Nokia. All rights reserved.
 *
 *  Revision History:
 *
 *  DATE/AUTHOR          COMMENT
 *  ---------------------------------------------------------------------
 *  Oct 7, 2014/Joseph.Angeles                            
 */
package com.josephharveyangeles.ss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * TODO:Write class description
 * @author <a HREF="mailto:yourMail@nsn.com">Your Name</a>
 *
 */
public class Main
{

    /**
     * 
     */
    private int[][] grid;
    private final static int X_AXIS = 0;
    private final static int Y_AXIS = 1;

    private ArrayDeque<SudokuState> stateStack;

    private static ArrayList<Integer> MASTERLIST;

    private final static Cell BOX0_CELL_ORIGIN = new Cell( 0, 0 );

    private final static Cell BOX1_CELL_ORIGIN = new Cell( 3, 0 );

    private final static Cell BOX2_CELL_ORIGIN = new Cell( 6, 0 );

    private final static Cell BOX3_CELL_ORIGIN = new Cell( 0, 3 );

    private final static Cell BOX4_CELL_ORIGIN = new Cell( 3, 3 );

    private final static Cell BOX5_CELL_ORIGIN = new Cell( 6, 3 );

    private final static Cell BOX6_CELL_ORIGIN = new Cell( 0, 6 );

    private final static Cell BOX7_CELL_ORIGIN = new Cell( 3, 6 );

    private final static Cell BOX8_CELL_ORIGIN = new Cell( 6, 6 );

    private final static int X_DOMAIN = 0;

    private final static int Y_DOMAIN = 1;

    private final static int BOX_DOMAIN = 2;

    private boolean stateFlag;

    public Main( File f )
    {

        try
        {
            grid = new int[ 9 ][ 9 ];
            initGrid( f );
            initMasterList();
            this.stateStack = new ArrayDeque<>();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }


        //solveVersionOne();
        //solveVersionTwo();
        solveVersionThree();

    }
    
    private void solveVersionThree(){
    	int x = 0;
    	int y = 0;
    	boolean popFlag = false;
    	ArrayList<Integer> answers = new ArrayList<>();
    	
    	while(true){
    		//System.out.println("Current coord: "+x+", "+y);
    		
    		if(grid[x][y]==0){
    			//do all calcs here
    			
    			// if answers are from pops, don't get another set of answers.
    			if(!popFlag){
    				answers = findPossibleAnswersOnCell(new Cell(x,y));
    			}
    			
    			if(answers.isEmpty()){
    				//pop
    				//System.out.println("no possible answers, reverting back..");
    				try{
    					SudokuState ss = this.stateStack.pop();
    					copyGrid( grid, ss.getGrid() );
    	                answers = ss.getChoices();
    	                Cell backCell = ss.getCell();
    	                x = backCell.getX();
    	                y = backCell.getY();
    	                grid[x][y] = 0;
    				}catch(NoSuchElementException nse){
    					break;
    				}
                   
    			}
    			//System.out.println("possible ans: "+answers);
    			
    			if(answers.size()>1){
    				//push
    				int[][] gridcopy = new int[ 9 ][ 9 ];
                    copyGrid( gridcopy, grid );
                    ArrayList<Integer> anscopy = new ArrayList<>( answers.subList( 1, answers.size() ) );
                    Cell currentCell = new Cell( x, y );
                    this.stateStack.push( new SudokuState( gridcopy, anscopy, currentCell ) );
                    //System.out.println("pushed ans: "+anscopy);
    			}
    			
    			int ans = answers.get(0);
    			if(!isBoardValid(x, y, ans)){
    				//pop
    				//if can't pop, break;
    				try{
    					SudokuState ss = this.stateStack.pop();
    					copyGrid( grid, ss.getGrid() );
    	                answers = ss.getChoices();
    	                Cell backCell = ss.getCell();
    	                x = backCell.getX();
    	                y = backCell.getY();
    	                grid[x][y] = 0;
    				}catch(NoSuchElementException nse){
    					break;
    				}
    				popFlag = true;
    				continue;
    			}
    			else{
    			
	    			grid[x][y] = ans;
	    			//System.out.println("Inserted ans: "+ans);
	    			
	    			//check if solved.
	        		if( findCellWithLowestBlankNeighbors() == null ){
	        			System.out.println("!!!!!!-- Grid Solved --!!!!!!");
	        			printGrid(this.grid);
	        			
	        			//pop, look for other solutions
	        			// if not possible break;
	        			try{
	    					SudokuState ss = this.stateStack.pop();
	    					copyGrid( grid, ss.getGrid() );
	    	                answers = ss.getChoices();
	    	                Cell backCell = ss.getCell();
	    	                x = backCell.getX();
	    	                y = backCell.getY();
	    	                grid[x][y] = 0;
	    				}catch(NoSuchElementException nse){
	    					break;
	    				}
	        			grid[x][y] = 0;
	        			popFlag = true;
	        			continue;
	        		}
    			
    			}
    		}
    		
    		
    		// move forward, proceed to the next
    		if(x==8){
    			x=0;
    			y++;
    			if(y>8){
    				//pop values
    				//if can't pop, break;
    				try{
    					SudokuState ss = this.stateStack.pop();
    					copyGrid( grid, ss.getGrid() );
    	                answers = ss.getChoices();
    	                Cell backCell = ss.getCell();
    	                x = backCell.getX();
    	                y = backCell.getY();
    	                grid[x][y] = 0;
    				}catch(NoSuchElementException nse){
    					break;
    				}
    				grid[x][y] = 0; //reset cell.
    				popFlag = true;
    				continue;
    			}
    		}else{
    			x++;
    			popFlag = false;
    		}
    	}
    	
    	System.out.println("Can't find other possible solutions..");
    	
    }

    private void solveVersionTwo()
    {
        int x = 0;
        int y = 0;
        boolean popFlag = false;
        ArrayList<Integer> answers = new ArrayList<>();
        while(true)
        {
            if( x == 9 )
            {
                x = 0;
                y++;
            }

            if( y == 9 )
            {
                //pop state, look for more solutions
            	System.out.println("reached the end");
                SudokuState ss = this.stateStack.pop();
                copyGrid( grid, ss.getGrid() );
                answers = ss.getChoices();
                Cell backCell = ss.getCell();
                x = backCell.getX();
                y = backCell.getY();
                popFlag = true;
            }
            
            if((grid[x][y]!=0)&&!popFlag){
            	x++;
            	continue;
            }
            
            if(!popFlag){
            	answers = findPossibleAnswersOnCell(new Cell(x,y));
            	System.out.println( "possible ans: "+answers );
            }
            
                if( answers.size() > 1 )
                {
                    // push choices
                    int[][] gridcopy = new int[ 9 ][ 9 ];
                    copyGrid( gridcopy, grid );
                    ArrayList<Integer> anscopy = new ArrayList<>( answers.subList( 1, answers.size() ) );
                    Cell currentCell = new Cell( x, y );
                    this.stateStack.push( new SudokuState( gridcopy, anscopy, currentCell ) );
                    System.out.println("pushed ans: "+anscopy);

                }
                
                if( answers.isEmpty() )
                {
                    //pop state solve again
                	System.out.println("no possible answers");
                    SudokuState ss = this.stateStack.pop();
                    copyGrid( grid, ss.getGrid() );
                    answers = ss.getChoices();
                    Cell backCell = ss.getCell();
                    x = backCell.getX();
                    y = backCell.getY();
                    popFlag = true;

                }
            

            //use ans[0]
            int ans = answers.get(0);
            grid[x][y] = ans;
            System.out.println("Inserted ans:"+ans);

            if( popFlag )
            {
                popFlag = false;
            }
            
            if(!isBoardValid(x,y,ans)){
            	//pop state
            	System.out.println( "Trying another run" );
                SudokuState ss = this.stateStack.pop();
                copyGrid( grid, ss.getGrid() );
                answers = ss.getChoices();
                Cell backCell = ss.getCell();
                x = backCell.getX();
                y = backCell.getY();
                popFlag = true;
                System.out.println("recovered answers: "+answers);
                continue;
            }
            
            if( findCellWithLowestBlankNeighbors() == null )
            {
                System.out.println( "Grid Solved!" );
                printGrid( grid );

                System.out.println( "Trying another run" );
                SudokuState ss = this.stateStack.pop();
                copyGrid( grid, ss.getGrid() );
                answers = ss.getChoices();
                Cell backCell = ss.getCell();
                x = backCell.getX();
                y = backCell.getY();
                popFlag = true;
                continue;
            }
            
            if(this.stateStack.size() == 0){
            	break;
            }
            
            x++;
        }

        System.out.println( "No other possible solutions found.." );

    }
    
    private boolean isBoardValid(int x, int y, int value){
    	boolean temp = false;
    	
    	Cell origin = findBoardCellOriginCoordinateWhereTheCellBelongs(x, y);
    	temp = isValueOnDomainXValid(y,value);
    	temp = isValueOnDomainYValid(x, value);
    	temp = isValueOnBoxDomainValid(origin.getX(), origin.getY(), value);
    	
    	return temp;
    }

    private void solveVersionOne()
    {
        ArrayList<Integer> choices = new ArrayList<>();
        Cell blankCell = new Cell();

        while( true )
        {

            //printGrid( grid );

            blankCell = findCellWithLowestBlankNeighbors();

            if( blankCell == null )
            {
                System.out.println( "\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Grid solved!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" );
                printGrid( grid );
                try
                {
                    System.out.println( "trying another run.." );
                    SudokuState ss = this.stateStack.pop();
                    copyGrid( this.grid, ss.getGrid() );
                    choices = ss.getChoices();
                    blankCell = ss.getCell();
                    stateFlag = true;

                    //System.out.println( "Recovered previous state.." );
                    //System.out.println( "Choices: " + choices );
                    //System.out.println( "Cell:[" + blankCell.getX() + "," + blankCell.getY() + "]" );
                    //System.out.println( "new grid:" );
                    //printGrid( grid );
                }
                catch( NoSuchElementException nse )
                {
                    System.out.println( "no other possible solutions can be found.." );
                    break;
                }
                //continue;
            }

            if( !stateFlag )
            {
                choices = findPossibleAnswersOnCell( blankCell );
            }
            else
            {
                stateFlag = false;
            }

            if( choices.size() > 1 )
            {
                int[][] pushGrid = new int[ 9 ][ 9 ];
                ArrayList<Integer> pushChoice = new ArrayList<>( choices.subList( 1, choices.size() ) );
                copyGrid( pushGrid, this.grid );
                stateStack.push( new SudokuState( pushGrid,  pushChoice,
                    blankCell ) );
                //System.out.println( "pushed a state" );
                //System.out.println( "stack length:" + this.stateStack.size() );
                //System.out.println( "Saved current state.." );
                //System.out.println( "pushed choice: " + pushChoice );
                //System.out.println( "Cell:[" + blankCell.getX() + "," + blankCell.getY() + "]" );
                //System.out.println( "Current grid:" );
                //printGrid( grid );

            }

            if( choices.isEmpty() )
            {
                //System.out.println( "unsolvable board." );
                //printGrid( grid );
                //System.out.println( "Conflict on Cell: [" + blankCell.getX() + "," + blankCell.getY() + "]" );
                //System.out.println( "Reverting back..." );
                
                try{
                    //System.out.println( "trying another run.." );
                    SudokuState ss = this.stateStack.pop();
                    copyGrid( this.grid, ss.getGrid() );
                    choices = ss.getChoices();
                    blankCell = ss.getCell();
                    //System.out.println( "popped a state" );
                    //System.out.println( "stack length:" + this.stateStack.size() );
                    //System.out.println( "Recovered previous state.." );
                    //System.out.println( "Choices: " + choices );
                    //System.out.println( "Cell:[" + blankCell.getX() + "," + blankCell.getY() + "]" );

                    //System.out.println( "new grid:" );
                    //printGrid( grid );

                }
                catch( NoSuchElementException nse )
                {
                        System.out.println( "no other possible solutions can be found.." );
                        break;
                }
                //continue;
            }

            grid[blankCell.getX()][blankCell.getY()] = choices.get( 0 );
            //System.out.println( "Put on grid:" + choices.get( 0 ) + "\n" );
        }
    }
    private ArrayList<Integer> findPossibleAnswersOnCell( Cell theCell )
    {
        ArrayList<Integer> possibleAns = new ArrayList<>( this.MASTERLIST );
        //System.out.println( "Values on DomainX" + getValuesOnDomainX( theCell ) );
        //System.out.println( "Values on DomainY" + getValuesOnDomainY( theCell ) );
        //System.out.println( "Values on Box Domain" + getValuesOnBoxDomain( theCell ) );
        possibleAns.removeAll( getValuesOnBoxDomain( theCell ) );
        possibleAns.removeAll( getValuesOnDomainX( theCell ) );
        possibleAns.removeAll( getValuesOnDomainY( theCell ) );
        //System.out.println( "possible ans:" + possibleAns );
        //System.out.println( "masterlist:" + this.MASTERLIST );
        return possibleAns;
    }
    private Cell findCellWithLowestBlankNeighbors()
    {
        int[] rows = new int[ 9 ];
        int[] cols = new int[ 9 ];
        int[] boxes = new int[ 9 ];
        countNumberOfExistingValueOnXY( rows, cols, 0 );
        countNumberOfExistingValueOnBoxes( boxes, 0 );

        int min = 9;
        int index = 0;
        int domain = 0;

        for( int i = 0; i < rows.length; i++ )
        {
            if( rows[i] <= min )
            {
                if( rows[i] == 0 )
                {
                    continue;
                }
                min = rows[i];
                index = i;
                domain = X_DOMAIN;
            }
        }

        for( int i = 0; i < cols.length; i++ )
        {
            if( cols[i] <= min )
            {
                if( cols[i] == 0 )
                {
                    continue;
                }
                min = cols[i];
                index = i;
                domain = Y_DOMAIN;

            }
        }

        for( int i = 0; i < boxes.length; i++ )
        {
            if( boxes[i] <= min )
            {
                if( boxes[i] == 0 )
                {
                    continue;
                }
                min = boxes[i];
                index = i;
                domain = BOX_DOMAIN;

            }
        }

        if( min >= 9 )
        {
            return null;
        }
        //System.out.println( "Lowest: " + min + " at index: " + index + " found on: " + domain );

        Cell blankCell = findBlankCell( index, domain );
        //System.out.println( "Blank cell found on: [" + blankCell.getX() + "," + blankCell.getY() + "] = " +
        //   blankCell.getValue() );

        return blankCell;
    }

    private ArrayList<Integer> getValuesOnDomainX( Cell theCell )
    {
        ArrayList<Integer> vals = new ArrayList<>();
        for( int i = 0; i < 9; i++ )
        {
            if( grid[i][theCell.getY()] != 0 )
            {
                vals.add( grid[i][theCell.getY()] );
            }
        }
        return vals;
    }

    private ArrayList<Integer> getValuesOnDomainY( Cell theCell )
    {
        ArrayList<Integer> vals = new ArrayList<>();
        for( int i = 0; i < 9; i++ )
        {
            if( grid[theCell.getX()][i] != 0 )
            {
                vals.add( grid[theCell.getX()][i] );
            }
        }
        return vals;
    }

    private ArrayList<Integer> getValuesOnBoxDomain( Cell theCell )
    {
        ArrayList<Integer> vals = new ArrayList<>();
        Cell theOrigin = findBoardCellOriginCoordinateWhereTheCellBelongs( theCell.getX(), theCell.getY() );

        for( int y = theOrigin.getY(); y < theOrigin.getY() + 3; y++ )
        {
            for( int x = theOrigin.getX(); x < theOrigin.getX() + 3; x++ )
            {
                if( grid[x][y] != 0 )
                {
                    vals.add( grid[x][y] );
                }
            }
        }
        return vals;
    }

    private void initMasterList()
    {
        this.MASTERLIST = new ArrayList<>();
        this.MASTERLIST.add( 1 );
        this.MASTERLIST.add( 2 );
        this.MASTERLIST.add( 3 );
        this.MASTERLIST.add( 4 );
        this.MASTERLIST.add( 5 );
        this.MASTERLIST.add( 6 );
        this.MASTERLIST.add( 7 );
        this.MASTERLIST.add( 8 );
        this.MASTERLIST.add( 9 );
    }

    private Cell findBlankCell( int index, int domain )
    {
        Cell blankCell = null;

        switch( domain )
        {
            case X_DOMAIN:
                blankCell = findCellWithValueOnDomainX( index, 0 );
                break;
            case Y_DOMAIN:
                blankCell = findCellWithValueOnDomainY( index, 0 );
                break;
            case BOX_DOMAIN:
                blankCell = findCellWithValueOnBoxDomain( index, 0 );
                break;
        }

        return blankCell;
    }

    private Cell findCellWithValueOnDomainX( int index, int value )
    {
        Cell theCell = null;
        for( int i = 0; i < 9; i++ )
        {
            if( grid[i][index] == value )
            {
                theCell = new Cell( i, index, grid[i][index] );
                break;
            }
        }
        return theCell;
    }
    
    private boolean isValueOnDomainXValid(int index, int value){
    	for(int i=0; i<9; i++){
    		if(grid[i][index]==value){
    			return false;
    		}
    	}
    	return true;
    }
    
    private boolean isValueOnDomainYValid(int index, int value){
    	for(int i=0; i<9; i++){
    		if(grid[index][i]==value){
    			return false;
    		}
    	}
    	return true;
    }

    private boolean isValueOnBoxDomainValid(int currX, int currY, int value){
    	Cell theOrigin = findBoardCellOriginCoordinateWhereTheCellBelongs( currX, currY );

        for( int y = theOrigin.getY(); y < theOrigin.getY() + 3; y++ )
        {
            for( int x = theOrigin.getX(); x < theOrigin.getX() + 3; x++ )
            {
                if( grid[x][y]==value ){
                	return false;
                }
                
            }
        }
        return true;
    }
    
    private Cell findCellWithValueOnDomainY( int index, int value )
    {
        Cell theCell = null;
        for( int i = 0; i < 9; i++ )
        {
            if( grid[index][i] == value )
            {
                theCell = new Cell( index, i, grid[index][i] );
                break;
            }
        }
        return theCell;
    }

    private Cell findCellWithValueOnBoxDomain( int index, int value )
    {
        Cell theOrigin = null;
        Cell theCell = null;
        switch( index )
        {
            case 0:
                theOrigin = this.BOX0_CELL_ORIGIN;
                break;
            case 1:
                theOrigin = this.BOX1_CELL_ORIGIN;
                break;
            case 2:
                theOrigin = this.BOX2_CELL_ORIGIN;
                break;
            case 3:
                theOrigin = this.BOX3_CELL_ORIGIN;
                break;
            case 4:
                theOrigin = this.BOX4_CELL_ORIGIN;
                break;
            case 5:
                theOrigin = this.BOX5_CELL_ORIGIN;
                break;
            case 6:
                theOrigin = this.BOX6_CELL_ORIGIN;
                break;
            case 7:
                theOrigin = this.BOX7_CELL_ORIGIN;
                break;
            case 8:
                theOrigin = this.BOX8_CELL_ORIGIN;
                break;
        }

        for( int y = theOrigin.getY(); y < theOrigin.getY() + 3; y++ )
        {
            for( int x = theOrigin.getX(); x < theOrigin.getX() + 3; x++ )
            {
                if( grid[x][y] == value )
                {
                    theCell = new Cell( x, y, grid[x][y] );
                    break;
                }
            }
        }

        return theCell;
    }

    private void countNumberOfExistingValueOnXY( int[] row, int[] column, int value )
    {
        //System.out.println( "Number of Blanks:" );
        for( int i = 0; i < 9; i++ )
        {
            row[i] = countNumberOfExistingValueOnLine( i, value, X_AXIS );
            column[i] = countNumberOfExistingValueOnLine( i, value, Y_AXIS );
            //System.out.println( "row " + i + ":" + row[i] + "\t" + "column " + i + ":" + column[i] );
        }
    }

    private void countNumberOfExistingValueOnBoxes( int[] box, int value )
    {
        box[0] = countNumberOfExistingValueOnBox( BOX0_CELL_ORIGIN, value );
        box[1] = countNumberOfExistingValueOnBox( BOX1_CELL_ORIGIN, value );
        box[2] = countNumberOfExistingValueOnBox( BOX2_CELL_ORIGIN, value );
        box[3] = countNumberOfExistingValueOnBox( BOX3_CELL_ORIGIN, value );
        box[4] = countNumberOfExistingValueOnBox( BOX4_CELL_ORIGIN, value );
        box[5] = countNumberOfExistingValueOnBox( BOX5_CELL_ORIGIN, value );
        box[6] = countNumberOfExistingValueOnBox( BOX6_CELL_ORIGIN, value );
        box[7] = countNumberOfExistingValueOnBox( BOX7_CELL_ORIGIN, value );
        box[8] = countNumberOfExistingValueOnBox( BOX8_CELL_ORIGIN, value );

        for( int i = 0; i < 9; i++ )
        {
            //System.out.println( "box " + i + ": " + box[i] );
        }
    }

    private int countNumberOfExistingValueOnBox( Cell origin, int value )
    {
        int count = 0;
        for( int y = origin.getY(); y < origin.getY() + 3; y++ )
        {
            for( int x = origin.getX(); x < origin.getX() + 3; x++ )
            {
                count += ( grid[x][y] == value ) ? 1 : 0;
            }
        }
        return count;
    }

    private int countNumberOfExistingValueOnLine(int origin, int value, int axis){
        int count = 0;
        for(int i=0; i<9; i++){
           switch(axis){
                case X_AXIS:
                    count += ( grid[i][origin] == value ) ? 1 : 0;
                            break;
               case Y_AXIS: count+= (grid[origin][i]==value)?1:0;
                            break;
           }
        }
        return count;
    }

    private int findBoardOriginAxisWhereTheCellBelongs( int axis )
    {
        int axisOrigin = 0;
        switch( axis )
        {
            case 0:
            case 1:
            case 2:
                axisOrigin = 0;
                break;
            case 3:
            case 4:
            case 5:
                axisOrigin = 3;
                break;
            case 6:
            case 7:
            case 8:
                axisOrigin = 6;

        }
        return axisOrigin;
    }

    private Cell findBoardCellOriginCoordinateWhereTheCellBelongs( int x, int y )
    {
        return new Cell( findBoardOriginAxisWhereTheCellBelongs( x ), findBoardOriginAxisWhereTheCellBelongs( y ) );
    }

    private void initGrid( File inputFile ) throws IOException
    {
        BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( inputFile ) ) );

        int c;
        int x = 0;
        int y = 0;
        while( ( c = reader.read() ) > -1 )
        {
            //System.out.println( c );
            if( !isValidInput( c ) )
                continue;

            if( x == 9 )
            {
                x = 0;
                y++;
            }

            if( y >= 9 )
            {
                break;
            }
            if( c == ' ' )
                continue;
            grid[x][y] = ( c != '-' ) ? c - 48 : 0;
            x++;
        }

        reader.close();
    }

    private boolean isValidInput( int c )
    {
        return ( ( c > 47 ) && ( c < 58 ) ) || ( c == '-' );
    }

    private void printGrid( int[][] grid )
    {
        for(int y=0;y<9;y++){

            for(int x=0; x<9; x++){
                if( ( x % 3 ) == 0 )
                    System.out.print( " " );


                System.out.print( grid[x][y] );
            }
            if( ( ( y + 1 ) % 3 ) == 0 )
                System.out.println();

            System.out.println();

        }
    }

    private void copyGrid( int[][] destGrid, int[][] srcGrid )
    {
        for( int i = 0; i < srcGrid.length; i++ )
            for( int j = 0; j < srcGrid[i].length; j++ )
                destGrid[i][j] = srcGrid[i][j];
    }

    public static void main(String[] args){

        if( args.length > 0 )
        {
            new Main( new File( args[0] ) );
        }
        else
        {
            new Main( new File( "asian.txt" ) );
        }

    }

}

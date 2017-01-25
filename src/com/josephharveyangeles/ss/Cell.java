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

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * TODO:Write class description
 * @author <a HREF="mailto:yourMail@nsn.com">Your Name</a>
 *
 */
public class Cell
{

    /**
     * 
     */
    private int x;

    private int y;

    private int value;

    public Cell()
    {

    }

    public Cell( int x, int y )
    {
        this.x = x;
        this.y = y;
    }

    public Cell( int x, int y, int value )
    {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public Cell( Cell cell )
    {
        this.x = cell.x;
        this.y = cell.y;
        this.value = cell.value;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getValue()
    {
        return this.value;
    }

    public void setValue( int value )
    {
        this.value = value;
    }

    public static void test( String[] args )
    {
        ArrayList<Integer> listA = new ArrayList<>();
        ArrayList<Integer> listB = new ArrayList<>();
        ArrayList<Integer> listC = new ArrayList<>();

        listA.add( 0 );
        listA.add( 1 );
        listA.add( 2 );
        listA.add( 3 );
        listA.add( 4 );

        listB.add( 0 );
        listB.add( 2 );
        listB.add( 4 );
        listB.add( 7 );
        listB.add( 9 );

        listC.add( 1 );
        listC.add( 0 );
        listC.add( 3 );
        listC.add( 5 );
        listC.add( 6 );

        System.out.println( "listA:" + listA );
        System.out.println( "listB:" + listB );
        System.out.println( "listC:" + listC );

        ArrayList<Integer> listD = new ArrayList<>();
        listD.add( 9 );
        listD.add( 1 );
        listD.add( 2 );
        listD.add( 3 );
        listD.add( 4 );
        listD.add( 5 );
        listD.add( 6 );
        listD.add( 7 );
        listD.add( 8 );

        listD.removeAll( listA );
        listD.removeAll( listB );
        listD.removeAll( listC );
        System.out.println( "Holy Grail:" + listD );
        ArrayList<Integer> listE = new ArrayList<>( listD );
        listE.add( 100 );
        System.out.println( listD );
        System.out.println( listE );
        ArrayList<Integer> listF = new ArrayList<>();
        System.out.println( listD.isEmpty() );
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        dq.push( 2 );
        dq.push( 3 );
        System.out.println( dq.pop() );
        System.out.println( dq.pop() );

        for( int i = 0; i < args.length; i++ )
        {
            System.out.println( i );
        }
    }

}

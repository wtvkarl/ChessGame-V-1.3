/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krlv.source.chessenginev1_3.moves;

import com.krlv.source.chessenginev1_3.board.Square;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author 3095515
 */
public class MoveHandler {
    
    public static Move getMove(Square start, Square target, ArrayList<Move> validMoves){
        for(Move move : validMoves){
            if(move.getStartSquare() == start && move.getTargetSquare() == target)
                return move;
        }
        return null;
    }
    
    public static Color getColorBasedOnMove(Move move){
        switch(move.type){
            case REGULAR : return Color.yellow;
            case CAPTURE : return Color.RED;
            case PROMOTION : return Color.green;
            case ENPASSANT : return Color.pink;
            case CASTLING : return Color.yellow;
            default : return Color.lightGray;
        }
    }
    
    public static boolean isValid(Move move){
        if(move.getStartSquare() == move.getTargetSquare())
            return false;
       
        return true;
    }
}

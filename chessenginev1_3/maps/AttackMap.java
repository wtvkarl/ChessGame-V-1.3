/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krlv.source.chessenginev1_3.maps;

import com.krlv.source.chessenginev1_3.board.Square;
import java.util.ArrayList;

/**
 *
 * @author 3095515
 */
public class AttackMap {
    
    public static ArrayList<Square> getAttackSquares(Square[] board, String pieceColor){
        long start = System.nanoTime();
        ArrayList<Square> attackSquares = new ArrayList();
        
        for(Square square : board){
            if(!square.getPiece().isBlank() && square.getPiece().getColor().equals(pieceColor)){
                attackSquares.addAll(square.getPiece().getAttackSquares(board));
            }
        }
        return attackSquares;
    }
    
    public static ArrayList<Square> getSlidingPieceAttackSquares(Square[] board, String pieceColor){
        ArrayList<Square> attackSquares = new ArrayList();
        
        for(Square square : board){
            if(!square.getPiece().isBlank() && square.getPiece().getColor().equals(pieceColor)){
                attackSquares.addAll(square.getPiece().getSlidingAttackSquares(board));
            }
        }
        
        return attackSquares;
    }
    
}

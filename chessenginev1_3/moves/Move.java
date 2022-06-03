/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.moves;

import com.krlv.source.chessenginev1_3.board.Piece;
import com.krlv.source.chessenginev1_3.board.Square;

/**
 *
 * @author 523ka
 */
public abstract class Move {
    
    public int startIndex, targetIndex;
    public Square startSquare, targetSquare;
    public Piece startPiece, targetPiece;
    public MoveType type;
    public Square[] board;
    
    public abstract void execute();
    public abstract void undo();
    public MoveType getMoveType(){
        return type;
    }
    
    public Square getStartSquare(){
        return startSquare;
    }
    
    public Square getTargetSquare(){
        return targetSquare;
    }
    
    public Piece getStartPiece(){
        return startPiece;
    }
    
    public Piece getTargetPiece(){
        return targetPiece;
    }
    
    public int getStartIndex(){
        return startIndex;
    }
    
    public int getTargetIndex(){
        return targetIndex;
    }
    
    @Override
    public String toString(){
        return String.format("%s @ %s to %s", startSquare.getPiece(), startSquare.getCoordinate(), targetSquare.getCoordinate());
    }
}

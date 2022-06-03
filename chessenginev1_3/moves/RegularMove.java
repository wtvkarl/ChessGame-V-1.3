/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.moves;

import com.krlv.source.chessenginev1_3.board.Blank;
import com.krlv.source.chessenginev1_3.board.Square;

/**
 *
 * @author 523ka
 */
public class RegularMove extends Move{
    
    public RegularMove(Square start, Square target, Square[] board){
        this.type = MoveType.REGULAR;
        this.startSquare = start;
        this.targetSquare = target;
        this.startIndex = start.getIndex();
        this.targetIndex = target.getIndex();
        this.startPiece = start.getPiece();
        this.targetPiece = target.getPiece(); //target piece is blank
        this.board = board;
    }

    @Override
    public void execute() {
        board[targetIndex].setPiece(startPiece);
        startPiece.setIndex(targetIndex);
        startPiece.timesMoved++;
        int rows = Math.abs(targetSquare.getRow() - startSquare.getRow());
        int cols = Math.abs(targetSquare.getCol() - startSquare.getCol());
        startPiece.squaresMoved = Math.max(rows, cols);
        board[startIndex].setPiece(new Blank(startIndex));
        //# of pieces stay the same, no need to update piece lists
    }

    @Override
    public void undo() {
        board[targetIndex].setPiece(new Blank(targetIndex));
        board[startIndex].setPiece(startPiece);
        startPiece.timesMoved--;
        startPiece.setIndex(startIndex);
    }
    
}

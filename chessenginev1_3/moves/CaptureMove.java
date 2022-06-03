/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krlv.source.chessenginev1_3.moves;

import com.krlv.source.chessenginev1_3.board.Blank;
import com.krlv.source.chessenginev1_3.board.ChessBoard;
import com.krlv.source.chessenginev1_3.board.Piece;
import com.krlv.source.chessenginev1_3.board.Square;

/**
 *
 * @author 3095515
 */
public class CaptureMove extends Move{
    
    Piece capturedPiece;
    
    public CaptureMove(Square start, Square target, Square[] board){
        this.type = MoveType.CAPTURE;
        this.startSquare = start;
        this.targetSquare = target;
        this.startIndex = start.getIndex();
        this.targetIndex = target.getIndex();
        this.startPiece = start.getPiece();
        this.targetPiece = target.getPiece(); //target piece is blank
        this.board = board;
        
        capturedPiece = this.targetPiece;
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
    }

    @Override
    public void undo() {
        board[targetIndex].setPiece(capturedPiece);
        board[startIndex].setPiece(startPiece);
        startPiece.timesMoved--;
        startPiece.setIndex(startIndex);
    }
    
}

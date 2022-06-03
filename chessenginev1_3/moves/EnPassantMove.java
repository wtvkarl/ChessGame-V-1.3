/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.moves;

import com.krlv.source.chessenginev1_3.board.Blank;
import com.krlv.source.chessenginev1_3.board.ChessBoard;
import com.krlv.source.chessenginev1_3.board.Piece;
import com.krlv.source.chessenginev1_3.board.Square;

/**
 *
 * @author 523ka
 */
public class EnPassantMove extends Move {
    
    Piece capturedPiece;
    
    public EnPassantMove(Square start, Square target, Square[] board){
        this.type = MoveType.ENPASSANT;
        this.startSquare = start;
        this.targetSquare = target;
        this.startIndex = start.getIndex();
        this.targetIndex = target.getIndex();
        this.startPiece = start.getPiece();
        this.targetPiece = target.getPiece(); //target piece is blank
        this.board = board;
    }

    @Override
    public void execute(){
        board[targetIndex].setPiece(startPiece);
        startPiece.setIndex(targetIndex);
        startPiece.timesMoved++;
        int rows = Math.abs(targetSquare.getRow() - startSquare.getRow());
        int cols = Math.abs(targetSquare.getCol() - startSquare.getCol());
        startPiece.squaresMoved = Math.max(rows, cols);
        //reversed since we want to get the square adjacent to start piece
        int direction = startPiece.isWhite() ? 1 : -1;
        int target = targetIndex - (8 * direction);
        board[startIndex].setPiece(new Blank(startIndex));
        capturedPiece = board[target].getPiece();
        board[target].setPiece(new Blank(target));
    }

    @Override
    public void undo() {
        board[startIndex].setPiece(startPiece);
        board[targetIndex].setPiece(new Blank(targetIndex));
        int direction = startPiece.isWhite() ? 1 : -1;
        int target = targetIndex - (8 * direction);
        board[target].setPiece(capturedPiece);
        startPiece.timesMoved--;
        startPiece.setIndex(startIndex);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krlv.source.chessenginev1_3.moves;

import com.krlv.source.chessenginev1_3.board.Blank;
import com.krlv.source.chessenginev1_3.board.Piece;
import com.krlv.source.chessenginev1_3.board.Square;

/**
 *
 * @author 3095515
 */
public class CastlingMove extends Move{

    private Square newKingSquare, newRookSquare, kingSquare, rookSquare;
    private int newKingIndex, newRookIndex;
    private Piece king, rook;
    
    public CastlingMove(Square start, Square target, Square[] board, String side){
        this.type = MoveType.CASTLING;
        kingSquare = start;
        rookSquare = target;
        king = start.getPiece();
        rook = target.getPiece();
        newKingIndex = (side.equals("kingside")) ? king.getIndex() + 2 : king.getIndex() - 2;
        newRookIndex = (side.equals("kingside")) ? newKingIndex - 1 : newKingIndex + 1;
        newKingSquare = board[newKingIndex];
        newRookSquare = board[newRookIndex];
        this.startSquare = start;
        this.targetSquare = newKingSquare;
        this.startPiece = king;
        this.targetPiece = rook;
        this.board = board;
    }

    @Override
    public void execute() {
        kingSquare.setPiece(new Blank(kingSquare.getIndex()));
        rookSquare.setPiece(new Blank(rookSquare.getIndex()));
        newKingSquare.setPiece(king);
        newRookSquare.setPiece(rook);
        rook.timesMoved++;
        king.timesMoved++;
        king.setIndex(newKingIndex);
        rook.setIndex(newRookIndex);
        //# of pieces stay the same, no need to update piece lists
    }
    
    @Override
    public void undo() {
        kingSquare.setPiece(king);
        rookSquare.setPiece(rook);
        king.setIndex(kingSquare.getIndex());
        rook.setIndex(rookSquare.getIndex());
        newKingSquare.setPiece(new Blank(newKingIndex));
        newRookSquare.setPiece(new Blank(newRookIndex));
        king.timesMoved--;
        rook.timesMoved--;
    }
    
    @Override
    public Square getTargetSquare(){
        return newKingSquare;
    }
    
}

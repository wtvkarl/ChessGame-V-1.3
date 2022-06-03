/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krlv.source.chessenginev1_3.moves;

import com.krlv.source.chessenginev1_3.board.Bishop;
import com.krlv.source.chessenginev1_3.board.Blank;
import com.krlv.source.chessenginev1_3.board.ChessBoard;
import com.krlv.source.chessenginev1_3.board.Knight;
import com.krlv.source.chessenginev1_3.board.Piece;
import com.krlv.source.chessenginev1_3.board.Queen;
import com.krlv.source.chessenginev1_3.board.Rook;
import com.krlv.source.chessenginev1_3.board.Square;
import com.krlv.source.chessenginev1_3.gfx.ImageLoader;
import com.krlv.source.chessenginev1_3.gfx.UI;

/**
 *
 * @author 3095515
 */
public class PromotionMove extends Move{
    
    Piece capturedPiece; //applies for blank pieces as well
    
    public PromotionMove(Square start, Square target, Square[] board){
        this.type = MoveType.PROMOTION;
        this.startSquare = start;
        this.targetSquare = target;
        this.startPiece = start.getPiece();
        this.targetPiece = target.getPiece();
        capturedPiece = this.targetPiece;
        
        this.startIndex = start.getIndex();
        this.targetIndex = target.getIndex();
        this.board = board;
    }
    
    @Override
    public void execute() {
        promote();
        startPiece.setIndex(targetIndex);
        startPiece.timesMoved++;
        int rows = Math.abs(targetSquare.getRow() - startSquare.getRow());
        int cols = Math.abs(targetSquare.getCol() - startSquare.getCol());
        startPiece.squaresMoved = Math.max(rows, cols);
        board[startIndex].setPiece(new Blank(startIndex));
    }
    
    private void promote(){
        Piece promotedPiece = null;
        
        UI.setPromotionButtonsVisible(true);
        String selection = "";
        do{
            selection = UI.getPromotionSelection();
            System.out.print("");  //somehow promotion ONLY works when this is here, will investigate later
        }while(selection.equals(""));
        
        
        switch(selection){
            case "queen": promotedPiece = new Queen(targetIndex, startPiece.getColor()); break;
            case "rook" : promotedPiece = new Rook(targetIndex, startPiece.getColor()); break;
            case "bishop" : promotedPiece = new Bishop(targetIndex, startPiece.getColor()); break;
            case "knight" : promotedPiece = new Knight(targetIndex, startPiece.getColor()); break;
        }
        
        UI.setPromotionButtonsVisible(false);
        
        board[targetIndex].setPiece(promotedPiece);
        promotedPiece.setImage(ImageLoader.getImage(promotedPiece));
    }

    @Override
    public void undo() {
        board[targetIndex].setPiece(capturedPiece);
        capturedPiece.setIndex(targetIndex);
        board[startIndex].setPiece(startPiece);
        startPiece.setIndex(startIndex);
        startPiece.timesMoved--;
    }
    
}

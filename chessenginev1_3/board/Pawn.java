/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.board;

import com.krlv.source.chessenginev1_3.maps.KingCheckMap;
import com.krlv.source.chessenginev1_3.moves.CaptureMove;
import com.krlv.source.chessenginev1_3.moves.EnPassantMove;
import com.krlv.source.chessenginev1_3.moves.Move;
import com.krlv.source.chessenginev1_3.moves.MoveType;
import com.krlv.source.chessenginev1_3.moves.RegularMove;
import com.krlv.source.chessenginev1_3.moves.PromotionMove;
import java.util.ArrayList;

/**
 *
 * @author 523ka
 */
public class Pawn extends Piece {
    
    public Pawn(int boardIndex, String color) {
        this.index = boardIndex;
        this.row = index / 8;
        this.col = index % 8;
        this.name = "pawn";
        this.value = 100;
        this.color = color;
        this.type = 0;
        this.fenNotation = (isWhite()) ? "P" : "p";
        this.isSlidingPiece = false;
        this.validMoves = new ArrayList();
    }
    
    @Override
    public ArrayList<Move> getValidMoves(Square[] board) {
        ArrayList<Move> validMoves = new ArrayList();
        Square startSquare = board[this.index];
        
        int spaces = (this.timesMoved == 0) ? 2 : 1;
        int direction = (isWhite()) ? 1: -1;
        
        for(int i = 1; i < spaces+1 ; i++){
            int targetRowIndex = this.index + (direction * 8 * i);
            if(isInBoard(targetRowIndex)){
                Square target = board[targetRowIndex];
                if(target.isBlank()){
                    if(isAboutToPromote())
                        validMoves.add(new PromotionMove(startSquare, target, board));
                    else
                        validMoves.add(new RegularMove(startSquare, target, board));
                }
                else
                    break;
            }
        }
        
        validMoves.addAll(getCaptureMoves(board));
        
        this.validMoves = validMoves;
        this.validMoves = filterPinnedMoves(board);
        return this.validMoves;        
    }
    
    public ArrayList<Move> getCaptureMoves(Square[] board){
        ArrayList<Move> captures = new ArrayList();
        Square start = board[this.index];
        
        int direction = (isWhite()) ? 1 : -1;
        int leftCaptureIndex = this.index + (7 * direction);
        int rightCaptureIndex = this.index + (9 * direction);
        Square leftDiagonal = null, rightDiagonal = null;
        
        if(isInBoard(leftCaptureIndex))
            leftDiagonal = board[leftCaptureIndex];
        if(isInBoard(rightCaptureIndex))
            rightDiagonal = board[rightCaptureIndex];
        
        Square enPassantSquare = ChessBoard.getSquare(FenInterpreter.getEnPassantSquare());
        if(leftDiagonal != null){
            if(Math.abs(leftDiagonal.getRow() - this.row) == 1){
                if(!leftDiagonal.isBlank()){
                    if(!leftDiagonal.getPiece().isFriendly(this)){
                        if(isAboutToPromote())
                            captures.add(new PromotionMove(start, leftDiagonal, board));
                        else
                            captures.add(new CaptureMove(start, leftDiagonal, board));
                    }
                }
                else if(enPassantSquare != null){
                    Piece enPassantPiece = board[enPassantSquare.getIndex() + (8 * direction)].getPiece();
                    if(leftDiagonal == enPassantSquare && !enPassantPiece.isFriendly(this)){
                        captures.add(new EnPassantMove(start, leftDiagonal, board));
                    }
                }
            }
        }
        if(rightDiagonal != null){
            if(Math.abs(rightDiagonal.getRow() - this.row) == 1){
                if(!rightDiagonal.isBlank()){
                    if(!rightDiagonal.getPiece().isFriendly(this)){
                        if(isAboutToPromote())
                            captures.add(new PromotionMove(start, rightDiagonal, board));
                        else
                            captures.add(new CaptureMove(start, rightDiagonal, board));
                    }
                }
                else if(enPassantSquare != null){
                    Piece enPassantPiece = board[enPassantSquare.getIndex() + (8 * direction)].getPiece();
                    if(rightDiagonal == enPassantSquare && !enPassantPiece.isFriendly(this)){
                        captures.add(new EnPassantMove(start, rightDiagonal, board));
                    }
                }
            }
        }
               
        return captures;
    }
    
    @Override
    public ArrayList<Square> getAttackSquares(Square[] board){
        ArrayList<Square> attackSquares = new ArrayList();
        
        int direction = (isWhite()) ? 1 : -1;
        int leftCaptureIndex = this.index + (7 * direction);
        int rightCaptureIndex = this.index + (9 * direction);
        Square leftDiagonal = null, rightDiagonal = null;
        
        if(isInBoard(leftCaptureIndex))
            leftDiagonal = board[leftCaptureIndex];
        if(isInBoard(rightCaptureIndex))
            rightDiagonal = board[rightCaptureIndex];
        
        if(leftDiagonal != null){
            if(Math.abs(leftDiagonal.getRow() - this.row) == 1){
                attackSquares.add(leftDiagonal);
            }
        }
        if(rightDiagonal != null){
            if(Math.abs(rightDiagonal.getRow() - this.row) == 1){
                attackSquares.add(rightDiagonal);
            }
        }
               
        return attackSquares;
    }
    
    private boolean isAboutToPromote(){
        return this.isWhite() ? (this.getRow() == 6) : (this.getRow() == 1);
    }
    
    @Override
    public ArrayList<Move> getValidCheckedMoves(Piece king, Square[] board) {
        if(!KingCheckMap.kingIsInCheck(king, board))
            return getValidMoves(board);
        
        ArrayList<Move> pseudoValidMoves = getValidMoves(board);
        ArrayList<Move> validMoves = new ArrayList();
        ArrayList<Piece> attackers = KingCheckMap.getAttackers(king, board);
        
        //if there is more than one attacker, we cannot capture of block a check, only the king can move.
        if(attackers.size() > 1)
            return new ArrayList();
        
        Piece attacker = attackers.get(0); //assuming there is only one attacker
        ArrayList<Square> squaresToBlock = KingCheckMap.getLinesOfSight(king, attacker, board);
        
        for(Move move : pseudoValidMoves){
            Square targetSquare = move.getTargetSquare();
            if(squaresToBlock.contains(targetSquare) || targetSquare == attacker.getSquare()){
                validMoves.add(move);
            }
        } 
        
        return validMoves;
    }
    
    @Override
    public ArrayList<Move> filterPinnedMoves(Square[] board) {
        ArrayList<Move> validPinnedMoves = new ArrayList();
        Piece king = (isWhite()) ? ChessBoard.getWhiteKing() : ChessBoard.getBlackKing();
        for(Move move : validMoves){
            //so we dont have to show the UI for this check, it messes everything up.
            if(move.getMoveType() == MoveType.PROMOTION){
                CaptureMove promotionPinCheck = new CaptureMove(move.getStartSquare(), move.getTargetSquare(), board);
                promotionPinCheck.execute();
                if(!KingCheckMap.kingIsInCheck(king, board))
                    validPinnedMoves.add(move);
                promotionPinCheck.undo();
            }
            else{
                move.execute();
                if(!KingCheckMap.kingIsInCheck(king, board)){
                    validPinnedMoves.add(move);
                }
                move.undo();
            }
        }
        
        return validPinnedMoves;
    }
    
    //since pawns only attack diagonally one square, there is no "blocking" possible with a checked king
    @Override
    public ArrayList<Square> getKingAttackPath(Piece king, Square[] board) {return new ArrayList();}
    @Override 
    public ArrayList<Square> getSlidingAttackSquares(Square[] board){return new ArrayList();}
    @Override
    public String getSide(){return "";}
}

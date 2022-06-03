/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.board;

import com.krlv.source.chessenginev1_3.maps.KingCheckMap;
import com.krlv.source.chessenginev1_3.moves.CaptureMove;
import com.krlv.source.chessenginev1_3.moves.Move;
import com.krlv.source.chessenginev1_3.moves.RegularMove;
import java.util.ArrayList;

/**
 *
 * @author 523ka
 */
public class Knight extends Piece {
    
    private static final int[] jumpIndecies = {-15, -6, 10, 17, 15, 6, -10, -17};
    
    public Knight(int boardIndex, String color) {
        this.index = boardIndex;
        this.row = index / 8;
        this.col = index % 8;
        this.name = "knight";
        this.value = 300;
        this.color = color;
        this.type = 1;
        this.fenNotation = (isWhite()) ? "N" : "n";
        this.isSlidingPiece = false;
        this.validMoves = new ArrayList();
    }
    
    @Override
    public ArrayList<Move> getValidMoves(Square[] board) {
        ArrayList<Move> validMoves = new ArrayList();
        for(Integer in : jumpIndecies){
            int pseudoIndex = getIndex() + in;
            if(isInBoard(pseudoIndex)){
                Square start = board[getIndex()];
                Square target = board[pseudoIndex];
                if(Math.abs(target.getCol() - start.getCol()) <= 2){
                    if(target.isBlank()){
                        validMoves.add(new RegularMove(start, target, board));
                    }
                    else{
                        if(!target.getPiece().isFriendly(this))
                            validMoves.add(new CaptureMove(start, target, board));
                    }
                }
            }
        }
        
        this.validMoves = validMoves;
        this.validMoves = filterPinnedMoves(board);
        return this.validMoves;        
    }
    
    @Override
    public ArrayList<Square> getAttackSquares(Square[] board){
        ArrayList<Square> attackSquares = new ArrayList();
        
        for(Integer in : jumpIndecies){
            int pseudoIndex = getIndex() + in;
            if(isInBoard(pseudoIndex)){
                Square start = board[getIndex()];
                Square target = board[pseudoIndex];
                if(Math.abs(target.getCol() - start.getCol()) <= 2){
                    attackSquares.add(target);
                }
            }
        }
        
        return attackSquares;
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
            move.execute();
            if(!KingCheckMap.kingIsInCheck(king, board)){
                validPinnedMoves.add(move);
            }
            move.undo();
        }
        
        return validPinnedMoves;
    }
    
    //since knights can jump, there is no "blocking" possible with a checked king
    @Override
    public ArrayList<Square> getKingAttackPath(Piece king, Square[] board) {return new ArrayList();}
    @Override 
    public ArrayList<Square> getSlidingAttackSquares(Square[] board){return new ArrayList();}
    @Override
    public String getSide(){return "";}
    
}

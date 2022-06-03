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
public class Queen extends Piece {
    
    private final int[] directionOffsets;
    private final int directionOffsetStartIndex = 0;

    public Queen(int boardIndex, String color) {
        this.index = boardIndex;
        this.row = index / 8;
        this.col = index % 8;
        this.name = "queen";
        this.value = 900;
        this.color = color;
        this.type = 4;
        this.fenNotation = (isWhite()) ? "Q" : "q";
        this.isSlidingPiece = true;
        this.validMoves = new ArrayList();
        directionOffsets = ChessBoard.getDirectionOffsets();
    }
    
    @Override
    public ArrayList<Move> getValidMoves(Square[] board) {
        ArrayList<Move> validMoves = new ArrayList();
        
        Square start = board[this.getIndex()];
        
        for(int i = directionOffsetStartIndex; i < directionOffsets.length; i++){
            int distToEdge = ChessBoard.getDistanceToEdge(this.getIndex(), i);
            int offset = directionOffsets[i];
            for(int j = 1; j < distToEdge + 1; j++){
                int targetIndex = this.getIndex() + (j * offset);
                Square targetSquare = board[targetIndex];
                if(targetSquare.isBlank()){
                    validMoves.add(new RegularMove(start, targetSquare, board));
                }
                else{
                    if(!targetSquare.getPiece().isFriendly(this))
                        validMoves.add(new CaptureMove(start, targetSquare, board));
                    break;
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
        
        for(int i = directionOffsetStartIndex; i < directionOffsets.length; i++){
            int distToEdge = ChessBoard.getDistanceToEdge(this.getIndex(), i);
            int offset = directionOffsets[i];
            for(int j = 1; j < distToEdge + 1; j++){
                int targetIndex = this.getIndex() + (j * offset);
                Square targetSquare = board[targetIndex];
                attackSquares.add(targetSquare);
                if(!targetSquare.isBlank())
                    break;
            }
        } 
        
        return attackSquares;
    }
    
    @Override 
    public ArrayList<Square> getSlidingAttackSquares(Square[] board){
        ArrayList<Square> attackSquares = new ArrayList();
        
        for(int i = directionOffsetStartIndex; i < directionOffsets.length; i++){
            int distToEdge = ChessBoard.getDistanceToEdge(this.getIndex(), i);
            int offset = directionOffsets[i];
            boolean encounteredKing = false;
            for(int j = 1; j < distToEdge + 1; j++){
                int targetIndex = this.getIndex() + (j * offset);
                Square targetSquare = board[targetIndex];
                Piece target = targetSquare.getPiece();
                
                if(encounteredKing){
                    if(targetSquare.isBlank()){
                        attackSquares.add(targetSquare);
                    }
                    //because the enemy king can only be in one diagonal, dont bother checking the others.
                    else{
                        return attackSquares;
                    }
                }
                else{
                    //if the square is blank, we continue but DON'T add to the arraylist
                    //this is because we already kept track of that square in the regular attack square map

                    if(targetSquare.isBlank()){
                        continue;
                    }
                    else{
                        if(target.isKing() && !target.isFriendly(this)){
                            encounteredKing = true;
                            continue;
                        }
                        //if its anything but the enemy king, we already cannot attack king, so break.
                        else{
                            break;
                        }
                    }
                }
            }
        } 
        
        return attackSquares;
    }
    
    @Override
    public ArrayList<Square> getKingAttackPath(Piece king, Square[] board) {
        ArrayList<Square> path = new ArrayList();
        boolean kingFound = false;
        
        for(int i = directionOffsetStartIndex; i < directionOffsets.length; i++){
            path.clear();
            int distToEdge = ChessBoard.getDistanceToEdge(this.getIndex(), i);
            int offset = directionOffsets[i];
            for(int j = 1; j < distToEdge + 1; j++){
                int targetIndex = this.getIndex() + (j * offset);
                Square targetSquare = board[targetIndex];
                if(targetSquare.getPiece() == king){
                    kingFound = true;
                    break;
                }
                else{
                    if(targetSquare.isBlank()){
                        path.add(targetSquare);
                    }
                    else
                        break;
                }
            }
            
            if(kingFound)
                break;
        } 
        
        return path;
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

    @Override
    public String getSide(){return "";}
}

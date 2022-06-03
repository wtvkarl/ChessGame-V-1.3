/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krlv.source.chessenginev1_3.maps;

import com.krlv.source.chessenginev1_3.board.ChessBoard;
import com.krlv.source.chessenginev1_3.board.Piece;
import com.krlv.source.chessenginev1_3.board.Square;
import com.krlv.source.chessenginev1_3.moves.Move;
import java.util.ArrayList;

/**
 *
 * @author 3095515
 */
public class KingCheckMap {
    
    public static boolean kingIsInCheck(Piece king, Square[] board){
        String enemyColor = king.isWhite() ? "b" : "w";
        ArrayList<Square> enemySquares = AttackMap.getAttackSquares(board, enemyColor);
        return enemySquares.contains(king.getSquare());
    }
    
    public static ArrayList<Piece> getAttackers(Piece king, Square[] board){
        ArrayList<Piece> attackers = new ArrayList();
        ArrayList<Piece> enemyPieces = (king.isWhite()) ? ChessBoard.getBlackPieces() : ChessBoard.getWhitePieces();
        
        for(Piece enemyPiece : enemyPieces){
            if(!enemyPiece.isKing()){ //this is to avoid a stack overflow error
                 ArrayList<Move> enemyMoves = enemyPiece.getValidMoves(board);
                 for(Move move : enemyMoves){
                     if(move.getTargetSquare() == king.getSquare())
                         attackers.add(enemyPiece);
                 }
            }     
        }
        
        
        return attackers;
    }
    
    public static ArrayList<Square> getLinesOfSight(Piece king, Piece attacker, Square[] board){
        return attacker.getKingAttackPath(king, board);
    }
    
    public static ArrayList<Move> getAllCheckedMoves(String color){
        //this method returns ALL moves that can be made when a king is in check, used for determining checkmate.

        Piece king = color.equalsIgnoreCase("w") ? ChessBoard.getWhiteKing() : ChessBoard.getBlackKing();
        if(!kingIsInCheck(king, ChessBoard.getBoard()))
            return new ArrayList();
        
        ArrayList<Move> validCheckMoves = new ArrayList();
        ArrayList<Piece> pieces = (color.equalsIgnoreCase("w") ? ChessBoard.getWhitePieces() : ChessBoard.getBlackPieces());
        
        for(Piece piece : pieces){
            validCheckMoves.addAll(piece.getValidCheckedMoves(king, ChessBoard.getBoard()));
        }
        
        return validCheckMoves;
    }
}

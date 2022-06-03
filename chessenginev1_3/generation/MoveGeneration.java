/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krlv.source.chessenginev1_3.generation;

import com.krlv.source.chessenginev1_3.board.ChessBoard;
import com.krlv.source.chessenginev1_3.board.Piece;
import com.krlv.source.chessenginev1_3.maps.KingCheckMap;
import com.krlv.source.chessenginev1_3.moves.Move;
import java.util.ArrayList;

/**
 *
 * @author 3095515
 */
public class MoveGeneration {
    
    //color must be lowercase since that is how the color is kept track of in these 
    public static int generatePositions(int depth){
        
        if(depth == 0)
            return 0;
        
        int positions = 0;
        
        ArrayList<Piece> whitePieces = ChessBoard.getWhitePieces();
        ArrayList<Piece> blackPieces = ChessBoard.getBlackPieces();
        
        String turnColor = "w";
        String enemyColor = "b";
        
        ArrayList<Move> generatedMoves = generateMoves(turnColor);
        for(Move move : generatedMoves){
           move.execute();
           ArrayList<Move> enemyGeneratedMoves = generateMoves(enemyColor);
           for(Move enemyMove : enemyGeneratedMoves){
               enemyMove.execute();
               positions++;
               enemyMove.undo();
           }
           move.undo();
           positions += generatePositions(depth - 1);
        }
        
        return positions;
    } 
    
    public static ArrayList<Move> generateMoves(String color){
        ArrayList<Move> allMoves = new ArrayList();
        
        ArrayList<Piece> pieces = (color.equals("w")) ? ChessBoard.getWhitePieces() : ChessBoard.getBlackPieces();
        Piece king = (color.equals("w")) ? ChessBoard.getWhiteKing() : ChessBoard.getBlackKing();
        
        for(Piece piece : pieces){
            ArrayList<Move> moves = piece.getValidCheckedMoves(king, ChessBoard.getBoard());
            for(Move move : moves){
                if(!allMoves.contains(move))
                    allMoves.add(move);
            }
        }
        
        return allMoves;
    }
    
    public static void testPositionCount(int depth){
        for(int i = 0; i < depth; i++){
            long start = System.nanoTime();
            int positions = generatePositions(i);
            System.out.println(String.format("Depth of %d : %d, calc time: %f", i, positions,
                    (System.nanoTime() - start) / 1_000_000_000.0));
        }
    }
}

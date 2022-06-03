/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.board;

import com.krlv.source.chessenginev1_3.moves.CaptureMove;
import com.krlv.source.chessenginev1_3.moves.Move;
import com.krlv.source.chessenginev1_3.moves.RegularMove;
import com.krlv.source.chessenginev1_3.maps.AttackMap;
import com.krlv.source.chessenginev1_3.maps.KingCheckMap;
import com.krlv.source.chessenginev1_3.moves.CastlingMove;
import java.util.ArrayList;

/**
 *
 * @author 523ka
 */
public class King extends Piece {

    private final int[] directionOffsets;
    private final int directionOffsetStartIndex = 0;
        
    private ArrayList<Square> enemySquares;
    private ArrayList<Square> enemySlidingPieceAttackSquares;
    
    public King(int boardIndex, String color) {
        this.index = boardIndex;
        this.row = index / 8;
        this.col = index % 8;
        this.name = "king";
        this.value = 10000;
        this.color = color;
        this.type = 5;
        this.fenNotation = (isWhite()) ? "K" : "k";
        this.isSlidingPiece = false;
        this.validMoves = new ArrayList();
        directionOffsets = ChessBoard.getDirectionOffsets();
        enemySquares = new ArrayList();
        enemySlidingPieceAttackSquares = new ArrayList();
    }
    
    @Override
    public ArrayList<Move> getValidMoves(Square[] board) {
        String enemyColor = isWhite() ? "b" : "w";
        
        ArrayList<Move> validMoves = new ArrayList();
        enemySquares = AttackMap.getAttackSquares(board, enemyColor);
        enemySlidingPieceAttackSquares = AttackMap.getSlidingPieceAttackSquares(board, enemyColor);
        
        Square start = board[this.getIndex()];
        
        for(int i = directionOffsetStartIndex; i < directionOffsets.length; i++){
            int distToEdge = ChessBoard.getDistanceToEdge(this.getIndex(), i);
            int offset = directionOffsets[i];
            for(int j = 1; j < distToEdge + 1; j++){
                int targetIndex = this.getIndex() + (j * offset);
                Square targetSquare = board[targetIndex];
                
                long k = System.nanoTime();
                
                if (enemySquares.contains(targetSquare) ||
                    enemySlidingPieceAttackSquares.contains(targetSquare))
                    break;
                
                System.out.println((System.nanoTime() - k) / 1000000000.0);
                
                if(targetSquare.isBlank()){
                    validMoves.add(new RegularMove(start, targetSquare, board));
                }
                else{
                    if(!targetSquare.getPiece().isFriendly(this))
                        validMoves.add(new CaptureMove(start, targetSquare, board));
                }
                break;
            }
        } 
        
        validMoves.addAll(getCastlingMoves(board));
        
        this.validMoves = validMoves;
        
        return validMoves;
    }
    
    private ArrayList<Move> getCastlingMoves(Square[] board){
        if(KingCheckMap.kingIsInCheck(this, board))
            return new ArrayList();
        
        ArrayList<Move> castlingMoves = new ArrayList();
        char[] castlingRights = FenInterpreter.getCastlingRights(this).toCharArray();
        
        for(char ch : castlingRights){
            //white castling possibilites
            if(Character.isUpperCase(ch)){
                //kingside castling
                if(ch == 'K'){
                    boolean canCastleKingSide = true;
                    //squares between king and rook, assuming they havent moved at all.
                    Square[] between = {board[5], board[6]};
                    for(Square square : between){
                        //if square is not blank or is attacked, we cannot castle
                        if(!square.isBlank() || enemySquares.contains(square)){
                            //cannot castle, check the next castling possibility
                            canCastleKingSide = false;
                        }
                    }
                    
                    //preconditions are fine, white can castle kingside
                    if(canCastleKingSide)
                        //get the rook square
                        castlingMoves.add(new CastlingMove(board[this.getIndex()], board[7], board, "kingside"));
                }
                //queenside castling (for qs castling, we need an extra check for 3rd square since king travels only 2)
                if(ch == 'Q'){
                    boolean canCastleQueenSide = true;
                    //squares between king and rook, assuming they havent moved at all.
                    Square[] between = {board[2], board[3]};
                    for(Square square : between){
                        //if square is not blank or is attacked, we cannot castle
                        if(!square.isBlank() || enemySquares.contains(square) || !board[1].isBlank()){
                            //cannot castle, check the next castling possibility
                            canCastleQueenSide = false;
                            break;
                        }
                    }
                    
                    //preconditions are fine, white can castle queenside
                    if(canCastleQueenSide)
                        //get the rook square
                        castlingMoves.add(new CastlingMove(board[this.getIndex()], board[0], board, "queenside"));
                }
            }
            
            //black castling possibilites
            if(Character.isLowerCase(ch)){
                //kingside castling
                if(ch == 'k'){
                    boolean canCastleKingSide = true;
                    //squares between king and rook, assuming they havent moved at all.
                    Square[] between = {board[61], board[62]};
                    for(Square square : between){
                        //if square is not blank or is attacked, we cannot castle
                        if(!square.isBlank() || enemySquares.contains(square)){
                            //cannot castle, check the next castling possibility
                            canCastleKingSide = false;
                        }
                    }
                    
                    //preconditions are fine, white can castle kingside
                    if(canCastleKingSide)
                        //get the rook square
                        castlingMoves.add(new CastlingMove(board[this.getIndex()], board[63], board, "kingside"));
                }
                //queenside castling (for qs castling, we need an extra check for 3rd square since king travels only 2)
                if(ch == 'q'){
                    boolean canCastleQueenSide = true;
                    //squares between king and rook, assuming they havent moved at all.
                    Square[] between = {board[58], board[59]};
                    for(Square square : between){
                        //if square is not blank or is attacked, we cannot castle
                        if(!square.isBlank() || enemySquares.contains(square) || !board[57].isBlank()){
                            //cannot castle, check the next castling possibility
                            canCastleQueenSide = false;
                            break;
                        }
                    }
                    
                    //preconditions are fine, white can castle queenside
                    if(canCastleQueenSide)
                        //get the rook square
                        castlingMoves.add(new CastlingMove(board[this.getIndex()], board[56], board, "queenside"));
                }
            }
        }
        
        return castlingMoves;
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
                break;
            }
        } 
        
        return attackSquares;
    }
    
    @Override
    public ArrayList<Move> getValidCheckedMoves(Piece king, Square[] board) {
        //this is just moves that the king can make to evade a check, we cannot block checks with the king
        //however we can also capture own attacker, assuming it is not protected
        return getValidMoves(board);
    }
    
    @Override 
    public ArrayList<Square> getSlidingAttackSquares(Square[] board){return new ArrayList();}
    @Override
    public String getSide(){return "";}
    @Override
    public ArrayList<Square> getKingAttackPath(Piece king, Square[] board) {return new ArrayList();}
    @Override
    public ArrayList<Move> filterPinnedMoves(Square[] board) {return new ArrayList();}
}

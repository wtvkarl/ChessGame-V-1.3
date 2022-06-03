/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.board;

import com.krlv.source.chessenginev1_3.moves.Move;
import com.krlv.source.chessenginev1_3.moves.MoveType;

/**
 *
 * @author 523ka
 */
public class FenInterpreter {

    private String fen;
    private String[] boardStates; 
    
    private static String pieceOrientation, colorToMove, castlingRights,
                   enPassantSquare, halfMoveCounter, fullMoveCounter;
    
    private static int halfMoves = 0, fullMoves = 0;
    
    public FenInterpreter(String fen) {
        this.fen = fen;
        initializeStateArray(fen);
    }
    
    private void initializeStateArray(String f){
        boardStates = f.split(" ");
        updateStates(boardStates);
    }
    
    private void updateStates(String[] newStates){
        pieceOrientation = newStates[0];
        colorToMove = newStates[1];
        castlingRights = newStates[2];
        enPassantSquare = newStates[3];
        halfMoveCounter = newStates[4];
        fullMoveCounter = newStates[5];
    }
    
    public void updatePieceOrientationString(Square[] board){
        String positions = "";
        for(int row = 7; row >= 0; row--){
            int blanks = 0;
            for(int col = 0; col < 8; col++){
                int index = row * 8 + col;
                Piece piece = board[index].getPiece();
                if(!piece.isBlank()){
                    if(blanks > 0){
                        positions += blanks;
                        blanks = 0;
                        col--;
                    }
                    else
                        positions += piece.getFenNotation();
                }
                else{
                    blanks++;
                    if(blanks == 8){
                        positions += blanks;
                        blanks = 0;
                        break;
                    }
                }
            }
            if(blanks > 0)
                positions += blanks;
            if(row != 0)
                positions += "/";
        }
        pieceOrientation = positions;
    }
    
    public void updateColorToMove(Piece draggedPiece){
        colorToMove = (draggedPiece.isWhite()) ? "B" : "W";
    }
    
    public void updateCastlingRights(Piece draggedPiece, Square[] board){
        if(draggedPiece.isRook()){
            if(draggedPiece.isWhite()){
                if(draggedPiece.getSide().equals("queenside")){
                    castlingRights = castlingRights.replace("Q", "");
                }
                else if(draggedPiece.getSide().equals("kingside")){
                    castlingRights = castlingRights.replace("K", "");
                }
            }
            else{
                if(draggedPiece.getSide().equals("queenside")){
                    castlingRights = castlingRights.replace("q", "");
                }
                else if(draggedPiece.getSide().equals("kingside")){
                    castlingRights = castlingRights.replace("k", "");
                }
            }
        }
        else if(draggedPiece.isKing()){
            if(draggedPiece.isWhite()){
                castlingRights = castlingRights.replace("Q", "");
                castlingRights = castlingRights.replace("K", "");
            }
            else if(draggedPiece.isBlack()){
                castlingRights = castlingRights.replace("q", "");
                castlingRights = castlingRights.replace("k", "");
            }
        }
        
        if(castlingRights.isEmpty()){
            castlingRights = "-";
        }
    }
    
    public void updateEnPassantSquare(Piece draggedPiece, Square[] board) {
        // we want to get the square behind the pawn
        if(!draggedPiece.isPawn() || draggedPiece.timesMoved > 1 || draggedPiece.squaresMoved != 2)
            enPassantSquare = "-";
        else{
            int direction = draggedPiece.isWhite() ? 1 : -1;
            int squareBehindPawnIndex = draggedPiece.getIndex() - (8 * direction);
            enPassantSquare = board[squareBehindPawnIndex].getCoordinate();
        }
    }
    
    public void updateHalfMoveClock(Move move){
        //if the move is a capture move or if the moved piece is a pawn, reset timer to 0
        //if this clock reaches 50, it is an automatic draw
        if(move.getMoveType() == MoveType.CAPTURE || move.getStartPiece().isPawn())
            halfMoves = 0;
        else
            halfMoves++;
        halfMoveCounter = Integer.toString(halfMoves);
    }
    
    public void updateFullMoveClock(){
        fullMoves++;
        fullMoveCounter = Integer.toString(fullMoves);
    }
    
    //this is called after ALL parameters of the fen are updated
    public void updateFen(){
        fen = "";
        fen += pieceOrientation + " ";
        fen += colorToMove + " ";
        fen += castlingRights + " ";
        fen += enPassantSquare + " ";
        fen += halfMoveCounter + " ";
        fen += fullMoveCounter;
    }
    
    public void setFen(String fen){
       this.fen = fen;
    }
    
    public String getPieceOrientation(){
        return pieceOrientation;
    }
    
    public String getColorToMove(){
        //to match piece color format
        return Character.toLowerCase(colorToMove.toCharArray()[0]) + "";
    }
    
    public boolean isColorToMove(Piece piece){
        return piece.getColor().equalsIgnoreCase(colorToMove);
    }
    
    public void switchTurn(){
        colorToMove = isWhitesTurn() ? "b" : "w";
    }
    
    public boolean isWhitesTurn(){
        return getColorToMove().equals("w");
    }
    
    public boolean isBlacksTurn(){
        return getColorToMove().equals("b");
    }
    
    public static String getEnPassantSquare(){
        return enPassantSquare;
    }
    
    public static String getCastlingRights(){
        return castlingRights;
    }
    
    public static String getCastlingRights(Piece piece){
        String rights = "";
        if(piece.isWhite()){
            for(char c : castlingRights.toCharArray()){
                if(Character.isUpperCase(c)){
                    rights += c;
                }
            }
        }
        else{
            for(char c : castlingRights.toCharArray()){
                if(Character.isLowerCase(c)){
                    rights += c;
                }
            }
        }
        
        return rights;
    }
    
    public void printFen(){
        System.out.println(fen);
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.board;

import com.krlv.source.chessenginev1_3.moves.Move;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author 523ka
 */
public abstract class Piece {
    
    public int index, row, col, value, type;
    public int timesMoved = 0;
    public String fenNotation;
    public String name;
    public String color;
    public BufferedImage image;
    public int squaresMoved = 0;
    public boolean isSlidingPiece;
    public String side; //only for rooks
    public ArrayList<Move> validMoves;
    
    public abstract ArrayList<Move> getValidMoves(Square[] board);
    public abstract ArrayList<Square> getAttackSquares(Square[] board);
    public abstract ArrayList<Square> getKingAttackPath(Piece king, Square[] board);
    public abstract ArrayList<Move> getValidCheckedMoves(Piece king, Square[] board);
    public abstract ArrayList<Move> filterPinnedMoves(Square[] board);
    public abstract String getSide();
    
    //this method ONLY to be implemented in SLIDING PIECES (rook, queen, bishop)
    public abstract ArrayList<Square> getSlidingAttackSquares(Square[] board);
    
    public boolean isWhite(){
        return color.equals("w");
    }
    
    public boolean isBlack(){
        return color.equals("b");
    }
    
    public boolean isBlank(){
        return color.equals("blank");
    }
    
    public int getValue(){
        return value;
    }
    
    public int getIndex(){
        return index;
    }
    
    public void setIndex(int newIndex){
        index = newIndex;
        row = index / 8;
        col = index % 8;
    }
    
    public String getFenNotation(){
        return fenNotation;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
    
    public int getType(){
        return type;
    }
    
    public String getName(){
        return name;
    }
    
    public String getColor(){
        return color;
    }
    
    public void setImage(BufferedImage img){
        image = img;
    }
    
    public BufferedImage getImage(){
        return image;
    }
    
    @Override
    public String toString(){
        return fenNotation;
    }

    public boolean isFriendly(Piece piece) {
        return piece.getColor().equals(getColor());
    }
    
    public boolean isPawn(){
        return name.equals("pawn");
    }
    
    public boolean isKnight(){
        return name.equals("knight");
    }
    
    public boolean isBishop(){
        return name.equals("bishop");
    }
    
    public boolean isRook(){
        return name.equals("rook");
    }
    
    public boolean isQueen(){
        return name.equals("queen");
    }
    
    public boolean isKing(){
        return name.equals("king");
    }
    
    public boolean isInBoard(int index){
        return index >= 0 && index <= 63;
    }
    
    public boolean isSlidingPiece(){
        return isSlidingPiece;
    }
    
    public Square getSquare(){
        return ChessBoard.getBoard()[this.index];
    }
    
}

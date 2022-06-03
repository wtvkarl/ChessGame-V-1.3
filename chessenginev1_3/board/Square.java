/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.board;

/**
 *
 * @author 523ka
 */
public class Square {
    
    private Piece piece;
    private int row, col;
    private char file, rank;
    private int index;
    private String coordinate;

    public Square(int index) {
        this.index = index;
        row = index / 8;
        col = index % 8;
        file = (char) (97 + col); //97 is character 'a'
        rank = (char) (49 + row); //49 is the character '1'
        coordinate = file + "" + rank + "";
    }
    
    public boolean isBlank(){
        return piece.isBlank();
    }
    
    public Piece getPiece(){
        return piece;
    }
    
    public void setPiece(Piece newPiece){
        piece = newPiece;
    }
    
    public int getIndex(){
        return index;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
    
    public String getCoordinate(){
        return coordinate;
    }
    
    @Override
    public String toString(){
        return "Index: " + index;
    }
}

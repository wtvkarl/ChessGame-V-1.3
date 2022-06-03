/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.board;

import com.krlv.source.chessenginev1_3.moves.Move;
import java.util.ArrayList;

/**
 *
 * @author 523ka
 */
public class Blank extends Piece {

    public Blank(int boardIndex) {
        this.index = boardIndex;
        this.row = index / 8;
        this.col = index % 8;
        this.name = "blank";
        this.value = 0;
        this.color = "blank";
        this.type = -1;
        this.fenNotation = "";
        this.isSlidingPiece = false;
        this.validMoves = new ArrayList();
    }
    
    @Override
    public ArrayList<Move> getValidMoves(Square[] board) {return new ArrayList();}
    @Override
    public ArrayList<Square> getAttackSquares(Square[] board){return new ArrayList();}
    @Override 
    public ArrayList<Square> getSlidingAttackSquares(Square[] board){return new ArrayList();}
    @Override
    public String getSide(){return "";}
    @Override
    public ArrayList<Square> getKingAttackPath(Piece king, Square[] board) {return new ArrayList();}
    @Override
    public ArrayList<Move> getValidCheckedMoves(Piece king, Square[] board) {return new ArrayList();}
    @Override
    public ArrayList<Move> filterPinnedMoves(Square[] board) {return new ArrayList();}

}

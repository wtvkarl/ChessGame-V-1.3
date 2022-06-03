/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.board;

import com.krlv.source.chessenginev1_3.Display;
import com.krlv.source.chessenginev1_3.InputHandler;
import com.krlv.source.chessenginev1_3.generation.MoveGeneration;
import com.krlv.source.chessenginev1_3.gfx.ImageLoader;
import com.krlv.source.chessenginev1_3.gfx.UI;
import com.krlv.source.chessenginev1_3.maps.KingCheckMap;
import com.krlv.source.chessenginev1_3.moves.Move;
import com.krlv.source.chessenginev1_3.moves.MoveHandler;
import com.krlv.source.chessenginev1_3.moves.MoveType;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;


/**
 *
 * @author 523ka
 * THIS IS TEMPLATE PROJECT FOR THE CHESS BOILERPLATE 
 * SO I DONT HAVE TO KEEP CODING THE SAME SHIT EVERY SINGLE TIME :)
 */
public class ChessBoard {
    
    private static final int tileSize = 80;
    private static final int boardOffset = 80;

    private static Square[] board;
    private final FenInterpreter fenReader;
    private static final String startingFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    
    private final InputHandler inputHandler;
    
    private final UI ui;
    
    //north, south, west, east, northeast, northwest, southeast, southwest
    private static final int[] directionOffsets = {8, -8, -1, 1, 9, 7, -7, -9};
    private static int[][] distancesToEdge;
    
    private Piece draggedPiece;
    private static Piece whiteKing, blackKing;
    
    private static ArrayList<Piece> whitePieces, blackPieces;
    
    private ArrayList<Move> validMoves;
    
    public ChessBoard(Display d) {
        board = new Square[64];
        fenReader = new FenInterpreter(startingFen);
        inputHandler = d.getInputHandler();
        initializeSquares();
        initializeEdgeDistanceArray();
        ui = new UI(d, inputHandler);
        
        for(int i = 0; i < ui.getPromotionButtons().length; i++){
            d.add(ui.getPromotionButtons()[i]);
        }
        validMoves = new ArrayList();
    }
    
    public static Square[] getBoard(){
        return board;
    }
    
    public static Piece getWhiteKing(){
        return whiteKing;
    }
    
    public static Piece getBlackKing(){
        return blackKing;
    }
    
    private void initializeSquares(){
        whitePieces = new ArrayList();
        blackPieces = new ArrayList();
        
        for(int i = 0; i < board.length; i++){
            board[i] = new Square(i);
        }
        
        String positions = fenReader.getPieceOrientation();
        String[] ranks = positions.split("/");
        int boardIndex = 0;
        
        for(int i = ranks.length - 1; i >= 0; i--){
            for(Character ch : ranks[i].toCharArray()){
                Square square = board[boardIndex];
                
                if(Character.isUpperCase(ch)){
                    switch(ch){
                        case 'P' : square.setPiece(new Pawn(boardIndex, "w")); break;
                        case 'R' : square.setPiece(new Rook(boardIndex, "w")); break;
                        case 'N' : square.setPiece(new Knight(boardIndex, "w")); break;
                        case 'B' : square.setPiece(new Bishop(boardIndex, "w")); break;
                        case 'Q' : square.setPiece(new Queen(boardIndex, "w")); break;
                        case 'K' : {
                            Piece king = new King(boardIndex, "w");
                            square.setPiece(king);
                            whiteKing = king;
                        } break;
                    }
                }
                else if(Character.isLowerCase(ch)){
                    switch(ch){
                        case 'p' : square.setPiece(new Pawn(boardIndex, "b")); break;
                        case 'r' : square.setPiece(new Rook(boardIndex, "b")); break;
                        case 'n' : square.setPiece(new Knight(boardIndex, "b")); break;
                        case 'b' : square.setPiece(new Bishop(boardIndex, "b")); break;
                        case 'q' : square.setPiece(new Queen(boardIndex, "b")); break;
                        case 'k' : {
                            Piece king = new King(boardIndex, "b");
                            square.setPiece(king);
                            blackKing = king;
                        } break;
                    }
                }
                else if(Character.isDigit(ch)){
                    int blanks = Integer.parseInt(ch + "");
                    while(blanks > 0){
                        square = board[boardIndex];
                        square.setPiece(new Blank(boardIndex));
                        boardIndex++;
                        blanks--;
                    }
                    continue;
                }
                Piece piece = square.getPiece();
                if(!piece.isBlank())
                    piece.setImage(ImageLoader.getImage(piece));
                
                boardIndex++;
            }
        }
        
        updatePieceLists();
    }
    
    private void initializeEdgeDistanceArray(){
        distancesToEdge = new int[board.length][directionOffsets.length];
        for(int i = 0; i < board.length; i++){
            int row = i / 8;
            int col = i % 8;
            int squareIndex = row * 8 + col;
            
            int distNorth = 7 - row;
            int distSouth = row;
            int distWest = col;
            int distEast = 7 - col;
            int distNorthWest = Math.min(distNorth, distWest);
            int distNorthEast = Math.min(distNorth, distEast);
            int distSouthWest = Math.min(distSouth, distWest);
            int distSouthEast = Math.min(distSouth, distEast);
            
            int[] distances = {
                distNorth,
                distSouth,
                distWest,
                distEast,
                distNorthEast,
                distNorthWest,
                distSouthEast,
                distSouthWest
            };
            
            distancesToEdge[squareIndex] = distances;
        }
    }
    
    public static int[] getDirectionOffsets(){
        return directionOffsets;
    }
    
    public static int getDistanceToEdge(int pieceIndex, int direction){
        //first four directions are the 4 cardinal directions
        //last four directions are the 4 intermediate directions
        
        return distancesToEdge[pieceIndex][direction];
    }
    
    public void draw(Graphics2D g2d){
        drawBackground(g2d);
        drawSquares(g2d);        
        drawValidMoves(g2d);
        drawCheckedKing(g2d);
        drawPieces(g2d);
        drawDraggedPiece(g2d);
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.drawImage(ImageLoader.getBackground(), 0, 0, 800, 800, null);
    }
    
    private void drawSquares(Graphics2D g2d){
        for (Square s : board) {
            int row = s.getRow();
            int col = s.getCol();
            int drawX = boardOffset + (col * tileSize);
            int drawY = boardOffset + (row * tileSize);
            boolean isWhiteSquare = (row + col) % 2 == 0;
            Color squareColor = (isWhiteSquare) ? Color.white : Color.darkGray;
            g2d.setColor(squareColor);
            g2d.fillRect(drawX, drawY, tileSize, tileSize);
        }
    }
    
    private void drawValidMoves(Graphics2D g2d){
        if(draggedPiece != null && validMoves.size() > 0 && fenReader.isColorToMove(draggedPiece)){
            for(int i = 0; i < validMoves.size(); i++){
                Move move = validMoves.get(i);
                g2d.setColor(MoveHandler.getColorBasedOnMove(move));
                Square target = move.getTargetSquare();
                int drawX = boardOffset + (target.getCol() * tileSize);
                int drawY =  720 - boardOffset - (target.getRow() * tileSize);
                g2d.fillRect(drawX + 5, drawY + 5, tileSize - 10, tileSize - 10);
            }
        }
    }
    
    private void drawCheckedKing(Graphics2D g2d){
        Piece king = fenReader.getColorToMove().equals("w") ? whiteKing : blackKing;
        if(KingCheckMap.kingIsInCheck(king, board)){
            g2d.setColor(Color.pink);
            Square target = king.getSquare();
            int drawX = boardOffset + (target.getCol() * tileSize);
            int drawY =  720 - boardOffset - (target.getRow() * tileSize);
            g2d.fillRect(drawX + 5, drawY + 5, tileSize - 10, tileSize - 10);
        }
    }
    
    private void drawPieces(Graphics2D g2d){
        for(int i = board.length - 1; i >= 0 ; i--){
            int index = (fenReader.isWhitesTurn()) ? 63 - i  : i; 
            Square square = board[index];
            int row = square.getRow();
            int col = square.getCol();
            int drawX = boardOffset + (col * tileSize);
            int drawY = 640 - (row * tileSize); // this way we draw the pieces properly, white indecies go up to black indecies
            Piece piece = square.getPiece();
            //blank squares' images are null, skipping them saves drawing time
            if(piece.getImage() != null && piece != draggedPiece)
                g2d.drawImage(piece.getImage(), drawX, drawY, null);
        }
    }
    
    private void drawDraggedPiece(Graphics2D g2d){
        if(draggedPiece == null){
            return;
        }
        int drawX = inputHandler.getX() - (tileSize/2);
        int drawY = inputHandler.getY() - (tileSize/2);
        g2d.drawImage(draggedPiece.getImage(), drawX, drawY, null);
    }
    
    public void update(){
        if(inputHandler.isPressed()){
            Square square = getSquare(inputHandler.getX(), inputHandler.getY());
            if(draggedPiece == null && square != null){
                draggedPiece = square.getPiece();
                if(draggedPiece.getColor().equals(fenReader.getColorToMove())){
                    Piece king = draggedPiece.isWhite() ? whiteKing : blackKing;
                    boolean kingIsInCheck = KingCheckMap.kingIsInCheck(king, board);

                    if(kingIsInCheck){
                        validMoves = draggedPiece.getValidCheckedMoves(king, board);
                    }
                    else{
                        validMoves = draggedPiece.getValidMoves(board);
                    }
                }
                System.out.println("INDEX: " + draggedPiece.getIndex());
            }
        }
        else{
            if(draggedPiece != null){
                Square start = board[draggedPiece.getIndex()];
                Square target = getSquare(inputHandler.getX(), inputHandler.getY());
                if(target != null && fenReader.isColorToMove(draggedPiece)){
                    Move move = MoveHandler.getMove(start, target, validMoves);
                    if(move != null && MoveHandler.isValid(move)){
                        
                        if(move.getMoveType() == MoveType.PROMOTION){
                            ui.setPromotionButtonColors(draggedPiece);
                            ui.setPromotionButtonIcons();
                            ui.setPromotionMenuBounds(move);
                        }
                        
                        move.execute();
                        fenReader.updatePieceOrientationString(board);
                        fenReader.switchTurn();
                        fenReader.updateCastlingRights(draggedPiece, board);
                        fenReader.updateEnPassantSquare(draggedPiece, board);
                        fenReader.updateHalfMoveClock(move);
                        //fullmove counter increment after black moves
                        if(draggedPiece.isBlack())
                            fenReader.updateFullMoveClock();
                        fenReader.updateFen();
                        fenReader.printFen();
                        
                        updatePieceLists();                        
                    }
                }
                
                if(isCheckmate()){
                    System.out.println("Checkmate!");
                }
                
                draggedPiece = null;
                validMoves.clear();
            }
        }
    }
    
    private boolean isCheckmate(){
        Piece king = (fenReader.getColorToMove().equalsIgnoreCase("W")) ? whiteKing : blackKing;
        if(KingCheckMap.kingIsInCheck(king, board)){
            return KingCheckMap.getAllCheckedMoves(king.getColor()).size() == 0;
        }
        else 
            return false;
    }
    
    public static void updatePieceLists(){
        whitePieces.clear();
        blackPieces.clear();
        for(Square square : board){
            Piece piece = square.getPiece();
            if(!piece.isBlank()){
                if(piece.isWhite()){
                    whitePieces.add(piece);
                }
                else{
                    blackPieces.add(piece);
                }
            }
        }
    }
    
    public boolean indexIsInBoard(int index){
        return index >= 0 && index <= 63;
    }
    
    public boolean mouseIsInBoard(InputHandler ih){
        return (ih.getX() >= tileSize && ih.getX() <= 800 - tileSize) &&
               (ih.getY() >= tileSize && ih.getY() <= 800 - tileSize);
    }
    
    public Square getSquare(int mouseX, int mouseY){
        int row = (800 - mouseY) / tileSize - 1; //bottom -> top
        int col = (mouseX) / tileSize - 1;       //left -> right
        int index = row * 8 + col;
        if(!indexIsInBoard(index) || !mouseIsInBoard(inputHandler))
            return null;
        
        return board[index];
    }
    
    public static Square getSquare(String coordinate){
        for(Square s : board){
            if(s.getCoordinate().equals(coordinate))
                return s;
        }
        return null;
    }
    
    public static ArrayList<Piece> getWhitePieces(){
        return whitePieces;
    }
    
    public static ArrayList<Piece> getBlackPieces(){
        return blackPieces;
    }
    
    @Override
    public String toString(){
        String str = "";
        //top to bottom, left to right
        for(int row = 7; row >= 0; row--){
            for(int col = 0; col < 8; col++){
                int index = row * 8 + col;
                Piece piece = board[index].getPiece();
                if(piece.isBlank())
                    str += "- ";
                else
                    str += piece + " ";
            }
            str += "\n";
        }
        return str;
    }
    
}

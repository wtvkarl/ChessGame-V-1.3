/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.gfx;

import com.krlv.source.chessenginev1_3.board.Piece;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author 523ka
 */
public class ImageLoader {
    
    private static BufferedImage[] whitePieces;
    private static BufferedImage[] blackPieces;
    private static BufferedImage background;
    
    private static final String bPawnFilePath = "res/images/blackPieces/blackpawn.png";
    private static final String bKnightFilePath = "res/images/blackPieces/blackknight.png";
    private static final String bBishopFilePath = "res/images/blackPieces/blackbishop.png";
    private static final String bRookFilePath = "res/images/blackPieces/blackrook.png";
    private static final String bQueenFilePath = "res/images/blackPieces/blackqueen.png";
    private static final String bKingFilePath = "res/images/blackPieces/blackking.png";
   
    private static final String wPawnFilePath = "res/images/whitePieces/whitepawn.png";
    private static final String wKnightFilePath = "res/images/whitePieces/whiteknight.png";
    private static final String wBishopFilePath = "res/images/whitePieces/whitebishop.png";
    private static final String wRookFilePath = "res/images/whitePieces/whiterook.png";
    private static final String wQueenFilePath = "res/images/whitePieces/whitequeen.png";
    private static final String wKingFilePath = "res/images/whitePieces/whiteking.png";
    
    private static final String backgroundFilePath = "res/images/background/woodbackground.jpg";
       
    public static void preloadAssets(){
        whitePieces = new BufferedImage[6];
        blackPieces = new BufferedImage[6];
        try{
            whitePieces[0] = ImageIO.read(new File(wPawnFilePath));
            whitePieces[1] = ImageIO.read(new File(wKnightFilePath));
            whitePieces[2] = ImageIO.read(new File(wBishopFilePath));
            whitePieces[3] = ImageIO.read(new File(wRookFilePath));
            whitePieces[4] = ImageIO.read(new File(wQueenFilePath));
            whitePieces[5] = ImageIO.read(new File(wKingFilePath));

            blackPieces[0] = ImageIO.read(new File(bPawnFilePath));
            blackPieces[1] = ImageIO.read(new File(bKnightFilePath));
            blackPieces[2] = ImageIO.read(new File(bBishopFilePath));
            blackPieces[3] = ImageIO.read(new File(bRookFilePath));
            blackPieces[4] = ImageIO.read(new File(bQueenFilePath));
            blackPieces[5] = ImageIO.read(new File(bKingFilePath));

            background = ImageIO.read(new File(backgroundFilePath));
        }
        catch(IOException e){}
        
    }
    
    public static BufferedImage getImage(Piece piece){
        int pieceType = piece.getType();
        return (piece.isWhite()) ? whitePieces[pieceType] : blackPieces[pieceType];
    }
    
    public static BufferedImage getBackground(){
        return background;
    }
    
    public static BufferedImage[] getWhitePieceImages() {
        return whitePieces;
    }
    
    public static BufferedImage[] getWhitePromotionIcons(){
        BufferedImage[] imgs = {whitePieces[1], whitePieces[2], whitePieces[3], whitePieces[4]};
        return imgs;
    }
    
    public static BufferedImage[] getBlackPromotionIcons(){
        BufferedImage[] imgs = {blackPieces[1], blackPieces[2], blackPieces[3], blackPieces[4]};
        return imgs;
    }

    public static BufferedImage[] getBlackPieceImages() {
        return blackPieces;
    }
}

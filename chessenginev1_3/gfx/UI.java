/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3.gfx;

import com.krlv.source.chessenginev1_3.Display;
import com.krlv.source.chessenginev1_3.InputHandler;
import com.krlv.source.chessenginev1_3.board.Piece;
import com.krlv.source.chessenginev1_3.board.Square;
import com.krlv.source.chessenginev1_3.moves.Move;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 *
 * @author 523ka
 */
public class UI implements ActionListener{

    private Display display;
    private InputHandler inputHandler;
    
    private static final int squareSize = 80;
    
    private static Button[] promotionButtons;
    private BufferedImage[] promotionIcons;
    private BufferedImage[] whiteIcons, blackIcons;
    
    private static String promotionSelection = "";
    
    public UI(Display d, InputHandler ih) {
        this.display = d;
        this.inputHandler = ih;
        initializeButtons();
    }
    
    private void initializeButtons(){
        promotionButtons = new Button[4];
        promotionIcons = new BufferedImage[4];
        whiteIcons = ImageLoader.getWhitePromotionIcons();
        blackIcons = ImageLoader.getBlackPromotionIcons();
        
        promotionButtons[0] = new Button(whiteIcons[0], "knight", this);
        promotionButtons[1] = new Button(whiteIcons[1], "bishop", this);
        promotionButtons[2] = new Button(whiteIcons[2], "rook", this);
        promotionButtons[3] = new Button(whiteIcons[3], "queen", this);
        
        for(int i = 0; i < 4; i++){
            promotionButtons[i].setBounds(squareSize * i, 0, squareSize, squareSize);
        }
    }
    
    public void setPromotionMenuBounds(Move move){
        Square square = move.getTargetSquare();
        Piece piece = move.getStartPiece();
        
        int menuX = (square.getCol() + 1) * squareSize;
        int menuY = (8 - square.getRow()) * squareSize;
        
        
        int direction = (menuY < 400) ? 1 : -1;
        
        for(int i = 0; i < 4; i++){
            int buttonX = menuX;
            int buttonY = menuY + (squareSize * direction * i);
            //draw buttons w/ queen first for convenience
            promotionButtons[3-i].setLocation(buttonX, buttonY);
        }
        
    }
    
    public static void setPromotionButtonsVisible(boolean visible){
        for(Button button : promotionButtons){
            button.setVisible(visible);
        }
    }
    
    public void setPromotionButtonColors(Piece piece){
        promotionIcons = (piece.isWhite()) ? whiteIcons : blackIcons;
    }
    
    public void setPromotionButtonIcons(){
        for(int i = 0; i < 4; i++){
            promotionButtons[i].setIcon(promotionIcons[i]);
        }
    }
    
    public Button[] getPromotionButtons(){
        return promotionButtons;
    }
    
    public static String getPromotionSelection(){
        return promotionSelection;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        promotionSelection = ae.getActionCommand();
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krlv.source.chessenginev1_3.gfx;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author 3095515
 */
public class Button extends JButton{
    
    private String command;
    
    public Button(BufferedImage image, String command, UI ui){
        this.setIcon(new ImageIcon(image));
        this.setActionCommand(command);
        this.setBackground(Color.gray);
        this.setVisible(false);
        this.addActionListener(ui);
        this.command = command;
    }
    
    public void setIcon(BufferedImage image){
        this.setIcon(new ImageIcon(image));
    }
    
    public String getActionCommand(){
        return command;
    }
    
    @Override
    public String toString(){
        return command;
    }
    
}

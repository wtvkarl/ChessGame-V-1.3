/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3;

import javax.swing.JFrame;

/**
 *
 * @author 523ka
 */
public class Engine {

    public Engine() {
        JFrame window = new JFrame("Chess Engine v0.3");
        Display display = new Display();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(display);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        display.start();
    }
    
}

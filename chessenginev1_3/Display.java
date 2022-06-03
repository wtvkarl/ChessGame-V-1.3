/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krlv.source.chessenginev1_3;

import com.krlv.source.chessenginev1_3.board.ChessBoard;
import com.krlv.source.chessenginev1_3.gfx.ImageLoader;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author 523ka
 */
public class Display extends JPanel implements Runnable{

    private Thread gameThread;
    private boolean running;
    
    private ChessBoard board;
    private InputHandler inputHandler;
    
    public Display() {
        long start = System.nanoTime();
        this.setLayout(null);
        this.setPreferredSize(new Dimension(800,800));
        this.setDoubleBuffered(true);
        inputHandler = new InputHandler();
        this.addMouseListener(inputHandler);
        this.addMouseMotionListener(inputHandler);
        
        ImageLoader.preloadAssets();
        board = new ChessBoard(this);
        System.out.println("Bootup Time: " + (System.nanoTime() - start) / 1_000_000_000.0);
        
        //compared to version 1.2, the 1.3 engine cuts down its avg boot up time from 0.75 to 0.35!!!
    }
    
    public void start(){
        running = true;
        gameThread = new Thread(this, "gameThread");
        gameThread.start();
    }
    
    
    @Override
    public void run() {
        int targetUpdates = 60;
        double nsPerSecond = 1_000_000_000.0;
        double drawInterval = nsPerSecond/targetUpdates;
        double delta = 0;
        long then = System.nanoTime();
        long now;
        boolean shouldRender = false;
        
        while(running){
            now = System.nanoTime();
            delta += (now - then) / drawInterval;
            then = now;
            
            if(delta >= 1){
                update();
                delta--;
                shouldRender = true;
            }
            
            if(shouldRender){
                repaint();
                shouldRender = false;
            }
            
        }
    }
    
    private void update(){
        board.update();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) (g);
        board.draw(g2d);
    }
    
    public InputHandler getInputHandler(){
        return inputHandler;
    }
    
}

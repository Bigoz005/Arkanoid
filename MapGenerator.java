package com.company;

import java.awt.*;
import java.awt.Graphics2D;


/**
 *
 * @author Michal Nawrot
 */
public class MapGenerator {
    private int map[][];

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public int getBrickWidth() {
        return brickWidth;
    }

    public void setBrickWidth(int brickWidth) {
        this.brickWidth = brickWidth;
    }

    public int getBrickHeight() {
        return brickHeight;
    }

    public void setBrickHeight(int brickHeight) {
        this.brickHeight = brickHeight;
    }

    private int brickWidth;
    private int brickHeight;

    /**
     * Create array with provided numbers of row and col
     * and set brick grafical size.
     * Sets the field values ​​to 1 for those
     * fields that match bricks.
     * @param row how many rows of bricks
     * @param col how many columns of bricks
     */
    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;
            }
        }
        brickWidth = 540/col;
        brickHeight = 150/row;
    }

    /**
     * Create array with provided numbers of bricks
     * and set brick grafical size.
     * Sets the field values ​​to 1 for those
     * fields that match bricks.
     * @param amount how many bricks
     */
    public MapGenerator(Integer amount) {
        int row = 0;
        int col = 0;

        if(amount <= 0 || amount == null){
            amount = 1;
        }

        if(amount > 40){
            amount = 40;
        }

        if(amount <= 10){
            row = 1;
            col = 10;
        }

        if(amount > 10 && amount <= 20){
            row = 2;
            col = 10;
        }

        if(amount > 20 && amount <= 30){
            row = 3;
            col = 10;
        }

        if(amount > 30 && amount <= 40){
            row = 4;
            col = 10;
        }

        map = new int[row][col];

        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                if(amount != 0) {
                    map[i][j] = 1;
                    amount--;
                }else{
                    map[i][j] = 0;
                }
            }
        }

        brickWidth = 900/col;
        brickHeight = 150/row;
    }

    /**
     * Creates a graphic representation of bricks.
     */
    public void draw(Graphics2D g){
        for(int i = 0; i<map.length; i++) {
            for(int j = 0; j<map[0].length; j++) {
                if(map[i][j] > 0) {
                    g.setColor(Color.orange);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}

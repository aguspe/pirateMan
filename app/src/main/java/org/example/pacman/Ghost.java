package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Ghost {

    private int ghostx;
    private int ghosty;
    private int ghostDirection;
    private Bitmap pacghost;
    public boolean isAlive;
    public int lastChangedDirectionTime;


    public Ghost(int ghostx, int ghosty, int ghostDirection, Context context) {
        this.ghostx = ghostx;
        this.ghosty = ghosty;
        this.ghostDirection = ghostDirection;
        this.isAlive = true;
        this.lastChangedDirectionTime = 0;
        pacghost = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacghost);
    }

    public Ghost (){}


    public int getGhostx() {
        return ghostx;
    }

    public void setGhostx(int ghostx) {
        this.ghostx = ghostx;
    }

    public int getGhosty() {
        return ghosty;
    }

    public void setGhosty(int ghosty) {
        this.ghosty = ghosty;
    }

    public void setGhostDirection(int ghostDirection) {
        this.ghostDirection = ghostDirection;
    }
    public int getGhostDirection(){
        return ghostDirection;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public boolean getAlive(){
        return isAlive;
    }

}

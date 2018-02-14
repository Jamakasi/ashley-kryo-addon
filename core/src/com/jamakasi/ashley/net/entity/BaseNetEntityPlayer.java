package com.jamakasi.ashley.net.entity;

/**
 *
 * @author jamakasi
 */
public abstract class BaseNetEntityPlayer extends BaseNetEntity{

    float move = 0.1f;
    public BaseNetEntityPlayer(){
        super();
    }
    public void moveTo(int dir){
        switch(dir){
            case 0: {
                setPosition(getPosition().x,
                        getPosition().y+move, 
                        getPosition().z);
                break;
            }
            case 1: {
                setPosition(getPosition().x,
                        getPosition().y-move, 
                        getPosition().z);
                break;
            }
            case 2: {
                setPosition(getPosition().x-move,
                        getPosition().y, 
                        getPosition().z);
                break;
            }
            case 3: {
                setPosition(getPosition().x+move,
                        getPosition().y, 
                        getPosition().z);
                break;
            }
            default: {

                break;
            }
        }
    }
}

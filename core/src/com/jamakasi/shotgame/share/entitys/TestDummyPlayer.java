package com.jamakasi.shotgame.share.entitys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jamakasi.ashley.net.entity.BaseNetEntityPlayer;
import com.jamakasi.shotgame.share.entComponents.TextureComponent;

/**
 *
 * @author jamakasi
 */
public class TestDummyPlayer extends BaseNetEntityPlayer{
    TextureComponent texture;
    
    
    public TestDummyPlayer() {
        super();
        texture = new TextureComponent();
        //setPosition(0, 0, 1);
        setScale(1, 1, 0);
        texture.region = new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg")), 0, 0, 32, 32);
        this.add(texture);
    }
    
   
}

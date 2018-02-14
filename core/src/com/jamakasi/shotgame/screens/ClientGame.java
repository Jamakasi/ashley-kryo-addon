package com.jamakasi.shotgame.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.jamakasi.ashley.net.systems.BaseNetEntityClientInterpolator;
import com.jamakasi.shotgame.share.GameWorld;
import com.jamakasi.shotgame.share.entSystems.CameraSystem;
import com.jamakasi.shotgame.share.entSystems.ImplNetClientSystem;
import com.jamakasi.shotgame.share.entSystems.RenderSystem;
import com.jamakasi.shotgame.share.net.netPackets.NetPlayerInput;

/**
 *
 * @author jamakasi
 */
public class ClientGame extends InputAdapter implements Screen{

    Engine cengine ;

    GameWorld gameWorld;
    
    SpriteBatch batch;
    
    ImplNetClientSystem netClient;

    
    public ClientGame() {
        batch = new SpriteBatch();
        
        cengine = new PooledEngine();
        
        gameWorld = new GameWorld("test.tmx");
        for(Entity e: gameWorld.loadEntity()){
            cengine.addEntity(e);
        }
        cengine.addSystem(new CameraSystem());
        cengine.addSystem(new RenderSystem(batch, 
                new OrthogonalTiledMapRenderer(gameWorld.tiledMap, 1.0f / 32.0f)));
        cengine.addSystem(new ImplNetClientSystem());
        
        netClient = cengine.getSystem(ImplNetClientSystem.class);
        cengine.addSystem(new BaseNetEntityClientInterpolator(60,netClient));
        netClient.setListenerToLag(10, 10);
        netClient.startClient();
    }
    protected void updateInput(){
        if(Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)){
            netClient.sendTCP(new NetPlayerInput(0));
            netClient.clientHelper.playerEntity.moveTo(0);
        }if(Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)){
            netClient.sendTCP(new NetPlayerInput(1));
            netClient.clientHelper.playerEntity.moveTo(1);
        }if(Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)){
            netClient.sendTCP(new NetPlayerInput(2));
            netClient.clientHelper.playerEntity.moveTo(2);
        }if(Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)){
            netClient.sendTCP(new NetPlayerInput(3));
            netClient.clientHelper.playerEntity.moveTo(3);
        }
    }
    @Override
    public boolean keyDown(int keycode) {
        if(keycode==(Keys.ESCAPE)){
            netClient.stopClient();
            ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
        }
        return true; 
        
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        updateInput();
        cengine.update(delta);
    }
    
    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        cengine.removeAllEntities();
        batch.dispose();
        
    }
    
}

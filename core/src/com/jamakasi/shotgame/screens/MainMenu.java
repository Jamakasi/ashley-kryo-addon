package com.jamakasi.shotgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jamakasi.shotgame.Constants;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

/**
 *
 * @author jamakasi
 */
public class MainMenu implements Screen{

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    //private TextureAtlas atlas;
    //protected Skin skin;
    static boolean visuiIsInited = false;
    public MainMenu() {
        this.batch = new SpriteBatch();
        if(!visuiIsInited){
            VisUI.load(SkinScale.X2);
            visuiIsInited=true;
        }
        
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WorldWidth, Constants.WorldHeight, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
        
    }
    
    @Override
    public void show() {
        //Stage should controll input:
        Gdx.input.setInputProcessor(stage);
        
        VisTable mainTable = new VisTable();
        mainTable.setFillParent(true);
        mainTable.top();
        
        VisTextButton localPlay = new VisTextButton("Local play");
        localPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LocalGame());
            }
        });
        VisTextButton serverMode = new VisTextButton("Create server");
        VisTextButton clientMode = new VisTextButton("Client");
        
        clientMode.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new ClientGame());
            }
        });
        VisTextButton exit = new VisTextButton("Exit");
        exit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        
         //Add buttons to table
        mainTable.add(localPlay);
        mainTable.row();
        mainTable.add(serverMode);
        mainTable.row();
        mainTable.add(clientMode);
        mainTable.row();
        mainTable.add(exit);

        //Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(.9f, .0f, .0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        //skin.dispose();
        //atlas.dispose();
        batch.dispose();
        VisUI.dispose();
    }
    
}

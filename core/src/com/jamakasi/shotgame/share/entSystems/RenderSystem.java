package com.jamakasi.shotgame.share.entSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.jamakasi.shotgame.Constants;
import com.jamakasi.shotgame.share.entComponents.MapLayerComponent;
import com.jamakasi.shotgame.share.entComponents.TextureComponent;
import com.jamakasi.ashley.net.entity.components.TransformComponent;
import java.util.Comparator;

/**
 *
 * @author jamakasi
 */
public class RenderSystem extends IteratingSystem{

    private SpriteBatch batch;
    private BatchTiledMapRenderer tiledMapRenderer;
    
    private Array<Entity> backgroundQueue;
    private Array<Entity> foregroundQueue;
    private Array<Entity> renderQueue;
    
    private Comparator<Entity> comparator;
    private Comparator<Entity> mapComparator;
    
    private OrthographicCamera cam;

    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<MapLayerComponent> mapLayerMapper;
    
    public RenderSystem(SpriteBatch batch,BatchTiledMapRenderer tiledMapRenderer) {
        super(Family.one(TransformComponent.class, TextureComponent.class, MapLayerComponent.class).get());
        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        mapLayerMapper = ComponentMapper.getFor(MapLayerComponent.class);
        
	backgroundQueue = new Array<Entity>();
        foregroundQueue = new Array<Entity>();
        renderQueue = new Array<Entity>();

        comparator = new Comparator<Entity>() {
            @Override
            public int compare(Entity entityA, Entity entityB) {
                    //return -1;
                    return (int)Math.signum(transformM.get(entityB).position.z -
                                                                    transformM.get(entityA).position.z);
            }
        };
        mapComparator = new Comparator<Entity>(){
            @Override
            public int compare(Entity entityA, Entity entityB) {
                    return (int)Math.signum(mapLayerMapper.get(entityB).zIndex -
                                                                    mapLayerMapper.get(entityA).zIndex);
            }
        };
        this.batch = batch;
        this.tiledMapRenderer = tiledMapRenderer;
        
        cam = new OrthographicCamera(Constants.FRUSTUM_WIDTH, Constants.FRUSTUM_HEIGHT);
        cam.position.set(Constants.FRUSTUM_WIDTH / 2, Constants.FRUSTUM_HEIGHT / 2, 0);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); 
        Gdx.gl.glClearColor(.0f, .0f, .0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        renderQueue.sort(comparator);
	backgroundQueue.sort(mapComparator);
        foregroundQueue.sort(mapComparator);
        
        cam.update();
        
        tiledMapRenderer.setView(cam);
        tiledMapRenderer.getBatch().setProjectionMatrix(cam.combined);
        for(Entity entity : backgroundQueue){
            //System.out.println("Printing background");
            renderMapLayer(entity);
        }
        
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        for (Entity entity : renderQueue) {
            TextureComponent tex = textureM.get(entity);

            if(tex==null){
                //System.err.println("tex=null");
                continue;
            }
            if (tex.region == null) {
                    continue;
            }
            TransformComponent t = transformM.get(entity);

            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();
            float originX = width * 0.5f;
            float originY = height * 0.5f;

            batch.draw(tex.region,
                               t.position.x - originX, t.position.y - originY,
                               originX, originY,
                               width, height,
                               t.size.x * Constants.PIXELS_TO_METRES, t.size.y * Constants.PIXELS_TO_METRES,
                               MathUtils.radiansToDegrees * t.pitchYawRoll.x);
        }

        batch.end();
        renderQueue.clear();
        
        for(Entity entity : foregroundQueue){
            //System.out.println("Printing foreground");
            renderMapLayer(entity);
        }
        foregroundQueue.clear();
        backgroundQueue.clear();
    }

    
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //System.err.println(entity.getComponents());
        if(mapLayerMapper.has(entity)){
            //System.err.println("Found Map");
            MapLayerComponent layer = mapLayerMapper.get(entity);
            if(layer.zIndex >= 0)
                    foregroundQueue.add(entity);
            else
                    backgroundQueue.add(entity);
        }else{
            renderQueue.add(entity);
        }
    }
    
    private void renderMapLayer(Entity entity){
        tiledMapRenderer.getBatch().begin();
        MapLayerComponent layer = mapLayerMapper.get(entity);
        if(layer.layer == null){
                System.err.println("Null map Layer");
                return;
        }
        tiledMapRenderer.renderTileLayer(layer.layer);
        tiledMapRenderer.getBatch().end();
    }
    
    public OrthographicCamera getCamera() {
		return cam;
    }
}

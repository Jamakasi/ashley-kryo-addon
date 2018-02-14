package com.jamakasi.shotgame.share;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.jamakasi.shotgame.share.entComponents.MapLayerComponent;
import com.jamakasi.shotgame.share.entitys.MapLayerEntity;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author jamakasi
 */
public class GameWorld {
    public TiledMap tiledMap;
    
    public GameWorld(String mapname) {
        tiledMap = new TmxMapLoader().load(mapname);
    }
    
    public ArrayList<Entity> loadEntity(){
        ArrayList<Entity> entitys = new ArrayList<Entity>();
        Iterator<MapLayer> i = tiledMap.getLayers().iterator();
        TiledMapTileLayer layer;
        while(i.hasNext()){
            MapLayer m = i.next();
            if(m instanceof TiledMapTileLayer)
                    layer  = (TiledMapTileLayer)m;
            else
                    continue;
            System.out.println("Loading layer " + layer.getName());
            MapLayerEntity mapLEntity = new MapLayerEntity(layer, -1, 32);
            entitys.add(mapLEntity);
        }
        
        return entitys;
    }
}

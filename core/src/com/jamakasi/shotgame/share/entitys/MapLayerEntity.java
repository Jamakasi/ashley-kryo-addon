package com.jamakasi.shotgame.share.entitys;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.jamakasi.shotgame.share.entComponents.MapLayerComponent;

/**
 *
 * @author jamakasi
 */
public class MapLayerEntity extends Entity{

    public MapLayerEntity( TiledMapTileLayer layer,int zindex, int tilesize) {
        MapLayerComponent c = new MapLayerComponent();
        c.layer = layer;
        c.zIndex = -1;
        c.tileSize = 32;
        add(c);
    }
    
}

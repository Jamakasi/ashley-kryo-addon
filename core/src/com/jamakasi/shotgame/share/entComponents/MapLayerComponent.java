package com.jamakasi.shotgame.share.entComponents;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 *
 * @author jamakasi
 */
public class MapLayerComponent implements Component{
	public TiledMapTileLayer layer;
	public int zIndex;
	public int tileSize;
	
	public TiledMapTileLayer.Cell getCell(int x, int y){
            return layer.getCell(x / tileSize, y / tileSize);
        }  
}

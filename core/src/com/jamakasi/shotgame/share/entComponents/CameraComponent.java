package com.jamakasi.shotgame.share.entComponents;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 *
 * @author jamakasi
 */
public class CameraComponent implements Component {
	public Entity target;
        public OrthographicCamera camera;
    
}

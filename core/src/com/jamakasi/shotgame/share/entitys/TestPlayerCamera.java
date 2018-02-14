package com.jamakasi.shotgame.share.entitys;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.jamakasi.shotgame.share.entComponents.CameraComponent;

/**
 *
 * @author jamakasi
 */
public class TestPlayerCamera extends Entity{

    public TestPlayerCamera(Entity target, OrthographicCamera cam) {
        CameraComponent camera = new CameraComponent();
        camera.camera = cam;
        camera.target = target;
        this.add(camera);
        
        //camera.camera.position.x = target.getComponent(TransformComponent.class).pos.x;
        //camera.camera.position.y = target.getComponent(TransformComponent.class).pos.y;
    }
    
}

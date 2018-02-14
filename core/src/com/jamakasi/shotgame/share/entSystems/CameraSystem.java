package com.jamakasi.shotgame.share.entSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jamakasi.shotgame.share.entComponents.CameraComponent;
import com.jamakasi.ashley.net.entity.components.TransformComponent;

/**
 *
 * @author jamakasi
 */
public class CameraSystem extends IteratingSystem{

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<CameraComponent> cm;
    
    public CameraSystem() {
        super(Family.all(CameraComponent.class).get());
        tm = ComponentMapper.getFor(TransformComponent.class);
        cm = ComponentMapper.getFor(CameraComponent.class);
    }

    

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraComponent cam = cm.get(entity);	
        if (cam.target == null) {
                return;
        }

        TransformComponent target = tm.get(cam.target);
        if (target == null) {
                return;
        }	
        cam.camera.position.y = target.position.y;//Math.max(cam.camera.position.y, target.pos.y);
        cam.camera.position.x = target.position.x;//Math.max(cam.camera.position.x, target.pos.x);
        //System.err.println("cam: x"+cam.camera.position.x +" cam: y"+cam.camera.position.y);
    }
    
}

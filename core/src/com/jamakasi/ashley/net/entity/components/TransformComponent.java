package com.jamakasi.ashley.net.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author jamakasi
 */
public class TransformComponent implements Component {
    public final Vector3 position = new Vector3();
    public final Vector3 size = new Vector3();
    public final Vector3 pitchYawRoll = new Vector3();
    
    public void set(TransformComponent comp){
        position.set(comp.position);
        size.set(comp.size);
        pitchYawRoll.set(comp.pitchYawRoll);
    }
}

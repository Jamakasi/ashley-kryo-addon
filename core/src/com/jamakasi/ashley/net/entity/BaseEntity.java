package com.jamakasi.ashley.net.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import com.jamakasi.ashley.net.entity.components.TransformComponent;

/**
 *
 * @author jamakasi
 */
public abstract class BaseEntity extends Entity{
    
    TransformComponent transform;
    
    public BaseEntity(){
        transform = new TransformComponent();
        this.add(transform);
    }
    
    public BaseEntity setPosition(Vector3 transform){
        this.transform.position.set(transform);
        return this;
    }
    public BaseEntity setPosition(float x, float y,float z){
        this.transform.position.set(x,y,z);
        return this;
    }
    public Vector3 getPosition(){return transform.position;};
    
    public BaseEntity setScale(Vector3 size){
        this.transform.size.set(size);
        return this;
    }
    public BaseEntity setScale(float x, float y,float z){
        this.transform.size.set(x,y,z);
        return this;
    }
    public Vector3 getScale(){return transform.size;};
    
    public BaseEntity setPitchYawRoll(Vector3 pitchYawRoll){
        this.transform.pitchYawRoll.set(pitchYawRoll);
        return this;
    }
    public BaseEntity setPitchYawRoll(float pitch, float yaw,float roll){
        this.transform.pitchYawRoll.set(pitch, yaw, roll);
        return this;
    }
    public Vector3 getPitchYawRoll(){return transform.pitchYawRoll;};
    public TransformComponent getTransform(){return this.transform;};
}

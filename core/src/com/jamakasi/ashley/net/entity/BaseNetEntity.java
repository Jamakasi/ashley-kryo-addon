package com.jamakasi.ashley.net.entity;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.jamakasi.ashley.net.entity.components.NetworkComponent;
import com.jamakasi.ashley.net.entity.components.TransformComponent;

/**
 *
 * @author jamakasi
 */
public abstract class BaseNetEntity extends BaseEntity{
    private int entityUID =-1;
    private int netEntityId =-1;
    
    public TransformComponent oldNetTransform = new TransformComponent();
    public TransformComponent nextNetTransform = new TransformComponent();
    public long lastUpdateTime;
    
    public BaseNetEntity(){
        super();
        this.add(new NetworkComponent());
    }
    public int getNetEntityID(){return this.netEntityId;};
    public int getEntityUniqueID(){return this.entityUID;};
    public void setEntityUniqueID(int uid){
        if(uid==-1) throw new GdxRuntimeException("Fatal Error: Setting -1 to entity unique id");
        if(this.entityUID!=-1) throw new GdxRuntimeException("Fatal Error: Entity has already been assigned id: "+this.netEntityId);
        
        this.entityUID=uid;
    };
    public void setNetEntityID(int id){
        if(this.netEntityId!=-1) throw new GdxRuntimeException("Fatal Error: Entity has already been assigned id: "+this.netEntityId);
        this.netEntityId=id;
    };
}

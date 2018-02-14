package com.jamakasi.ashley.net.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.esotericsoftware.minlog.Log;
import com.jamakasi.ashley.net.entity.components.NetworkComponent;
import com.jamakasi.ashley.net.systems.BaseNetClientSystem;
import com.jamakasi.ashley.net.systems.BaseNetServerSystem;

/**
 *
 * @author jamakasi
 */
public class NetEntityManager {
    protected IntMap<BaseNetEntity> idMap;
    
    protected IntMap<Class> entitys;
    
    protected Array<Integer> usedIDs;

    protected int nextEntityId;
    
    protected EntitySystem system;

    private boolean isServer;
    public NetEntityManager() {
        this.idMap = new IntMap<>();
        this.entitys = new IntMap<>();
        this.usedIDs = new Array<>();
    }
    
    public void init(EntitySystem system){
        this.system = system;
        if(this.system instanceof BaseNetClientSystem){
            isServer=false;
        }else if(this.system instanceof BaseNetServerSystem){
            isServer=true;
        }else{
            throw new GdxRuntimeException("Fatal Error: NetEntityManager applied to unknown system: "+system.getClass()+"sllowed BaseNetClientSystem , BaseNetServerSystem");
        }
        this.system.getEngine().addEntityListener(new NetEntityListener());
        
    }
    public IntMap<BaseNetEntity> getNetEntitys(){
        return idMap;
    }
    
    private class NetEntityListener implements EntityListener{
        @Override
        public void entityAdded(Entity entity) {
            if(entity instanceof BaseNetEntity){
                //Set entity uid
                ((BaseNetEntity)entity).setEntityUniqueID(getId((BaseNetEntity)entity));
                if(isServer){
                    assignServerNetEntityID((BaseNetEntity)entity);
                }
            }
        }
        @Override
        public void entityRemoved(Entity entity) {
            if(entity instanceof BaseNetEntity){
                usedIDs.removeValue(((BaseNetEntity)entity).getNetEntityID(),true);
                idMap.remove(((BaseNetEntity)entity).getNetEntityID());
            }
        }
    }
     /**
     * Find next unique net entity id for sync server-client
     * Called only on server side
     * do not use it manually!!!
     * @param ent 
     */
    private void assignServerNetEntityID(BaseNetEntity ent) {
        if(!isServer){
            throw new GdxRuntimeException("Fatal Error: Client not allowed to generate net entity id: ");
        }
        while (usedIDs.contains(nextEntityId, false)) {
                nextEntityId++;
        }
        usedIDs.add(nextEntityId);
        int id = nextEntityId;
        
        ent.setNetEntityID(id);
        idMap.put(id, ent);
        
        //assignEntityID(ent, id);
    }
    /**
     * Return network entity from ashley engine by net id
     * 
     * @param id
     * @return entity or null
     */
    public BaseNetEntity getEntityByNetId(int id) {
        return idMap.get(id);
    }
    /**
     * Remove entity from engine by network id
     * @param id network entity id
     */
    public void removeEntityByNetId(int id) {
        BaseNetEntity ent = idMap.get(id);
        if (ent != null) {
            system.getEngine().removeEntity(ent);
        }
    }
    
    public void registerNetEntity(int uid,Class clazz){
        synchronized(entitys){
            entitys.put(uid, clazz);
        }
    }
    /**
     * Return unique network entity(Class) id from registred entity(Class)
     * @param ent
     * @return 
     */
    public int getId(BaseNetEntity ent){
        synchronized(entitys){
            for(Entry<Class> entry:entitys.entries()){
                if(entry.value.equals(ent.getClass()))return entry.key;
            }  
        }
        throw new GdxRuntimeException("NetEntityManager getId Class of entity "+ent.getClass()+" not registred! return id= -1");
    }
    
    /**
     * Create new network entity by unique entity id
     * @param uid unique entity(Class) id
     * @return 
     */
    public BaseNetEntity createNetEntityByUID(int uid, int netId){
        synchronized(entitys){
            Class clazz = entitys.get(uid);
            if(clazz==null){
                throw new GdxRuntimeException("NetEntityManager fail create instance of " + uid+ "class with this uid not registred");
            }
            try {
                for (Entity e : system.getEngine().getEntitiesFor(Family.one(NetworkComponent.class).get())) {
                    if (((BaseNetEntity)e).getNetEntityID() == netId) {
                        throw new GdxRuntimeException("Fatal Error: Cannot assign id to entity, other entity with id already exists: " + uid);
                    }
                }
                BaseNetEntity netEnt = (BaseNetEntity)entitys.get(uid).newInstance();
                netEnt.setNetEntityID(netId);
                idMap.put(netId, netEnt);
                return netEnt;
            } catch (InstantiationException | IllegalAccessException ex) {
                Log.error("NetEntityManager", ex);
                throw new GdxRuntimeException(ex);
            }
        }
    }
}

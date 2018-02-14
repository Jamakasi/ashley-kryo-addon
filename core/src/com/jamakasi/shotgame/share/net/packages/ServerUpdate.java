package com.jamakasi.shotgame.share.net.packages;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.jamakasi.ashley.net.entity.BaseNetEntity;
import com.jamakasi.ashley.net.entity.NetEntityManager;



/** Contains game state information that is sent over the net from Server to Client */
public class ServerUpdate{
    public int tickNum;
    public int playerInputTick; // the last input processed from the player who will receive this update
    public EntityUpdate[] entityUpdates;
    private boolean processed;
    //protected NetEntityManager entManager;

    public ServerUpdate() {
    }

    
    
    public ServerUpdate(NetEntityManager entManager) {
        //this.entManager =entManager;
        synchronized (entManager.getNetEntitys()) {
            entityUpdates = new EntityUpdate[entManager.getNetEntitys().size];
            int i = 0;
            for(BaseNetEntity ent : entManager.getNetEntitys().values()){
                EntityUpdate entUpd = new EntityUpdate();
                entUpd.entityId = ent.getNetEntityID();
                //entUpd.position = ent.getPosition();
                //entUpd.rotation = ent.getYaw();
                entityUpdates[i] = entUpd;
                i++;
            }
        }
    }
    
    /*public void applyUpdates() {
        if (processed) throw new GdxRuntimeException("serverUpdate already processed");
        for (EntityUpdate entUpdate : entityUpdates) {
            BaseNetEntity ent = entManager.getEntityById(entUpdate.entityId);
            if (ent != null) {
                if (Main.inst.client.player == null && ent.id == Main.inst.client.playerId) {
                        Main.inst.client.assignClientPlayerToId(ent.id);
                        Log.debug("Entity matched player id, entity was assigned to player: " + ent.id);
                }
                ent.interpolator.handleUpdateFromServer(entUpdate, playerInputTick);
            }
        }
        processed = true;
    }*/
}

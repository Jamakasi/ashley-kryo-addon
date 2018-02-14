package com.jamakasi.ashley.net.packet.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.jamakasi.ashley.net.packet.BaseNetPacket;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.jamakasi.ashley.net.entity.BaseNetEntity;
import com.jamakasi.ashley.net.entity.components.TransformComponent;
import com.jamakasi.ashley.net.packet.NetBasePacketProcessor;
import com.jamakasi.ashley.net.packet.impl.NetEntityPacket.Action;
import com.jamakasi.ashley.net.systems.BaseNetClientSystem.ClientHelper;
import com.jamakasi.ashley.net.systems.BaseNetServerSystem.ServerHelper;

/**
 * Sended only to client and drop on server
 * @author jamakasi
 */
public class NetEntUpdate extends BaseNetPacket{
    public int serverTick;
    //public int playerInputTick; // the last input processed from the player who will receive this update
    public EntityUpdate[] updates;

    public NetEntUpdate() {
    }
    
    public NetEntUpdate(int tick) {
        serverTick = tick;
    }
    public static class EntityUpdate{
        public int entityUID;
        public int netid;
        public TransformComponent transforms; 

        public EntityUpdate() {
            transforms = new TransformComponent();
        }
        
    }
    
    public static class NetEntUpdateProcessor implements NetBasePacketProcessor<NetEntUpdate>{
        
        final Array<NetEntUpdate> incomingToUpdate = new Array<>();
        int lastServerTick;
        
        @Override
        public void recievedClientSide(Connection connection, NetEntUpdate object , ClientHelper helper) {
            if(lastServerTick <= object.serverTick){ //
                lastServerTick = object.serverTick;
                synchronized(incomingToUpdate){
                    incomingToUpdate.add(object);
                }
            }else{
                Log.info("update packet"," old packet lastServerTick:"+lastServerTick+" object.serverTick:"+object.serverTick);
            }
        }

        @Override
        public void recievedServerSide(Connection connection, NetEntUpdate object,ServerHelper helper) {
            //just ignore
           
        }


        @Override
        public void updateServer(ServerHelper helper) {
            //send updates to all client
            synchronized(helper.getNetEntitys()){
                IntMap<BaseNetEntity> entitys = helper.getNetEntitys();
                EntityUpdate[] updates = new EntityUpdate[entitys.size];
                int i = 0;
                for (Entry<BaseNetEntity> netEnt: entitys.entries()) {
                    EntityUpdate update = new EntityUpdate();
                    update.entityUID = netEnt.value.getEntityUniqueID();
                    update.netid = netEnt.value.getNetEntityID();
                    update.transforms.position.set(netEnt.value.getPosition());
                    update.transforms.size.set(netEnt.value.getScale());
                    update.transforms.pitchYawRoll.set(netEnt.value.getPitchYawRoll());
                    updates[i]=update;
                    i++;
                }
                NetEntUpdate netEntUpdate = new NetEntUpdate(helper.getTick());
                netEntUpdate.updates = updates;
                netEntUpdate.serverTick = helper.getTick();
                for(Connection conn: helper.connectedClients){
                    //netEntUpdate.playerInputTick = helper
                    conn.sendUDP(netEntUpdate);
                }
            }
        }
        
        
        @Override
        public void updateClient(ClientHelper helper) {
            synchronized(incomingToUpdate){
                for(NetEntUpdate ent:incomingToUpdate){
                    for(EntityUpdate update: ent.updates){
                        
                        BaseNetEntity netEnt = helper.getEntityByNetId(update.netid);
                        if(netEnt!=null){
                            //For interpolator
                            netEnt.oldNetTransform.set(netEnt.getTransform());
                            netEnt.nextNetTransform.set(update.transforms);
                        }else{
                            helper.sendTCPToServer(new NetEntityPacket(update.netid,helper.getTick(),Action.QUERRY_FROM_SERVER));
                        }
                    }
                }
                incomingToUpdate.clear();
            }
        }
        
    }
}

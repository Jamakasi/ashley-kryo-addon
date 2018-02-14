package com.jamakasi.ashley.net.packet.impl;

import com.badlogic.gdx.Gdx;
import com.jamakasi.ashley.net.packet.BaseNetPacket;
import com.esotericsoftware.kryonet.Connection;
import com.jamakasi.ashley.net.entity.BaseNetEntity;
import com.jamakasi.ashley.net.packet.NetBasePacketProcessor;
import static com.jamakasi.ashley.net.packet.impl.NetEntityPacket.Action.QUERRY_FROM_SERVER;
import com.jamakasi.ashley.net.systems.BaseNetClientSystem.ClientHelper;
import com.jamakasi.ashley.net.systems.BaseNetServerSystem.ServerHelper;

/**
 * Sended to client for create entity
 * @author jamakasi
 */
public class NetEntityPacket extends BaseNetPacket{
    public static enum Action{
        CREATE,
        DESTROY,
        QUERRY_FROM_SERVER
    }
    //for CREATE and DESTROY unique entity id, for QUERRY_FROM_SERVER clientID
    public int entityUID;
    //unique instance entity id
    public int netid;
    //for CREATE and DESTROY server tick, for QUERRY_FROM_SERVER client tick
    public int tick;
    public Action action;

            
            
    public NetEntityPacket() {
    }
    
    public NetEntityPacket(BaseNetEntity netEntity,int tick,Action action) {
        this.entityUID = netEntity.getEntityUniqueID();
        this.netid = netEntity.getNetEntityID();
        this.tick = tick;
        this.action = action;
    }
    public NetEntityPacket(int netId,int tick,Action action) {
        this.netid = netId;
        this.tick = tick;
        this.action = action;
    }
    
    public static class NetEntityPacketProcessor implements NetBasePacketProcessor<NetEntityPacket>{
        @Override
        public void recievedClientSide(Connection connection, final NetEntityPacket object,final ClientHelper helper) {
            switch(object.action){
                case CREATE:{
                    if(helper.getEntityByNetId(object.netid)==null){
                        //Invoke later because entity may want opengl thread
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                BaseNetEntity netEnt = (BaseNetEntity) helper.createNetEntityByUID(object.entityUID,object.netid);
                                helper.getEngine().addEntity(netEnt);
                            }
                        });
                        
                        System.err.println("client entity recieved and added");
                    }else{
                        System.err.println("Client recieved exist entity: "+object.netid);
                    }
                    break;
                }
                case DESTROY:{
                    helper.removeEntityByNetId(object.netid);
                    break;
                }
            }
        }

        @Override
        public void recievedServerSide(Connection connection, NetEntityPacket object , ServerHelper helper) {
            if(object.action == QUERRY_FROM_SERVER){
                BaseNetEntity entity = helper.getEntityByNetId(object.netid);
                if(entity!=null){
                    
                    for(Connection conn:helper.connectedClients){
                        if(conn.getID()==object.entityUID){
                            connection.sendTCP(new NetEntityPacket(entity, helper.getTick(),Action.CREATE));
                        }
                    }
                }
            }
        }


        @Override
        public void updateServer(ServerHelper helper) {

        }

        @Override
        public void updateClient(ClientHelper helper) {

        }
        
    }
}

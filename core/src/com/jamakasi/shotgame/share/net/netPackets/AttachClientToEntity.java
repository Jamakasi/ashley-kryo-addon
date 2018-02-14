package com.jamakasi.shotgame.share.net.netPackets;

import com.esotericsoftware.kryonet.Connection;
import com.jamakasi.ashley.net.entity.BaseNetEntity;
import com.jamakasi.ashley.net.entity.BaseNetEntityPlayer;
import com.jamakasi.ashley.net.packet.BaseNetPacket;
import com.jamakasi.ashley.net.packet.NetBasePacketProcessor;
import com.jamakasi.ashley.net.systems.BaseNetClientSystem;
import com.jamakasi.ashley.net.systems.BaseNetServerSystem;
import com.jamakasi.shotgame.share.entSystems.RenderSystem;
import com.jamakasi.shotgame.share.entitys.TestPlayerCamera;

/**
 *
 * @author jamakasi
 */
public class AttachClientToEntity extends BaseNetPacket{
    public int playerEntityId;

    public AttachClientToEntity() {
    }

    public AttachClientToEntity(int playerEntityId) {
        this.playerEntityId = playerEntityId;
    }
    
    
    public static class AttachClientToEntityProcessor implements NetBasePacketProcessor<AttachClientToEntity>{

        AttachClientToEntity attach ;
        boolean attached =false;
        
        @Override
        public void updateServer(BaseNetServerSystem.ServerHelper helper) {
            //noop
        }

        @Override
        public void updateClient(BaseNetClientSystem.ClientHelper helper) {
            if(attached==false && attach!=null){
                synchronized(attach){
                    BaseNetEntity netEnt = helper.getEntityByNetId(attach.playerEntityId);
                    if(netEnt!=null){
                        TestPlayerCamera attachCam = new TestPlayerCamera(netEnt,
                        helper.getEngine().getSystem(RenderSystem.class).getCamera());
                        helper.getEngine().addEntity(attachCam);
                        helper.playerEntity = (BaseNetEntityPlayer)netEnt;
                        attached = true;
                    }
                    
                }
            }
        }

        @Override
        public void recievedClientSide(Connection connection, AttachClientToEntity object ,BaseNetClientSystem.ClientHelper helper) {
                attach = object;
        }

        @Override
        public void recievedServerSide(Connection connection, AttachClientToEntity object,BaseNetServerSystem.ServerHelper helper) {
            //noop
        }
        
    }
}

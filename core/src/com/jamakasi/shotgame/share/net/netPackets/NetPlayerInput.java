package com.jamakasi.shotgame.share.net.netPackets;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.jamakasi.ashley.net.entity.BaseNetEntityPlayer;
import com.jamakasi.ashley.net.packet.BaseNetPacket;
import com.jamakasi.ashley.net.packet.NetBasePacketProcessor;
import com.jamakasi.ashley.net.systems.BaseNetClientSystem;
import com.jamakasi.ashley.net.systems.BaseNetServerSystem;
import static com.jamakasi.shotgame.share.entSystems.ImplNetServerSystem.playersEntity;

/**
 *
 * @author jamakasi
 */
public class NetPlayerInput extends BaseNetPacket{
    public int dir; //0 up 1 down 2 left 3 right
    public int connectionId; 

    public NetPlayerInput() {
    }

    public NetPlayerInput(int dir) {
        this.dir = dir;
    }
    
    
    
    public static class NetPlayerInputProcessor implements NetBasePacketProcessor<NetPlayerInput>{
        
        final Array<NetPlayerInput> playersInput = new Array<>();
        
        @Override
        public void updateServer(BaseNetServerSystem.ServerHelper helper) {
            synchronized(playersInput){
                for(NetPlayerInput inp: playersInput){
                    BaseNetEntityPlayer plrControlledEntity = (BaseNetEntityPlayer)playersEntity.get(inp.connectionId);
                    if(plrControlledEntity!=null){
                        plrControlledEntity.moveTo(inp.dir);
                    }
                }
                playersInput.clear();
            }
        }

        @Override
        public void updateClient(BaseNetClientSystem.ClientHelper helper) {
            
        }

        @Override
        public void recievedClientSide(Connection connection, NetPlayerInput object,BaseNetClientSystem.ClientHelper helper) {
            //noop    
        }

        @Override
        public void recievedServerSide(Connection connection, NetPlayerInput object,BaseNetServerSystem.ServerHelper helper) {
            synchronized(playersInput){
                object.connectionId = connection.getID();
                playersInput.add(object);
            }
        }
    }
}

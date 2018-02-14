package com.jamakasi.shotgame.share.entSystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntMap;
import com.esotericsoftware.kryonet.Connection;
import com.jamakasi.ashley.net.systems.BaseNetServerSystem;
import com.jamakasi.shotgame.share.entitys.TestDummyPlayer;
import com.jamakasi.shotgame.share.net.netPackets.AttachClientToEntity;
import com.jamakasi.shotgame.share.net.netPackets.NetPlayerInput;

/**
 *
 * @author jamakasi
 */
public class ImplNetServerSystem extends BaseNetServerSystem{

    public static IntMap<Entity> playersEntity; //key - connectionID val - entity net id
    
    public ImplNetServerSystem() {
        //   1/ 60.0f =  0.01f         ; 60fps
        //   1/ 20.0f =  0.05f         ; 20fps like overwatch
        super(20);
        playersEntity = new IntMap<>();
        registerNetEntity(0, TestDummyPlayer.class);
        registerNetPacket(AttachClientToEntity.class, new AttachClientToEntity.AttachClientToEntityProcessor());
        registerNetPacket(NetPlayerInput.class, new NetPlayerInput.NetPlayerInputProcessor());
    }

    
    
    @Override
    public void handleConnected(final Connection connection) {
        super.handleConnected(connection);
        Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            TestDummyPlayer player = new TestDummyPlayer();
            getEngine().addEntity(player);
            playersEntity.put(connection.getID(), player);
            sendTCP(connection, new AttachClientToEntity(player.getNetEntityID()));
            }
        });
        
        
    }

    @Override
    public void handleDisconnected(Connection connection) {
        super.handleDisconnected(connection);
        if(playersEntity.get(connection.getID())!=null){
            getEngine().removeEntity(playersEntity.get(connection.getID()));
            playersEntity.remove(connection.getID());
        }
        
    }

    @Override
    protected void handleUpdateInterval() {
        
    }
    
}

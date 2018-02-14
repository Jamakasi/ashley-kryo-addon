package com.jamakasi.shotgame.share.entSystems;

import com.esotericsoftware.kryonet.Connection;
import com.jamakasi.ashley.net.systems.BaseNetClientSystem;
import com.jamakasi.shotgame.share.entitys.TestDummyPlayer;
import com.jamakasi.shotgame.share.net.netPackets.AttachClientToEntity;
import com.jamakasi.shotgame.share.net.netPackets.NetPlayerInput;

/**
 *
 * @author jamakasi
 */
public class ImplNetClientSystem extends BaseNetClientSystem{

    public ImplNetClientSystem() {
        //      ; 60fps
        //         ; 20fps like overwatch
        super(20);
        registerNetEntity(0, TestDummyPlayer.class);
        registerNetPacket(AttachClientToEntity.class, new AttachClientToEntity.AttachClientToEntityProcessor());
        registerNetPacket(NetPlayerInput.class, new NetPlayerInput.NetPlayerInputProcessor());
    }

    @Override
    protected void handleUpdateInterval() {
        
    }

    @Override
    protected void handleConnected(Connection connection) {
        
    }

    @Override
    protected void handleDisconnected(Connection connection) {
        
    }

    
}

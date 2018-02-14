package com.jamakasi.ashley.net.entity;

import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.jamakasi.ashley.net.packet.BaseNetPacket;
import com.jamakasi.ashley.net.packet.NetBasePacketProcessor;
import com.jamakasi.ashley.net.systems.BaseNetClientSystem.ClientHelper;
import com.jamakasi.ashley.net.systems.BaseNetServerSystem.ServerHelper;

/**
 *
 * @author jamakasi
 */
public class NetPacketManager {
    private final ObjectMap<Class,NetBasePacketProcessor> map;
    private final Kryo kryo;

    
    public NetPacketManager(Kryo k) {
        kryo = k;
        map = new ObjectMap<>();
    }

    public void registerKryoNetPacket(Class packetClass, NetBasePacketProcessor packetProcessor){
        map.put(packetClass, packetProcessor);
        kryo.register(packetClass);
    }
    public boolean processPacketServer(Connection connection, BaseNetPacket object,ServerHelper helper){
        NetBasePacketProcessor processor =map.get(object.getClass());
        if(processor!=null){
            processor.recievedServerSide(connection, object, helper);
            return true;
        }
        return false;
    }
    public boolean processPacketClient(Connection connection, BaseNetPacket object,ClientHelper helper){
        NetBasePacketProcessor processor =map.get(object.getClass());
        if(processor!=null){
            processor.recievedClientSide(connection, object, helper);
            return true;
        }
        return false;
    }

    public void processUpdateServer(ServerHelper helper) {
        for(NetBasePacketProcessor processor: map.values()){
            processor.updateServer(helper);
        }
    }
    public void processUpdateClient(ClientHelper helper) {
        for(NetBasePacketProcessor processor: map.values()){
            processor.updateClient(helper);
        }
    }
}

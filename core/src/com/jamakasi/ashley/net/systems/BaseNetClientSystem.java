package com.jamakasi.ashley.net.systems;

import com.badlogic.ashley.core.Engine;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.jamakasi.ashley.net.entity.BaseNetEntity;
import com.jamakasi.ashley.net.entity.BaseNetEntityPlayer;
import com.jamakasi.ashley.net.packet.BaseNetPacket;
import com.jamakasi.shotgame.Constants;
import java.io.IOException;

/**
 *
 * @author jamakasi
 */
public abstract class BaseNetClientSystem extends AbstractNetSystem{

    public ClientHelper clientHelper;
    public BaseNetClientSystem(int interval) {
        super(interval,new Client());
        
    }
    
    @Override
    public void postAddedToEngine(Engine engine) {
        clientHelper = new ClientHelper(engine);
    }
    
    @Override
    protected void updateInterval(){
        super.updateInterval();
        netPacketFabrick.processUpdateClient(clientHelper);
    }
    
    @Override
    public void recievedBaseNetPacket(Connection connection, BaseNetPacket object){
        if(netPacketFabrick.processPacketClient(connection, object,clientHelper)){
        }else{
            Log.error("Client","received unregistred BaseNetPacket"+object);
        }
    }
    
    public void startClient(){
        startClient(5000,"127.0.0.1", Constants.tcpPort, Constants.udpPort);
    }
    public void startClient(int timeout,String ip,int tcpPort,int udpPort){
        Client client = (Client)endPoint;
        try {
            client.start();
            client.connect(timeout, ip, tcpPort, udpPort);
            
            Log.info("Client", "connected to:"+ip+" tcp:"+tcpPort+" ,udp:"+udpPort);
        } catch (IOException ex) {
            Log.error("Client", "Error connecting to server.", ex);
        }
    }
    
    public void stopClient(){
        Client client = (Client)endPoint;
        try {
            client.stop();
            client.dispose();
            Log.info("Client", "stopped.");
        } catch (IOException ex) {
            Log.error("Client", "Error stopping client.", ex);
        }
    }
    
    public void sendTCP(BaseNetPacket packet){
        ((Client)endPoint).sendTCP(packet);
    }
    public void sendUDP(BaseNetPacket packet){
        ((Client)endPoint).sendUDP(packet);
    }

    public class ClientHelper extends NetHelper{
        public BaseNetEntityPlayer playerEntity;
        
        public ClientHelper(Engine engine) {
            super(engine);
        }
        public void sendTCPToServer(BaseNetPacket packet){
            sendTCP(packet);
        }
        public void sendUDPToServer(BaseNetPacket packet){
            sendUDP(packet);
        }
        /**
        * Create new network entity by unique entity id
        * @param uid unique entity(Class) id
        * @param netId network entity id
        * @return 
        */
        public BaseNetEntity createNetEntityByUID(int uid, int netId){
            return netEntityManager.createNetEntityByUID(uid, netId);
        }
        
    }
}

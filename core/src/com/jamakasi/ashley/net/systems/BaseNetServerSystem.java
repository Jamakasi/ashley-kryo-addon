package com.jamakasi.ashley.net.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.jamakasi.ashley.net.entity.BaseNetEntity;
import com.jamakasi.ashley.net.packet.BaseNetPacket;
import com.jamakasi.ashley.net.packet.impl.NetEntityPacket;
import com.jamakasi.ashley.net.packet.impl.NetEntityPacket.Action;
import com.jamakasi.shotgame.Constants;
import java.io.IOException;

/**
 *
 * @author diman
 */
public abstract class BaseNetServerSystem extends AbstractNetSystem{
    Array<Connection> connectedClients;
    public ServerHelper serverHelper;
    
    public BaseNetServerSystem(int interval) {
        super(interval,new Server());
        connectedClients = new Array<>();
        
    }
    
    @Override
    protected void postAddedToEngine(Engine engine) {
        serverHelper = new ServerHelper(engine);
        serverHelper.connectedClients = connectedClients;
        engine.addEntityListener(new NetEntityListenerSrv());
    }
    
    @Override
    protected void updateInterval(){
        super.updateInterval();
        netPacketFabrick.processUpdateServer(serverHelper);
    }
    
    public void startServer(int tcpPort, int udpPort){
        Server server = (Server)endPoint;
        server.start();
        try {
            server.bind(tcpPort, udpPort);
            Log.info("Server", "start listening at: tcp:"+tcpPort+" ,udp:"+udpPort);
        } catch (IOException ex) {
            Log.error("Server", "Error starting server.", ex);
        }
    }
            
    public void startServer(){
        startServer(Constants.tcpPort,Constants.udpPort);
    }
    
    public void stopServer(){
        Server server = (Server)endPoint;
        try {
            server.stop();
            server.dispose();
            Log.info("Server", "stopped");
        } catch (IOException ex) {
            Log.error("Server", "Error stopping server.", ex);
        }
    }

    @Override
    protected void handleDisconnected(Connection connection){
        connectedClients.removeValue(connection,true);
    };

    @Override
    protected void handleConnected(Connection connection){
        connectedClients.add(connection);
    };
    
    @Override
    protected void recievedBaseNetPacket(Connection connection, BaseNetPacket object){
        if(netPacketFabrick.processPacketServer(connection, object,serverHelper)){
        }else{
            Log.error("Server","received unregistred BaseNetPacket"+object);
        }
    }
    
    public void sendTCP(Connection connection,BaseNetPacket packet){
        ((Server)endPoint).sendToTCP(connection.getID(),packet);
    }
    public void sendUDP(Connection connection,BaseNetPacket packet){
        ((Server)endPoint).sendToUDP(connection.getID(),packet);
    }
    
    public void sendAllTCP(BaseNetPacket packet){
        ((Server)endPoint).sendToAllTCP(packet);
    }
    public void sendAllUDP(BaseNetPacket packet){
        ((Server)endPoint).sendToAllUDP(packet);
    }
    
    private class NetEntityListenerSrv implements EntityListener{
        @Override
        public void entityAdded(Entity entity) {
            if(entity instanceof BaseNetEntity){
                //Notify all clients
                System.err.println("Server send NetEntCreate");
                sendAllTCP(new NetEntityPacket((BaseNetEntity)entity,getTickNum(),Action.CREATE));
            }
        }
        @Override
        public void entityRemoved(Entity entity) {
            System.err.println("Server send NetEntDestroy");
            if(entity instanceof BaseNetEntity){
                System.err.println("Server send NetEntDestroy");
                sendAllTCP(new NetEntityPacket((BaseNetEntity)entity,getTickNum(),Action.DESTROY));
            }
        }
    }
    
    
    public class ServerHelper extends NetHelper{
        public Array<Connection> connectedClients;

        public ServerHelper(Engine engine) {
            super(engine);
        }
        public void sendToTCP(Connection connection,BaseNetPacket packet){
            sendTCP(connection, packet);
        }
        public void sendToUDP(Connection connection,BaseNetPacket packet){
            sendUDP(connection, packet);
        }
        public void sendToAllTCP(BaseNetPacket packet){
            sendAllTCP(packet);
        }
        public void sendToAllUDP(BaseNetPacket packet){
            sendAllUDP(packet);
        }
    }
}

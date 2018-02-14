package com.jamakasi.ashley.net.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.LagListener;
import com.esotericsoftware.minlog.Log;
import com.jamakasi.ashley.net.entity.BaseNetEntity;
import com.jamakasi.ashley.net.entity.NetEntityManager;
import com.jamakasi.ashley.net.entity.NetPacketManager;
import com.jamakasi.ashley.net.entity.components.TransformComponent;
import com.jamakasi.ashley.net.packet.BaseNetPacket;
import com.jamakasi.ashley.net.packet.NetBasePacketProcessor;
import com.jamakasi.ashley.net.packet.impl.NetEntUpdate;
import com.jamakasi.ashley.net.packet.impl.NetEntUpdate.EntityUpdate;
import com.jamakasi.ashley.net.packet.impl.NetEntityPacket;
import com.jamakasi.ashley.net.packet.impl.NetEntityPacket.Action;

/**
 *
 * @author jamakasi
 */
public abstract class AbstractNetSystem extends IntervalSystem{
    EndPoint endPoint;
    int tickNum;
    float tickIntervalMS;
    int tickIntervalSEC;
    NetPacketManager netPacketFabrick;
    NetEntityManager netEntityManager;
    
    protected Listener listener;
    protected LagListener lagListener;

    
    public AbstractNetSystem(int tickIntervalSEC,EndPoint endPoint) {
        super((float)1/tickIntervalSEC);//1/tickIntervalSEC
        tickIntervalMS = (float)1/tickIntervalSEC;
        this.tickIntervalSEC = tickIntervalSEC;
        this.endPoint = endPoint;
        netEntityManager = new NetEntityManager();
        netPacketFabrick = new NetPacketManager(endPoint.getKryo());
        listener = new NetListener();
        endPoint.addListener(listener);
        endPoint.getKryo().register(Vector3.class); 
        endPoint.getKryo().register(EntityUpdate.class);
        endPoint.getKryo().register(EntityUpdate[].class);
        endPoint.getKryo().register(TransformComponent.class);
        endPoint.getKryo().register(Action.class);
        registerNetPacket(NetEntityPacket.class, new NetEntityPacket.NetEntityPacketProcessor());
        registerNetPacket(NetEntUpdate.class, new NetEntUpdate.NetEntUpdateProcessor());
    }

    public int getTickNum() {
        return tickNum;
    }
    
    public void registerNetPacket(Class packetClass, NetBasePacketProcessor packetProcessor){
        netPacketFabrick.registerKryoNetPacket(packetClass, packetProcessor);
    }
    public void registerNetEntity(int uniqueEntityID,Class entClass){
        netEntityManager.registerNetEntity(uniqueEntityID, entClass);
    }
    
    
    @Override
    public void addedToEngine(Engine engine) {
        netEntityManager.init(this);
        postAddedToEngine(engine);
    }
    
    
    @Override
    protected void updateInterval(){
        tickNum++;
        handleUpdateInterval();
    }
    protected abstract void postAddedToEngine(Engine engine);
    protected abstract void handleUpdateInterval();
    protected abstract void handleConnected(Connection connection);
    protected abstract void handleDisconnected(Connection connection);
    protected boolean handleReceived(Connection connection, Object object){return false;};
    protected void handleIdle(Connection connection){};
    protected abstract void recievedBaseNetPacket(Connection connection, BaseNetPacket object);
    
    public void setListenerToLag(int lagMillisMin, int lagMillisMax){
        endPoint.removeListener(listener);
        lagListener = new LagListener(lagMillisMin, lagMillisMax, listener);
        endPoint.addListener(lagListener);
    }
    public void setListenerToNormal(){
        endPoint.removeListener(lagListener);
        endPoint.addListener(listener);
    }
    
    private class NetListener extends Listener{ 
        @Override
        public void disconnected(Connection connection) {
            handleDisconnected(connection);
        }
        @Override
        public void connected(Connection connection) {
            handleConnected(connection);
        }
        
        @Override
        public void received (Connection connection, Object object) {
            if(handleReceived(connection, object)) return;
            if (object == null) {
                Log.error("NetSystem","received null object from kryonet");
                return;
            }
            if(object instanceof BaseNetPacket){
                BaseNetPacket packet = (BaseNetPacket)object;
                recievedBaseNetPacket(connection, packet);
            }else{
                if(!object.getClass().getName().contains("com.esotericsoftware.kryonet")){
                    Log.error("NetSystem", "unhandled object recieved: " + object);
                }
            }
        }
        @Override
        public void idle(Connection connection) {
            handleIdle(connection);
        }
    }
    
    public class NetHelper{
                
        private Engine engine;
        public NetHelper(Engine engine) {
            this.engine = engine;
        }
        public IntMap<BaseNetEntity> getNetEntitys(){
            return netEntityManager.getNetEntitys();
        }
        /**
         * 
         * @return ashley engine
         */
        public Engine getEngine(){
            return this.engine;
        }
        /**
         * Get current tick num
         * @return int
         */
        public int getTick(){
            return getTickNum();
        }
        /**
         * Get system update interval in ms
         * @return int
         */
        public float getTickIntervalMS(){
            return tickIntervalMS;
        }
        /**
         * Get system update interval in sec
         * @return int
         */
        public int getTickIntervalSEC(){
            return tickIntervalSEC;
        }
        /**
        * Return network entity from ashley engine by net id
        * 
        * @param netId
        * @return entity or null
        */
        public BaseNetEntity getEntityByNetId(int netId){
            return netEntityManager.getEntityByNetId(netId);
        }
        /**
        * Remove entity from engine by network id
        * @param netId network entity id
        */
        public void removeEntityByNetId(int netId){
            netEntityManager.removeEntityByNetId(netId);
        }
        
    }
}

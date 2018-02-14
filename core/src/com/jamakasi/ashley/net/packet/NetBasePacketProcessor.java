package com.jamakasi.ashley.net.packet;

import com.esotericsoftware.kryonet.Connection;
import com.jamakasi.ashley.net.systems.BaseNetClientSystem.ClientHelper;
import com.jamakasi.ashley.net.systems.BaseNetServerSystem;

/**
 *
 * @author jamakasi
 * @param <T>
 */
public interface NetBasePacketProcessor<T extends BaseNetPacket> {
    
    
    
    /**Calls on server every fixed interval
     * Works in GDX thread
     * @param helper
    */
    public void updateServer(BaseNetServerSystem.ServerHelper helper);
    
    /**Calls on client every fixed interval
     * Works in GDX thread
     * @param helper
    */
    public void updateClient(ClientHelper helper);
    
    /**Calls on client
     * Works in KryoNet thread
    * @param connection
    * @param object
    */
    public void recievedClientSide(Connection connection, T object, ClientHelper helper);
    
    /**Calls on server
     * Works in KryoNet thread
    * @param connection
    * @param object
    */
    public void recievedServerSide(Connection connection, T object, BaseNetServerSystem.ServerHelper helper);
}

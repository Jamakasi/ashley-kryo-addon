# ashley-kryo-addon
I'm trying to implement network support in ashley ecs with the help of kryonet and that's what happened at the moment.

Base components:
com.jamakasi.ashley.net.entity.components.TransformComponent - includes Vector3 position,size,pitchYawRoll . This is enough for 2d and 3d games.
com.jamakasi.ashley.net.entity.components.NetworkComponent - just marker

Based entity types:
com.jamakasi.ashley.net.entity.BaseEntity - includes TransformComponent . Serves as a starting point for all entities that have a position in the world.
com.jamakasi.ashley.net.entity.BaseNetEntity - extends BaseEntity. All inherited entities will be automatically synchronized with clients. The TransformComponent is synchronized, adding and deleting to ashley ecs.

Based ashley systems:
com.jamakasi.ashley.net.systems.AbstractNetSystem - "Magic"
com.jamakasi.ashley.net.systems.BaseNetServerSystem(int intervalSec) - extends AbstractNetSystem. This is the starting point for your server system. Tons of Magic.
com.jamakasi.ashley.net.systems.BaseNetClientSystem(int intervalSec) - extends AbstractNetSystem. This is the starting point for your client system. Tons of Magic. 
com.jamakasi.ashley.net.systems.BaseNetEntityClientInterpolator - Client system of naive linear interpolation of lags. Always add to the client.

Base network packets:
com.jamakasi.ashley.net.packet.BaseNetPacket - dummy packet. Always inherit your packages from it!
com.jamakasi.ashley.net.packet.impl.NetEntityPacket - magic, do not touch but watch. Create and destroy message to clients, querry server about unknown entity
com.jamakasi.ashley.net.packet.impl.NetEntUpdate - magic, do not touch but watch.  Sent to the client with entity updates( TransformComponent only! )
com.jamakasi.ashley.net.packet.NetBasePacketProcessor<T extends BaseNetPacket> - A specific type of packet handler. 

Using:
Client side:
extend BaseNetClientSystem. In the constructor, call super (time).Where time indicates the number of system updates in seconds. Example 20 = 20 times per second or 20 fps.
call registerNetEntity(int unique_entity_id, YouEntityClass.class extends BaseNetEntity) - register you entity
registerNetPacket(YouCustomNetPacket.class extends BaseNetPacket, new YouCustomNetPacketProcessor() extends NetBasePacketProcessor<YouCustomNetPacket>);
add system to ashley.
startClient(int timeout,String ip,int tcpPort,int udpPort).

Server side:
extend BaseNetServerSystem. In the constructor, call super (time).Where time indicates the number of system updates in seconds. Example 20 = 20 times per second or 20 fps.
call registerNetEntity(int unique_entity_id, YouEntityClass.class extends BaseNetEntity) - register you entity
registerNetPacket(YouCustomNetPacket.class extends BaseNetPacket, new YouCustomNetPacketProcessor() extends NetBasePacketProcessor<YouCustomNetPacket>);
add system to ashley.
call startServer(int tcpPort, int udpPort)

And this is all that is necessary!
for example see test demo project in com.jamakasi.shotgame. It includes simply menu, render, player controls. 
Menu buttons:
Local play - start local server and connect local client
Create server - not working
Client - connect to local server(If it created in another game via Local play)
Controls: w,s,a,d,up,down,left,right.

A bit technical part:
server always send NetEntityPacket trought TCP. On the client side is applied immediately
server always send NetEntUpdate trought UDP. On client side it buffered and apply in interval
 
Requirements;
libgdx,ashley ecs, kryonet. In build.gradle set sourceCompatibility = 1.7
package com.jamakasi.shotgame.share.net.packages;

/** When client gets unknown entity id, it asks server for the Entity info
 * so it can create the entity
 */
public class EntityInfoRequest {
    public int id;

    public static class Response {
        public int id;
        public int uid;
        //public Entity.EntityGraphicsType graphicsType;
        public boolean isPlayer;
        // add more things like health, speed, graphics type, etc
    }
}

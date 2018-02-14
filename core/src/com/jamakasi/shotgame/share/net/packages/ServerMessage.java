package com.jamakasi.shotgame.share.net.packages;

/**
 *
 * @author jamakasi
 */
public class ServerMessage {
    public static class AssignPlayerEntityId {
        public int id;
    }

    public static class DestroyEntity {
        public int id;
    }
}

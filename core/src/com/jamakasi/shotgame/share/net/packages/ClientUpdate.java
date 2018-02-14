package com.jamakasi.shotgame.share.net.packages;

/**
 *
 * @author jamakasi
 */
public class ClientUpdate{

    /** tick number this input was sent at, used for client-side prediction, not related to main server timing tick */
    public int inputTick;
    public int entityId;
    public CommandPackage cmdPack = new CommandPackage();

    public void set(ClientUpdate other) {
            entityId = other.entityId;
            cmdPack.commandBits = other.cmdPack.commandBits;
    }
    
}

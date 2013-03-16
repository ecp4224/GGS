package net.mcforge.networking.packets.clients;

import net.mcforge.networking.ClassicClientType;

/**
 * The type of Client the SimpleIOClient is.
 * This will help the PacketManager filter out packets that the Client does not support.
 * @author MCForgeTeam
 *
 */
public enum Client {
    /**
     * This SimpleIOClient support the Minecraft Classic Protocol only and possibly any other types of packets.
     * Check {@link ClassicClientType} for other packets this client might support.
     */
    MINECRAFT_CLASSIC,
    
    /**
     * This SimpleIOClient supports the Offical Minecraft protocol only and all other packets should be filtered out.
     */
    MINECRAFT,
    
    /**
     * This SimpleIOClient is a browser and all other protocols should be filtered out.
     */
    BROWSER;
}

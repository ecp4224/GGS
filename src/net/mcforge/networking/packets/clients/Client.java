package net.mcforge.networking.packets.clients;

import net.mcforge.networking.ClassicClientType;

/**
 * The type of Client the IOClient is.
 * This will help the PacketManager filter out packets that the Client does not support.
 * @author MCForgeTeam
 *
 */
public enum Client {
    /**
     * This IOClient support the Minecraft Classic Protocol only and possibly any other types of packets.
     * Check {@link ClassicClientType} for other packets this client might support.
     */
    MINECRAFT_CLASSIC,
    
    /**
     * This IOClient supports the Offical Minecraft protocol only and all other packets should be filtered out.
     */
    MINECRAFT,
    
    /**
     * This IOClient is a browser and all other protocols should be filtered out.
     */
    BROWSER;
}

package com.ep.ggs.networking.packets;

/**
 * This exception is thrown when the parameters given to a packet are invalid
 */
public class PacketParamException extends RuntimeException {
	private static final long serialVersionUID = -7429672883155088822L;
	
	private final DynamicPacket packet;
	private final Object[] args;

	public PacketParamException(DynamicPacket packet, Object[] args, String message) {
        super(message);
        this.packet = packet;
        this.args = args;
    }

    public PacketParamException(DynamicPacket packet, Object[] args, String message, Throwable throwable) {
        super(message, throwable);
        this.packet = packet;
        this.args = args;
    }
    
    /**
     * Returns the {@link DynamicPacket} that threw this exception.
     */
    public final DynamicPacket getThrowingPacket() {
    	return packet;
    }
    
    /**
     * Gets the invalid object array that was passed to this packet.
     */
    public final Object[] getPacketArguments() {
    	return args;
    }
}

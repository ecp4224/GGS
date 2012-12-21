package net.mcforge.networking.packets;

public class InvalidPacketCall extends RuntimeException {

    public InvalidPacketCall(String string) {
        super(string);
    }

    private static final long serialVersionUID = 1L;
}

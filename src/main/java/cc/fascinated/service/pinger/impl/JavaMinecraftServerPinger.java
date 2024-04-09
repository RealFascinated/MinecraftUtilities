package cc.fascinated.service.pinger.impl;

import cc.fascinated.Main;
import cc.fascinated.common.packet.impl.java.JavaPacketHandshakingInSetProtocol;
import cc.fascinated.common.packet.impl.java.JavaPacketStatusInStart;
import cc.fascinated.model.mojang.JavaServerStatusToken;
import cc.fascinated.model.server.JavaMinecraftServer;
import cc.fascinated.service.pinger.MinecraftServerPinger;
import lombok.extern.log4j.Log4j2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Braydon
 */
@Log4j2(topic = "Java Pinger")
public final class JavaMinecraftServerPinger implements MinecraftServerPinger<JavaMinecraftServer> {
    public static final JavaMinecraftServerPinger INSTANCE = new JavaMinecraftServerPinger();

    private static final int TIMEOUT = 3000; // The timeout for the socket

    @Override
    public JavaMinecraftServer ping(String hostname, int port) {
        log.info("Pinging {}:{}...", hostname, port);

        // Open a socket connection to the server
        try (Socket socket = new Socket()) {
            socket.setTcpNoDelay(true);
            socket.connect(new InetSocketAddress(hostname, port), TIMEOUT);

            // Open data streams to begin packet transaction
            try (DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                 DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
                // Begin handshaking with the server
                new JavaPacketHandshakingInSetProtocol(hostname, port, 47).process(inputStream, outputStream);

                // Send the status request to the server, and await back the response
                JavaPacketStatusInStart packetStatusInStart = new JavaPacketStatusInStart();
                packetStatusInStart.process(inputStream, outputStream);
                System.out.println("packetStatusInStart.getResponse() = " + packetStatusInStart.getResponse());
                JavaServerStatusToken token = Main.GSON.fromJson(packetStatusInStart.getResponse(), JavaServerStatusToken.class);
                return new JavaMinecraftServer(hostname, port, token.getDescription());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
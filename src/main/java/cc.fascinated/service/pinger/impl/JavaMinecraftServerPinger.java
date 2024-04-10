package cc.fascinated.service.pinger.impl;

import cc.fascinated.Main;
import cc.fascinated.common.DNSUtils;
import cc.fascinated.common.ServerUtils;
import cc.fascinated.common.packet.impl.java.JavaPacketHandshakingInSetProtocol;
import cc.fascinated.common.packet.impl.java.JavaPacketStatusInStart;
import cc.fascinated.exception.impl.BadRequestException;
import cc.fascinated.exception.impl.ResourceNotFoundException;
import cc.fascinated.model.mojang.JavaServerStatusToken;
import cc.fascinated.model.server.JavaMinecraftServer;
import cc.fascinated.model.server.MinecraftServer;
import cc.fascinated.service.pinger.MinecraftServerPinger;
import lombok.extern.log4j.Log4j2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * @author Braydon
 */
@Log4j2(topic = "Java Pinger")
public final class JavaMinecraftServerPinger implements MinecraftServerPinger<JavaMinecraftServer> {
    public static final JavaMinecraftServerPinger INSTANCE = new JavaMinecraftServerPinger();

    private static final int TIMEOUT = 3000; // The timeout for the socket

    @Override
    public JavaMinecraftServer ping(String hostname, int port) {
        InetAddress inetAddress = DNSUtils.resolveA(hostname); // Resolve the hostname to an IP address
        String ip = inetAddress == null ? null : inetAddress.getHostAddress(); // Get the IP address
        if (ip != null) { // Was the IP resolved?
            log.info("Resolved hostname: {} -> {}", hostname, ip);
        }
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
                JavaServerStatusToken token = Main.GSON.fromJson(packetStatusInStart.getResponse(), JavaServerStatusToken.class);
                return JavaMinecraftServer.create(hostname, ip, port, token);
            }
        } catch (IOException ex) {
            if (ex instanceof UnknownHostException) {
                throw new BadRequestException("Unknown hostname: %s".formatted(hostname));
            } else if (ex instanceof ConnectException || ex instanceof SocketTimeoutException) {
                throw new ResourceNotFoundException(ex);
            }
            log.error("An error occurred pinging %s".formatted(ServerUtils.getAddress(hostname, port)), ex);
        }
        return null;
    }
}
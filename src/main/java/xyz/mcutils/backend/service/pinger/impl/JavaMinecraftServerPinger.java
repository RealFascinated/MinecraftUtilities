package xyz.mcutils.backend.service.pinger.impl;

import xyz.mcutils.backend.Main;
import cc.fascinated.common.JavaMinecraftVersion;
import cc.fascinated.common.ServerUtils;
import cc.fascinated.common.packet.impl.java.JavaPacketHandshakingInSetProtocol;
import cc.fascinated.common.packet.impl.java.JavaPacketStatusInStart;
import cc.fascinated.exception.impl.BadRequestException;
import cc.fascinated.exception.impl.ResourceNotFoundException;
import cc.fascinated.model.dns.DNSRecord;
import cc.fascinated.model.server.JavaMinecraftServer;
import cc.fascinated.model.token.JavaServerStatusToken;
import xyz.mcutils.backend.service.pinger.MinecraftServerPinger;
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
    private static final int TIMEOUT = 1500; // The timeout for the socket

    @Override
    public JavaMinecraftServer ping(String hostname, String ip, int port, DNSRecord[] records) {
        log.info("Pinging {}:{}...", hostname, port);

        // Open a socket connection to the server
        try (Socket socket = new Socket()) {
            socket.setTcpNoDelay(true);
            socket.connect(new InetSocketAddress(hostname, port), TIMEOUT);

            // Open data streams to begin packet transaction
            try (DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                 DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
                // Begin handshaking with the server
                new JavaPacketHandshakingInSetProtocol(hostname, port, JavaMinecraftVersion.getMinimumVersion().getProtocol()).process(inputStream, outputStream);

                // Send the status request to the server, and await back the response
                JavaPacketStatusInStart packetStatusInStart = new JavaPacketStatusInStart();
                packetStatusInStart.process(inputStream, outputStream);
                JavaServerStatusToken token = Main.GSON.fromJson(packetStatusInStart.getResponse(), JavaServerStatusToken.class);
                return JavaMinecraftServer.create(hostname, ip, port, records, token);
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
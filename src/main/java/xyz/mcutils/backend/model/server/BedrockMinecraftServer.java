package xyz.mcutils.backend.model.server;

import lombok.*;
import xyz.mcutils.backend.model.dns.DNSRecord;

/**
 * A Bedrock edition {@link MinecraftServer}.
 *
 * @author Braydon
 */
@Getter @ToString(callSuper = true) @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public final class BedrockMinecraftServer extends MinecraftServer {
    /**
     * The unique ID of this server.
     */
    @EqualsAndHashCode.Include @NonNull private final String id;

    /**
     * The edition of this server.
     */
    @NonNull private final Edition edition;

    /**
     * The version information of this server.
     */
    @NonNull private final Version version;

    /**
     * The gamemode of this server.
     */
    @NonNull private final GameMode gamemode;

    private BedrockMinecraftServer(@NonNull String id, @NonNull String hostname, String ip, int port, @NonNull DNSRecord[] records,
                                   @NonNull Edition edition, @NonNull Version version, @NonNull Players players, @NonNull MOTD motd,
                                   @NonNull GameMode gamemode, GeoLocation location) {
        super(hostname, ip, port, records, motd, players, location);
        this.id = id;
        this.edition = edition;
        this.version = version;
        this.gamemode = gamemode;
    }

    /**
     * Create a new Bedrock Minecraft server.
     * <p>
     *     <a href="https://wiki.vg/Raknet_Protocol#Unconnected_Pong">Token Format</a>
     * </p>
     *
     * @param hostname the hostname of the server
     * @param ip the IP address of the server
     * @param port the port of the server
     * @param token the status token
     * @return the Bedrock Minecraft server
     */
    @NonNull
    public static BedrockMinecraftServer create(@NonNull String hostname, String ip, int port, DNSRecord[] records, GeoLocation location, @NonNull String token) {
        String[] split = token.split(";"); // Split the token
        Edition edition = Edition.valueOf(split[0]);
        Version version = new Version(Integer.parseInt(split[2]), split[3]);
        Players players = new Players(Integer.parseInt(split[4]), Integer.parseInt(split[5]), null);
        MOTD motd = MOTD.create(split[1] + "\n" + split[7]);
        GameMode gameMode = new GameMode(split[8], split.length > 9 ? Integer.parseInt(split[9]) : -1);
        return new BedrockMinecraftServer(
                split[6],
                hostname,
                ip,
                port,
                records,
                edition,
                version,
                players,
                motd,
                gameMode,
                location
        );
    }

    /**
     * The edition of a Bedrock server.
     */
    @AllArgsConstructor @Getter
    public enum Edition {
        /**
         * Minecraft: Pocket Edition.
         */
        MCPE,

        /**
         * Minecraft: Education Edition.
         */
        MCEE
    }

    /**
     * Version information for a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class Version {
        /**
         * The protocol version of the server.
         */
        private final int protocol;

        /**
         * The version name of the server.
         */
        @NonNull private final String name;
    }

    /**
     * The gamemode of a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class GameMode {
        /**
         * The name of this gamemode.
         */
        @NonNull private final String name;

        /**
         * The numeric of this gamemode.
         */
        private final int numericId;
    }
}

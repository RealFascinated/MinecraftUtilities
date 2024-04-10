package cc.fascinated.model.mojang;

import cc.fascinated.model.server.JavaMinecraftServer;
import cc.fascinated.model.server.MinecraftServer;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class JavaServerStatusToken {

    /**
     * The version of the server.
     */
    private final JavaMinecraftServer.Version version;

    /**
     * The players on the server.
     */
    private final MinecraftServer.Players players;

    /**
     * The mods running on this server.
     */
    @SerializedName("modinfo")
    private JavaMinecraftServer.ForgeModInfo modInfo;

    /**
     * The mods running on this server.
     * <p>
     *     This is only used for servers
     *     running 1.13 and above.
     * </p>
     */
    private JavaMinecraftServer.ForgeData forgeData;

    /**
     * The motd of the server.
     */
    private final Object description;

    /**
     * The favicon of the server.
     */
    private final String favicon;
}

package com.adrianka.mcenhancedlib.server.world;

import net.minecraft.server.MinecraftServer;

/**
 * Provides utilities for modifying server-side world conditions.
 */
public class MCELModifyWorldServer {

    /**
     * Updates the server's weather conditions.
     *
     * @param server the Minecraft server
     * @param rainTime the weather duration in ticks
     * @param raining whether rain is enabled
     * @param thundering whether thunderstorms are enabled
     */
    public static void setWeather(MinecraftServer server, int rainTime, boolean raining, boolean thundering) {
        server.setWeatherParameters(0, rainTime, raining, thundering);
    }

    private MCELModifyWorldServer() {}
}
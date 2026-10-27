package com.adrianka.mcenhancedlib.world;

import net.minecraft.server.MinecraftServer;

public class ModifyWorldServer {
    public static void setWeather(MinecraftServer server, int rainTime, boolean raining, boolean thundering) {
        server.setWeatherParameters(0, rainTime, raining, thundering);
    }
}

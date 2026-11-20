package com.adrianka.mcenhancedlib;

import com.adrianka.mcenhancedlib.server.attachment.MCELAttachments;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

/**
 * Main entry point for MC Enhanced Lib.
 */
@Mod(MCEnhancedLIB.MODID)
public class MCEnhancedLIB {
    public static final String MODID = "mcenhancedlib";

    public static final Logger LOGGER = LogUtils.getLogger();

    public MCEnhancedLIB(IEventBus modEventBus, ModContainer modContainer) {
        MCELAttachments.ATTACHMENTS.register(modEventBus);
    }
}

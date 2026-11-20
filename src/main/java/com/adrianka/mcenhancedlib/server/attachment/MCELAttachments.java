package com.adrianka.mcenhancedlib.server.attachment;

import com.adrianka.mcenhancedlib.MCEnhancedLIB;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * Defines the attachment types provided by MC Enhanced Lib.
 */
public class MCELAttachments {

    /**
     * Registry for the library's attachment types.
     */
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(
                    NeoForgeRegistries.ATTACHMENT_TYPES,
                    MCEnhancedLIB.MODID
            );

    /**
     * Stores an additional health modifier for a player. The value is persistent and preserved after death.
     */
    public static final Supplier<AttachmentType<Double>> HEALTH_MODIFIED  =
            ATTACHMENTS.register(
                    "modified_health",
                    () -> AttachmentType.builder(() -> 0.0)
                            .serialize(Codec.DOUBLE.fieldOf("modified_health"))
                            .copyOnDeath()
                            .build()
            );

    private MCELAttachments() {}
}
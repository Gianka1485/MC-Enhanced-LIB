package com.adrianka.mcenhancedlib.attachment;

import com.adrianka.mcenhancedlib.MCEnhancedLIB;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MCELAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(
                    NeoForgeRegistries.ATTACHMENT_TYPES,
                    MCEnhancedLIB.MODID
            );

    public static final Supplier<AttachmentType<Double>> HEALTH_MODIFIED  =
            ATTACHMENTS.register(
                    "modified_hearts",
                    () -> AttachmentType.builder(() -> 0.0)
                            .serialize(Codec.DOUBLE.fieldOf("modified_hearts"))
                            .copyOnDeath()
                            .build()
            );

    private MCELAttachments() {}
}

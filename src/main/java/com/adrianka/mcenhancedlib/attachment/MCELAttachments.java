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

    public static final Supplier<AttachmentType<Integer>> HEARTS_MODIFIED  =
            ATTACHMENTS.register(
                    "hearts_modified",
                    () -> AttachmentType.builder(() -> 0)
                            .serialize(Codec.INT.fieldOf("hearts_modified"))
                            .copyOnDeath()
                            .build()
            );

    private MCELAttachments() {}
}

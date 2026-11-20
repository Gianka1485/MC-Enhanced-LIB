package com.adrianka.mcenhancedlib.server.entity;

import com.adrianka.mcenhancedlib.MCEnhancedLIB;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.List;

/**
 * Provides utilities for managing player effects, attribute modifiers,
 * and their associated attachment values.
 */
public class MCELModifyPlayerProperties {

    /**
     * Applies multiple effects to a player using a shared duration and amplifier.
     *
     * @param player the player receiving the effects
     * @param duration the duration of each effect in ticks
     * @param amplifier the effect amplifier, starting at 0 for level I
     * @param effects the collection of effects to apply
     */
    public static void addPlayerEffects(
            ServerPlayer player,
            int duration,
            int amplifier,
            List<Holder<MobEffect>> effects
    ) {
        for (Holder<MobEffect> effect : effects) player.addEffect(new MobEffectInstance(effect, duration, amplifier));
    }

    /**
     * Restores an attachment to its base value and removes its associated permanent attribute modifier.
     *
     * @param player the player whose properties will be reset
     * @param baseValue the value assigned to the attachment
     * @param attachment the attachment storing the modifier value
     * @param attribute the attribute containing the modifier
     * @param modID the namespace of the modifier identifier
     * @param modifierID the path of the modifier identifier
     */
    public static void resetPlayerAttributeAndAttachment(
            ServerPlayer player,
            double baseValue,
            AttachmentType<Double> attachment,
            Holder<Attribute> attribute,
            String modID, String modifierID
    ) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);

        GameProfile playerProfile = player.getGameProfile();

        if (isAttributeUnavailable(attributeInstance, playerProfile, modifierID, "reset")) return;

        player.setData(attachment, baseValue);

        attributeInstance.removeModifier(Identifier.fromNamespaceAndPath(modID, modifierID));

        MCEnhancedLIB.LOGGER.info(
                "Reset {} attachment for {}. UUID = {}",
                modifierID,
                playerProfile.name(),
                playerProfile.id()
        );
    }

    /**
     * Updates a player's attachment value while keeping it within the specified minimum and maximum limits.
     *
     * @param player the player whose attachment will be updated
     * @param attachment the attachment storing the value
     * @param modifier the amount added to the current value
     * @param minModifier the minimum permitted value
     * @param maxModifier the maximum permitted value
     */
    public static void updatePlayerStatsAttachments(
            ServerPlayer player,
            AttachmentType<Double> attachment,
            double modifier, double minModifier, double maxModifier
    ) {
        double attachmentValue = player.getData(attachment);
        double newValue = Math.clamp(attachmentValue + modifier, minModifier, maxModifier);

        if (Double.compare(attachmentValue, newValue) == 0) return;

        player.setData(attachment, newValue);
    }

    /**
     * Applies the value stored in an attachment as a permanent additive
     * modifier to a player's attribute.
     *
     * <p>Existing modifiers with the same identifier are replaced.
     * Update the attachment before calling this method.</p>
     *
     * @param player the player receiving the attribute modifier
     * @param attachment the attachment containing the modifier value
     * @param attribute the attribute to modify
     * @param modID the namespace of the modifier identifier
     * @param modifierID the path of the modifier identifier
     */
    public static void applyPlayerPermanentModifier(
            ServerPlayer player,
            AttachmentType<Double> attachment,
            Holder<Attribute> attribute,
            String modID, String modifierID
    ) {
        GameProfile playerProfile = player.getGameProfile();

        AttributeInstance attributeInstance = player.getAttribute(attribute);

        if (isAttributeUnavailable(attributeInstance, playerProfile, modifierID, "apply")) return;

        double attachmentValue = player.getData(attachment);

        attributeInstance.addOrReplacePermanentModifier(
                new AttributeModifier(
                        Identifier.fromNamespaceAndPath(modID, modifierID),
                        attachmentValue,
                        AttributeModifier.Operation.ADD_VALUE
                ));

        MCEnhancedLIB.LOGGER.info(
                "{} modifier edited for {}: value = {}, mod ID = {}, UUID = {}",
                modifierID,
                playerProfile.name(),
                attachmentValue,
                modID,
                playerProfile.id()
        );
    }

    private static boolean isAttributeUnavailable(
            AttributeInstance attributeInstance,
            GameProfile playerProfile,
            String modifierID,
            String operation
    ) {
        if (attributeInstance != null) return false;

        MCEnhancedLIB.LOGGER.warn(
                "Cannot {} modifier {} for {}: attribute is unavailable.",
                operation,
                modifierID,
                playerProfile.name()
        );

        return true;
    }

    private MCELModifyPlayerProperties() {}
}
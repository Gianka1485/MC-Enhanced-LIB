package com.adrianka.mcenhancedlib.entity;

import com.adrianka.mcenhancedlib.MCEnhancedLIB;
import com.adrianka.mcenhancedlib.attachment.MCELAttachments;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.List;

public class MCELModifyPlayerProperties {
    /**
     * @param effects Create a list of type {@code List<Holder<MobEffect>>} that stores effects such as {@code MobEffects.SPEED}.
     * @param amplifier A value of 0 corresponds to a standard potion, and a value of 1 corresponds to a level 2 potion;
     *                  values greater than 5 can become uncontrollable (as is the case with {@code MobEffects.SPEED}), so increase the value with caution.
     */
    public static void addPlayerEffects(ServerPlayer player, int duration, int amplifier, List<Holder<MobEffect>> effects) {
        for (Holder<MobEffect> effect : effects)
            player.addEffect(new MobEffectInstance(effect, duration, amplifier));
    }

    /**
     * This method is used to reset the values of attachments related to a player's attributes.
     * Reset the attachment value and remove the player attribute modifier.
     * @param baseValue This is the base value to which the attachment value will be assigned;
     *                  it is recommended not to use a base value different from the one declared when the attachment was created.
     * @param attribute Receives attributes related to the player—for example, {@code Attributes.MAX_HEALTH}.
     *                  These attributes must be related to the {@code attachment} to be reset.
     * <p><b>Important:</b> {@code modID} and {@code modifierID} it must be the same as the one declared when creating the modifier identifier,
     * in order to avoid issues related to 'NullPointerException'.
     */
    public static void resetPlayerAttributeAndAttachment(
            ServerPlayer player, double baseValue, AttachmentType<Double> attachment, Holder<Attribute> attribute, String modID, String modifierID
    ) {
        GameProfile playerProfile = player.getGameProfile();

        player.setData(attachment, baseValue);
        player.getAttribute(attribute).removeModifier(Identifier.fromNamespaceAndPath(modID, modifierID));

        MCEnhancedLIB.LOGGER.info(
                "Reset {} attachment for {}. UUID = {}",
                modifierID,
                playerProfile.name(),
                playerProfile.id()
        );
    }

    /**
     * This method is used to modify the values of attachments related to the player; execute this method before modifying any of a player's attributes.
     * @param attachment You should be able to modify any attachment related to the player—even from other solutions,
     *                   though it was designed with {@link MCELAttachments} in mind. Should any issues arise,
     *                   please do not hesitate to report them.
     */
    public static void updatePlayerStatsAttachments(
            ServerPlayer player, AttachmentType<Double> attachment, double modifier, double minModifier, double maxModifier
    ) {
        double attachmentValue = player.getData(attachment);

        if (attachmentValue <= minModifier && modifier < 0.0) return;

        player.setData(attachment, Math.clamp(attachmentValue + modifier, minModifier, maxModifier));
    }

    /**
     * This method applies modifications to player-related attributes, such as {@code Attributes.MAX_HEALTH}.
     * Use this method once you have already modified the player-related attachments using {@link updatePlayerStatsAttachments}.
     * <p><b>Important:</b> {@code modID} and {@code modifierID} They must be related to the origin of the related attachments;
     * if the attachment used comes from your mod, please use its {@code MODID} from your main class,
     * also use a related name in {@code modifierID}.
     */
    public static void applyPlayerPermanentModifier(
            ServerPlayer player, AttachmentType<Double> attachment, Holder<Attribute> attribute, String modID, String modifierID
    ) {
        double attachmentValue = player.getData(attachment);

        GameProfile playerProfile = player.getGameProfile();

        player.getAttribute(attribute).addOrReplacePermanentModifier(
                new AttributeModifier(
                        Identifier.fromNamespaceAndPath(modID, modifierID), attachmentValue, AttributeModifier.Operation.ADD_VALUE
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

    private MCELModifyPlayerProperties() {}
}

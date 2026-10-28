package com.adrianka.mcenhancedlib.entity;

import com.adrianka.mcenhancedlib.MCEnhancedLIB;
import com.adrianka.mcenhancedlib.attachment.Attachments;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class ModifyEntityProperties {
    /**
     * @param effects Create a list of type {@code List<Holder<MobEffect>>} that stores effects such as MobEffects.SPEED.
     */
    public static void addPlayerEffects(ServerPlayer player, int duration, int amplifier, List<Holder<MobEffect>> effects) {
        for (Holder<MobEffect> effect : effects)
            player.addEffect(new MobEffectInstance(effect, duration, amplifier));
    }

    /**
     * @param mob First, check whether "mob" is the correct type of mob you want to modify—for example, by verifying whether it is of type Enemy, UNDEAD, Animal, etc.
     * @param effects Create a list of type {@code List<Holder<MobEffect>>} that stores effects such as MobEffects.SPEED.
     */
    public static void addMobEffects(Mob mob, int duration, int amplifier, List<Holder<MobEffect>> effects) {
        for (Holder<MobEffect> effect : effects)
            mob.addEffect(new MobEffectInstance(effect, duration, amplifier));
    }

    private static final Identifier HEARTS_COUNT_ID = Identifier.fromNamespaceAndPath(MCEnhancedLIB.MODID, "hearts_count");

    public static void resetPlayerMaxHealth(ServerPlayer player) {
        GameProfile playerProfile = player.getGameProfile();

        player.setData(Attachments.HEARTS_MODIFIED, 0);
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(HEARTS_COUNT_ID);

        MCEnhancedLIB.LOGGER.info(
                "Reseted hearts for {}. UUID = {}",
                playerProfile.name(),
                playerProfile.id()
        );
    }

    /**
     * @param hearts The total number of hearts to be modified—for example, a value of 1 corresponds to 1 heart, or 2 units of health.
     */
    public static void updatePlayerMaxHealth(ServerPlayer player, int hearts) {
        int heartsModified = player.getData(Attachments.HEARTS_MODIFIED);

        /*  minTotalHearts will be modified in the future to change the minimum number of negative hearts,
            in order to avoid errors related to negative numbers beyond -9. */
        int minTotalHearts = -10 + 1;

        GameProfile playerProfile = player.getGameProfile();

        if (heartsModified <= minTotalHearts && hearts < 0) return;

        if (heartsModified + hearts < minTotalHearts)
            player.setData(Attachments.HEARTS_MODIFIED, heartsModified = minTotalHearts);
        else
            player.setData(Attachments.HEARTS_MODIFIED, heartsModified += hearts);

        player.getAttribute(Attributes.MAX_HEALTH).addOrReplacePermanentModifier(
                new AttributeModifier(HEARTS_COUNT_ID, heartsModified * 2.0, AttributeModifier.Operation.ADD_VALUE)
        );

        MCEnhancedLIB.LOGGER.info(
                "Hearts edited for {}: Hearts modified attachment = {}, Hearts variable = {}, UUID = {}",
                playerProfile.name(),
                heartsModified,
                hearts,
                playerProfile.id()
        );
    }

    private ModifyEntityProperties() {}
}

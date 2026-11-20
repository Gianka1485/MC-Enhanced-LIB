package com.adrianka.mcenhancedlib.server.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;

import java.util.List;

/**
 * Provides utilities for modifying entity properties.
 */
public class MCELModifyEntityProperties {

    /**
     * Applies a collection of effects to a mob, limiting Resistance to amplifier level 3.
     *
     * @param mob the mob receiving the effects
     * @param duration the duration of each effect in ticks
     * @param amplifier the amplifier applied to each effect
     * @param effects the collection of effects to apply
     */
    public static void addMobEffects(Mob mob, int duration, int amplifier, List<Holder<MobEffect>> effects) {
        for (Holder<MobEffect> effect : effects)
            mob.addEffect(
                    new MobEffectInstance(
                            effect,
                            duration,
                            effect.equals(MobEffects.RESISTANCE) && amplifier > 3 ? 3 : amplifier
                    ));
    }

    private MCELModifyEntityProperties() {}
}
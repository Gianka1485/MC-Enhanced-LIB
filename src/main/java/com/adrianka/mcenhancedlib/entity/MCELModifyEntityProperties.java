package com.adrianka.mcenhancedlib.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;

import java.util.List;

public class MCELModifyEntityProperties {
    /**
     * @param mob First, check whether "mob" is the correct type of mob you want to modify—for example,
     *            by verifying whether it is of type Enemy, UNDEAD, Animal, etc.
     * @param effects Create a list of type {@code List<Holder<MobEffect>>} that stores effects such as {@code MobEffects.SPEED}.
     */
    public static void addMobEffects(Mob mob, int duration, int amplifier, List<Holder<MobEffect>> effects) {
        for (Holder<MobEffect> effect : effects)
            mob.addEffect(new MobEffectInstance(effect, duration, amplifier));
    }

    private MCELModifyEntityProperties() {}
}

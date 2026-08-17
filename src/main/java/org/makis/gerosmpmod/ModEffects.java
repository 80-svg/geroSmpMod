package org.makis.gerosmpmod;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.makis.gerosmpmod.effect.KHoleEffect;

public class ModEffects {
    public static final Holder<MobEffect> KHole = registerMobEffect("khole",
            new KHoleEffect(MobEffectCategory.HARMFUL, 0xde601d));

    private static Holder<MobEffect> registerMobEffect(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, name), effect);
    }

    public static void registerEffects() {
        GeroSmpMod.LOGGER.info("Registering effects for " + GeroSmpMod.MOD_ID);
    }
}

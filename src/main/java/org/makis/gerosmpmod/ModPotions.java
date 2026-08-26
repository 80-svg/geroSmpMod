package org.makis.gerosmpmod;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotions {
    public static final Holder<Potion> ESKETAMINE = registerPotion("esketamine",
            new Potion("esketamine", new MobEffectInstance(ModEffects.KHole, 60 * 15, 1)));

    private static Holder<Potion> registerPotion(String name, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, name), potion);
    }
    public static void registerPotions() {
        GeroSmpMod.LOGGER.info("Registering Potions for " + GeroSmpMod.MOD_ID);
    }
}

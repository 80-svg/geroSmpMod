package org.makis.gerosmpmod;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class ModGamerules {
    public static void initialize() {}
    public static final GameRule<Boolean> ENABLE_VILLAGERS = GameRuleBuilder
            .forBoolean(false)
            .category(GameRuleCategory.MISC)
            .buildAndRegister(Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "enable_villagers"));
    public static final GameRule<Integer> MODVER_TIMEOUT_COOLDOWN_SECS = GameRuleBuilder
            .forInteger(2)
            .category(GameRuleCategory.MISC)
            .buildAndRegister(Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "timeout_secs"));
}

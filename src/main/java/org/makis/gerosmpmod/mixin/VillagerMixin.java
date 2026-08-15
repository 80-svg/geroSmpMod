package org.makis.gerosmpmod.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import org.makis.gerosmpmod.GeroSmpMod;
import org.makis.gerosmpmod.ModGamerules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public class VillagerMixin {
    /*
    @Inject(method = "getPlayerReputation", at = @At("HEAD"), cancellable = true)
    public void getPlayerReputation(Player player, CallbackInfoReturnable<Integer> cir) {
        if (player.getUUID() == GeroSmpMod.championUUID) {

        }
        cir.setReturnValue(0);
    }
    */
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!player.level().isClientSide()) {
            if (!player.level().getServer().getGameRules().get(ModGamerules.ENABLE_VILLAGERS)) {
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }
}

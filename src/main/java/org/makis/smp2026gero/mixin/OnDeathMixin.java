package org.makis.smp2026gero.mixins;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class OnDeathMixin {
    @Inject(at=@At("TAIL"), method = "die")
    private void onPlayerDeath(DamageSource source, CallbackInfo callbackInfo) {
        ServerPlayer player = (ServerPlayer)(Object)this;
    }
}

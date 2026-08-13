package org.makis.gerosmpmod.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.makis.gerosmpmod.ModAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class OnDeathMixin {

    @Inject(at=@At("TAIL"), method = "die")
    private void onPlayerDeath(DamageSource source, CallbackInfo callbackInfo) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        checkBounty(player, source);
    }

    @Unique
    private void checkBounty(ServerPlayer target, DamageSource source) {
        if (!target.hasAttached(ModAttachments.BOUNTY_AMOUNT)) {
            return;
        }
        if (target.getLastHurtByMob() == null) {
            return;
        }
        LivingEntity temp = target.getLastHurtByMob();
        if (!(temp instanceof ServerPlayer)) {
            return;
        }
        if (!source.isDirect()) {
            return;
        }
        if (target.hasAttached(ModAttachments.BOUNTY_AMOUNT)) {
            ServerPlayer attacker = (ServerPlayer) target.getLastHurtByMob();
            ServerPlayer fallen = target;
            int rewardAmount = target.getAttachedOrThrow(ModAttachments.BOUNTY_AMOUNT);
            target.removeAttached(ModAttachments.BOUNTY_AMOUNT);
            ItemStack stack = new ItemStack(Items.DIAMOND, rewardAmount);
            if (attacker != fallen) {
                attacker.getInventory().add(stack);
            }
        }
    }
}

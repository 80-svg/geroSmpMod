package org.makis.gerosmpmod.effect;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.makis.gerosmpmod.payloads.FpsCapPayload;

public class KHoleEffect extends MobEffect {
    private static final int CAPPED_FPS = 30;

    public KHoleEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
        if (mob instanceof ServerPlayer player) {

        }
        return super.applyEffectTick(serverLevel, mob, amplification);
    }

    @Override
    public void onEffectStarted(LivingEntity mob, int amplifier) {
        if (mob instanceof ServerPlayer player) {
            ServerPlayNetworking.send(player, new FpsCapPayload(true, CAPPED_FPS / amplifier));
        }
        super.onEffectStarted(mob, amplifier);
    }

    @Override
    public void onEffectRemoved(MobEffectInstance effectInstance, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            ServerPlayNetworking.send(player, new FpsCapPayload(false, 60));
        }
        super.onEffectRemoved(effectInstance, entity);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        return true;
    }
}

package org.makis.gerosmpmod;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportManager {
    private static final Map<UUID, PendingTeleport> pendingTeleports = new HashMap<>();
    public record PendingTeleport(UUID playerUuid, Vec3 initialPos, ServerLevel targetLevel, Vec3 targetPos, int ticksRemaining) {}
    public static boolean requestTeleport(ServerPlayer player, ServerLevel level, Vec3 targetPos, int warmupSeconds) {
        // Checks
        if (player.isOnFire() || player.fallDistance > 1.0f || player.isInWater()) {
            return false;
        }
        UUID uuid = player.getUUID();
        int ticks = warmupSeconds * 20;
        pendingTeleports.put(uuid, new PendingTeleport(
                uuid,
                player.getEyePosition(),
                level,
                targetPos,
                ticks
        ));

        player.sendSystemMessage(Component.literal("Teleporting in " + warmupSeconds + " seconds. Don't move or take damage!").withStyle(ChatFormatting.YELLOW));
        return true;
    }
    public static void cancelTeleport(UUID playerUuid, String reason) {
        PendingTeleport pending = pendingTeleports.remove(playerUuid);
//        if (pending != null) {
//        }
    }
    public static void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            var iterator = pendingTeleports.entrySet().iterator();
            while (iterator.hasNext()) {
                var entry = iterator.next();
                ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
                PendingTeleport pending = entry.getValue();

                if (player == null || player.hasDisconnected()) {
                    iterator.remove();
                    continue;
                }
                if (player.getEyePosition().distanceToSqr(pending.initialPos()) > 0.04) {
                    player.sendSystemMessage(Component.literal("Teleport cancelled because you moved.").withStyle(ChatFormatting.RED));
                    iterator.remove();
                    continue;
                }
                if (pending.ticksRemaining() <= 1) {
                    Vec3 target = pending.targetPos();
                    player.teleportTo(
                            pending.targetLevel(),
                            target.x(),
                            target.y(),
                            target.z(),
                            java.util.Collections.emptySet(),
                            player.getYRot(),
                            player.getXRot(),
                            true
                    );
                    player.sendSystemMessage(Component.literal("Teleport successful!").withStyle(ChatFormatting.GREEN));
                    iterator.remove();
                    continue;
                } else {
                    entry.setValue(new PendingTeleport(
                            pending.playerUuid(),
                            pending.initialPos(),
                            pending.targetLevel(),
                            pending.targetPos(),
                            pending.ticksRemaining() - 1
                    ));
                }
            }
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(((entity, source, amount) -> {
            if (entity instanceof ServerPlayer player) {
                if (pendingTeleports.containsKey(player.getUUID())) {
                    player.sendSystemMessage(Component.literal("You took damage :/").withStyle(ChatFormatting.RED));
                    cancelTeleport(player.getUUID(), "damage");
                }
            }
            return true;
        }));
    }
}

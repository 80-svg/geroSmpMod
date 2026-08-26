package org.makis.gerosmpmod;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

import static org.makis.gerosmpmod.GeroSmpMod.getModVersion;

public class commonMethods {
    public static boolean deductItem(ServerPlayer player, int amount, Item item) {
        if (amount <= 0) {
            return false;
        }

        int total = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                total += stack.getCount();
            }
        }
        if (total < amount) {
            return false;
        }
        int toRemove = amount;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                int removeFromThis = Math.min(stack.getCount(), toRemove);
                stack.shrink(removeFromThis);
                toRemove -= removeFromThis;
            }
        }
        return true;
    }
    public static void refundItem(ServerPlayer player, int amount, Item item) {
        player.getInventory().placeItemBackInInventory(new ItemStack(item, amount));
    }
    public static void registerModVersionEvents() {
        ServerPlayNetworking.registerGlobalReceiver(ModVersionPayload.VersionPayload.ID, (versionPayload, context) -> {
            context.server().execute(() -> {
                List<String> message = versionPayload.message();
                String version = versionPayload.version();
                ServerPlayer player = context.player();
                GeroSmpMod.ModlistPlayers.remove(player.getUUID());
                Executors.newSingleThreadExecutor().execute(() -> {
//                    player.sendSystemMessage(Component.literal("Version: " + version + " Mods:" + message));
                    GeroSmpMod.LOGGER.info("Version: {} Mods:{}", version, message);
                });
            });
        });
        ServerPlayConnectionEvents.JOIN.register(((listener, sender, server) -> {
            ServerPlayer player = (ServerPlayer) listener.getPlayer();
            UUID uuid = player.getUUID();
            GeroSmpMod.ModlistPlayers.add(uuid);
            server.execute(() -> {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    server.execute(() -> {
                        if (server.getPlayerList().getPlayer(uuid) != null && GeroSmpMod.ModlistPlayers.contains(player.getUUID())) {
                            player.connection.disconnect(Component.literal("Please update to " + getModVersion()));
                        }
                    });
                }).start();
            });
        }));
    }
}

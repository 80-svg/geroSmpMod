package org.makis.gerosmpmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.makis.gerosmpmod.commands.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GeroSmpMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("gerosmpmod");
    public static final String MOD_ID = "gerosmpmod";
    public static final String GITHUB_REPO = "80-svg/geroSmpMod";
    public static Random random = new Random();
    public static final List<Item> DIAMOND_ITEM_TYPE = List.of(
            Items.DIAMOND_BOOTS,
            Items.DIAMOND_LEGGINGS,
            Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_HELMET,
            Items.DIAMOND_SWORD,
            Items.DIAMOND_AXE,
            Items.DIAMOND_PICKAXE,
            Items.DIAMOND_SHOVEL
    );
    public static final Set<UUID> ModlistPlayers = ConcurrentHashMap.newKeySet();
    public static String getModVersion() {
        return FabricLoader.getInstance()
                .getModContainer(MOD_ID)
                .map(ModContainer::getMetadata)
                .map(modMetadata -> modMetadata.getVersion().getFriendlyString())
                .orElse("1.0.0");
    }
    public static final Set<UUID> MUTED_PLAYERS = new HashSet<>();
    public static boolean isChatLocked = false;
    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModAttachments.initialize();
        ModSounds.initialize();
        ModGamerules.initialize();
        ModPotions.registerPotions();
        ModPotionRecipes.registerPotionRecipes();
        CommandRegistrationCallback.EVENT.register(((dispatcher, buildContext, selection) -> {
            BountyCommand.register(dispatcher);
            TpaCommands.register(dispatcher);
            SpawnCommand.register(dispatcher);
            BroadcastCommand.register(dispatcher);
            ChatUtilsCommand.register(dispatcher);
            StationsCommand.register(dispatcher);
        }));
        UseBlockCallback.EVENT.register(((player, level, hand, hitResult) -> {
            BlockState state = level.getBlockState(hitResult.getBlockPos());

            if (state.is(Blocks.END_PORTAL_FRAME) && player.getItemInHand(hand).getItem() == Items.ENDER_EYE) {
                player.sendSystemMessage(Component.literal("End is disabled."));
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        }));
        // Custom packet payloads
        PayloadTypeRegistry.serverboundPlay().register(ModVersionPayload.VersionPayload.ID, ModVersionPayload.VersionPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(
                org.makis.gerosmpmod.payloads.FpsCapPayload.TYPE,
                org.makis.gerosmpmod.payloads.FpsCapPayload.CODEC
        );
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) commonMethods.registerModVersionEvents();
        TeleportManager.registerEvents();
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(((message, sender, boundChatType) -> {
            UUID uuid = sender.getUUID();
            if (GeroSmpMod.MUTED_PLAYERS.contains(uuid)) {
                sender.sendSystemMessage(Component.literal("You are muted.").withStyle(ChatFormatting.GOLD));
                return false;
            }
            if (GeroSmpMod.isChatLocked && !sender.checkPermission(Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "admin_chat"), PermissionLevel.GAMEMASTERS)) {
                sender.sendSystemMessage(Component.literal("Chat is locked.").withStyle(ChatFormatting.GOLD));
                return false;
            }
            return true;
        }));
    }
}

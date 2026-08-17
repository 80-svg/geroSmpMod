package org.makis.gerosmpmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.makis.gerosmpmod.commands.BountyCommand;
import org.makis.gerosmpmod.commands.BroadcastCommand;
import org.makis.gerosmpmod.commands.SpawnCommand;
import org.makis.gerosmpmod.commands.TpaCommands;

import java.util.List;
import java.util.Random;
import java.util.UUID;

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
        }));
        UseBlockCallback.EVENT.register(((player, level, hand, hitResult) -> {
            BlockState state = level.getBlockState(hitResult.getBlockPos());

            if (state.is(Blocks.END_PORTAL_FRAME) && player.getItemInHand(hand).getItem() == Items.ENDER_EYE) {
                player.sendSystemMessage(Component.literal("End is disabled."));
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        }));
    }
}

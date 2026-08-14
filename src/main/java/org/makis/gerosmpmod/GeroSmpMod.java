package org.makis.gerosmpmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.makis.gerosmpmod.commands.BountyCommand;

public class GeroSmpMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("smpmodmak");
    public static final String MOD_ID = "gerosmpmod";
    public static final String GITHUB_REPO = "80-svg/geroSmpMod";
    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModAttachments.initialize();
        CommandRegistrationCallback.EVENT.register(((dispatcher, buildContext, selection) -> {
            BountyCommand.register(dispatcher);
        }));
        UseBlockCallback.EVENT.register(((player, level, hand, hitResult) -> {
            BlockState state = level.getBlockState(hitResult.getBlockPos());

            if (state.is(Blocks.END_PORTAL_FRAME) && player.getItemInHand(hand).getItem() == Items.ENDER_EYE) {
                if (!level.isClientSide()) {
                    player.sendSystemMessage(Component.literal("End is disabled."));
                }
            }
            return InteractionResult.FAIL;
        }));
    }
}

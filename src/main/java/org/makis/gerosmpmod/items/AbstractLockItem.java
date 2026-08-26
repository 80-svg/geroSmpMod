package org.makis.gerosmpmod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jspecify.annotations.NonNull;
import org.makis.gerosmpmod.LockableChestHolder;

public abstract class AbstractLockItem extends Item {
    public AbstractLockItem(Properties properties) {
        super(properties);
    }
    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        if (player == null) return InteractionResult.PASS;

        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof ChestBlock)) return InteractionResult.PASS;

        BlockEntity be =  level.getBlockEntity(pos);
        if (!(be instanceof LockableChestHolder holder)) return InteractionResult.PASS;

        if (holder.isLocked()) {
            if (!level.isClientSide()) {
                player.sendSystemMessage(Component.literal("The chest is already locked."));
            }
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide()) {
            BlockPos neighborPos = getNeighborPos(state, pos);

            boolean success = applyLockData(level, pos, holder, player, context.getItemInHand());
            if (success && neighborPos != null) {
                if (level.getBlockEntity(neighborPos) instanceof LockableChestHolder holder2) {
                    applyLockData(level, pos, holder2, player, context.getItemInHand());
                }
            }
            if (success && !player.isCreative()) {
                context.getItemInHand().shrink(1);
            }
        }

        return InteractionResult.SUCCESS;
    }
    protected abstract boolean applyLockData(Level level, BlockPos pos, LockableChestHolder holder, Player player, ItemStack item);
    protected BlockPos getNeighborPos(BlockState state, BlockPos pos) {
        ChestType type = state.getValue(ChestBlock.TYPE);
        if (type == ChestType.SINGLE) return null;
        return ChestBlock.getConnectedBlockPos(pos, state);
    }
}

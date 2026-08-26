package org.makis.gerosmpmod.items;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.makis.gerosmpmod.LockableChestHolder;

public class PlayerChestLockItem extends AbstractLockItem {

    public PlayerChestLockItem(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean applyLockData(Level level, BlockPos pos, LockableChestHolder holder, Player player, ItemStack item) {
        holder.setOwnerUuid(player.getUUID());
        holder.setLockType(LockableChestHolder.LockType.UUID);
        return true;
    }
}

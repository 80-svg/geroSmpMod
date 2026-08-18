package org.makis.gerosmpmod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
}

package org.makis.gerosmpmod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class commonMethods {
    public static boolean deductDiamonds(ServerPlayer player, int amount) {
        if (amount <= 0) {
            return false;
        }

        int total = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(Items.DIAMOND)) {
                total += stack.getCount();
            }
        }
        if (total < amount) {
            return false;
        }
        int toRemove = amount;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(Items.DIAMOND)) {
//                int count = stack.getCount();
//                if (count <= toRemove) {
//                    toRemove -= count;
//                    player.getInventory().removeItem(stack);
//                } else {
//                    stack.shrink(toRemove);
//                    toRemove = 0;
//                }
                int removeFromThis = Math.min(stack.getCount(), toRemove);
                stack.shrink(removeFromThis);
                toRemove -= removeFromThis;
            }
        }
        return true;
    }
}

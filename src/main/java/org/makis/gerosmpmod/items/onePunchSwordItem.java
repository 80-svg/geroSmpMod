package org.makis.gerosmpmod.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.makis.gerosmpmod.GeroSmpMod;

public class onePunchSwordItem extends Item {
    public onePunchSwordItem(Properties properties) {
        super(properties);
    }

    @Override
    public void postHurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
        if (!attacker.level().isClientSide()) {
            if (GeroSmpMod.random.nextInt(200) == 3) {
                mob.kill((ServerLevel) attacker.level());
            }
        }
        super.postHurtEnemy(itemStack, mob, attacker);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            player.swing(hand);
        }
        return InteractionResult.SUCCESS;
    }
}

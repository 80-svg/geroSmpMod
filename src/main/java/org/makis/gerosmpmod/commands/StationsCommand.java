package org.makis.gerosmpmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import org.makis.gerosmpmod.GeroSmpMod;

public class StationsCommand {
public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(Commands.literal("craftingtable").executes(context -> {
        ServerPlayer player = context.getSource().getPlayerOrException();
        player.openMenu(new SimpleMenuProvider(
                ((containerId, inventory, player1) ->
                        new CraftingMenu(containerId, inventory, ContainerLevelAccess.create(player.level(), player.blockPosition())) {
                            @Override
                            public boolean stillValid(Player player) {
                                return true;
                            }
                        }),
                Component.translatable("container.crafting"))
        );
        return 1;
    }));
    dispatcher.register(Commands.literal("echest")
            .executes(context -> openEchest(context, context.getSource().getPlayer()))
            .then(Commands.argument("target", EntityArgument.player())
                    .requires(commandSourceStack -> commandSourceStack.checkPermission(Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "impersonation_echest"), PermissionLevel.GAMEMASTERS))
                    .executes(context -> openEchest(context, EntityArgument.getPlayer(context, "target")))));
}
private static int openEchest(CommandContext<CommandSourceStack> context, ServerPlayer target) throws CommandSyntaxException {
    ServerPlayer player = context.getSource().getPlayerOrException();
    player.openMenu(new SimpleMenuProvider(
            ((containerId, inventory, player1) ->
                    ChestMenu.threeRows(containerId, inventory, target.getEnderChestInventory())),
            Component.translatable("container.enderchest")
    ));
    return 1;
}
}

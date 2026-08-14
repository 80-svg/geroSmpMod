package org.makis.gerosmpmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class TpaCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tpa").then(Commands.argument("target", EntityArgument.player())).executes(TpaCommands::TpaCommand));
    }
    public static int TpaCommand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayer();
        ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
        assert player != null;
        player.sendSystemMessage(Component.literal("To be added :("));
        return 1;
    }
}

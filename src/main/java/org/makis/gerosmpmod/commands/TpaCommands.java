package org.makis.gerosmpmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class TpaCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tpa")
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(TpaCommands::executeTpa)));
        dispatcher.register(Commands.literal("tpaccept")
                .executes(TpaCommands::executeTpaccept));
        dispatcher.register(Commands.literal("tpahere")
                .executes(TpaCommands::executeTpahere));
        dispatcher.register(Commands.literal("tpdeny")
                .executes(TpaCommands::executeTpdeny));
        dispatcher.register(Commands.literal("tpcancel")
                .executes(TpaCommands::executeTpcancel));
    }
    public static int executeTpa(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayer();
        ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
        assert player != null;
        player.sendSystemMessage(Component.literal("To be added :(").withStyle(ChatFormatting.DARK_BLUE));
        return 1;
    }
    public static int executeTpaccept(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return 1;
    }
    public static int executeTpahere(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return 1;
    }
    public static int executeTpdeny(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return 1;
    }
    public static int executeTpcancel(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return 1;
    }
}

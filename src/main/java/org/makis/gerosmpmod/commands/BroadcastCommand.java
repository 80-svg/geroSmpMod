package org.makis.gerosmpmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.permission.v1.PermissionNode;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.PermissionLevel;
import org.makis.gerosmpmod.GeroSmpMod;

public class BroadcastCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("broadcast")
                        .requires(commandSourceStack -> commandSourceStack.checkPermission(Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "can_broadcast"), PermissionLevel.GAMEMASTERS))
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(BroadcastCommand::run)));
    }
    private static int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String message = StringArgumentType.getString(context, "message");

        Component broadcastText = Component.literal("[Broadcast] ")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                .append(Component.literal(message).withStyle(ChatFormatting.WHITE));

        context.getSource().getServer().getPlayerList().broadcastSystemMessage(broadcastText, false);
        return 1;
    }
}

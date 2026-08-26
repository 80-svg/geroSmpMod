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
import org.makis.gerosmpmod.GeroSmpMod;

import java.util.UUID;

public class ChatUtilsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mute")
                .requires(commandSourceStack -> commandSourceStack.checkPermission(Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "can_mute"), PermissionLevel.GAMEMASTERS))
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(context -> changeMuteState(context, true))));
        dispatcher.register(Commands.literal("unmute")
                .requires(commandSourceStack -> commandSourceStack.checkPermission(Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "can_mute"), PermissionLevel.GAMEMASTERS))
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(context -> changeMuteState(context, false))));
        dispatcher.register(Commands.literal("lock_chat")
                .requires(commandSourceStack -> commandSourceStack.checkPermission(Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "can_mute"), PermissionLevel.GAMEMASTERS))
                .executes(ChatUtilsCommand::toggleLock));
    }
    private static int changeMuteState(CommandContext<CommandSourceStack> context, boolean isMuted) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        UUID uuid = target.getUUID();
        boolean changed;
        if (isMuted) {
            changed = GeroSmpMod.MUTED_PLAYERS.add(uuid);
        } else {
            changed = GeroSmpMod.MUTED_PLAYERS.remove(uuid);
        }
        if (!changed) {
            String message = isMuted ? "That player is already muted." : "That player is not muted.";
            context.getSource().sendFailure(Component.literal(message));
            return 0;
        }
        String message = isMuted ? " was muted" : " was unmuted.";
        context.getSource().sendSuccess(() -> Component.literal(target.getName().getString() + " has been muted."), false);
        return 1;
    }
    private static int toggleLock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        GeroSmpMod.isChatLocked = !GeroSmpMod.isChatLocked;
        context.getSource().sendSuccess(() -> Component.literal(GeroSmpMod.isChatLocked ? "Chat locked." : "Chat unlocked."), false);
        return 1;
    }
}

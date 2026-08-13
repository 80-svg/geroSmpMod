package org.makis.gerosmpmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import org.makis.gerosmpmod.ModAttachments;

import static org.makis.gerosmpmod.commonMethods.deductDiamonds;

public class BountyCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("setbounty")
                        .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                        .executes(BountyCommand::setBounty))));
        dispatcher.register(Commands.literal("helpbounty").executes(BountyCommand::helpBounty));
        dispatcher.register(Commands.literal("bounties").executes(BountyCommand::listBounties));
    }

    private static int setBounty(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        boolean success = deductDiamonds(player, amount);
        if (success) {
            int current = target.getAttachedOrElse(ModAttachments.BOUNTY_AMOUNT, 0);
            int newBounty = current + amount;
            target.setAttached(ModAttachments.BOUNTY_AMOUNT, newBounty);
            player.sendSystemMessage(Component.literal("Success! Player's bounty is now: " + newBounty).withColor(TextColor.GREEN));
        } else {
            player.sendSystemMessage(Component.literal("Insufficient Diamonds.").withColor(TextColor.RED));
        }
        return 1;
    }
    private static int helpBounty(CommandContext<CommandSourceStack>  context) throws CommandSyntaxException {
        final CommandSourceStack source = context.getSource();
        source.sendSuccess(() -> Component.literal("/setbounty <player> <amount>: Set a bounty on an online player"), false);
        return 1;
    }
    private static int listBounties(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
            context.getSource().sendSuccess(() -> Component.nullToEmpty(player.getName().getString() + ": " + player.getAttached(ModAttachments.BOUNTY_AMOUNT)), false);
        }
        return 1;
    }
}

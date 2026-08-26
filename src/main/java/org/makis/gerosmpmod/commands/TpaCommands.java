package org.makis.gerosmpmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import org.makis.gerosmpmod.ModItems;
import org.makis.gerosmpmod.TeleportManager;
import org.makis.gerosmpmod.commonMethods;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.makis.gerosmpmod.commonMethods.deductItem;
import static org.makis.gerosmpmod.commonMethods.refundItem;

public class TpaCommands {
    public static class TpaRequest {
        public final UUID requesterId;
        public final boolean isTpahere;
        public TpaRequest(UUID requesterId, boolean isTpahere) {
            this.requesterId = requesterId;
            this.isTpahere = isTpahere;
        }
    }

    private static final Map<UUID, TpaRequest> TPA_REQUEST_MAP = new HashMap<>();
    private static final int payment_amount = 1;
    private static final Item payment_item = ModItems.COIN_SILVER;
    private static final int TPA_TIME = 4;
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tpa")
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(context -> ManageTpaCommand(context, false))));
        dispatcher.register(Commands.literal("tpaccept")
                .executes(TpaCommands::executeTpaccept));
        dispatcher.register(Commands.literal("tpahere")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> ManageTpaCommand(context, true))));
        dispatcher.register(Commands.literal("tpdeny")
                .executes(TpaCommands::executeTpdeny));
        dispatcher.register(Commands.literal("tpcancel")
                .executes(TpaCommands::executeTpcancel));
    }
    private static void cancelOutgoingRequest(ServerPlayer requester) {
        UUID oldTarget = null;
        for (Map.Entry<UUID, TpaRequest> entry : TPA_REQUEST_MAP.entrySet()) {
            if (entry.getValue().requesterId.equals(requester.getUUID())) {
                oldTarget = entry.getKey();
                break;
            }
        }
        if (oldTarget != null) {
            TPA_REQUEST_MAP.remove(oldTarget);
            refundItem(requester, payment_amount, payment_item);
            requester.sendSystemMessage(Component.literal("Teleport request overwritten. You have been refunded"));
        }
    }

    private static void clearIncomingRequest(ServerPlayer target, CommandContext<CommandSourceStack> context) {
        if (TPA_REQUEST_MAP.containsKey(target.getUUID())) {
            TpaRequest oldReq = TPA_REQUEST_MAP.remove(target.getUUID());
            ServerPlayer oldPlayer = context.getSource().getServer().getPlayerList().getPlayer(oldReq.requesterId);
            if (oldPlayer != null) {
                refundItem(oldPlayer, payment_amount, payment_item);
                oldPlayer.sendSystemMessage(Component.literal("Teleport request overwritten. You have been refunded"));
            }
        }
    }
    public static int executeTpaccept(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer target = ctx.getSource().getPlayerOrException();

        if (!TPA_REQUEST_MAP.containsKey(target.getUUID())) {
            target.sendSystemMessage(Component.literal("No pending requests."));
            return 0;
        }

        TpaRequest request = TPA_REQUEST_MAP.remove(target.getUUID());
        ServerPlayer requester = ctx.getSource().getServer().getPlayerList().getPlayer(request.requesterId);

        if (requester == null) {
            target.sendSystemMessage(Component.literal("requester no longer online"));
            return 0;
        }

        if (request.isTpahere) {
//            target.teleportTo(requester.level(), requester.getX(), requester.getY(), requester.getZ(), java.util.Set.of(), requester.getYRot(), requester.getXRot(), false);
            TeleportManager.requestTeleport(target, ctx.getSource().getLevel(), requester.getEyePosition(), TPA_TIME);
            target.sendSystemMessage(Component.literal("TPAHere request accepted. Teleporting..."));
            requester.sendSystemMessage(Component.literal(target.getScoreboardName() + " accepted your TPAHere request."));
        } else {
//            requester.teleportTo(target.level(), target.getX(), target.getY(), target.getZ(), java.util.Set.of(), target.getYRot(), target.getXRot(), false);
            TeleportManager.requestTeleport(requester, ctx.getSource().getLevel(), target.getEyePosition(), TPA_TIME);
            target.sendSystemMessage(Component.literal("TPA request accepted."));
            requester.sendSystemMessage(Component.literal("TPA request accepted. Teleporting..."));
        }
        return 1;
    }
    private static int ManageTpaCommand(CommandContext<CommandSourceStack> ctx, boolean isTpaHere) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayer();
        ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
        assert player != null;
        if (player.getUUID().equals(target.getUUID())) {
            player.sendSystemMessage(Component.literal(isTpaHere ? "You can't send a tpa request to yourself!" : "You can't send a tpa here request to yourself!"));
            return 0;
        }
        boolean success = deductItem(player, payment_amount, payment_item);
        if (!success) {
            player.sendSystemMessage(Component.literal("You need a silver coin to send a request"));
            return 0;
        }

        cancelOutgoingRequest(player);
        clearIncomingRequest(target, ctx);
        TPA_REQUEST_MAP.put(target.getUUID(), new TpaRequest(player.getUUID(), isTpaHere));
        player.sendSystemMessage(Component.literal("tpa request sent to " + target.getScoreboardName()));
        target.sendSystemMessage(Component.literal(player.getScoreboardName() + " requested to tp to you. type /tpaccept or /tpdeny"));


        return 1;
    }
    public static int executeTpdeny(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return 1;
    }
    public static int executeTpcancel(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer requester = ctx.getSource().getPlayerOrException();

        UUID target = null;
        for (Map.Entry<UUID, TpaRequest> entry : TPA_REQUEST_MAP.entrySet()) {
            if (entry.getValue().requesterId.equals(requester.getUUID())) {
                target = entry.getKey();
                break;
            }
        }

        if (target == null) {
            requester.sendSystemMessage(Component.literal("No pending requests."));
            return 0;
        }

        TPA_REQUEST_MAP.remove(target);
        commonMethods.refundItem(requester, payment_amount, payment_item);
        requester.sendSystemMessage(Component.literal("tpa canceled. your stuff was refunded."));
        return 1;
    }
}

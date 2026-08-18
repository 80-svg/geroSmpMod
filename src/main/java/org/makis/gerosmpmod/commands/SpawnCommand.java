package org.makis.gerosmpmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class SpawnCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawn").executes(SpawnCommand::executeSpawnCommand));
    }
    private static int executeSpawnCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayer();
        ServerLevel overworld = context.getSource().getServer().getLevel(ServerLevel.OVERWORLD);
        assert player != null;
        if (overworld != null) {
            player.teleportTo(overworld, -25.0, 80.0, -23.0, java.util.Collections.emptySet(), player.getYRot(), player.getXRot(), true);
        }
        return 1;
    }
}

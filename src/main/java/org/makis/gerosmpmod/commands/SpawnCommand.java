package org.makis.gerosmpmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.makis.gerosmpmod.TeleportManager;

public class SpawnCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawn").executes(SpawnCommand::executeSpawnCommand));
    }
    private static int executeSpawnCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayer();
        ServerLevel overworld = context.getSource().getServer().getLevel(ServerLevel.OVERWORLD);
        Vec3 spawnPos = new Vec3(-25.0, 80.5, -23.0);
        assert player != null;
        if (overworld != null) {
//            player.teleportTo(overworld, -25.0, 80.0, -23.0, java.util.Collections.emptySet(), player.getYRot(), player.getXRot(), true);
            TeleportManager.requestTeleport(player, overworld, spawnPos, 5);
        }
        return 1;
    }
}

package org.makis.gerosmpmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.makis.gerosmpmod.commands.BountyCommand;

public class GeroSmpMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("smpmodmak");
    public static final String MOD_ID = "gerosmpmod";
    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModAttachments.initialize();
        CommandRegistrationCallback.EVENT.register(((dispatcher, buildContext, selection) -> {
            BountyCommand.register(dispatcher);
        }));
    }
}

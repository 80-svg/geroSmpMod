package org.makis.smp2026gero;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.makis.smp2026gero.commands.BountyCommand;

public class Smp2026gero implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("smpmodmak");
    public static final String MOD_ID = "smp2026gero";
    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModAttachments.initialize();
        CommandRegistrationCallback.EVENT.register(((dispatcher, buildContext, selection) -> {
            BountyCommand.register(dispatcher);
        }));
    }
}

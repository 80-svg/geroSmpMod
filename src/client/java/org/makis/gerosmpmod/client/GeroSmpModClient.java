package org.makis.gerosmpmod.client;

import net.fabricmc.api.ClientModInitializer;
import org.makis.gerosmpmod.UpdateChecker;

public class GeroSmpModClient implements ClientModInitializer {
    public static final boolean UPDATES_ENABLED = true;
    @Override
    public void onInitializeClient() {
        if (UPDATES_ENABLED) UpdateChecker.checkForUpdates();
    }
}

package org.makis.gerosmpmod.client;

import net.fabricmc.api.ClientModInitializer;
import org.makis.gerosmpmod.UpdateChecker;

public class GeroSmpModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        UpdateChecker.checkForUpdates();
    }
}

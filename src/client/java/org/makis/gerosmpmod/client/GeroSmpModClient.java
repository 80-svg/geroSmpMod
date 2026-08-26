package org.makis.gerosmpmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.datagen.v1.advancement.FabricAdvancementBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.ModOrigin;
import org.makis.gerosmpmod.GeroSmpMod;
import org.makis.gerosmpmod.ModVersionPayload;
import org.makis.gerosmpmod.UpdateChecker;
import org.makis.gerosmpmod.datagen.RecipeDisablerDataGenerator;

import java.util.List;

public class GeroSmpModClient implements ClientModInitializer {
    public static final boolean UPDATES_ENABLED = true;
    @Override
    public void onInitializeClient() {
        if (UPDATES_ENABLED) UpdateChecker.checkForUpdates();
        RecipeDisablerDataGenerator.registerClientVersionEvent();
    }
}

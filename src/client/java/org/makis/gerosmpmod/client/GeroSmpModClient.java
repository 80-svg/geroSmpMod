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
import org.makis.gerosmpmod.payloads.FpsCapPayload;
import org.makis.gerosmpmod.UpdateChecker;
import org.makis.gerosmpmod.datagen.RecipeDisablerDataGenerator;

import java.util.List;

public class GeroSmpModClient implements ClientModInitializer {
    public static final boolean UPDATES_ENABLED = true;
    private static Integer fpsBeforeServerCap;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(FpsCapPayload.TYPE, (payload, context) ->
                context.client().execute(() -> applyFpsCap(payload)));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                client.execute(GeroSmpModClient::restoreFpsLimit));
        if (UPDATES_ENABLED) UpdateChecker.checkForUpdates();
        RecipeDisablerDataGenerator.registerClientVersionEvent();
    }

    private static void applyFpsCap(FpsCapPayload payload) {
        var option = net.minecraft.client.Minecraft.getInstance().options.framerateLimit();

        if (payload.active()) {
            if (fpsBeforeServerCap == null) {
                fpsBeforeServerCap = option.get();
            }
            option.set(Math.max(1, payload.fps()));
        } else {
            restoreFpsLimit();
        }
    }

    private static void restoreFpsLimit() {
        if (fpsBeforeServerCap != null) {
            net.minecraft.client.Minecraft.getInstance().options.framerateLimit().set(fpsBeforeServerCap);
            fpsBeforeServerCap = null;
        }
    }
}

package org.makis.gerosmpmod.datagen;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModOrigin;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.makis.gerosmpmod.GeroSmpMod;
import org.makis.gerosmpmod.ModVersionPayload;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
// Did it commit? (Yeah future me will be confused by this comment)
public class RecipeDisablerDataGenerator implements DataProvider {

    private final FabricPackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registriesFuture;

    // Set of items whose recipes are automatically disabled
    public static final Set<ItemLike> DISABLED_ITEMS = Set.of(
            Items.MACE
    );

    public RecipeDisablerDataGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        this.output = output;
        this.registriesFuture = registriesFuture;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return registriesFuture.thenCompose(registries -> {
            PackOutput.PathProvider pathProvider = output.createRegistryElementsPathProvider(Registries.RECIPE);
            List<CompletableFuture<?>> futures = new ArrayList<>();

            for (ItemLike itemLike : DISABLED_ITEMS) {
                Item item = itemLike.asItem();
                Identifier itemId = BuiltInRegistries.ITEM.getKey(item);

                JsonObject dummyRecipeJson = createDisabledRecipeJson();
                Identifier recipeId = Identifier.fromNamespaceAndPath(itemId.getNamespace(), itemId.getPath());

                futures.add(DataProvider.saveStable(cachedOutput, dummyRecipeJson, pathProvider.json(recipeId)));
            }

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }
    private JsonObject createDisabledRecipeJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");
        json.add("pattern", new com.google.gson.JsonArray());
        json.add("key", new JsonObject());

        JsonObject result = new JsonObject();
        result.addProperty("count", 0);
        result.addProperty("id", "minecraft:air");
        json.add("result", result);

        return json;
    }
    public static void registerClientVersionEvent() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            String version = GeroSmpMod.getModVersion();
            List<String> mods = FabricLoader.getInstance()
                    .getAllMods()
                    .stream()
                    .filter(mod -> mod.getOrigin().getKind() == ModOrigin.Kind.PATH)
                    .filter(mod -> !mod.getMetadata().getId().startsWith("fabric-"))
                    .map(mod -> mod.getMetadata().getName())
                    .toList();
            sender.sendPacket(new ModVersionPayload.VersionPayload(version, mods));
        });
    }
    @Override
    public String getName() {
        return "Recipe Disabler Generator";
    }
}

package org.makis.gerosmpmod.client;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;
import org.makis.gerosmpmod.ModItemIDs;
import org.makis.gerosmpmod.ModItems;

public class ModModelGenerator extends FabricModelProvider {
    public ModModelGenerator(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModItems.COIN_SILVER, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.KETAMINE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.PLAYER_CHEST_LOCK, ModelTemplates.FLAT_ITEM);
    }
}

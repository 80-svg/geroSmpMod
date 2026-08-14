package org.makis.gerosmpmod.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class GeroSmpModDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModModelGenerator::new);
//        pack.addProvider(org.makis.gerosmpmod.datagen.RecipeDisablerDataGenerator::new);
    }
}

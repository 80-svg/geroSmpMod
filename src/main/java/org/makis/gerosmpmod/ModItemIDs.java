package org.makis.gerosmpmod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIDs {
    public static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, name));
    }
    public static final ResourceKey<Item> KETAMINE = create("ketamine");
}

package org.makis.gerosmpmod;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import org.makis.gerosmpmod.items.onePunchSwordItem;

import java.util.List;
import java.util.function.Function;

public class ModItems {
    public static void initialize() {
        addItemToCreativeModTab(KETAMINE, MAKIS_TAB_KEY);
        addItemToCreativeModTab(COIN_SILVER, MAKIS_TAB_KEY);
        addItemToCreativeModTab(ONE_PUNCH_SWORD,  MAKIS_TAB_KEY);
        addItemToCreativeModTab(PLAYER_CHEST_LOCK,  MAKIS_TAB_KEY);
    }
    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));

        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }
    public static void addItemToCreativeModTab(Item item, ResourceKey<CreativeModeTab> tab) {
        CreativeModeTabEvents.modifyOutputEvent(tab)
                .register((creativeTab) -> creativeTab.accept(item));
    }
    public static final ResourceKey<CreativeModeTab> MAKIS_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(GeroSmpMod.MOD_ID, "makis_tab"));
    public static final CreativeModeTab MAKIS_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAKIS_TAB_KEY,
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup." + GeroSmpMod.MOD_ID))
                    .icon(() -> {
                        assert ModItems.KETAMINE != null;
                        return new ItemStack(ModItems.KETAMINE);
                    })
                    .displayItems(((parameters, output) -> {}))
                    .build());
    public static final Item KETAMINE = register(ModItemIDs.KETAMINE, Item::new, new Item.Properties());
    public static final Item COIN_SILVER = register(ModItemIDs.COIN_SILVER, Item::new, new Item.Properties());
    public static final Item ONE_PUNCH_SWORD = register(ModItemIDs.ONE_PUNCH_SWORD, onePunchSwordItem::new, new onePunchSwordItem.Properties().sword(ToolMaterial.IRON, 1f, 1f));
    public static final Item PLAYER_CHEST_LOCK = register(ModItemIDs.PLAYER_CHEST_LOCK, Item::new, new Item.Properties());
}

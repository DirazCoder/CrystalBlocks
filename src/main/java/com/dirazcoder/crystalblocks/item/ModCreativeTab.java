package com.dirazcoder.crystalblocks.item;

import com.dirazcoder.crystalblocks.CrystalBlocksMod;
import com.dirazcoder.crystalblocks.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

// own creative tab so all the block families sit together instead of
// getting scattered across vanilla's building blocks tab
public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CrystalBlocksMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CRYSTAL_TAB = TABS.register(
            "crystal_blocks_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.crystalblocks"))
                    .icon(() -> new ItemStack(ModItems.BLOCK_ITEMS_BY_FAMILY.get("crystal").get("cyan").get()))
                    .displayItems((parameters, output) -> {
                        for (String family : ModBlocks.FAMILIES) {
                            ModItems.BLOCK_ITEMS_BY_FAMILY.get(family).values().forEach(item -> output.accept(item.get()));
                            ModItems.SLAB_ITEMS_BY_FAMILY.get(family).values().forEach(item -> output.accept(item.get()));
                            ModItems.STAIRS_ITEMS_BY_FAMILY.get(family).values().forEach(item -> output.accept(item.get()));
                            ModItems.FENCE_ITEMS_BY_FAMILY.get(family).values().forEach(item -> output.accept(item.get()));
                            ModItems.FENCE_GATE_ITEMS_BY_FAMILY.get(family).values().forEach(item -> output.accept(item.get()));
                            ModItems.WALL_ITEMS_BY_FAMILY.get(family).values().forEach(item -> output.accept(item.get()));
                            output.accept(ModItems.GLOW_BLOCK_ITEMS_BY_FAMILY.get(family).get());
                            output.accept(ModItems.GLOW_SLAB_ITEMS_BY_FAMILY.get(family).get());
                            output.accept(ModItems.GLOW_STAIRS_ITEMS_BY_FAMILY.get(family).get());
                        }
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}

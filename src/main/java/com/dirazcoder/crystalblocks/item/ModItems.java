/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2026 DirazCoder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.dirazcoder.crystalblocks.item;

import com.dirazcoder.crystalblocks.CrystalBlocksMod;
import com.dirazcoder.crystalblocks.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

// gives every block from ModBlocks a BlockItem so it's actually obtainable
// and shows up in creative, same family -> color -> block structure
public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CrystalBlocksMod.MOD_ID);

    public static final Map<String, Map<String, RegistryObject<Item>>> BLOCK_ITEMS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, RegistryObject<Item>>> SLAB_ITEMS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, RegistryObject<Item>>> STAIRS_ITEMS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, RegistryObject<Item>>> FENCE_ITEMS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, RegistryObject<Item>>> FENCE_GATE_ITEMS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, RegistryObject<Item>>> WALL_ITEMS_BY_FAMILY = new LinkedHashMap<>();

    public static final Map<String, RegistryObject<Item>> GLOW_BLOCK_ITEMS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<Item>> GLOW_SLAB_ITEMS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<Item>> GLOW_STAIRS_ITEMS_BY_FAMILY = new LinkedHashMap<>();

    static {
        for (String family : ModBlocks.FAMILIES) {
            Map<String, RegistryObject<Item>> blockItems = new LinkedHashMap<>();
            Map<String, RegistryObject<Item>> slabItems = new LinkedHashMap<>();
            Map<String, RegistryObject<Item>> stairsItems = new LinkedHashMap<>();
            Map<String, RegistryObject<Item>> fenceItems = new LinkedHashMap<>();
            Map<String, RegistryObject<Item>> fenceGateItems = new LinkedHashMap<>();
            Map<String, RegistryObject<Item>> wallItems = new LinkedHashMap<>();

            ModBlocks.BLOCKS_BY_FAMILY.get(family).forEach((color, block) ->
                    blockItems.put(color, ITEMS.register(
                            family + "_block_" + color,
                            () -> new BlockItem(block.get(), new Item.Properties()))));

            ModBlocks.SLABS_BY_FAMILY.get(family).forEach((color, block) ->
                    slabItems.put(color, ITEMS.register(
                            family + "_slab_" + color,
                            () -> new BlockItem(block.get(), new Item.Properties()))));

            ModBlocks.STAIRS_BY_FAMILY.get(family).forEach((color, block) ->
                    stairsItems.put(color, ITEMS.register(
                            family + "_stairs_" + color,
                            () -> new BlockItem(block.get(), new Item.Properties()))));

            ModBlocks.FENCES_BY_FAMILY.get(family).forEach((color, block) ->
                    fenceItems.put(color, ITEMS.register(
                            family + "_fence_" + color,
                            () -> new BlockItem(block.get(), new Item.Properties()))));

            ModBlocks.FENCE_GATES_BY_FAMILY.get(family).forEach((color, block) ->
                    fenceGateItems.put(color, ITEMS.register(
                            family + "_fence_gate_" + color,
                            () -> new BlockItem(block.get(), new Item.Properties()))));

            ModBlocks.WALLS_BY_FAMILY.get(family).forEach((color, block) ->
                    wallItems.put(color, ITEMS.register(
                            family + "_wall_" + color,
                            () -> new BlockItem(block.get(), new Item.Properties()))));

            BLOCK_ITEMS_BY_FAMILY.put(family, blockItems);
            SLAB_ITEMS_BY_FAMILY.put(family, slabItems);
            STAIRS_ITEMS_BY_FAMILY.put(family, stairsItems);
            FENCE_ITEMS_BY_FAMILY.put(family, fenceItems);
            FENCE_GATE_ITEMS_BY_FAMILY.put(family, fenceGateItems);
            WALL_ITEMS_BY_FAMILY.put(family, wallItems);

            RegistryObject<Block> glowBlock = ModBlocks.GLOW_BLOCKS_BY_FAMILY.get(family);
            GLOW_BLOCK_ITEMS_BY_FAMILY.put(family, ITEMS.register(
                    family + "_block_glow",
                    () -> new BlockItem(glowBlock.get(), new Item.Properties())));

            RegistryObject<Block> glowSlab = ModBlocks.GLOW_SLABS_BY_FAMILY.get(family);
            GLOW_SLAB_ITEMS_BY_FAMILY.put(family, ITEMS.register(
                    family + "_slab_glow",
                    () -> new BlockItem(glowSlab.get(), new Item.Properties())));

            RegistryObject<Block> glowStairs = ModBlocks.GLOW_STAIRS_BY_FAMILY.get(family);
            GLOW_STAIRS_ITEMS_BY_FAMILY.put(family, ITEMS.register(
                    family + "_stairs_glow",
                    () -> new BlockItem(glowStairs.get(), new Item.Properties())));
        }
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

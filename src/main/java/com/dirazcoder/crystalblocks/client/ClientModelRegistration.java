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

package com.dirazcoder.crystalblocks.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.dirazcoder.crystalblocks.block.ModBlocks;

// this is the piece that was missing the whole time: RegistryEvent.Register
// only puts blocks/items in the registry, it never wires a block's
// IBlockState to a model or an item to its inventory icon. Blocks get a
// DEFAULT state mapper for free (that's why world rendering ever worked
// at all once the blockstate JSON paths were fixed), but items never do
// - every ItemBlock needs an explicit ModelLoader.setCustomModelResourceLocation
// call or it just renders as the missing-texture checkerboard, which is
// exactly the symptom in the creative tab/hotbar screenshots.
//
// separately, the double slab needs its own custom state mapper. its
// BlockStateContainer deliberately has no HALF property (CrystalSlab
// only adds HALF for the single slab), but CrystalSlab.getVariantProperty()
// unconditionally returns HALF because BlockSlab demands *some* variant
// property from every subclass and we have no real one to give it. the
// DEFAULT state mapper trusts getVariantProperty() blindly and tries to
// read HALF off the double slab's state anyway, which throws
// MissingVariantException since that property was never registered on
// that block's container. ignoring HALF here for double slabs only
// routes them straight to the blockstate's "normal" variant instead,
// which is what their JSON already defines.
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = "crystalblocks")
public class ClientModelRegistration {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Map<String, Block> blocks : ModBlocks.BLOCKS_BY_FAMILY.values()) {
            registerItemModels(blocks);
        }
        for (Map<String, Block> slabs : ModBlocks.SLABS_BY_FAMILY.values()) {
            registerItemModels(slabs);
        }
        // double slabs have no item form, so no item model to register -
        // but they still need the state mapper fix below
        for (Map<String, Block> doubleSlabs : ModBlocks.DOUBLE_SLABS_BY_FAMILY.values()) {
            for (Block doubleSlab : doubleSlabs.values()) {
                fixDoubleSlabStateMapper(doubleSlab);
            }
        }
        for (Map<String, Block> stairs : ModBlocks.STAIRS_BY_FAMILY.values()) {
            registerItemModels(stairs);
        }
        for (Map<String, Block> fences : ModBlocks.FENCES_BY_FAMILY.values()) {
            registerItemModels(fences);
        }
        for (Map<String, Block> fenceGates : ModBlocks.FENCE_GATES_BY_FAMILY.values()) {
            registerItemModels(fenceGates);
        }
        for (Map<String, Block> walls : ModBlocks.WALLS_BY_FAMILY.values()) {
            registerItemModels(walls);
        }

        registerItemModels(ModBlocks.GLOW_BLOCKS_BY_FAMILY.values());
        registerItemModels(ModBlocks.GLOW_SLABS_BY_FAMILY.values());
        for (Block glowDoubleSlab : ModBlocks.GLOW_DOUBLE_SLABS_BY_FAMILY.values()) {
            fixDoubleSlabStateMapper(glowDoubleSlab);
        }
        registerItemModels(ModBlocks.GLOW_STAIRS_BY_FAMILY.values());
    }

    private static void registerItemModels(Map<String, Block> blocksByColor) {
        registerItemModels(blocksByColor.values());
    }

    private static void registerItemModels(Iterable<Block> blocks) {
        List<Block> list = new ArrayList<>();
        for (Block block : blocks) {
            list.add(block);
        }
        for (Block block : list) {
            Item item = Item.getItemFromBlock(block);
            if (item == null) {
                continue;
            }
            ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }
    }

    private static void fixDoubleSlabStateMapper(Block doubleSlab) {
        ModelLoader.setCustomStateMapper(doubleSlab,
            new StateMap.Builder().ignore(BlockSlab.HALF).build());
    }
}

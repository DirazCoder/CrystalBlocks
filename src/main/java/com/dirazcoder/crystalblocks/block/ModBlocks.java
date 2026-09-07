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

package com.dirazcoder.crystalblocks.block;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.dirazcoder.crystalblocks.item.ModCreativeTab;

import cpw.mods.fml.common.registry.GameRegistry;

// 1.7.10 has no DeferredRegister, so this builds blocks directly and
// hands them to GameRegistry, in a preInit call instead of a static
// block. same family -> color -> block shape as the 1.20.1 version, just
// built on 1.7.10's block classes. slabs need a single AND a double
// instance here (BlockSlab doesn't merge two halves into a full block on
// its own like 1.20.1's SlabBlock does - see SlabCrystalBlocks). Block
// (Material) and BlockStairs(Block, int) are both protected, so plain
// blocks/stairs go through CrystalBlock/CrystalBlockStairs instead of
// calling those constructors directly. BlockFenceGate and BlockWall
// both hardcode their getIcon(int, int) to vanilla textures (oak
// planks / cobblestone) regardless of constructor args or
// setBlockTextureName - confirmed from decompiled source - so
// CrystalBlockFenceGate/CrystalBlockWall override getIcon directly.
// BlockFence is different: it actually implements registerBlockIcons
// for real, so CrystalBlockFence only needed to override that
public class ModBlocks {

    public static final List<String> FAMILIES = Arrays
        .asList("crystal", "speckle", "brick", "corroded", "mossy", "marble", "hexplate");

    public static final List<String> COLORS = Arrays.asList("cyan", "red", "green", "purple", "orange", "blue");

    public static final Map<String, Map<String, Block>> BLOCKS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, Block>> SLABS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, Block>> DOUBLE_SLABS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, Block>> STAIRS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, Block>> FENCES_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, Block>> FENCE_GATES_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, Block>> WALLS_BY_FAMILY = new LinkedHashMap<>();

    // one glowing block per family, full light level, block/slab/stairs
    // only - no fence/gate/wall, same call as the 1.20.1 version (nobody's
    // building a glowing fence line)
    public static final Map<String, Block> GLOW_BLOCKS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Block> GLOW_SLABS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Block> GLOW_DOUBLE_SLABS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Block> GLOW_STAIRS_BY_FAMILY = new LinkedHashMap<>();

    public static void registerBlocks() {
        for (String family : FAMILIES) {
            Map<String, Block> blocks = new LinkedHashMap<>();
            Map<String, Block> slabs = new LinkedHashMap<>();
            Map<String, Block> doubleSlabs = new LinkedHashMap<>();
            Map<String, Block> stairs = new LinkedHashMap<>();
            Map<String, Block> fences = new LinkedHashMap<>();
            Map<String, Block> fenceGates = new LinkedHashMap<>();
            Map<String, Block> walls = new LinkedHashMap<>();

            for (String color : COLORS) {
                String blockName = family + "_block_" + color;
                Block block = new CrystalBlock(Material.rock).setHardness(2.0f)
                    .setResistance(6.0f)
                    .setStepSound(Block.soundTypeStone)
                    .setBlockName(blockName)
                    .setBlockTextureName("crystalblocks:" + blockName)
                    .setCreativeTab(ModCreativeTab.CRYSTAL_TAB);
                GameRegistry.registerBlock(block, blockName);
                blocks.put(color, block);

                String slabName = family + "_slab_" + color;
                SlabCrystalBlocks slab = new SlabCrystalBlocks(false, blockName);
                slab.setHardness(2.0f);
                slab.setResistance(6.0f);
                slab.setStepSound(Block.soundTypeStone);
                slab.setBlockName(slabName);
                slab.setCreativeTab(ModCreativeTab.CRYSTAL_TAB);
                GameRegistry.registerBlock(slab, slabName);
                slabs.put(color, slab);

                // double slab has no item form - it's never craftable or
                // holdable directly, only produced by stacking two singles,
                // same as vanilla stone/wood double slabs
                String doubleSlabName = family + "_double_slab_" + color;
                SlabCrystalBlocks doubleSlab = new SlabCrystalBlocks(true, blockName);
                doubleSlab.setHardness(2.0f);
                doubleSlab.setResistance(6.0f);
                doubleSlab.setStepSound(Block.soundTypeStone);
                doubleSlab.setBlockName(doubleSlabName);
                GameRegistry.registerBlock(doubleSlab, doubleSlabName);
                doubleSlabs.put(color, doubleSlab);
                slab.setDoubleSlabBlock(doubleSlab);
                doubleSlab.setSingleSlabBlock(slab);

                String stairsName = family + "_stairs_" + color;
                Block stair = new CrystalBlockStairs(block, 0).setHardness(2.0f)
                    .setResistance(6.0f)
                    .setStepSound(Block.soundTypeStone)
                    .setBlockName(stairsName)
                    .setCreativeTab(ModCreativeTab.CRYSTAL_TAB);
                GameRegistry.registerBlock(stair, stairsName);
                stairs.put(color, stair);

                String fenceName = family + "_fence_" + color;
                Block fence = new CrystalBlockFence(blockName, Material.rock).setHardness(2.0f)
                    .setResistance(6.0f)
                    .setStepSound(Block.soundTypeStone)
                    .setBlockName(fenceName)
                    .setBlockTextureName("crystalblocks:" + blockName)
                    .setCreativeTab(ModCreativeTab.CRYSTAL_TAB);
                GameRegistry.registerBlock(fence, fenceName);
                fences.put(color, fence);

                String fenceGateName = family + "_fence_gate_" + color;
                Block fenceGate = new CrystalBlockFenceGate(blockName).setHardness(2.0f)
                    .setResistance(6.0f)
                    .setStepSound(Block.soundTypeStone)
                    .setBlockName(fenceGateName)
                    .setBlockTextureName("crystalblocks:" + blockName)
                    .setCreativeTab(ModCreativeTab.CRYSTAL_TAB);
                GameRegistry.registerBlock(fenceGate, fenceGateName);
                fenceGates.put(color, fenceGate);

                String wallName = family + "_wall_" + color;
                Block wall = new CrystalBlockWall(block, blockName).setHardness(2.0f)
                    .setResistance(6.0f)
                    .setStepSound(Block.soundTypeStone)
                    .setBlockName(wallName)
                    .setBlockTextureName("crystalblocks:" + blockName)
                    .setCreativeTab(ModCreativeTab.CRYSTAL_TAB);
                GameRegistry.registerBlock(wall, wallName);
                walls.put(color, wall);
            }

            BLOCKS_BY_FAMILY.put(family, blocks);
            SLABS_BY_FAMILY.put(family, slabs);
            DOUBLE_SLABS_BY_FAMILY.put(family, doubleSlabs);
            STAIRS_BY_FAMILY.put(family, stairs);
            FENCES_BY_FAMILY.put(family, fences);
            FENCE_GATES_BY_FAMILY.put(family, fenceGates);
            WALLS_BY_FAMILY.put(family, walls);

            String glowBlockName = family + "_block_glow";
            Block glowBlock = new CrystalBlock(Material.rock).setHardness(2.0f)
                .setResistance(6.0f)
                .setStepSound(Block.soundTypeStone)
                .setLightLevel(1.0f)
                .setBlockName(glowBlockName)
                .setBlockTextureName("crystalblocks:" + glowBlockName)
                .setCreativeTab(ModCreativeTab.CRYSTAL_TAB);
            GameRegistry.registerBlock(glowBlock, glowBlockName);
            GLOW_BLOCKS_BY_FAMILY.put(family, glowBlock);

            String glowSlabName = family + "_slab_glow";
            SlabCrystalBlocks glowSlab = new SlabCrystalBlocks(false, glowBlockName);
            glowSlab.setHardness(2.0f);
            glowSlab.setResistance(6.0f);
            glowSlab.setStepSound(Block.soundTypeStone);
            glowSlab.setLightLevel(1.0f);
            glowSlab.setBlockName(glowSlabName);
            glowSlab.setCreativeTab(ModCreativeTab.CRYSTAL_TAB);
            GameRegistry.registerBlock(glowSlab, glowSlabName);
            GLOW_SLABS_BY_FAMILY.put(family, glowSlab);

            String glowDoubleSlabName = family + "_double_slab_glow";
            SlabCrystalBlocks glowDoubleSlab = new SlabCrystalBlocks(true, glowBlockName);
            glowDoubleSlab.setHardness(2.0f);
            glowDoubleSlab.setResistance(6.0f);
            glowDoubleSlab.setStepSound(Block.soundTypeStone);
            glowDoubleSlab.setLightLevel(1.0f);
            glowDoubleSlab.setBlockName(glowDoubleSlabName);
            GameRegistry.registerBlock(glowDoubleSlab, glowDoubleSlabName);
            GLOW_DOUBLE_SLABS_BY_FAMILY.put(family, glowDoubleSlab);
            glowSlab.setDoubleSlabBlock(glowDoubleSlab);
            glowDoubleSlab.setSingleSlabBlock(glowSlab);

            String glowStairsName = family + "_stairs_glow";
            Block glowStair = new CrystalBlockStairs(glowBlock, 0).setHardness(2.0f)
                .setResistance(6.0f)
                .setStepSound(Block.soundTypeStone)
                .setLightLevel(1.0f)
                .setBlockName(glowStairsName)
                .setCreativeTab(ModCreativeTab.CRYSTAL_TAB);
            GameRegistry.registerBlock(glowStair, glowStairsName);
            GLOW_STAIRS_BY_FAMILY.put(family, glowStair);
        }
    }
}

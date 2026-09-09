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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.dirazcoder.crystalblocks.item.ModCreativeTab;

// registration goes through RegistryEvent.Register<Block> instead of
// 1.7.10's direct GameRegistry.registerBlock calls in preInit - Forge
// fires this event itself, in dependency order, so there's no need to
// call registerBlocks() from anywhere by hand anymore.
//
// same family -> color -> block shape loop as the 1.7.10 version, just
// built on 1.12.2's block classes. slabs still need a single AND a
// double instance (BlockSlab doesn't merge two halves into a full
// block on its own the way 1.13+'s SlabBlock does), but the merge
// logic itself now lives entirely in CrystalSlab/vanilla BlockSlab -
// no more manual world-editing onBlockPlacedBy like SlabCrystalBlocks
// had to do in 1.7.10.
//
// registry names take the place of setBlockName - GameRegistry looks
// up the unlocalized name off registry name automatically as long as
// setUnlocalizedName isn't called with something different, so both
// are set to the same value below for clarity rather than relying on
// that default
@Mod.EventBusSubscriber
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
    // only - no fence/gate/wall, same restriction as the 1.7.10 version
    // (nobody's building a glowing fence line)
    public static final Map<String, Block> GLOW_BLOCKS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Block> GLOW_SLABS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Block> GLOW_DOUBLE_SLABS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Block> GLOW_STAIRS_BY_FAMILY = new LinkedHashMap<>();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        List<Block> toRegister = new ArrayList<>();

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
                Block block = configure(new CrystalBlock(Material.ROCK), blockName);
                toRegister.add(block);
                blocks.put(color, block);

                String slabName = family + "_slab_" + color;
                Block slab = configure(new CrystalSlab(false), slabName);
                toRegister.add(slab);
                slabs.put(color, slab);

                // double slab has no item form - registerItemBlock is
                // skipped for it in ModItems, same as vanilla stone/wood
                // double slabs which are never craftable or holdable
                // directly, only ever produced by stacking two singles
                String doubleSlabName = family + "_double_slab_" + color;
                Block doubleSlab = configure(new CrystalSlab(true), doubleSlabName);
                toRegister.add(doubleSlab);
                doubleSlabs.put(color, doubleSlab);

                String stairsName = family + "_stairs_" + color;
                Block stair = configure(new CrystalBlockStairs(block.getDefaultState()), stairsName);
                toRegister.add(stair);
                stairs.put(color, stair);

                String fenceName = family + "_fence_" + color;
                Block fence = configure(new CrystalBlockFence(Material.ROCK, MapColor.STONE), fenceName);
                toRegister.add(fence);
                fences.put(color, fence);

                String fenceGateName = family + "_fence_gate_" + color;
                Block fenceGate = configure(new CrystalBlockFenceGate(Material.ROCK), fenceGateName);
                toRegister.add(fenceGate);
                fenceGates.put(color, fenceGate);

                String wallName = family + "_wall_" + color;
                Block wall = configure(new CrystalBlockWall(block), wallName);
                toRegister.add(wall);
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
            Block glowBlock = configure(new CrystalBlock(Material.ROCK), glowBlockName);
            glowBlock.setLightLevel(1.0f);
            toRegister.add(glowBlock);
            GLOW_BLOCKS_BY_FAMILY.put(family, glowBlock);

            String glowSlabName = family + "_slab_glow";
            Block glowSlab = configure(new CrystalSlab(false), glowSlabName);
            glowSlab.setLightLevel(1.0f);
            toRegister.add(glowSlab);
            GLOW_SLABS_BY_FAMILY.put(family, glowSlab);

            String glowDoubleSlabName = family + "_double_slab_glow";
            Block glowDoubleSlab = configure(new CrystalSlab(true), glowDoubleSlabName);
            glowDoubleSlab.setLightLevel(1.0f);
            toRegister.add(glowDoubleSlab);
            GLOW_DOUBLE_SLABS_BY_FAMILY.put(family, glowDoubleSlab);

            String glowStairsName = family + "_stairs_glow";
            Block glowStair = configure(new CrystalBlockStairs(glowBlock.getDefaultState()), glowStairsName);
            glowStair.setLightLevel(1.0f);
            toRegister.add(glowStair);
            GLOW_STAIRS_BY_FAMILY.put(family, glowStair);
        }

        event.getRegistry()
            .registerAll(toRegister.toArray(new Block[0]));
    }

    private static Block configure(Block block, String registryName) {
        return block.setHardness(2.0f)
            .setResistance(6.0f)
            .setUnlocalizedName(registryName)
            .setRegistryName(registryName)
            .setCreativeTab(ModCreativeTab.CRYSTAL_TAB);
    }
}

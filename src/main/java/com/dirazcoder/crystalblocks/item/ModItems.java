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

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.dirazcoder.crystalblocks.block.ModBlocks;

import cpw.mods.fml.common.registry.GameRegistry;

// 1.7.10's GameRegistry.registerBlock already creates a plain ItemBlock for
// every block we registered in ModBlocks, so there's no separate item
// registration step like 1.20.1's DeferredRegister<Item> - this class is
// really just the 273 crafting recipes. dye ingredients go through
// ShapedOreRecipe + OreDictionary names ("dyeCyan" etc) instead of a raw
// dye + metadata ItemStack, since that's the standard way to reference
// dye colors without hardcoding metadata values that flip between item
// forms across versions
public class ModItems {

    // border material each family crafts from - same table the 1.20.1
    // recipes used, except mossy (moss_block doesn't exist until 1.17,
    // swapped for mossy_cobblestone) and corroded (iron_nugget doesn't
    // exist until 1.11, swapped for redstone)
    private static final Map<String, Item> FAMILY_INGREDIENTS = new HashMap<>();
    static {
        FAMILY_INGREDIENTS.put("crystal", Item.getItemFromBlock(Blocks.stone));
        FAMILY_INGREDIENTS.put("speckle", Item.getItemFromBlock(Blocks.cobblestone));
        FAMILY_INGREDIENTS.put("brick", Items.brick);
        FAMILY_INGREDIENTS.put("corroded", Items.redstone);
        FAMILY_INGREDIENTS.put("mossy", Item.getItemFromBlock(Blocks.mossy_cobblestone));
        FAMILY_INGREDIENTS.put("marble", Item.getItemFromBlock(Blocks.quartz_block));
        FAMILY_INGREDIENTS.put("hexplate", Items.iron_ingot);
    }

    private static final Map<String, String> COLOR_DYE_ORE_NAMES = new HashMap<>();
    static {
        COLOR_DYE_ORE_NAMES.put("cyan", "dyeCyan");
        COLOR_DYE_ORE_NAMES.put("red", "dyeRed");
        COLOR_DYE_ORE_NAMES.put("green", "dyeGreen");
        COLOR_DYE_ORE_NAMES.put("purple", "dyePurple");
        COLOR_DYE_ORE_NAMES.put("orange", "dyeOrange");
        COLOR_DYE_ORE_NAMES.put("blue", "dyeBlue");
    }

    public static void registerItems() {
        // nothing to do here - registerBlock in ModBlocks already gave
        // every block its item form
    }

    public static void registerRecipes() {
        for (String family : ModBlocks.FAMILIES) {
            Item ingredient = FAMILY_INGREDIENTS.get(family);

            for (String color : ModBlocks.COLORS) {
                String dyeOreName = COLOR_DYE_ORE_NAMES.get(color);

                Block block = ModBlocks.BLOCKS_BY_FAMILY.get(family)
                    .get(color);
                GameRegistry.addRecipe(
                    new ShapedOreRecipe(
                        new ItemStack(block, 8),
                        "III",
                        "IDI",
                        "III",
                        'I',
                        ingredient,
                        'D',
                        dyeOreName));

                Block slab = ModBlocks.SLABS_BY_FAMILY.get(family)
                    .get(color);
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(slab, 6), "BBB", 'B', block));

                Block stairs = ModBlocks.STAIRS_BY_FAMILY.get(family)
                    .get(color);
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stairs, 4), "B  ", "BB ", "BBB", 'B', block));

                Block fence = ModBlocks.FENCES_BY_FAMILY.get(family)
                    .get(color);
                GameRegistry.addRecipe(
                    new ShapedOreRecipe(new ItemStack(fence, 3), "B#B", "B#B", 'B', block, '#', Items.stick));

                Block fenceGate = ModBlocks.FENCE_GATES_BY_FAMILY.get(family)
                    .get(color);
                GameRegistry.addRecipe(
                    new ShapedOreRecipe(new ItemStack(fenceGate, 1), "#B#", "#B#", 'B', block, '#', Items.stick));

                Block wall = ModBlocks.WALLS_BY_FAMILY.get(family)
                    .get(color);
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(wall, 6), "BBB", "BBB", 'B', block));
            }

            Block glowBlock = ModBlocks.GLOW_BLOCKS_BY_FAMILY.get(family);
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(glowBlock, 8),
                    "III",
                    "IGI",
                    "III",
                    'I',
                    ingredient,
                    'G',
                    Items.glowstone_dust));

            Block glowSlab = ModBlocks.GLOW_SLABS_BY_FAMILY.get(family);
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(glowSlab, 6), "BBB", 'B', glowBlock));

            Block glowStairs = ModBlocks.GLOW_STAIRS_BY_FAMILY.get(family);
            GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(glowStairs, 4), "B  ", "BB ", "BBB", 'B', glowBlock));
        }
    }
}

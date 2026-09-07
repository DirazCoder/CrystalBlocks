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

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

// 1.7.10's BlockSlab doesn't merge two placed halves into a full block on
// its own like 1.20.1's SlabBlock does - vanilla handles this by pairing
// up a single and a double block instance and swapping between them on
// placement. this class covers both halves of that pair; which one an
// instance is gets fixed at construction via the isDouble flag
public class SlabCrystalBlocks extends BlockSlab {

    private final boolean isDouble;
    private final String textureName;
    private Block singleSlabBlock;
    private Block doubleSlabBlock;

    public SlabCrystalBlocks(boolean isDouble, String textureName) {
        super(isDouble, Material.rock);
        this.isDouble = isDouble;
        this.textureName = textureName;
        this.useNeighborBrightness = true;
    }

    public void setSingleSlabBlock(Block singleSlabBlock) {
        this.singleSlabBlock = singleSlabBlock;
    }

    public void setDoubleSlabBlock(Block doubleSlabBlock) {
        this.doubleSlabBlock = doubleSlabBlock;
    }

    // BlockSlab's abstract per-variant name hook - confirmed by the compiler
    // for this MCP mapping, unlike my last guess. it's what getUnlocalizedName
    // (int meta) below actually needs to route through, not the other way
    // around
    @Override
    public String func_150002_b(int meta) {
        return super.getUnlocalizedName();
    }

    @Override
    public void registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("crystalblocks:" + textureName);
    }

    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(singleSlabBlock, 2, meta);
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return Item.getItemFromBlock(singleSlabBlock);
    }

    @Override
    public int quantityDropped(Random random) {
        return isDouble ? 2 : 1;
    }

    // vanilla's BlockSlab.getItem() (used by pick-block / middle-click) only
    // special-cases Blocks.double_stone_slab and Blocks.double_wooden_slab -
    // anything else falls through to Item.getItemFromBlock(Blocks.stone_slab).
    // that's the middle-click-gives-stone-slab bug: our double variant isn't
    // either of those, so it hits the fallback. overriding here routes it
    // through our own singleSlabBlock instead.
    @Override
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(isDouble ? singleSlabBlock : this);
    }

    // stacking two single slabs on top of each other swaps them for the
    // double variant, same as vanilla stone/wood slabs. isDouble here is
    // our own field mirroring the constructor arg - BlockSlab doesn't
    // expose its internal copy as anything accessible from a subclass
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        if (!isDouble && doubleSlabBlock != null) {
            int meta = world.getBlockMetadata(x, y, z);
            Block blockBelow = world.getBlock(x, y - 1, z);
            Block blockAbove = world.getBlock(x, y + 1, z);
            if (blockBelow == this) {
                world.setBlock(x, y - 1, z, doubleSlabBlock, 0, 2);
                world.setBlockToAir(x, y, z);
            } else if (blockAbove == this) {
                world.setBlock(x, y, z, doubleSlabBlock, 0, 2);
                world.setBlockToAir(x, y + 1, z);
            }
        }
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        // only the single slab shows up in creative - the double slab is
        // never obtainable directly, only ever produced by stacking
        if (!isDouble) {
            list.add(new ItemStack(item, 1, 0));
        }
    }
}

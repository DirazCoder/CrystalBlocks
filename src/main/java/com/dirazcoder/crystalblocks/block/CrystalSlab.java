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

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

// don't manually manage double slabs here - 1.12.2's BlockSlab already
// knows how to swap a placed single for the paired double block, the
// same way vanilla's stone/wood slabs do. this class just needs to
// answer BlockSlab's abstract hooks (variant property, meta<->state,
// which ItemStack represents this block) - no texture wiring needed
// here at all, unlike 1.7.10's getIcon/registerBlockIcons hack, since
// it's all JSON models wired up by whatever blockstate file matches
// this block's registry name
public class CrystalSlab extends BlockSlab {

    private final boolean isDoubleSlab;

    public CrystalSlab(boolean isDoubleSlab) {
        super(Material.ROCK);
        setSoundType(SoundType.STONE);
        this.isDoubleSlab = isDoubleSlab;
        this.useNeighborBrightness = !isDoubleSlab;

        IBlockState state = this.blockState.getBaseState();
        if (!isDoubleSlab) {
            state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
        }
        setDefaultState(state);
    }

    @Override
    public boolean isDouble() {
        return isDoubleSlab;
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return getUnlocalizedName();
    }

    @Override
    public IProperty<?> getVariantProperty() {
        // BlockSlab still demands a variant property even though we
        // don't have color/type metadata to track - every color is its
        // own registered block here, not a meta variant on a shared
        // one - so this just points back at the half property, which
        // is a no-op for the double slab since it never reads HALF
        return HALF;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return EnumBlockHalf.BOTTOM;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getDefaultState();
        if (!isDoubleSlab) {
            state = state.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (isDoubleSlab) {
            return 0;
        }
        return state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return isDoubleSlab ? new BlockStateContainer(this) : new BlockStateContainer(this, HALF);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        // only the single slab is obtainable directly - the double slab
        // is never craftable or holdable on its own, only ever produced
        // by stacking two singles, same restriction as vanilla stone/
        // wood double slabs
        if (!isDoubleSlab) {
            items.add(new ItemStack(this));
        }
    }
}

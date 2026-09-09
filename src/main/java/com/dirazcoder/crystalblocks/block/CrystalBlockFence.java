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

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

// JSON models handle texturing entirely now - no getIcon/registerBlockIcons
// override needed here at all, unlike 1.7.10 where BlockFence genuinely
// implemented registerBlockIcons and this class had to hook it.
//
// BlockFence checks each neighbor with both canBeConnectedTo(world, pos,
// facing) on the neighbor and canConnectTo(world, pos, facing) on itself
// (both 3-arg on this Forge build, per compileJava - not the 2-arg
// canConnectTo some older/other mapping sets describe); either one
// returning true is enough to connect. canBeConnectedTo's "pos" is
// always our own position, so an instanceof check inside it is a
// tautology - it can't tell our own fences apart from a vanilla fence
// asking, and has to just return false unconditionally. Our own
// fence-to-fence and fence-to-gate connections come entirely from
// canConnectTo below, where "pos" is genuinely the neighbor's position.
public class CrystalBlockFence extends BlockFence {

    public CrystalBlockFence(Material material, MapColor mapColor) {
        super(material, mapColor);
        setSoundType(SoundType.STONE);
    }

    // BlockFence.isFullCube(IBlockState) is already false in vanilla,
    // but keeping this explicit removes any doubt - vanilla's fallback
    // treats an opaque, full-cube neighbor as connectable regardless of
    // Material, and Material.ROCK is opaque.
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    // Called on our own instance with the neighbor's position, so this is
    // the one place we can actually tell what the neighbor is.
    @Override
    public boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        Block block = world.getBlockState(pos)
            .getBlock();
        if (block instanceof CrystalBlockFence || block instanceof CrystalBlockFenceGate) {
            return true;
        }
        return super.canConnectTo(world, pos, facing);
    }

    // See the class comment - "pos" here is always our own block, so we
    // can't tell who's asking. Always false; canConnectTo above is what
    // lets our own fences and gates connect to each other.
    @Override
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return false;
    }
}

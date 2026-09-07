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
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;

// BlockFence does implement registerBlockIcons for real (unlike
// BlockFenceGate/BlockWall, see those classes), it just reads from an
// internal field this constructor arg fills - overriding here to be
// explicit about which domain the icon comes from
public class CrystalBlockFence extends BlockFence {

    private final String textureName;

    public CrystalBlockFence(String textureName, Material material) {
        super(textureName, material);
        this.textureName = textureName;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("crystalblocks:" + textureName);
    }

    // canConnectFenceTo hardcodes block != Blocks.fence_gate, so any
    // custom gate class (CrystalBlockFenceGate included) fails that
    // check and falls through to the opacity test, which fence gates
    // always fail too - net result, fences never visually connect to
    // our gates. also treats vanilla's fence as one singular type
    // (block != this only matches the exact same color instance), so
    // without this override our own different-colored fences wouldn't
    // connect to each other either
    @Override
    public boolean canConnectFenceTo(IBlockAccess world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof CrystalBlockFence || block instanceof CrystalBlockFenceGate) {
            return true;
        }
        return super.canConnectFenceTo(world, x, y, z);
    }
}

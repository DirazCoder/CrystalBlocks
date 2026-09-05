package com.dirazcoder.crystalblocks.block;

import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

// BlockFence(String, Material) still rendered missing-texture even with
// the model texture name passed in and setBlockTextureName called - the
// constructor arg is presumably resolved under the minecraft: domain
// since that's how vanilla itself calls it (nether_brick_fence etc),
// but that's not confirmed against the real registerBlockIcons body.
// forcing the icon lookup here sidesteps the question entirely
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
}

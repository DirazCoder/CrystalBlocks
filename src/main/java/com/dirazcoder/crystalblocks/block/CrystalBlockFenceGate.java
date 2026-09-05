package com.dirazcoder.crystalblocks.block;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

// confirmed from decompiled BlockFenceGate.java: getIcon(int, int)
// hardcodes Blocks.planks regardless of anything passed to the
// constructor or set via setBlockTextureName, and registerBlockIcons
// is an empty override that does nothing. overriding getIcon here is
// the only way to make the gate show its own texture instead of oak
// planks
public class CrystalBlockFenceGate extends BlockFenceGate {

    private final String textureName;
    private IIcon icon;

    public CrystalBlockFenceGate(String textureName) {
        super();
        this.textureName = textureName;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icon = iconRegister.registerIcon("crystalblocks:" + textureName);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icon;
    }
}

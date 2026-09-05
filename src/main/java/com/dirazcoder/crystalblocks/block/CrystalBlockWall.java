package com.dirazcoder.crystalblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

// confirmed from decompiled BlockWall.java: getIcon(int, int) hardcodes
// Blocks.cobblestone/mossy_cobblestone regardless of the model block
// passed to the constructor - that model block is only ever read for
// hardness/resistance/sound, never texture. registerBlockIcons is an
// empty override that does nothing. overriding getIcon here is the
// only way to make the wall show its own texture instead of cobble
public class CrystalBlockWall extends BlockWall {

    private final String textureName;
    private IIcon icon;

    public CrystalBlockWall(Block modelBlock, String textureName) {
        super(modelBlock);
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

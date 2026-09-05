package com.dirazcoder.crystalblocks.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

    // getSubBlocks on vanilla BlockWall always lists meta 0 and meta 1,
    // since one wall instance covers both normal and mossy cobblestone
    // there. we don't use meta 1 at all and getIcon above ignores meta
    // entirely, so both entries would show as identical duplicates in
    // the creative tab - only list the one we actually use
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }
}

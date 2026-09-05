package com.dirazcoder.crystalblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

// Block(Material) is protected in 1.7.10, so nothing outside
// net.minecraft.block can call it directly - not even GameRegistry.
// this exists purely to open that constructor back up for ModBlocks.
public class CrystalBlock extends Block {

    public CrystalBlock(Material material) {
        super(material);
    }
}

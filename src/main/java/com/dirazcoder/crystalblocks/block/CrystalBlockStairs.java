package com.dirazcoder.crystalblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

// same story as CrystalBlock - BlockStairs(Block, int) is protected,
// so ModBlocks needs a subclass in this package to reach it.
public class CrystalBlockStairs extends BlockStairs {

    public CrystalBlockStairs(Block modelBlock, int modelMeta) {
        super(modelBlock, modelMeta);
    }
}

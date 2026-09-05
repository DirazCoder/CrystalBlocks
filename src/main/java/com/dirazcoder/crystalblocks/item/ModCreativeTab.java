package com.dirazcoder.crystalblocks.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.dirazcoder.crystalblocks.CrystalBlocksMod;
import com.dirazcoder.crystalblocks.block.ModBlocks;

// own creative tab so all the block families sit together instead of
// getting scattered across vanilla's building blocks tab. CreativeTabs'
// abstract icon hook on this Forge build is getTabIconItem (returns
// Item), not the older getIconItemStack - the compiler is the source
// of truth here since obfuscated/Forge-patched signatures drift between
// MCP mappings
public class ModCreativeTab extends CreativeTabs {

    public static final ModCreativeTab CRYSTAL_TAB = new ModCreativeTab();

    private ModCreativeTab() {
        super(CrystalBlocksMod.MOD_ID);
    }

    // safe to reach into ModBlocks here even though ModBlocks also
    // references CRYSTAL_TAB - this only runs when the game actually
    // renders the tab icon, well after registerBlocks() has populated
    // BLOCKS_BY_FAMILY, not at class-init time
    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(
            ModBlocks.BLOCKS_BY_FAMILY.get("crystal")
                .get("cyan"));
    }
}

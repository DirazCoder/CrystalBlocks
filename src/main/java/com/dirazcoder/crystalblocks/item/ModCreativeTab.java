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

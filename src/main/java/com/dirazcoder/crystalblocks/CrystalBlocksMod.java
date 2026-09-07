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

package com.dirazcoder.crystalblocks;

import com.dirazcoder.crystalblocks.block.ModBlocks;
import com.dirazcoder.crystalblocks.item.ModCreativeTab;
import com.dirazcoder.crystalblocks.item.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CrystalBlocksMod.MOD_ID)
public class CrystalBlocksMod {

    public static final String MOD_ID = "crystalblocks";

    public CrystalBlocksMod() {
        // yeah the .get() call throws a deprecation warning in some setups but
        // it's not actually deprecated til forge 1.21.1, checked forge's own
        // source to be sure. not swapping this for the "fixed" version since
        // that needs a constructor param i haven't confirmed works on 47.4.23,
        // not worth risking a real bug just to shut up a fake warning
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTab.register(modEventBus);
    }
}

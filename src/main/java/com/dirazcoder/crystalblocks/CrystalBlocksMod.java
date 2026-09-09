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

import net.minecraftforge.fml.common.Mod;

// 1.7.10 had no event bus / deferred registry, so registration was
// explicit static calls from preInit (ModBlocks.registerBlocks(),
// ModItems.registerItems()) and init (ModItems.registerRecipes()).
//
// 1.12.2 replaces all of that with RegistryEvent.Register<T>, fired by
// Forge itself at the right point in the load order - ModBlocks and
// ModItems each subscribe via @Mod.EventBusSubscriber and never need
// to be called from here. Recipes are no longer Java at all; they're
// JSON files under assets/crystalblocks/recipes/ that Forge scans and
// loads on its own. That leaves nothing left for this class to do in
// preInit/init - the @Mod annotation alone is enough to make Forge
// discover the mod and its @Mod.EventBusSubscriber classes.
@Mod(modid = CrystalBlocksMod.MOD_ID, name = "CrystalBlocks", version = "1.0.0")
public class CrystalBlocksMod {

    public static final String MOD_ID = "crystalblocks";
}

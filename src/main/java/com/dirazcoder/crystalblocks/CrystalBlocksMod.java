package com.dirazcoder.crystalblocks;

import com.dirazcoder.crystalblocks.block.ModBlocks;
import com.dirazcoder.crystalblocks.item.ModItems;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

// 1.7.10 has no event bus / deferred registry, registration is just static
// calls off GameRegistry, done in preInit so blocks/items exist before
// anything else (recipes, tabs) tries to reference them in init
@Mod(modid = CrystalBlocksMod.MOD_ID, name = "CrystalBlocks", version = "1.0.0")
public class CrystalBlocksMod {

    public static final String MOD_ID = "crystalblocks";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModBlocks.registerBlocks();
        ModItems.registerItems();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModItems.registerRecipes();
    }
}

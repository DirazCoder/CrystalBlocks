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

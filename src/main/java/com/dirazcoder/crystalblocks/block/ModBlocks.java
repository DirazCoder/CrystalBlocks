package com.dirazcoder.crystalblocks.block;

import com.dirazcoder.crystalblocks.CrystalBlocksMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

// registers every block family (crystal, speckle, brick, corroded, mossy,
// marble, hexplate) across 6 colors - each gets a block, slab, stairs,
// fence, fence gate, and wall, all same stone-tier properties, only
// texture/color differ. glow variant per family gets its own block, slab,
// and stairs too, just no fence/gate/wall - didn't seem worth it for a
// light source nobody's building a full fence line out of
public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CrystalBlocksMod.MOD_ID);

    // add a family name here to extend the set, everything else loops off this
    public static final List<String> FAMILIES = List.of(
            "crystal", "speckle", "brick", "corroded", "mossy", "marble", "hexplate"
    );

    private static final Map<String, MapColor> VARIANTS = new LinkedHashMap<>();
    static {
        VARIANTS.put("cyan", MapColor.COLOR_CYAN);
        VARIANTS.put("red", MapColor.COLOR_RED);
        VARIANTS.put("green", MapColor.COLOR_GREEN);
        VARIANTS.put("purple", MapColor.COLOR_PURPLE);
        VARIANTS.put("orange", MapColor.COLOR_ORANGE);
        VARIANTS.put("blue", MapColor.COLOR_BLUE);
    }

    // family -> color -> registered block, e.g. BLOCKS_BY_FAMILY.get("brick").get("red")
    public static final Map<String, Map<String, RegistryObject<Block>>> BLOCKS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, RegistryObject<Block>>> SLABS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, RegistryObject<Block>>> STAIRS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, RegistryObject<Block>>> FENCES_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, RegistryObject<Block>>> FENCE_GATES_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, Map<String, RegistryObject<Block>>> WALLS_BY_FAMILY = new LinkedHashMap<>();

    // one glowing block per family, full light level, crafted with glowstone
    // dust instead of dye. family -> registered block. now also has a slab
    // and stairs form - kept the light level at 15 on both halves instead of
    // trying to dim it for a half-block, not worth the complexity
    public static final Map<String, RegistryObject<Block>> GLOW_BLOCKS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<Block>> GLOW_SLABS_BY_FAMILY = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<Block>> GLOW_STAIRS_BY_FAMILY = new LinkedHashMap<>();

    static {
        for (String family : FAMILIES) {
            Map<String, RegistryObject<Block>> blocks = new LinkedHashMap<>();
            Map<String, RegistryObject<Block>> slabs = new LinkedHashMap<>();
            Map<String, RegistryObject<Block>> stairs = new LinkedHashMap<>();
            Map<String, RegistryObject<Block>> fences = new LinkedHashMap<>();
            Map<String, RegistryObject<Block>> fenceGates = new LinkedHashMap<>();
            Map<String, RegistryObject<Block>> walls = new LinkedHashMap<>();

            for (Map.Entry<String, MapColor> entry : VARIANTS.entrySet()) {
                String color = entry.getKey();
                MapColor mapColor = entry.getValue();

                RegistryObject<Block> block = registerBlock(
                        family + "_block_" + color,
                        () -> new Block(BlockBehaviour.Properties.of()
                                .mapColor(mapColor)
                                .strength(2.0f, 6.0f)
                                .requiresCorrectToolForDrops())
                );
                blocks.put(color, block);

                // apparently SlabBlock doesn't even waterlog on its own, very
                // stupid, but fixed up in WaterloggedSlabBlock, dont worry
                RegistryObject<Block> slab = registerBlock(
                        family + "_slab_" + color,
                        () -> new WaterloggedSlabBlock(BlockBehaviour.Properties.of()
                                .mapColor(mapColor)
                                .strength(2.0f, 6.0f)
                                .requiresCorrectToolForDrops())
                );
                slabs.put(color, slab);

                RegistryObject<Block> stair = registerBlock(
                        family + "_stairs_" + color,
                        () -> new StairBlock(
                                () -> block.get().defaultBlockState(),
                                BlockBehaviour.Properties.of()
                                        .mapColor(mapColor)
                                        .strength(2.0f, 6.0f)
                                        .requiresCorrectToolForDrops())
                );
                stairs.put(color, stair);

                // FenceBlock/WallBlock/FenceGateBlock already waterlog
                // themselves (unlike SlabBlock above), so no wrapper class
                // needed here, just register them straight
                RegistryObject<Block> fence = registerBlock(
                        family + "_fence_" + color,
                        () -> new FenceBlock(BlockBehaviour.Properties.of()
                                .mapColor(mapColor)
                                .strength(2.0f, 6.0f)
                                .requiresCorrectToolForDrops())
                );
                fences.put(color, fence);

                RegistryObject<Block> fenceGate = registerBlock(
                        family + "_fence_gate_" + color,
                        () -> new FenceGateBlock(BlockBehaviour.Properties.of()
                                .mapColor(mapColor)
                                .strength(2.0f, 6.0f)
                                .requiresCorrectToolForDrops(),
                                WoodType.OAK)
                );
                fenceGates.put(color, fenceGate);

                RegistryObject<Block> wall = registerBlock(
                        family + "_wall_" + color,
                        () -> new WallBlock(BlockBehaviour.Properties.of()
                                .mapColor(mapColor)
                                .strength(2.0f, 6.0f)
                                .requiresCorrectToolForDrops())
                );
                walls.put(color, wall);
            }

            BLOCKS_BY_FAMILY.put(family, blocks);
            SLABS_BY_FAMILY.put(family, slabs);
            STAIRS_BY_FAMILY.put(family, stairs);
            FENCES_BY_FAMILY.put(family, fences);
            FENCE_GATES_BY_FAMILY.put(family, fenceGates);
            WALLS_BY_FAMILY.put(family, walls);

            RegistryObject<Block> glow = registerBlock(
                    family + "_block_glow",
                    // this glows the block, 15 is max light level in MC
                    () -> new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_YELLOW)
                            .strength(2.0f, 6.0f)
                            .lightLevel(state -> 15)
                            .requiresCorrectToolForDrops())
            );
            GLOW_BLOCKS_BY_FAMILY.put(family, glow);

            RegistryObject<Block> glowSlab = registerBlock(
                    family + "_slab_glow",
                    () -> new WaterloggedSlabBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_YELLOW)
                            .strength(2.0f, 6.0f)
                            .lightLevel(state -> 15)
                            .requiresCorrectToolForDrops())
            );
            GLOW_SLABS_BY_FAMILY.put(family, glowSlab);

            RegistryObject<Block> glowStair = registerBlock(
                    family + "_stairs_glow",
                    () -> new StairBlock(
                            () -> glow.get().defaultBlockState(),
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_YELLOW)
                                    .strength(2.0f, 6.0f)
                                    .lightLevel(state -> 15)
                                    .requiresCorrectToolForDrops())
            );
            GLOW_STAIRS_BY_FAMILY.put(family, glowStair);
        }
    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        return BLOCKS.register(name, block);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

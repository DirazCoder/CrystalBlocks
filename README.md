# Crystal Blocks

Forge mod for Minecraft 1.7.10. Adds 7 decorative block families, 6 colors each, plus a glowing variant per family. Backported from a newer branch of the same mod, no mechanics, no gimmicks, just blocks you can build with.

## What's in it

| Family | Look | Crafted from |
|-|-|-|
| Crystal | glowing diagonal vein in stone | stone + dye |
| Speckle | scattered mineral flecks | cobblestone + dye |
| Brick | offset masonry, proper mortar lines | bricks + dye |
| Corroded | blotchy rust patches | redstone + dye |
| Mossy | patchy organic overlay | mossy cobblestone + dye |
| Marble | flowing curved vein | quartz block + dye |
| Hexplate | honeycomb plating | iron ingot + dye |

Corroded and mossy swap ingredients from the newer branch this was backported from - iron nugget and moss block don't exist yet at this Minecraft version, so those went to redstone and mossy cobblestone instead.

Each family comes in 6 colors (cyan, red, green, purple, orange, blue). Each color has the full building set - block, slab, stairs, fence, fence gate, wall.

Each family also has one glow variant, crafted with glowstone dust instead of dye, full light level (15). Glow variant gets block + slab + stairs, no fence/gate/wall - didn't seem worth it, nobody's building a glowing fence line.

Slabs pair up single and double instances by hand (`SlabCrystalBlocks`), since this Minecraft version's `BlockSlab` doesn't merge two placed halves into a full block on its own the way later versions do.

Everything drops itself when broken, own creative tab so it's not buried in vanilla's building blocks tab.

## Building it

Needs Java 8 (`enableModernJavaSyntax = jabel` in `gradle.properties` lets the source use newer syntax like `List` type inference while still compiling down to Java 8 bytecode, which is what this Forge version wants).

```
./gradlew build
```

first build takes a while, same story as any Forge/RetroFuturaGradle project - decompiling and patching vanilla, merging MCP mappings. cached after that, rebuilds are fast.

output lands in `build/libs` as three jars: the plain one (drop this in `mods`), `-dev` (deobfuscated, for loading in another mod's dev workspace), and `-sources` (just the `.java` files, not loadable). jar names are derived from the nearest git tag - tag the repo (`git tag 1.0.0`) before a real build or the filename falls back to a commit-hash-plus-dirty string instead of a clean version.

## Dev notes

not stuff a user needs but keeping it here for whoever (probably me in 6 months) opens this again

- `Block(Material)` and `BlockStairs(Block, int)` are both protected constructors, so plain blocks/stairs can't be built directly outside `net.minecraft.block`. `CrystalBlock`/`CrystalBlockStairs` exist purely to reopen those constructors from this package - they don't add any behavior of their own.

- `BlockFenceGate.getIcon(int, int)` hardcodes oak planks and `BlockWall.getIcon(int, int)` hardcodes cobblestone/mossy cobblestone, regardless of what's passed to the constructor or set via `setBlockTextureName` - confirmed straight from the decompiled source, both classes' `registerBlockIcons` overrides are empty no-ops that do nothing. `CrystalBlockFenceGate`/`CrystalBlockWall` override `getIcon` directly to fix this. `BlockFence` doesn't have this problem - it actually implements `registerBlockIcons` for real, so `CrystalBlockFence` only needed to override that.

- texture files must sit under `textures/blocks/` (plural) - not `textures/block/` (singular, which is what later versions use). wrong folder name doesn't error, it just silently falls back to the missing-texture pattern on every block.

- `SlabCrystalBlocks` gets its own `isDouble` field from the constructor arg rather than reading anything off `BlockSlab` - that field is private on the vanilla class with no exposed getter, so there's nothing to inherit.

- `func_150002_b(int)` is `BlockSlab`'s real abstract per-variant name hook on this MCP mapping - it's not optional, leaving it unimplemented is a compile error, not a missing-feature warning.

- registries loop over a `FAMILIES` list instead of hand registering each one so an 8th family = one line added to that list + textures/recipes, not a copy paste job through the java.

## Known gaps

- glow blocks don't have fence/gate/wall (see above, intentional not forgotten - might revisit)
- no waterlogging - this Minecraft version doesn't have the concept at all, slabs/stairs/fences here behave like vanilla stone slabs/stairs/fences always did (solid, no water interaction)
- haven't compiled this against every possible RetroFuturaGradle/GTNH toolchain version, if it doesn't build fresh somewhere it's probably a one line api mismatch not something structurally wrong

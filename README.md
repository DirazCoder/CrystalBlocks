# Crystal Blocks

Forge mod for Minecraft 1.12.2. Adds 7 decorative block families, 6 colors each, plus a glowing variant per family. Same mod as the 1.7.10 branch, ported forward - no mechanics, no gimmicks, just blocks you can build with.

## What's in it

| Family | Look | Crafted from |
|-|-|-|
| Crystal | glowing diagonal vein in stone | stone + dye |
| Speckle | scattered mineral flecks | cobblestone + dye |
| Brick | offset masonry, proper mortar lines | bricks + dye |
| Corroded | blotchy rust patches | iron nugget + dye |
| Mossy | patchy organic overlay | mossy cobblestone + dye |
| Marble | flowing curved vein | quartz block + dye |
| Hexplate | honeycomb plating | iron ingot + dye |

Corroded uses its real ingredient here (iron nugget) instead of the 1.7.10 branch's stand-in (redstone) - iron nugget wasn't a standalone craftable item until 1.8, so that version had to substitute something. Mossy cobblestone's been in vanilla since well before 1.7.10, so mossy's recipe never needed a swap in either branch.

Each family comes in 6 colors (cyan, red, green, purple, orange, blue). Each color has the full building set - block, slab, stairs, fence, fence gate, wall.

Each family also has one glow variant, crafted with glowstone dust instead of dye, full light level (15). Glow variant gets block + slab + stairs, no fence/gate/wall - same call as the 1.7.10 branch, didn't seem worth it, nobody's building a glowing fence line.

Single and double slabs merge into each other the vanilla way - slab items register as `ItemSlab` (paired with their double-slab block) instead of plain `ItemBlock`, so stacking one on a placed slab merges them same as stone/wood slabs. 1.12.2 ships this logic in vanilla, unlike 1.7.10 where it had to be hand-rolled.

Everything drops itself when broken, own creative tab so it's not buried in vanilla's building blocks tab.

## Building it

Needs a JDK Forge's toolchain accepts for 1.12.2 (Java 8 is the safe target; ForgeGradle 3 on this version doesn't need the Jabel-style bytecode-downleveling trick the 1.7.10 branch relies on).

```
./gradlew build
```

first build takes a while - downloads and decompiles vanilla, applies the MCP mappings (`snapshot 20171003-1.12`, pinned in `build.gradle`), sets up the dev workspace. cached after that, rebuilds are fast.

output lands in `build/libs`. plain jar goes in `mods`; reobfuscated automatically by ForgeGradle 3's default task, no separate `-dev`/`-sources` jars unless you configure that yourself (this differs from the 1.7.10 branch's RetroFuturaGradle setup, which produces all three by default).

## Dev notes

not stuff a user needs but keeping it here for whoever (probably me in 6 months) opens this again

- `BlockFence.canConnectTo(IBlockAccess, BlockPos, EnumFacing)` on this Forge build (14.23.5.2864) is a real three-arg method, not the two-arg version some older docs and forum posts quote - confirmed the hard way, straight off a `javac` error, not off search results. `canBeConnectedTo` is also three-arg and is what a neighbor calls on us; `canConnectTo` is what we call on ourselves as a fallback. `canBeConnectedTo` reading `world.getBlockState(pos).getBlock()` at our own position makes an `instanceof` check on that result tautological (it's always `this`) - it can't tell who's asking, so it stays a flat `false`, and `canConnectTo` is where the real same-family check lives.

- `isFullCube(IBlockState)` - the state-taking overload - is the real 1.12.2 signature for this hook, not the no-arg `isFullCube()`. Easy to "fix" the wrong one if you're going off memory of a different version.

- vanilla `ItemSlab`'s constructor is `(Block block, BlockSlab singleSlab, BlockSlab doubleSlab)`. Registering slab items through this instead of plain `ItemBlock` is what gets the placed-slab-merges-into-double-slab behavior for free - no custom `onItemUse` needed.

- textures sit under `textures/block/` (singular) here, not `textures/blocks/` (plural, which is what the 1.7.10 branch uses). wrong folder name doesn't error, it just silently falls back to the missing-texture checkerboard on every block that used it.

- blockstate `"model"` values and item-model `"parent"` values are NOT the same format. Forge's `VanillaLoader` auto-prepends `block/` to a blockstate `"model"` string when resolving multipart/weighted-random variants (stairs, fences, walls, gates) - an item's `"parent"` doesn't get that treatment and needs `block/` written explicitly. Writing both through one shared helper is exactly how you get `block/block/whatever` doubling up in the generated JSON. `generate_assets.py` keeps these as two separate functions (`blockstate_model` / `item_parent`) specifically so this doesn't happen again - don't remerge them.

- `BlockFenceGate.getIcon`/`BlockWall.getIcon` don't exist on this version's `Block`/`BlockFenceGate`/`BlockWall` classes the way they did in 1.7.10 - texturing goes through the model JSON's `textures` block instead (see `models/block/*.json`), so there's no equivalent icon-hardcoding gotcha to work around here. If a wall or gate is showing the wrong texture, the model JSON's `parent` or `textures.wall`/`textures.texture` key is where to look, not the Java class.

- registries loop over a `FAMILIES` list (`ModBlocks`) instead of hand registering each one, so an 8th family is one list entry plus textures/recipes, not a copy-paste job through the Java.

- `generate_assets.py` is a standalone script, not part of the Gradle build - it writes directly into `src/main/resources/assets/crystalblocks/{models,blockstates,recipes}`. Re-run it after touching any family/color/shape logic and check the diff; nothing regenerates automatically on `./gradlew build`, so a stale asset tree from before a script edit will keep shipping in the jar with no warning.

## Known gaps

- glow blocks don't have fence/gate/wall (see above, intentional not forgotten - might revisit)
- no waterlogging on slabs/stairs/fences - this Minecraft version doesn't have the concept at all (it's a 1.13+ flattening-era addition), slabs/stairs/fences here behave like vanilla stone slabs/stairs/fences always did on 1.12.2: solid, no water interaction
- haven't compiled this against every possible Forge/mapping combination for 1.12.2, if it doesn't build fresh somewhere it's probably a one-line mapping mismatch not something structurally wrong
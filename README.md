# Crystal Blocks

Forge mod for Minecraft 1.20.1. Adds 7 decorative block families, 6 colors each, plus a glowing variant per family. First mod, built from scratch, no mechanics, no gimmicks, just blocks you can build with.

## What's in it

| Family | Look | Crafted from |
|-|-|-|
| Crystal | glowing diagonal vein in stone | stone + dye |
| Speckle | scattered mineral flecks | cobblestone + dye |
| Brick | offset masonry, proper mortar lines | bricks + dye |
| Corroded | blotchy rust patches | iron nugget + dye |
| Mossy | patchy organic overlay | moss block + dye |
| Marble | flowing curved vein | quartz block + dye |
| Hexplate | honeycomb plating | iron ingot + dye |

Each family comes in 6 colors (cyan, red, green, purple, orange, blue). Each color has the full building set - block, slab, stairs, fence, fence gate, wall.

Each family also has one glow variant, crafted with glowstone dust instead of dye, full light level (15). Glow variant gets block + slab + stairs, no fence/gate/wall - didn't seem worth it, nobody's building a glowing fence line.

Slabs (colored and glow) waterlog properly, place one in water and it holds the water like a vanilla slab. Fences/gates/walls waterlog too but that's their vanilla base classes doing the work, not something I had to build.

Everything drops itself when broken, needs at least a stone pickaxe, own creative tab so it's not buried in vanilla's building blocks tab.

## Building it

Needs Java 17 (that's what 1.20.1 forge wants - different major version is what'd actually break this, not fix it)

```
./gradlew build
```

first build takes a while, forge has to download + decompile vanilla and merge mojang's mappings onto it. all cached after that though, rebuilds are fast.

## Dev notes

not stuff a user needs but keeping it here for whoever (probably me in 6 months) opens this again

- `WaterloggedSlabBlock` exists because vanilla's `SlabBlock` doesn't implement waterlogging on its own. stone slab / cobble slab etc get it because THEIR classes wire it up, `SlabBlock` itself just doesn't. `StairBlock` already handles it fine so stairs use plain vanilla `StairBlock`, no subclass. same deal for `FenceBlock`/`FenceGateBlock`/`WallBlock` - all three do it themselves, registered straight, no wrapper needed.

- don't add `WATERLOGGED` in a `createBlockStateDefinition` override on top of `SlabBlock`. it already adds that property itself (every slab tracks it whether or not it can hold water) so adding it twice = "duplicate property" crash before you even hit the main menu. ask me how I know

- `FMLJavaModLoadingContext.get()` throws a compiler warning but it's not actually deprecated til Forge 1.21.1 (checked forge's source to be sure, the @Deprecated annotation is version gated). don't "fix" it just cause the IDE's yelling - the replacement isn't confirmed working on 47.4.23 and swapping working code for an untested guess to shut up a fake warning is a bad trade

- `FenceGateBlock` here takes a plain `Properties` ctor, no `WoodType` arg. that's correct for 47.4.23/1.20.1 - the WoodType version is a 1.21+ thing tied to the sound events rework. if some newer guide says otherwise the guide's just ahead of this MC version

- registries loop over a `FAMILIES` list instead of hand registering each one so an 8th family = one line added to that list + textures/json, not a copy paste job through the java

## Known gaps

- glow blocks don't have fence/gate/wall (see above, intentional not forgotten - might revisit)
- haven't compiled this against a live forge maven in every env it's touched, if it doesn't build fresh somewhere it's probably just a one line api mismatch not something structurally wrong
![Beds, but Endgame banner](assets/beds-but-endgame-banner.png)

# Beds, but Endgame

Beds, but Endgame is a Fabric challenge mod for Minecraft Java Edition 26.2.

Sleeping requires a Bedside Table next to the head of the bed. Beds still set the player's respawn point without one, so the restriction affects skipping the night rather than basic respawning.

## Main features

- Bedside Table block with a custom model and texture
- Sleeping requires a table beside the head of the bed
- Either side of the bed head works
- Respawn points remain available without a table
- Accurate model-shaped selection outline
- Full-block collision for consistent movement
- Wooden sounds and breaking properties
- Axe as the preferred tool
- Fixed diamond-and-planks recipe

## How the sleep requirement works

<p align="center">
  <img src="assets/bedside-table-ingame.png" width="620" alt="Bedside Table placed beside the head of a bed">
</p>

Place a Bedside Table directly to the left or right of the bed's head. A table beside the foot, diagonally placed, above the bed or below it does not count.

When no valid table is present, the bed still sets the player's respawn point but refuses to start sleeping.

## Bedside Table

<p align="center">
  <img src="assets/bedside-table-dropped.png" width="620" alt="Dropped Bedside Table item">
</p>

## Recipe

<p align="center">
  <img src="assets/bedside-table-recipe.png" width="480" alt="Bedside Table crafting recipe">
</p>

```text
PPP
PDP
P P
```

`P` is any item in the vanilla planks tag and `D` is a diamond. The recipe unlocks after the player obtains a diamond.

## Planned mechanics

- Optional secured-sleep check in a 40x20x40 area around the bed
- Sleep denial when hostile mobs could spawn and pathfind to the player
- Optional disabling of natural insomnia phantom spawning
- Optional nightmares unless a Soul Lantern is placed on the Bedside Table
- Configuration through Mod Menu

## Installation

1. Install Fabric Loader 0.19.3 or newer for Minecraft 26.2.
2. Install Fabric API 0.156.0+26.2 or a compatible newer 26.2 build.
3. Put the installable mod JAR in the `mods` folder.
4. Start Minecraft with Java 25.

## Inspiration

Beds, but Endgame was inspired by [Harder Beds](https://modrinth.com/mod/harder-beds). I liked its approach to requiring a properly secured shelter before sleeping, but wanted the mechanic to work differently and include my own Bedside Table progression, so I made a separate implementation.

## Current testing status

The Bedside Table, recipe, item rendering, selection outline, lighting and basic sleep requirement have been tested in singleplayer. Version 0.1.4 restores full-block collision and moves the recipe to the Miscellaneous recipe-book category.

Dedicated multiplayer has not been tested yet.

## Build from source

Java 25 and Gradle 9.5.1 are required.

```bash
gradle build
```

The installable JAR is written to `build/libs/`.

## License

Beds, but Endgame is available under the MIT License.

<img src="assets/passo-logo.png" width="48" alt="Passo logo">

Created by passo.

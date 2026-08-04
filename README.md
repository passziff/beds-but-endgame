<p align="center">
  <img src="assets/beds-but-endgame-banner.png" alt="Beds, but Endgame" width="800">
</p>

# Beds, but Endgame

Beds, but Endgame is a Fabric mod for Minecraft Java Edition 26.2 that makes sleeping something the player has to prepare for.

The finished mod will require a Bedside Table next to the head of a bed before sleeping is allowed. Setting a respawn point will still work without one. Optional settings will add a secured-sleep check around the bed and disable natural insomnia phantom spawning.

## Current build

Version 0.1.1 contains the Bedside Table and its recipe. The sleeping mechanics are not implemented yet.

- Directional Bedside Table block
- Full-block collision and hitbox
- Wooden sounds and breaking properties
- Axe as the preferred tool
- Standard block rendering in the inventory, hand and as a dropped item
- Fixed diamond-and-planks recipe
- Custom model and texture

## Bedside Table

<p align="center">
  <img src="assets/bedside-table-render.png" alt="Bedside Table model" width="360">
</p>

Recipe:

```text
PPP
PDP
P P
```

`P` is any plank and `D` is a diamond. The recipe unlocks after obtaining a diamond.

## Planned sleep rules

- A Bedside Table must be placed directly beside the head of the bed
- Respawn points can still be set without a Bedside Table
- Optional secured-sleep check in a 40x20x40 area around the bed
- Sleep is denied when hostile mobs could spawn and pathfind to the player
- Optional disabling of natural insomnia phantom spawning

## Inspiration

This mod was inspired by [Harder Beds](https://modrinth.com/mod/harder-beds). I liked its approach to requiring a properly secured shelter before sleeping, but wanted the mechanic to work differently and include a Bedside Table, so I made my own implementation.

## Installation

1. Install Fabric Loader 0.19.3 or newer for Minecraft 26.2.
2. Install Fabric API 0.156.0+26.2 or a compatible newer 26.2 build.
3. Put the mod JAR in the `mods` folder.
4. Start Minecraft with Java 25.

## Testing status

The current Bedside Table build has been tested in singleplayer. The sleeping systems and dedicated multiplayer support have not been implemented yet.

## Build from source

Java 25 is required.

```bash
gradle build
```

The installable JAR is written to `build/libs/`.

## License

Beds, but Endgame is available under the MIT License.

<p align="center">
  <img src="assets/passo-logo.png" alt="passo" width="64"><br>
  Created by passo.
</p>

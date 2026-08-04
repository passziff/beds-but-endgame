![Beds, but Endgame banner](assets/beds-but-endgame-banner.png)

# Beds, but Endgame

Beds, but Endgame is a Fabric challenge mod for Minecraft Java Edition 26.2. It turns sleeping into a progression mechanic built around Bedside Tables, nightmares and an optional Soul Lantern upgrade.

## How it works

<p align="center">
  <img src="assets/bedside-table-ingame.png" width="620" alt="Bedside Table beside a bed with a Soul Lantern on top">
</p>

Place any Bedside Table directly beside either side of the bed's head. A table beside the foot, placed diagonally, above the bed or below it does not count.

Without a valid table, the bed still sets the player's respawn point but sleeping is denied.

## Features

- Twelve Bedside Table variants matching the standard Minecraft wood families
- Sleeping requires a table beside the bed's head
- Respawn points remain available without a table
- Configurable nightmare chance from 0% to 100%
- A Soul Lantern on a valid Bedside Table prevents nightmares
- Natural insomnia phantoms can be disabled
- Shared configuration through Mod Menu and `/bbe` commands

The default nightmare chance is 35%. Phantom suppression is enabled by default.

## Wood variants

The Bedside Table is available in:

- Oak
- Spruce
- Birch
- Jungle
- Acacia
- Dark Oak
- Mangrove
- Cherry
- Pale Oak
- Bamboo
- Crimson
- Warped

Each variant uses the matching vanilla plank texture, so resource packs that change vanilla planks also change the corresponding Bedside Table. The original `bedsbutendgame:bedside_table` block is now the Oak Bedside Table, preserving existing worlds.

## Bedside Table recipe

<p align="center">
  <img src="assets/bedside-table-recipe.png" width="480" alt="Bedside Table crafting recipe">
</p>

```text
PPP
PDP
P P
```

`P` is the matching plank type and `D` is a diamond. For example, spruce planks craft a Spruce Bedside Table. All Bedside Table recipes unlock after the player obtains a diamond and share one cycling recipe-book slot.

## Nightmares

Without a Soul Lantern directly on top of either valid Bedside Table, sleeping can trigger a nightmare.

The player falls asleep normally, then wakes during the same night instead of reaching morning. After a nightmare, that player cannot sleep again until daytime. The lockout is handled separately for each player.

The chance can be set from 0% to 100%. Setting it to 0% disables nightmares. A Soul Lantern prevents nightmares completely regardless of the configured chance.

## Phantom suppression

When enabled, the mod prevents natural insomnia-based phantom spawning. Existing phantoms and phantoms created through commands, spawn eggs or other mechanics are unaffected.

## Configuration

The settings are server-authoritative and saved in:

```text
config/bedsbutendgame.json
```

Default values:

```json
{
  "disablePhantoms": true,
  "nightmareChance": 35
}
```

With Mod Menu installed, the same settings can be changed from the mod list. The nightmare chance uses a slider in 5% steps. Changes are applied when Done is pressed, and Reset to Defaults restores the default values. On multiplayer servers, changing settings requires operator permission.

Commands:

```text
/bbe config
/bbe config disablePhantoms <on|off>
/bbe config nightmareChance <0-100>
/bbe config reset
```

Command changes are saved immediately and synchronized with the Mod Menu screen. Existing 0.2.0 config files are migrated automatically: an enabled nightmare toggle becomes 35%, while a disabled toggle becomes 0%.

## Installation

1. Install Fabric Loader 0.19.3 or newer for Minecraft 26.2.
2. Install Fabric API 0.156.0+26.2 or a compatible newer 26.2 build.
3. Put the installable mod JAR in the `mods` folder.
4. Start Minecraft with Java 25.

Mod Menu is optional and only required for the graphical config screen.

## Inspiration

Beds, but Endgame was inspired by [Harder Beds](https://modrinth.com/mod/harder-beds). I liked its approach to making sleep require a properly secured shelter, but wanted the mechanic to function differently and include my own Bedside Table progression system, so I created a separate implementation.

## Testing status

Version 0.2.0 was tested in singleplayer, including world joining, sleeping, respawn handling, nightmares, Soul Lantern protection, phantom suppression, Mod Menu configuration and `/bbe` commands.

Version 0.3.0 adds the nightmare chance slider, reset controls and all wood variants. These changes still need a complete singleplayer test pass. Multiplayer and dedicated-server behavior have not yet been fully tested.

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

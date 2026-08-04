# Changelog

## 0.3.0

- Added Oak, Spruce, Birch, Jungle, Acacia, Dark Oak, Mangrove, Cherry, Pale Oak, Bamboo, Crimson and Warped Bedside Tables
- Kept the original `bedside_table` ID as the Oak variant for world compatibility
- Switched Bedside Tables to matching vanilla plank textures with shared model geometry
- Added matching-plank recipes and grouped them into one cycling recipe-book slot
- Replaced the nightmare toggle with a configurable 0% to 100% chance
- Added a Mod Menu nightmare chance slider in 5% steps
- Added Reset to Defaults in Mod Menu and `/bbe config reset`
- Added `/bbe config nightmareChance <0-100>`
- Added automatic migration from the 0.2.0 nightmare toggle
- Removed the redundant “Server settings.” text from Mod Menu
- Fixed overly dark model shading when a Bedside Table touches a full block
- Updated the README banner and screenshots

## 0.2.0

- Added a server-authoritative configuration shared by Mod Menu and commands
- Added `/bbe config` commands using `on` and `off` values
- Allowed config changes in singleplayer without enabling cheats
- Added optional natural insomnia phantom suppression, enabled by default
- Added nightmares with a 35% chance, enabled by default
- Added per-player sleep lockout until daytime after a nightmare
- Added Soul Lantern protection against nightmares
- Synchronized configuration values to connected clients
- Removed the diamond handles from the Bedside Table selection outline
- Replaced the first README screenshot with the Soul Lantern image
- Reworked the README around the new mechanics and configuration

## 0.1.4

- Restored a full-block collision box for the Bedside Table
- Kept the model-shaped selection outline and lighting shape
- Moved the Bedside Table recipe to the Miscellaneous recipe-book category beside beds
- Removed the technical collision paragraph from the README
- Updated the build workflow to use `actions/upload-artifact@v7`

## 0.1.3

- Added a model-shaped selection outline for the Bedside Table
- Added model-shaped physical collision without the small drawer handles
- Added correctly rotated shapes for all four placement directions
- Reworked light occlusion to follow the wooden model instead of a full cube
- Restored normal model ambient shading
- Updated the artifact upload action to remove the Node.js 20 warning
- Reorganized the README and placed screenshots beside the features they explain

## 0.1.2

- Added the Bedside Table requirement for sleeping
- Bedside Tables work on either side of the bed's head
- Kept respawn-point setting available without a Bedside Table
- Added a clear message when sleeping is denied
- Disabled model ambient occlusion while investigating the misplaced shadow fringe
- Added in-game screenshots to the README

## 0.1.1

- Fixed the Bedside Table block codec for Minecraft 26.2
- Fixed neighboring block faces disappearing next to the Bedside Table
- Added standard block item transforms for the inventory, hand and dropped item
- Added the mod icon and repository branding
- Updated the README and project structure

## 0.1.0

- Added the Bedside Table block
- Added directional placement
- Added wooden sounds and breaking properties
- Added the diamond and planks recipe
- Added the custom model and texture

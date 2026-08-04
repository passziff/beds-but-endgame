# Beds, but Endgame

Beds, but Endgame makes sleeping something that has to be prepared for instead of being available immediately.

The finished mod will require a bedside table next to the head of a bed before a player can sleep. Setting a respawn point will still be possible without one. It will also include optional secured-sleep checks and an option to disable natural insomnia phantom spawning.

## Current build

Version 0.1.0 contains the first implementation step:

- Bedside Table block
- directional placement
- wooden block sounds and breaking properties
- axe as the preferred tool
- fixed diamond-and-planks recipe
- custom model and texture

Recipe:

```text
PPP
PDP
P P
```

`P` is any plank and `D` is a diamond.

## Inspiration

This mod was inspired by [Harder Beds](https://modrinth.com/mod/harder-beds). I liked its idea of requiring a properly secured shelter before sleeping, but wanted the mechanic to work differently and include a bedside-table requirement, so I made a separate implementation.

## Building

Java 25 is required.

The included GitHub Actions workflow installs Gradle 9.5.1 and builds the project automatically. The installable JAR is uploaded as a workflow artifact after a successful build.

To build locally with Gradle installed:

```bash
gradle build
```

The JAR is written to `build/libs/`.

## License

MIT

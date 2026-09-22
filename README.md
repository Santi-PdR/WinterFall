# WinterFall

WinterFall is a Forge 1.20.1 survival-combat mod inspired by the tense scavenging and preparation loop of winter survival games. It is an original implementation and does not include copied Decaying Winter assets or code.

## Requirements

- Minecraft 1.20.1
- Forge 47.4.10
- Java 17

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-temurin-jdk
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew clean build
```

## Current playable systems

- Server-authoritative stamina, thirst, morale, salvage, wounds, perks and wave state.
- Light and heavy melee attacks, backstabs, bleeding/stun feedback.
- Reloadable firearm framework with hitscan, recoil, spread, headshots and short ammunition.
- Scrapper and Workbench interactions.
- Combat Knife Scavenger AI, progression waves, development commands, HUD and data generation.

See `AUDIO_ASSETS.md` for the intentionally external audio asset manifest.

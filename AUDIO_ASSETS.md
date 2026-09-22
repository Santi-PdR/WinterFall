# WinterFall audio asset manifest

WinterFall registers a stable sound-event namespace for weapons, impacts, hostile scavs, UI and storm ambience. No low-quality or copied audio is bundled in this first source release.

Before a content release, provide original `.ogg` files using these exact event paths:

- `winterfall:weapon.melee_light`, `weapon.melee_heavy`, `weapon.fire`, `weapon.reload`
- `winterfall:impact.flesh`, `impact.headshot`
- `winterfall:scavenger.alert`, `scavenger.attack`, `scavenger.hurt`, `scavenger.death`
- `winterfall:ui.salvage`, `ui.wave_start`, `ambient.storm`

The gameplay code gracefully falls back to vanilla sounds until those files are supplied.

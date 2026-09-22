# WinterFall — stabilization report (September 22, 2026)

## Crash investigation and fix

The real instance log at `~/.sklauncher/instances/test-1/logs/latest.log` identified the root cause:

```text
java.lang.NullPointerException: ... EntityRenderer ... because "entityrenderer" is null
at EntityRenderDispatcher
```

`CombatKnifeScavenger` had valid registration, constructor, attributes, goals and spawn egg, but no client entity renderer was registered. When the client attempted to render it, Forge resolved a `null` renderer.

Fixed with a client-only `CombatKnifeScavengerRenderer`, `ClientModEvents` registration through `EntityRenderersEvent.RegisterRenderers`, and an original entity texture. The registration is protected by `Dist.CLIENT`, so dedicated servers do not load rendering classes.

## Improvements made

- Added the ordered `WINTERFALL` creative tab for every currently registered WinterFall item.
- Added useful melee and firearm statistic tooltips.
- Corrected firearm headshots to use the actual server-side hitscan intersection rather than the target eye position.
- Added `/winterfall wave stop`, `/winterfall injury bleeding|wounded|clear <target>`, and `/winterfall morale set <target> <0-100>`.

## Validation

`./gradlew clean build` completed successfully with Forge 47.4.10 / Minecraft 1.20.1 / Java 17.

`runClient` was launched after the fix. It completed WinterFall mod discovery, client event subscription and resource-pack loading without a WinterFall exception. It was intentionally stopped after 45 seconds by the test timeout; manual in-world spawn-egg validation is the next test.

## Remaining known scope

- Combat Knife Scavenger is render-safe, but the seven additional requested enemy archetypes and bespoke models remain a content phase.
- Original `.ogg` files listed in `AUDIO_ASSETS.md` remain pending; vanilla fallbacks are intentional.
- Datagen providers, specialized station UI, expanded scavenging loot and wave compositions remain pending.

## Git

- Branch: `codex/winterfall-primary`
- This stabilization stage is pending commit and push.
- `AI_WORKSPACE.md` remains intentionally unmodified and uncommitted.

## Next priority

Install this jar in the test instance and verify the Spawn Egg and `/summon winterfall:combat_knife_scavenger` in a fresh test world, then continue enemy and wave content.

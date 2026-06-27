# Kwin's Sector Industry Compilation

A Starsector faction and content compilation for `0.98a`, centered on several corporate and postwar industrial groups:

- Zenith Era Corporation
- Flicker Aviation
- Monolith Ordnance
- Yunze General Ordnance
- Exodyne Biotechnology
- Tianhong legacy technology

The mod adds corporate shipyards, experimental weapons, rare hulls, fighters, colony items, custom hullmods, hidden support systems, and faction/lore polish.

## Requirements

- Starsector `0.98a`
- LazyLib
- GraphicsLib
- MagicLib
- zz BoxUtil

## Optional Compatibility

- Nexerelin faction-generation support is included, with the corporate factions configured as non-starting, non-random, lore-preserving factions.
- Version Checker/TriOS support is included through `data/config/version/version_files.csv` and `KwinExpand.version`.

## Current Maintenance Notes

This repository contains a polished local maintenance build. Recent cleanup work focused on:

- Nexerelin faction wiring
- Version Checker compatibility
- safer custom weapon and missile behavior
- reduced per-frame campaign/combat overhead
- listener cleanup fixes
- improved ship-local support hullmod behavior
- clearer English descriptions and faction presentation

## Release Packaging

The current Version Checker download URL points at the repository copy:

```text
https://raw.githubusercontent.com/Compterra/Starsector-CFF-Kwin/main/Kwin.zip
```

The zip should contain the top-level `Kwin` folder so users can extract it directly into `Starsector/mods`.
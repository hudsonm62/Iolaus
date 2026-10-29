---
icon: lucide/rocket
---

# Getting Started

An Extensions Library for Heracles (aka Odyssey Quests)

## About

Named after the nephew, squire and charioteer of Heracles,
Iolaus is a library mod that provides a number of useful things for extending Heracles (aka Odyssey Quests) in your own mods.

**For Users**: You only need to install this mod if you use another mod that requires it (such as [Cobblemon Quest Bridge](https://modrinth.com/project/eLwyHtUJ)).

## Using Iolaus

To use Iolaus in your mod, add the following to your `build.gradle` file:

```groovy
repositories {
    maven("https://maven.ziroau.com")
}
dependencies {
    modImplementation "com.ziroau.iolaus:${iolaus_version}+fabric"
}
```

Whilst you _could_ Jar-in-Jar Iolaus, it is entirely untested & recommended to use the `modImplementation` method above to avoid conflicts with other mods.

From here you can access the Iolaus API in your mod code.

### Notice

Heracles doesn't provide any concrete info for extending the mod, so Iolaus is a best-effort attempt
to provide a usable library API for mod developers, fully inferred from the Heracles codebase.
It is not guaranteed to be complete or correct, and may break with future updates to Heracles.

If you find any issues, please report them on the [Iolaus Issue Tracker](https://github.com/hudsonm62/iolaus/issues) or create a [Pull Request](https://github.com/hudsonm62/iolaus/pulls)!

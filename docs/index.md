---
icon: lucide/rocket
---

# Iolaus

An Extensions Library for Heracles (aka Odyssey Quests)

## About

Named after the nephew, squire and charioteer of Heracles,
Iolaus is a library mod that provides a number of useful things for extending Heracles (aka Odyssey Quests) in your own mods.

**For Users**: You only need to install this mod if you use another mod that requires it (such as [Cobblemon Quest Bridge](https://modrinth.com/project/eLwyHtUJ)).

## Using Iolaus

To use Iolaus in your mod, add the following to your `build.gradle` file:

```groovy
repositories {
    maven {
        name = "ziro's maven"
        url = "https://maven.ziroau.com"
    }
}
dependencies {
    modImplementation "com.ziroau.iolaus:${iolaus_version}+fabric"
}
```

For multiloader/architectury setups, you may want to use this in your `common` project:

```groovy
dependencies {
    modCompileOnlyAPI "com.ziroau.iolaus:${iolaus_version}+fabric"
}
```

### Notice

Heracles doesn't provide any concrete info for extending the mod, so Iolaus is a best-effort attempt
to provide a usable library API for mod developers, fully inferred from the Heracles codebase.
It is not guaranteed to be complete or correct, and may break with future updates to Heracles.

If you find any issues, please report them on the [Iolaus Issue Tracker](https://github.com/hudsonm62/iolaus/issues) or create a [Pull Request](https://github.com/hudsonm62/iolaus/pulls)!

## Permissions & Modpack Usage

You're welcome to include Iolaus in your modpack or server, including publicly distributed modpacks.

All I ask is that you:

- **Keep attribution intact.**
    - Include the mod through your modpack's [Modrinth `.mrpack` manifest](https://support.modrinth.com/en/articles/8802351-modrinth-modpack-format-mrpack) where applicable. This helps support the project through download statistics and creator revenue. 🙏
- **Keep features accessible.**
    - Don't lock the mod's functionality behind paywalls or otherwise restrict access to its features.
- **Don't redistribute the mod separately.**
    - Please don't re-upload the mod to other platforms.
- **Respect authorship.**
    - Don't claim the mod or its code as your own.

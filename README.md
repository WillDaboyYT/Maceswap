# MaceSwap

Client-side Fabric mod for Minecraft Java 1.21.11.

## Behavior

When the player left-clicks while holding a mace in hotbar slot 5, MaceSwap:
1. Selects the axe in hotbar slot 4 and runs the normal attack action.
2. Selects the mace in hotbar slot 5 and runs the normal attack action again.
3. Leaves the mace selected.

The macro runs only when slot 4 contains an axe and slot 5 contains a mace. If either condition is not met, the click is left unchanged.

## Build

Every push runs the GitHub Actions workflow. The installable JAR is uploaded as the workflow artifact named **MaceSwap-1.21.11**.

## Install

Copy the built `.jar` into the Minecraft instance's `mods` folder. The instance must use Fabric Loader 0.18.1 or newer and Java 21.

## Important

This performs two client attack actions in immediate succession. Server-side hit cooldowns, packet handling, or server-specific mechanics can affect whether both attacks register.

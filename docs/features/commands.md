# Commands

Iolaus adds some useful commands that (for some reason) aren't already part of the `/heracles` set:

- `/iolaus`
    - **Permission**: `iolaus`
- `/iolaus debug <player> <questid>`
    - Shows debug information for a player's quest.
    - **Permission**: `iolaus.debug`
- `/iolaus completetask <player> <questid> <taskid>`
    - Marks a task as complete.
    - **Permission**: `iolaus.completetask`
- `/iolaus claimreward <player> <questid> <rewardid>`
    - Claims a reward for a completed task.
    - **Permission**: `iolaus.claimreward`

For server-admins, we include [Permission Validator](https://modrinth.com/project/MgGi86RS) under the hood for cross-loader checks for permission nodes.

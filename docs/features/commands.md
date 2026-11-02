# Commands

Iolaus adds some useful commands that (for some reason) aren't already part of the `/heracles` set:

- `/iolaus` & `/iolaus help`
    - Shows the help message.
    - **Permission**: `iolaus`
- `/iolaus completetask <player> <taskid> <questid>`
    - Marks a task as complete.
    - **Permission**: `iolaus.completetask`
- `/iolaus claimreward <player> <taskid> <questid>`
    - Claims a reward for a completed task.
    - **Permission**: `iolaus.claimreward`

For server-admins, we include [Permission Validator](https://modrinth.com/project/MgGi86RS) under the hood for cross-loader checks for permission nodes.

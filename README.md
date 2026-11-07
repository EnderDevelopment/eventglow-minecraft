# EventGlow Minecraft Plugin

EventGlow is a Paper Minecraft plugin that creates a timed event show at the command sender’s location, with particle effects, animated rings, sounds, fireworks, broadcasts, and `/event start` and `/event stop` commands.

## Install

1. Build the plugin with Java 21 and Maven:

   ```bash
   mvn package
   ```

2. Copy the generated JAR from `target/plugin.jar` to your server’s `plugins` folder.

3. Start or restart a Paper 1.21.11 server.

4. Make sure the player has the `event.use` permission. This permission is granted to operators by default.

## Use

Start an event from your current location:

```text
/event start
```

The event creates a timed sequence of particle bursts, expanding rings, sounds, a colorful firework, and a final celebration effect. The event location is fixed at the position where the player starts it.

Stop the active event:

```text
/event stop
```

The start and stop commands are available to players with the `event.use` permission. Only players can start an event because the event uses the player’s location. The stop command can also be used from the console.

The plugin automatically stops the active event when the server shuts down or when a new event is started.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/c2a80de6-e285-4457-aeca-779d04a8d5ab?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=c2a80de6-e285-4457-aeca-779d04a8d5ab

package com.enderdevelopment.plugin;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Main extends JavaPlugin implements CommandExecutor {
    private BukkitTask eventTask;

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("event")).setExecutor(this);
    }

    @Override
    public void onDisable() {
        stopEvent();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(Component.text("Gebruik: /event <start|stop>", NamedTextColor.YELLOW));
            return true;
        }
        if (args[0].equalsIgnoreCase("start")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Alleen spelers kunnen een event starten.", NamedTextColor.RED));
                return true;
            }
            startEvent(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("stop")) {
            if (eventTask == null) {
                sender.sendMessage(Component.text("Er draait momenteel geen event.", NamedTextColor.YELLOW));
                return true;
            }
            stopEvent();
            Bukkit.broadcast(Component.text("Het event is gestopt.", NamedTextColor.RED));
            return true;
        }
        sender.sendMessage(Component.text("Gebruik: /event <start|stop>", NamedTextColor.YELLOW));
        return true;
    }

    private void startEvent(Player player) {
        stopEvent();
        Location origin = player.getLocation().clone().add(0, 0.15, 0);
        World world = origin.getWorld();
        if (world == null) {
            return;
        }
        Bukkit.broadcast(Component.text(player.getName() + " heeft een event gestart!", NamedTextColor.GOLD));
        world.playSound(origin, Sound.BLOCK_BEACON_ACTIVATE, 1.3f, 1.1f);
        eventTask = Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            private int tick;

            @Override
            public void run() {
                if (tick == 0) {
                    burst(world, origin, 0.8, Particle.END_ROD, 36);
                    world.playSound(origin, Sound.ENTITY_PLAYER_LEVELUP, 1.2f, 0.8f);
                } else if (tick == 12) {
                    ring(world, origin, 1.8, Particle.FIREWORK, 48);
                    world.playSound(origin, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.3f, 1.2f);
                } else if (tick == 24) {
                    burst(world, origin.clone().add(0, 1.0, 0), 1.6, Particle.GLOW, 60);
                    world.playSound(origin, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.4f, 1.6f);
                } else if (tick == 36) {
                    ring(world, origin.clone().add(0, 1.5, 0), 2.8, Particle.DUST, 72);
                } else if (tick == 48) {
                    firework(world, origin.clone().add(0, 1.0, 0));
                } else if (tick == 60) {
                    burst(world, origin.clone().add(0, 2.0, 0), 2.3, Particle.TOTEM_OF_UNDYING, 90);
                    world.playSound(origin, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.5f, 1.0f);
                } else if (tick >= 72) {
                    world.spawnParticle(Particle.FIREWORK, origin.clone().add(0, 0.1, 0), 80, 1.8, 0.2, 1.8, 0.03);
                    world.playSound(origin, Sound.BLOCK_BEACON_POWER_SELECT, 0.8f, 1.4f);
                    Bukkit.broadcast(Component.text("Het event is afgelopen.", NamedTextColor.GREEN));
                    stopEvent();
                    return;
                }
                tick += 3;
            }
        }, 0L, 3L);
    }

    private void stopEvent() {
        if (eventTask != null) {
            eventTask.cancel();
            eventTask = null;
        }
    }

    private void burst(World world, Location center, double radius, Particle particle, int count) {
        world.spawnParticle(particle, center, count, radius, radius, radius, 0.03);
    }

    private void ring(World world, Location center, double radius, Particle particle, int points) {
        for (int i = 0; i < points; i++) {
            double angle = Math.PI * 2 * i / points;
            Location point = center.clone().add(Math.cos(angle) * radius, 0, Math.sin(angle) * radius);
            if (particle == Particle.DUST) {
                world.spawnParticle(particle, point, 2, new Particle.DustOptions(Color.fromRGB(255, 70, 190), 1.4f));
            } else {
                world.spawnParticle(particle, point, 2, 0, 0.1, 0, 0.02);
            }
        }
    }

    private void firework(World world, Location location) {
        Firework firework = world.spawn(location, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(1);
        meta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.FUCHSIA, Color.AQUA, Color.YELLOW)
                .withFade(Color.WHITE).flicker(true).trail(true).build());
        firework.setFireworkMeta(meta);
    }
}

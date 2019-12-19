package com.reliableplugins.oregenerator.listeners;

import com.reliableplugins.oregenerator.OreGenerator;
import com.reliableplugins.oregenerator.generator.Generator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GeneratorListeners implements Listener {

    private OreGenerator plugin;
    private final Set<Material> materials = new HashSet<>(Arrays.asList(Material.LAVA, Material.STATIONARY_LAVA, Material.WATER, Material.STATIONARY_WATER));

    public GeneratorListeners(OreGenerator plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGenerator(BlockFromToEvent event) {
        if (event.getToBlock().getType() == Material.AIR) {
            Block block = event.getToBlock();
            BlockFace blockFace = event.getFace();
            if (materials.contains(block.getRelative(blockFace).getType()) && materials.contains(block.getRelative(blockFace.getOppositeFace()).getType())) {
                // TODO: get generator based on player (below)
                Generator generator = plugin.getGenerators().get("default");
                block.setType(generator.generateRandomMaterial());
                block.getWorld().playSound(block.getLocation(), Sound.FIZZ, 1.0f, 2f);

                // make it so

                // block.breakNaturally(); // bug testing
                event.setCancelled(true);
            }
        }
    }

}
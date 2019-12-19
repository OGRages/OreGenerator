package com.reliableplugins.oregenerator.menu;

import com.reliableplugins.oregenerator.OreGenerator;
import com.reliableplugins.oregenerator.generator.Generator;
import com.reliableplugins.oregenerator.util.Message;
import com.reliableplugins.oregenerator.util.Util;
import com.reliableplugins.oregenerator.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProbabilityMenu extends MenuBuilder {

    private Material material;
    private Generator generator;
    private OreGenerator plugin;

    private List<String> lore = new ArrayList<>();

    private Map<Integer, Float> slotValue = new HashMap<>();
    private DecimalFormat format = new DecimalFormat("0.0");

    public ProbabilityMenu(String title, Generator generator, Material material, OreGenerator plugin) {
        super(title, 3, plugin);
        this.material = material;
        this.generator = generator;
        this.plugin = plugin;

        lore.add(ChatColor.GRAY + "Add an item to a generator by clicking");
        lore.add(ChatColor.GRAY + "a material from your inventory.");

        slotValue.put(10, 5.0f);
        slotValue.put(11, 1.0f);
        slotValue.put(12, 0.1f);

        slotValue.put(14, -0.1f);
        slotValue.put(15, -1.0f);
        slotValue.put(16, -5.0f);
    }

    @Override
    public ProbabilityMenu init() {

        ItemStack itemStack = Util.setName(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem(), " ");

        ItemStack add = XMaterial.LIME_STAINED_GLASS_PANE.parseItem();
        ItemStack remove = XMaterial.RED_STAINED_GLASS_PANE.parseItem();

        ItemStack item = new ItemStack(material);

        getInventory().setItem(10, Util.setName(add, "&7Add &a[+5.0%]"));
        getInventory().setItem(11, Util.setName(add, "&7Add &a[+1.0%]"));
        getInventory().setItem(12, Util.setName(add, "&7Add &a[+0.1%]"));

        getInventory().setItem(13, Util.setName(item, ChatColor.DARK_GREEN + CraftItemStack.asNMSCopy(item).getName()));

        getInventory().setItem(14, Util.setName(remove, "&7Remove &c[+0.1%]"));
        getInventory().setItem(15, Util.setName(remove, "&7Remove &c[+1.0%]"));
        getInventory().setItem(16, Util.setName(remove, "&7Remove &c[+5.0%]"));

        getInventory().setItem(22, Util.setName(new ItemStack(Material.BARRIER), ChatColor.RED + "Exit"));

        for (int i = 0; i < getInventory().getSize(); i++) {
            if (getInventory().getItem(i) == null) {
                getInventory().setItem(i, itemStack);
            }
        }

        return this;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {

        int chance = (int) (generator.getItems().get(material) * 10);

        Player player = (Player) event.getWhoClicked();

        if (!slotValue.containsKey(event.getSlot())) return;

        chance += slotValue.get(event.getSlot()) * 10;
        
        //TODO: add back 100 and 0 percent checks
//        if (getPercent() + (chance / 10f) < 0) {
//            player.sendMessage(Message.ERROR_ALREADY_0.getMessage());
//            return;
//        }
//
//        if (getPercent() +  (chance / 10f) > 100) {
//            player.sendMessage(Message.ERROR_ALREADY_100.getMessage());
//            return;
//        }

        generator.getItems().put(material, chance / 10f);

        // Save new probability into config
        plugin.getMaterialsConfig().save();

        // Update inventory
        getInventory().clear();
        player.openInventory(init().getInventory());

    }

    private int getPercent() {
        int percent = 0;
        for (Map.Entry<Material, Float> entry : generator.getItems().entrySet()) {
            percent += entry.getValue();
        }
        return percent;
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {

    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event) {

    }
}

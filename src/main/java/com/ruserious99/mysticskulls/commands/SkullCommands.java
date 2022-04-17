package com.ruserious99.mysticskulls.commands;

import com.ruserious99.mysticskulls.skulls.SkullsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SkullCommands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You need to op to use this command");
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /mask give <name> <mask>");
            sender.sendMessage(ChatColor.RED + "Usage: /mask give random <name>");
            return false;
        }
        final Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Invalid Player!");
            sender.sendMessage(ChatColor.RED + "Usage: /mask give <name> <mask>");
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(String.valueOf(player));

        if (args[0].equalsIgnoreCase("give")) {
            final String skullType = args[2].toUpperCase();
            switch (skullType) {
                case "TEST":
                    if(isInventoryFull(player, SkullsManager.test, skullType)) {
                        player.getInventory().addItem(SkullsManager.test);
                        player.sendMessage(skullType + " " + ChatColor.RED + "Skull given to " + player.getName() + "!");
                    }
                    break;

                default:
                    sender.sendMessage(ChatColor.RED + "Invalid Skull given!");
                    sender.sendMessage(ChatColor.RED + "Usage: /skull give <name> <mask>");
            }
        }
        return false;
    }

    private boolean  isInventoryFull(Player player, ItemStack item, String maskType){
        if(player.getInventory().firstEmpty() == -1){
            Location location = player.getLocation();
            World world = player.getWorld();
            world.dropItemNaturally(location, item);
            player.sendMessage(ChatColor.RED + "Inventory full  !!!!!!");
            player.sendMessage(maskType + " " + ChatColor.RED + "Skull dropped near " + player.getName() + "!");
            return false;
        }
        return true;
    }
}

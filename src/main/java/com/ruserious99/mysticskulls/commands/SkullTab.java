package com.ruserious99.mysticskulls.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkullTab implements TabCompleter {

    List<String> give = new ArrayList<>();
    List<String> arguments = new ArrayList<>();
    List<String> playerNames = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        List<String> result = new ArrayList<>();
        List<String> resultNames = new ArrayList<>();

        if (give.isEmpty()) {
            give.add("give");
        }

        if (arguments.isEmpty()) {
            arguments.add("TEST");

        }

        if (playerNames.isEmpty()) {
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player player : players) {
                playerNames.add(player.getName());
            }
        }

        if (args.length == 1) {
            return give;
        }

        if (args.length == 2) {
            for (String a : playerNames) {
                if (a.toUpperCase().startsWith(args[1].toUpperCase())) {
                    resultNames.add(a);
                }
            }
            return resultNames;
        }

        if (args.length == 3) {
            for (String a : arguments) {
                if (a.toUpperCase().startsWith(args[2].toUpperCase())) {
                    result.add(a);
                }
            }
            playerNames.clear();
            return result;
        }
        playerNames.clear();
        return null;
    }
}

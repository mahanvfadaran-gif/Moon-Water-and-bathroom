package ir.moon.mahan.waterandbathroom.commands;

import ir.moon.mahan.waterandbathroom.managers.BossBarManager;
import ir.moon.mahan.waterandbathroom.managers.MessagesManager;
import ir.moon.mahan.waterandbathroom.managers.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BossBarCommand implements CommandExecutor, TabCompleter {
    private static final List<String> SUBCOMMANDS = Arrays.asList("show", "hide", "toggle");
    private final MessagesManager messagesManager;
    private final BossBarManager bossBarManager;

    public BossBarCommand(MessagesManager messagesManager, BossBarManager bossBarManager) {
        this.messagesManager = messagesManager;
        this.bossBarManager = bossBarManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            messagesManager.sendMessage(sender, "general.player-only");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(PermissionManager.BOSSBAR)) {
            messagesManager.sendMessage(player, "general.no-permission");
            return true;
        }
        String subCommand = args.length > 0 ? args[0].toLowerCase() : "toggle";
        switch (subCommand) {
            case "hide":
                bossBarManager.setHidden(player, true);
                messagesManager.sendMessage(player, "bossbar.hidden");
                break;
            case "show":
                bossBarManager.setHidden(player, false);
                messagesManager.sendMessage(player, "bossbar.shown");
                break;
            case "toggle":
                boolean nowHidden = bossBarManager.toggleHidden(player);
                messagesManager.sendMessage(player, nowHidden ? "bossbar.hidden" : "bossbar.shown");
                break;
            default:
                messagesManager.sendMessage(player, "bossbar.usage");
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> matches = new ArrayList<>();
            for (String subCommand : SUBCOMMANDS) {
                if (subCommand.startsWith(args[0].toLowerCase())) matches.add(subCommand);
            }
            return matches;
        }
        return Collections.emptyList();
    }
}
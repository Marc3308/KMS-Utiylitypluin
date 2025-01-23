package me.marc3308.monsterplugin.cosmetiks.subcommands;

import me.marc3308.monsterplugin.cosmetiks.subcommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class help extends subcommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "sagt dir sachen";
    }

    @Override
    public String getSyntax() {
        return "/cosmetics help";
    }

    @Override
    public void perform(Player p, String[] args) {

        p.sendMessage(ChatColor.DARK_GREEN+"----------------------------------------------------");
        p.sendMessage("/cosmetics add <Name> <Nummer> <Block> <Körperteil>");
        p.sendMessage("/cosmetics remove <Name>");
        p.sendMessage("/cosmetics edit <Name> <art> <neuerwert>");
        p.sendMessage("/cosmetics info <Name>");
        p.sendMessage("/cosmetics give <Name>");
        p.sendMessage("/cosmetics unlock <Name> <Spieler>");
        p.sendMessage("/cosmetics load");
        p.sendMessage(ChatColor.DARK_GREEN+"----------------------------------------------------");

    }
}

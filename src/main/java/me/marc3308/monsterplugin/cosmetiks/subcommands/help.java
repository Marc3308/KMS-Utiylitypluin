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
        return "/cosemtiks help";
    }

    @Override
    public void perform(Player p, String[] args) {

        p.sendMessage(ChatColor.DARK_GREEN+"----------------------------------------------------");
        p.sendMessage("/cosemtiks add <Name> <Nummer> <Block> <Körperteil>");
        p.sendMessage("/cosemtiks remove <Name>");
        p.sendMessage("/cosemtiks edit <Name> <art> <neuerwert>");
        p.sendMessage("/cosemtiks info <Name>");
        p.sendMessage("/cosemtiks give <Name>");
        p.sendMessage("/cosemtiks unlock <Name> <Spieler>");
        p.sendMessage("/cosemtiks load");
        p.sendMessage(ChatColor.DARK_GREEN+"----------------------------------------------------");

    }
}

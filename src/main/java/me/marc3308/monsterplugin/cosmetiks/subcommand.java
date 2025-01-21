package me.marc3308.monsterplugin.cosmetiks;

import org.bukkit.entity.Player;

public abstract class subcommand {
    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void perform(Player p, String args[]);
}

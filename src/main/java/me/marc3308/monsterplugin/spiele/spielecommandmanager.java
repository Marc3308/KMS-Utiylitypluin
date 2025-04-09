package me.marc3308.monsterplugin.spiele;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.marc3308.monsterplugin.spiele.spielguicontroller.openSpieleMenu;

public class spielecommandmanager implements CommandExecutor {

    private String error = "§2/spiele §e<game> <command> §4<args>..";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return false;
        Player p= (Player) sender;
        openSpieleMenu(p,1,"Schach");
        return false;
    }
}

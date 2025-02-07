package me.marc3308.monsterplugin.spiele.chess.objekts;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;


import static org.bukkit.Particle.END_ROD;

public class Chessbord {

    private chessgame game;
    private String name;
    private Location feltstart;
    private Location feltend;
    private boolean help;
    private int time;

    public Chessbord(Location loc, String name, boolean help, int time) {
        this.time =time;
        this.help=help;
        this.name = name;
        this.feltstart =loc.getBlock().getLocation();
        this.feltend =loc.getBlock().getLocation().add(8,0,8);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(!Bukkit.getOnlinePlayers().isEmpty())loc.getWorld().spawnParticle(END_ROD,loc.clone().add(i,1,j),5,0,0,0,0.01);
            }
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public boolean isHelp() {
        return help;
    }

    public void setGame(Player p) {
        if(p==null && game!=null && game.isGamehasstarted()){
            game.getBlackstand().remove();
            game.getWhitstand().remove();
            game.setGamehasstarted(false);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(game.getBord()[i][j]!=null)game.getBord()[i][j].getArmorStand().remove();
                }
            }
            game.getPromolist().forEach(s -> s.getArmorStand().remove());
        }
        this.game = (p==null ? null : new chessgame(this,p));
    }

    public chessgame getGame() {
        return game;
    }

    public boolean hasGame() {
        return game != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFeltend(Location feltend) {
        this.feltend = feltend;
    }

    public void setFeltstart(Location feltstart) {
        this.feltstart = feltstart;
    }

    public Location getFeltend() {
        return feltend.clone();
    }

    public Location getFeltstart() {
        return feltstart.clone();
    }

}

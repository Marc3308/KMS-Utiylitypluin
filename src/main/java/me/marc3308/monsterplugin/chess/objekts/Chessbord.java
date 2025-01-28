package me.marc3308.monsterplugin.chess.objekts;

import org.bukkit.Location;
import org.bukkit.entity.Player;


import static org.bukkit.Particle.END_ROD;

public class Chessbord {

    private chessgame game;
    private String name;
    private Location feltstart;
    private Location feltend;

    public Chessbord(Location loc, String name){
        this.name = name;
        this.feltstart =loc.getBlock().getLocation();
        this.feltend =loc.getBlock().getLocation().add(8,0,8);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                loc.getWorld().spawnParticle(END_ROD,loc.clone().add(i,1,j),5,0,0,0,0.01);
            }
        }
    }

    public void setGame(Player p) {
        if(p==null && game!=null && game.isGamehasstarted()){
            game.setGamehasstarted(false);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(game.getBord()[i][j]!=null)game.getBord()[i][j].getArmorStand().remove();
                }
            }
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

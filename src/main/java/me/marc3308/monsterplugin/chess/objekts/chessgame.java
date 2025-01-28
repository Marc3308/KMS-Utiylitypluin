package me.marc3308.monsterplugin.chess.objekts;

import me.marc3308.monsterplugin.Monsterplugin;
import me.marc3308.monsterplugin.chess.utilitys;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class chessgame {
    private Chessbord chessbord;
    private Player spieler1;
    private Player spieler2;
    private boolean gamehasstarted;
    private Figuren[][] bord = new Figuren[8][8];
    private String turn;
    private boolean white_kingside;
    private boolean black_kingside;
    private boolean black_queenside;
    private boolean white_queenside;

    public boolean isBlack_kingside() {
        return black_kingside;
    }

    public boolean isBlack_queenside() {
        return black_queenside;
    }

    public boolean isWhite_kingside() {
        return white_kingside;
    }

    public boolean isWhite_queenside() {
        return white_queenside;
    }

    public void setWhite_queenside(boolean white_queenside) {
        this.white_queenside = white_queenside;
    }

    public void setWhite_kingside(boolean white_kingside) {
        this.white_kingside = white_kingside;
    }

    public void setBlack_queenside(boolean black_queenside) {
        this.black_queenside = black_queenside;
    }

    public void setBlack_kingside(boolean black_kingside) {
        this.black_kingside = black_kingside;
    }

    public chessgame(Chessbord chessbord, Player spieler1) {
        this.spieler1 = spieler1;
        this.chessbord = chessbord;
    }

    public void setGamehasstarted(boolean gamehasstarted) {
        this.gamehasstarted = gamehasstarted;
    }

    public void setBord(Figuren[][] bord) {
        this.bord = bord;
    }

    public Figuren[][] getBord() {
        return bord;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getTurn() {
        return turn;
    }

    public Player getSpieler1() {
        return spieler1;
    }

    public Player getSpieler2() {
        return spieler2;
    }

    public void setSpieler2(Player spieler2) {
        this.spieler2 = spieler2;
    }

    public void setSpieler1(Player spieler1) {
        this.spieler1 = spieler1;
    }

    public boolean hasSpieler2() {
        return spieler2 != null;
    }

    public void startgame(Player p, Player p2) {
        white_kingside = true;
        white_queenside = true;
        black_kingside = true;
        black_queenside = true;
        gamehasstarted = true;
        p.setSneaking(false);
        p2.setSneaking(false);
        spieler1=p;
        spieler2=p2;
        final int[] i = {0};
        new BukkitRunnable(){
            @Override
            public void run() {
                if(i[0] >=30 || !gamehasstarted){
                    if(gamehasstarted)gameend("none");
                    chessbord.setGame(null);
                    cancel();
                    return;
                }
                Chessbord s = chessbord;
                if(!spieler1.isOnline() || !spieler2.isOnline()
                        || !((s.getFeltend().getX()<s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())-2<=p.getX()
                        && (s.getFeltend().getX()>s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())+2>=p.getX()
                        && (s.getFeltend().getZ()<s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())-2<=p.getZ()
                        && (s.getFeltend().getZ()>s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())+2>=p.getZ()))i[0]++;
                else i[0]=0;

                if(i[0]!=0){
                    ArmorStand ar=p.getWorld().spawn(s.getFeltstart().add(4,0,4),ArmorStand.class);
                    ar.setInvulnerable(true);
                    ar.setCustomName((i[0]<=10 ? ChatColor.GREEN : i[0]<=20 ? ChatColor.YELLOW : ChatColor.RED)+""+(30-i[0]));
                    ar.setCustomNameVisible(true);
                    ar.setGravity(false);
                    ar.setVisible(false);
                    ar.setSmall(true);
                    Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> ar.remove(),20);
                }
            }
        }.runTaskTimer(Monsterplugin.getPlugin(),0,20);

        if(p.equals(p2)){
            setTurn("white");
            spawnbordside("white");
            spawnbordside("black");
        } else {
            setTurn("farbauswal");
            bord[0][0] = new Bauer(chessbord.getFeltstart().clone().add(3.5,0,3), Material.PAPER, 10001,true);
            bord[0][1] = new Bauer(chessbord.getFeltstart().clone().add(3.5,0,4), Material.PAPER, 10011,false);
        }
    }

    public void gameend(String winner){
        gamehasstarted=false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(bord[i][j]==null)continue;
                utilitys.movefigure(bord[i][j].getArmorStand(),bord[i][j].getArmorStand().getLocation().clone().add(0,-3,0));
                int finalI = i;
                int finalJ = j;
                Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> bord[finalI][finalJ].getArmorStand().remove(),20*4);

                if(!winner.equals("none")){
                    Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () ->{
                        // Create the firework entity at the specified location
                        Firework firework = (Firework) chessbord.getFeltstart().getWorld().spawnEntity(
                                 chessbord.getFeltstart().add(winner.equals("white") ? 0 : 7 - finalI,0,finalJ), EntityType.FIREWORK_ROCKET);

                        // Get the firework meta data
                        FireworkMeta fireworkMeta = firework.getFireworkMeta();

                        // Create a firework effect
                        FireworkEffect effect = FireworkEffect.builder()
                                .withColor(winner.equals("white") ? Color.WHITE : Color.BLACK)
                                .withFade(winner.equals("white") ? Color.WHITE : Color.BLACK)
                                .with(FireworkEffect.Type.STAR)
                                .trail(true)
                                .flicker(true)
                                .build();

                        // Add the effect to the firework meta
                        fireworkMeta.addEffect(effect);

                        // Set the power of the firework (how high it will fly)
                        fireworkMeta.setPower(finalI);

                        // Apply the meta to the firework
                        firework.setFireworkMeta(fireworkMeta);
                    },i);
                }
            }
        }
    }

    public void spawnbordside(String side){
        if(side.equals("white")){
            int modeldata=spieler1.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(),"chessskin"))
                    ? spieler1.getPersistentDataContainer().get(new NamespacedKey(Monsterplugin.getPlugin(),"chessskin"), PersistentDataType.INTEGER)
                    : 10000;

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 8; j++) {
                    if(i==1)bord[i][j] = new Bauer(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+1,true);
                    if(i==0)bord[i][j] =
                             j==0 || j==7 ? new Turm(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+2,true)
                            : j==1 || j==6 ? new Springer(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+3,true)
                            : j==2 || j==5 ? new Laufer(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+4,true)
                            : j==3 ? new Koenigin(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+5,true)
                            : new Koenig(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+6,true); //white
                }
            }
        } else {
            int modeldata=spieler2.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(),"chessskin"))
                    ? spieler2.getPersistentDataContainer().get(new NamespacedKey(Monsterplugin.getPlugin(),"chessskin"), PersistentDataType.INTEGER)
                    : 10000;

            for (int i = 7; i > 5; i--) {
                for (int j = 0; j < 8; j++) {
                    if(i==6)bord[i][j] = new Bauer(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+11,false);
                    if(i==7)bord[i][j] =
                            j==0 || j==7 ? new Turm(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+12,false)
                                    : j==1 || j==6 ? new Springer(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+13,false)
                                    : j==2 || j==5 ? new Laufer(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+14,false)
                                    : j==3 ? new Koenigin(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+15,false)
                                    : new Koenig(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+16,false); //black
                }
            }
        }
    }

    public boolean isGamehasstarted() {
        return gamehasstarted;
    }
}

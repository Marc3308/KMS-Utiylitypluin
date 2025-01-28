package me.marc3308.monsterplugin.chess;

import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static me.marc3308.monsterplugin.chess.utilitys.movefigure;
import static me.marc3308.monsterplugin.chess.utilitys.schachliste;

public class chessevents implements Listener {

    @EventHandler
    public void onsneak(PlayerToggleSneakEvent e){
        Player p = e.getPlayer();
        if(p.isSneaking())return;

        schachliste.stream().filter(s ->
                (s.getFeltend().getX()<s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())<=p.getX() && (s.getFeltend().getX()>s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())>=p.getX()
                    && (s.getFeltend().getZ()<s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())<=p.getZ() && (s.getFeltend().getZ()>s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())>=p.getZ()
                        && ((s.hasGame() && !s.getGame().hasSpieler2() && !s.getGame().isGamehasstarted()) || !s.hasGame())
        ).findFirst().ifPresent(s -> {

            if(!s.hasGame()){
                s.setGame(p);
            } else {
                s.getGame().setSpieler2(p);
            }

            double y1=p.getLocation().getBlockY();
            double x1=p.getLocation().getBlockX();
            double z1=p.getLocation().getBlockZ();
            double yaww1=p.getLocation().getYaw();
            boolean p2 =s.getGame().hasSpieler2();

            double radius=0.5;
            final double[] y={0};
            new BukkitRunnable() {
                @Override
                public void run() {

                    if(s.getGame().isGamehasstarted()){
                        cancel();
                        return;
                    }
                    if(!p.isOnline() || !p.isSneaking()){
                        if(s.getGame().getSpieler1().equals(p)){
                            if(s.getGame().hasSpieler2()){
                                s.getGame().setSpieler1(s.getGame().getSpieler2());
                                s.getGame().setSpieler2(null);
                            } else {
                                s.setGame(null);
                            }
                        } else {
                            s.getGame().setSpieler2(null);
                        }
                        cancel();
                        return;
                    }

                    double y2=p.getLocation().getBlockY();
                    double x2=p.getLocation().getBlockX();
                    double z2=p.getLocation().getBlockZ();
                    double yaww2=p.getLocation().getYaw();

                    double x=radius * Math.cos(y[0]);
                    double z=radius * Math.sin(y[0]);

                    //kleiner ring
                    Particle.DustOptions dustOptions = new Particle.DustOptions((s.getGame().getSpieler1().equals(p) ? Color.WHITE : Color.BLACK), 1.0f); // Color and size
                    p.getWorld().spawnParticle(Particle.DUST, p.getLocation().add(x,0,z), 1, 0.5, 0.5, 0.5, dustOptions);
                    p.getWorld().spawnParticle(Particle.DUST, p.getLocation().add(-x,0,-z), 1, 0.5, 0.5, 0.5, dustOptions);
                    if(y[0]%1.0<=0.1)p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, (float) (y[0]), (float) (y[0]));

                    y[0]+=0.1;
                    //start the game
                    if(!s.getGame().hasSpieler2() && y[0]>=10
                            || s.getGame().hasSpieler2() && s.getGame().getSpieler2().equals(p) && y[0]>=5) {
                        s.getGame().startgame(s.getGame().getSpieler1(),p);
                        cancel();
                        return;
                    }
                }
            }.runTaskTimer(Monsterplugin.getPlugin(),20L,2L);
        });

    }

    @EventHandler
    public void onzug(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(!e.getAction().isLeftClick())return;
        if(e.getClickedBlock()==null)return;
        schachliste.stream().filter(s ->
                        (s.getFeltend().getX()<s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())<=p.getX() && (s.getFeltend().getX()>s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())>=p.getX()
                                && s.getFeltstart().getBlockY()==e.getClickedBlock().getY()+1
                                && (s.getFeltend().getZ()<s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())<=p.getZ() && (s.getFeltend().getZ()>s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())>=p.getZ()
                                && s.hasGame() && s.getGame().isGamehasstarted() && (s.getGame().getSpieler1().equals(p) || s.getGame().getSpieler2().equals(p))
                ).findFirst().ifPresent(s -> {
            switch (s.getGame().getTurn().split(":")[0]){
                case "white":
                    if(s.getGame().getSpieler1().equals(p)){
                        int x = e.getClickedBlock().getX()-s.getFeltstart().getBlockX();
                        int z = e.getClickedBlock().getZ()-s.getFeltstart().getBlockZ();
                        if(x<0 || x>7 || z<0 || z>7)return;
                        if(s.getGame().getTurn().equals("netfinished"))return;
                        if(s.getGame().getTurn().split(":").length>1
                                && (s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])]==null
                                || s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].isWhite())
                                && utilitys.darferdas(s,Integer.valueOf(s.getGame().getTurn().split(":")[1]),Integer.valueOf(s.getGame().getTurn().split(":")[2]),x,z)){

                            movefigure(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand(),s.getFeltstart().clone().add(x,0,z).clone().add(0.5,-1.9,0.5));
                            s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand().removePotionEffect(PotionEffectType.GLOWING);
                            if(s.getGame().getBord()[x][z]!=null && s.getGame().getBord()[x][z].isWhite()!=s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].isWhite()){
                                ArmorStand ar1=s.getGame().getBord()[x][z].getArmorStand();
                                ArmorStand ar2=s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand();
                                Boolean iswhite=s.getGame().getBord()[x][z].isWhite();

                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        if(ar1.getLocation().distance(
                                                ar2.getLocation())<=0.3){
                                            p.getWorld().playSound(ar1, Sound.ENTITY_ARMOR_STAND_BREAK,3,4);
                                            // Create the firework entity at the specified location
                                            Firework firework = (Firework) ar1.getWorld().spawnEntity(
                                                    ar1.getLocation(), EntityType.FIREWORK_ROCKET);

                                            // Get the firework meta data
                                            FireworkMeta fireworkMeta = firework.getFireworkMeta();

                                            // Create a firework effect
                                            FireworkEffect effect = FireworkEffect.builder()
                                                    .withColor(iswhite ? Color.WHITE : Color.BLACK)
                                                    .withFade(iswhite ? Color.WHITE : Color.BLACK)
                                                    .with(FireworkEffect.Type.STAR)
                                                    .trail(true)
                                                    .flicker(true)
                                                    .build();

                                            // Add the effect to the firework meta
                                            fireworkMeta.addEffect(effect);

                                            // Set the power of the firework (how high it will fly)
                                            fireworkMeta.setPower(0);

                                            // Apply the meta to the firework
                                            firework.setFireworkMeta(fireworkMeta);
                                            firework.detonate();

                                            if(ar1.getName().substring(2,ar1.getName().length()).equals("König"))s.getGame().gameend(iswhite ? "black" : "white");
                                            ar1.remove();
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(Monsterplugin.getPlugin(),0,10);
                            }
                            if(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Koenig")
                                    || s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Turm")){
                                if(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Koenig")){
                                    if(Integer.valueOf(s.getGame().getTurn().split(":")[2])+2==z){
                                        movefigure(s.getGame().getBord()[0][7].getArmorStand(),s.getFeltstart().clone().add(0,0,5).clone().add(0.5,-1.9,0.5));
                                        s.getGame().getBord()[0][5]=s.getGame().getBord()[0][7];
                                        s.getGame().getBord()[0][7]=null;
                                    } else if(Integer.valueOf(s.getGame().getTurn().split(":")[2])-2==z){
                                        movefigure(s.getGame().getBord()[0][0].getArmorStand(),s.getFeltstart().clone().add(0,0,3).clone().add(0.5,-1.9,0.5));
                                        s.getGame().getBord()[0][3]=s.getGame().getBord()[0][0];
                                        s.getGame().getBord()[0][0]=null;
                                    }
                                    s.getGame().setWhite_kingside(false);
                                    s.getGame().setWhite_queenside(false);
                                } else {
                                    if(Integer.valueOf(s.getGame().getTurn().split(":")[1])==0 && Integer.valueOf(s.getGame().getTurn().split(":")[2])==0){
                                        s.getGame().setWhite_queenside(false);
                                    } else if(Integer.valueOf(s.getGame().getTurn().split(":")[1])==0 && Integer.valueOf(s.getGame().getTurn().split(":")[2])==7){
                                        s.getGame().setWhite_kingside(false);
                                    }
                                }
                            }
                            double delay = s.getFeltstart().add(x,0,z).distance(
                                    s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand().getLocation());
                            s.getGame().getBord()[x][z]=s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])];
                            s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])]=null;
                            s.getGame().setTurn("netfinished");
                            Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () ->{
                                if(s.hasGame())s.getGame().setTurn("black");
                            }, (long) (delay*25));

                        } else {
                            if(s.getGame().getBord()[x][z]!=null && s.getGame().getBord()[x][z].isWhite()){
                                s.getGame().getBord()[x][z].getArmorStand().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1));
                                if(s.getGame().getTurn().split(":").length>1){
                                    s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand().removePotionEffect(PotionEffectType.GLOWING);
                                }
                                s.getGame().setTurn(s.getGame().getTurn().split(":").length>1 && Integer.valueOf(s.getGame().getTurn().split(":")[1]).equals(x) && Integer.valueOf(s.getGame().getTurn().split(":")[2]).equals(z)
                                        ? "white" : "white:"+x+":"+z);

                            }
                        }
                    }
                    break;
                case "black":
                    if(s.getGame().getSpieler2().equals(p)){
                        int x = e.getClickedBlock().getX()-s.getFeltstart().getBlockX();
                        int z = e.getClickedBlock().getZ()-s.getFeltstart().getBlockZ();
                        if(x<0 || x>7 || z<0 || z>7)return;
                        if(s.getGame().getTurn().split(":").length>1
                                && (s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])]==null
                                || !s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].isWhite())
                                && utilitys.darferdas(s,Integer.valueOf(s.getGame().getTurn().split(":")[1]),Integer.valueOf(s.getGame().getTurn().split(":")[2]),x,z)){
                            movefigure(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand(),s.getFeltstart().clone().add(x,0,z).clone().add(0.5,-1.9,0.5));
                            s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand().removePotionEffect(PotionEffectType.GLOWING);
                            if(s.getGame().getBord()[x][z]!=null && s.getGame().getBord()[x][z].isWhite()!=s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].isWhite()){
                                ArmorStand ar1=s.getGame().getBord()[x][z].getArmorStand();
                                ArmorStand ar2=s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand();
                                Boolean iswhite=s.getGame().getBord()[x][z].isWhite();
                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        if(ar1.getLocation().distance(
                                                ar2.getLocation())<=0.3){
                                            p.getWorld().playSound(ar1, Sound.ENTITY_ARMOR_STAND_BREAK,3,4);
                                            // Create the firework entity at the specified location
                                            Firework firework = (Firework) ar1.getWorld().spawnEntity(
                                                    ar1.getLocation(), EntityType.FIREWORK_ROCKET);

                                            // Get the firework meta data
                                            FireworkMeta fireworkMeta = firework.getFireworkMeta();

                                            // Create a firework effect
                                            FireworkEffect effect = FireworkEffect.builder()
                                                    .withColor(iswhite ? Color.WHITE : Color.BLACK)
                                                    .withFade(iswhite ? Color.WHITE : Color.BLACK)
                                                    .with(FireworkEffect.Type.BALL)
                                                    .trail(true)
                                                    .flicker(true)
                                                    .build();

                                            // Add the effect to the firework meta
                                            fireworkMeta.addEffect(effect);

                                            // Set the power of the firework (how high it will fly)
                                            fireworkMeta.setPower(0);

                                            // Apply the meta to the firework
                                            firework.setFireworkMeta(fireworkMeta);
                                            firework.detonate();

                                            if(ar1.getName().substring(2,ar1.getName().length()).equals("König"))s.getGame().gameend(iswhite ? "black" : "white");
                                            ar1.remove();
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(Monsterplugin.getPlugin(),0,10);
                            }
                            if(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Koenig")
                                    ||s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Turm")){
                                if(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Koenig")){
                                    if(Integer.valueOf(s.getGame().getTurn().split(":")[2])+2==z){
                                        movefigure(s.getGame().getBord()[7][7].getArmorStand(),s.getFeltstart().clone().add(7,0,5).clone().add(0.5,-1.9,0.5));
                                        s.getGame().getBord()[7][5]=s.getGame().getBord()[0][7];
                                        s.getGame().getBord()[7][7]=null;
                                    } else if(Integer.valueOf(s.getGame().getTurn().split(":")[2])-2==z){
                                        movefigure(s.getGame().getBord()[7][0].getArmorStand(),s.getFeltstart().clone().add(7,0,3).clone().add(0.5,-1.9,0.5));
                                        s.getGame().getBord()[7][3]=s.getGame().getBord()[7][0];
                                        s.getGame().getBord()[7][0]=null;
                                    }
                                    s.getGame().setBlack_kingside(false);
                                    s.getGame().setBlack_queenside(false);
                                } else {
                                    if(Integer.valueOf(s.getGame().getTurn().split(":")[1])==7 && Integer.valueOf(s.getGame().getTurn().split(":")[2])==7){
                                        s.getGame().setBlack_kingside(false);
                                    } else if(Integer.valueOf(s.getGame().getTurn().split(":")[1])==7 && Integer.valueOf(s.getGame().getTurn().split(":")[2])==0){
                                        s.getGame().setBlack_queenside(false);
                                    }
                                }
                            }

                            double delay = s.getFeltstart().add(x,0,z).distance(
                                    s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand().getLocation());
                            s.getGame().getBord()[x][z]=s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])];
                            s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])]=null;
                            s.getGame().setTurn("netfinished");
                            Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () ->{
                                if(s.hasGame())s.getGame().setTurn("white");
                            }, (long) (delay*25));
                        } else {
                            if(s.getGame().getBord()[x][z]!=null && !s.getGame().getBord()[x][z].isWhite()){
                                s.getGame().getBord()[x][z].getArmorStand().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1));
                                if(s.getGame().getTurn().split(":").length>1){
                                    s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand().removePotionEffect(PotionEffectType.GLOWING);
                                }
                                s.getGame().setTurn(s.getGame().getTurn().split(":").length>1 && Integer.valueOf(s.getGame().getTurn().split(":")[1]).equals(x) && Integer.valueOf(s.getGame().getTurn().split(":")[2]).equals(z)
                                        ? "black" : "black:"+x+":"+z);

                            }
                        }
                    }
                    break;
                case "farbauswal":
                    int x = e.getClickedBlock().getX()-s.getFeltstart().getBlockX();
                    int z = e.getClickedBlock().getZ()-s.getFeltstart().getBlockZ();
                    if(x==3 || x==4 && z==3 || z==4){
                        Player p1 = s.getGame().getSpieler1();
                        Player p2 = s.getGame().getSpieler2();
                        s.getGame().setSpieler1(z==3 ? p : p1.equals(p) ? p2 : p1);
                        s.getGame().setSpieler2(z==4 ? p : p1.equals(p) ? p2 : p1);
                        s.getGame().setTurn("white");
                        ArmorStand ar1 = s.getGame().getBord()[0][0].getArmorStand();
                        ArmorStand ar2 = s.getGame().getBord()[0][1].getArmorStand();

                        utilitys.movefigure(ar1,ar1.getLocation().clone().add(0,-3,0));
                        utilitys.movefigure(ar2,ar2.getLocation().clone().add(0,-3,0));
                        Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> {
                            ar1.remove();
                            ar2.remove();
                        },20*4);

                        s.getGame().spawnbordside("white");
                        s.getGame().spawnbordside("black");
                    }
                    break;
            }
        });
    }
}

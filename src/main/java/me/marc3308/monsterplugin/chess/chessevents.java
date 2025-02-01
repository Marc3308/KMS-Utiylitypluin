package me.marc3308.monsterplugin.chess;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static me.marc3308.monsterplugin.chess.utilitys.*;

public class chessevents implements Listener {

    @EventHandler
    public void onsneak(PlayerToggleSneakEvent e){
        Player p = e.getPlayer();
        //watch game
        schachliste.stream().filter(s -> (s.hasGame() && s.getGame().isGamehasstarted()
                && (s.getFeltend().getX()<s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())-5<=p.getX()
                && (s.getFeltend().getX()>s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())+5>=p.getX()
                && (s.getFeltend().getY()<s.getFeltstart().getY() ? s.getFeltend().getY() : s.getFeltstart().getY())+7>=p.getY()
                && (s.getFeltend().getY()<s.getFeltstart().getY() ? s.getFeltend().getY() : s.getFeltstart().getY())-1<=p.getY()
                && (s.getFeltend().getZ()<s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())-5<=p.getZ()
                && (s.getFeltend().getZ()>s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())+5>=p.getZ())).findFirst().ifPresent(s ->{
            if (!p.isSneaking()) {
                if(s.getGame().getZuschauerlist().contains(p)){
                    if (p.equals(s.getGame().getSpieler1()) || p.equals(s.getGame().getSpieler2())) {
                        Bukkit.getOnlinePlayers().forEach(p1 -> p.showPlayer(Monsterplugin.getPlugin(), p1));
                    }
                    // Packet
                    PacketContainer cameraPacket2 = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
                    cameraPacket2.getIntegers().write(0, p.getEntityId());
                    ProtocolLibrary.getProtocolManager().sendServerPacket(p, cameraPacket2);
                    s.getGame().getZuschauerlist().remove(p);
                } else {
                    s.getGame().getZuschauerlist().add(p);
                }
            }
        });

        if(p.isSneaking())return;

        //find chessgame
        schachliste.stream().filter(s ->
                (s.getFeltend().getX()<s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())<=p.getX() && (s.getFeltend().getX()>s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())>=p.getX()
                    && (s.getFeltend().getZ()<s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())<=p.getZ() && (s.getFeltend().getZ()>s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())>=p.getZ()
                        && (!s.hasGame() || (s.hasGame() && !s.getGame().isGamehasstarted() && !s.getGame().hasSpieler2()))
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
                    if(!s.getGame().hasSpieler2() && y[0]>=5
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
                        (s.getFeltend().getX()<s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())-1<=p.getX() && (s.getFeltend().getX()>s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())+1>=p.getX()
                                && s.getFeltstart().getBlockY()==e.getClickedBlock().getY()+1
                                && (s.getFeltend().getZ()<s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())-1<=p.getZ() && (s.getFeltend().getZ()>s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())+1>=p.getZ()
                                && s.hasGame() && s.getGame().isGamehasstarted() && (s.getGame().getSpieler1().equals(p) || s.getGame().getSpieler2().equals(p))
                ).findFirst().ifPresent(s -> makeamove(s,p,e.getClickedBlock().getLocation()));
    }

    @EventHandler
    public void onchat(PlayerChatEvent e){

        Player p=e.getPlayer();
        if(e.getMessage().startsWith("/") || !(e.getMessage().length()!=2 || e.getMessage().length()!=5))return;
        schachliste.stream().filter(s -> s.hasGame() && (s.getGame().getSpieler1().equals(p) || (s.getGame().hasSpieler2() && s.getGame().getSpieler2().equals(p)))).findFirst().ifPresent(s -> {
            if(e.getMessage().equals("gg")){
                s.getGame().gameend(s.getGame().getSpieler1().equals(p) ? "white" : "black");
                return;
            }
            if(Character.isAlphabetic(e.getMessage().charAt(0)) && Character.isDigit(e.getMessage().charAt(1))){
                Location loc=s.getFeltstart().add(Integer.valueOf(e.getMessage().substring(1,2))-1,-1,
                        e.getMessage().substring(0,1).equalsIgnoreCase("a")
                        ? 0 : e.getMessage().substring(0,1).equalsIgnoreCase("b")
                        ? 1 : e.getMessage().substring(0,1).equalsIgnoreCase("c")
                        ? 2 : e.getMessage().substring(0,1).equalsIgnoreCase("d")
                        ? 3 : e.getMessage().substring(0,1).equalsIgnoreCase("e")
                        ? 4 : e.getMessage().substring(0,1).equalsIgnoreCase("f")
                        ? 5 : e.getMessage().substring(0,1).equalsIgnoreCase("g") ? 6 : 7);
                makeamove(s,p,loc);
            }
            if(e.getMessage().length()==5 && Character.isAlphabetic(e.getMessage().charAt(3)) && Character.isDigit(e.getMessage().charAt(4))){
                Location loc=s.getFeltstart().add(Integer.valueOf(e.getMessage().substring(4,5))-1,-1,
                        e.getMessage().substring(3,4).equalsIgnoreCase("a")
                                ? 0 : e.getMessage().substring(0,1).equalsIgnoreCase("b")
                                ? 1 : e.getMessage().substring(0,1).equalsIgnoreCase("c")
                                ? 2 : e.getMessage().substring(0,1).equalsIgnoreCase("d")
                                ? 3 : e.getMessage().substring(0,1).equalsIgnoreCase("e")
                                ? 4 : e.getMessage().substring(0,1).equalsIgnoreCase("f")
                                ? 5 : e.getMessage().substring(0,1).equalsIgnoreCase("g") ? 6 : 7);
                Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> makeamove(s,p,loc), 10);
            }
            e.setMessage("");
        });
    }

    @EventHandler
    public void oncommand(PlayerCommandPreprocessEvent e){
        Player p=e.getPlayer();
        if(!e.getMessage().startsWith("/cpe"))return;
        e.setCancelled(true);
        if(e.getMessage().split(" ")[1].length()!=2)return;
        schachliste.stream().filter(s -> s.hasGame() && (s.getGame().getSpieler1().equals(p) || s.getGame().getSpieler2().equals(p))).findFirst().ifPresent(s -> {
            if(Character.isAlphabetic(e.getMessage().split(" ")[1].charAt(0)) && Character.isDigit(e.getMessage().split(" ")[1].charAt(1))){
                Location loc=s.getFeltstart().add(Integer.valueOf(e.getMessage().split(" ")[1].substring(1,2))-1,-1,
                        e.getMessage().split(" ")[1].substring(0,1).equalsIgnoreCase("a")
                                ? 0 : e.getMessage().split(" ")[1].substring(0,1).equalsIgnoreCase("b")
                                ? 1 : e.getMessage().split(" ")[1].substring(0,1).equalsIgnoreCase("c")
                                ? 2 : e.getMessage().split(" ")[1].substring(0,1).equalsIgnoreCase("d")
                                ? 3 : e.getMessage().split(" ")[1].substring(0,1).equalsIgnoreCase("e")
                                ? 4 : e.getMessage().split(" ")[1].substring(0,1).equalsIgnoreCase("f")
                                ? 5 : e.getMessage().split(" ")[1].substring(0,1).equalsIgnoreCase("g") ? 6 : 7);
                makeamove(s,p,loc);
            }

        });
    }

}

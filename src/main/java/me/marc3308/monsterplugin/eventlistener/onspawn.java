package me.marc3308.monsterplugin.eventlistener;

import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static me.marc3308.monsterplugin.Monsterplugin.eiss;

public class onspawn implements Listener {

    @EventHandler
    public void onfjoin(PlayerJoinEvent e){

        if(!eiss.getBoolean("spawnschutz"))return;
        Player p=e.getPlayer();

        p.setInvulnerable(true);

        double y1=p.getLocation().getBlockY();
        double x1=p.getLocation().getBlockX();
        double z1=p.getLocation().getBlockZ();
        double yaww1=p.getLocation().getYaw();

        double radius=0.5;
        final double[] y={0};


        new BukkitRunnable(){

            @Override
            public void run(){
                double y2=p.getLocation().getBlockY();
                double x2=p.getLocation().getBlockX();
                double z2=p.getLocation().getBlockZ();
                double yaww2=p.getLocation().getYaw();

                //willkommens nachicht für mich
                if(p.getUniqueId().toString().equalsIgnoreCase("502eb3b3-b02b-4489-bb1b-6cda18b3f034") && !p.getGameMode().equals(GameMode.SURVIVAL)){

                    double x=radius * Math.cos(y[0]);
                    double z=radius * Math.sin(y[0]);

                    //kleiner ring
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(x,0,z),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(-x,0,-z),25);

                    //mittel ring
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(x*3,0,z*3),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(-x*3,0,-z*3),25);

                    //großer ring
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(-x*6,0,z*6),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(x*6,0,-z*6),25);

                    //klein mittel bangreiß

                    //greiße der geraden
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(x*3,0,z*3+1),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(x*3,0,-(z*3+1)),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(x*3+1,0,z*3),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(-(x*3+1),0,z*3),25);

                    //mittelgroßer ring dinger

                    //schrägen
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(x*3+0.5,0,x*3+0.5),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(x*3+0.5,0,-(x*3+0.5)),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(-(x*3+0.5),0,x*3+0.5),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(-(x*3+0.5),0,-(x*3+0.5)),25);

                    //geraden
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(0,0,z*3+1.5),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(0,0,-(z*3+1.5)),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(x*3+1.5,0,0),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(-(x*3+1.5),0,0),25);


                    //ovalen
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(x*4+1.5,0,(x+z)*4+1.5),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(x*4+1.5,0,-((x+z)*4+1.5)),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(-((x+z)*4+1.5),0,-(x*4+1.5)),25);
                    p.getWorld().spawnParticle(Particle.DRIPPING_LAVA,p.getLocation().add(-((x+z)*4+1.5),0,x*4+1.5),25);

                    y[0]+=0.1;
                }

                if(y1!=y2 || x1!=x2 || z1!=z2 || yaww1!=yaww2 || Bukkit.getPlayerExact(p.getName())==null){
                    p.setInvulnerable(false);
                    cancel();
                }

            }
        }.runTaskTimer(Monsterplugin.getPlugin(),0,1);

    }
}

package me.marc3308.monsterplugin.chess;

import me.marc3308.monsterplugin.Monsterplugin;
import me.marc3308.monsterplugin.chess.objekts.Chessbord;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class utilitys {

    public static ArrayList<Chessbord> schachliste = new ArrayList<>();

    public static void movefigure(ArmorStand ar, Location newloc){

        // Start moving the armor stand
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Math.abs(ar.getX() - newloc.getX()) < 0.1 &&
                        Math.abs(ar.getY() - newloc.getY()) < 0.1 &&
                        Math.abs(ar.getZ() - newloc.getZ()) < 0.1) {
                    ar.teleport(newloc);
                    cancel();
                    return;
                }

                // Move the armor stand
                ar.teleport(ar.getLocation().add(ar.getLocation().getX()>=newloc.getX()
                        ? ar.getLocation().getX()==newloc.getX() ? 0 : -0.1 : 0.1,
                        ar.getLocation().getY()>=newloc.getY()
                                ? ar.getLocation().getY()==newloc.getY() ? 0 : -0.1 : 0.1,
                        ar.getLocation().getZ()>=newloc.getZ()
                                ? ar.getLocation().getZ()==newloc.getZ() ? 0 : -0.1 : 0.1));

            }
        }.runTaskTimer(Monsterplugin.getPlugin(), 0L, 3L);
    }

    public static boolean darferdas(Chessbord s, int x1, int z1, int x2, int z2){

        switch (s.getGame().getBord()[x1][z1].getClass().getSimpleName()){
            case "Bauer":
                if(s.getGame().getBord()[x1][z1].isWhite()){
                    if(s.getGame().getBord()[x2][z2]==null && (x1+1==x2 || (x1+2==x2 && x1==1)) && z1==z2)return true;
                    if(s.getGame().getBord()[x2][z2]!=null && !s.getGame().getBord()[x2][z2].isWhite() && x1+1==x2 && (z1+1==z2 || z1-1==z2))return true;
                } else {
                    if(s.getGame().getBord()[x2][z2]==null && (x1-1==x2 || (x1-2==x2 && x1==6)) && z1==z2)return true;
                    if(s.getGame().getBord()[x2][z2]!=null && s.getGame().getBord()[x2][z2].isWhite() && x1-1==x2 && (z1+1==z2 || z1-1==z2))return true;
                }
                break;
            case "Turm":
                if(x1==x2){
                    if(z1>z2){
                        for (int i = z1-1; i > z2; i--) {
                            if(s.getGame().getBord()[x1][i]!=null){
                                return false;
                            }
                        }
                    } else {
                        for (int i = z1+1; i < z2; i++) {
                            if(s.getGame().getBord()[x1][i]!=null){
                                return false;
                            }
                        }
                    }
                    if(s.getGame().getBord()[x2][z2]==null || s.getGame().getBord()[x2][z2].isWhite()!=s.getGame().getBord()[x1][z1].isWhite())return true;
                } else if(z1==z2){
                    if(x1>x2){
                        for (int i = x1-1; i > x2; i--) {
                            if(s.getGame().getBord()[i][z1]!=null){
                                return false;
                            }
                        }
                    } else {
                        for (int i = x1+1; i < x2; i++) {
                            if(s.getGame().getBord()[i][z1]!=null){
                                return false;
                            }
                        }
                    }
                    if(s.getGame().getBord()[x2][z2]==null || s.getGame().getBord()[x2][z2].isWhite()!=s.getGame().getBord()[x1][z1].isWhite())return true;
                }
                break;
            case "Springer":
                if((x1+2==x2 && z1+1==z2) || (x1+2==x2 && z1-1==z2) || (x1-2==x2 && z1+1==z2) || (x1-2==x2 && z1-1==z2) || (x1+1==x2 && z1+2==z2) || (x1+1==x2 && z1-2==z2) || (x1-1==x2 && z1+2==z2) || (x1-1==x2 && z1-2==z2)){
                    if (s.getGame().getBord()[x2][z2]==null || s.getGame().getBord()[x2][z2].isWhite()!=s.getGame().getBord()[x1][z1].isWhite())return true;
                }
                break;
            case "Laufer":
                if(Math.abs(x1-x2)==Math.abs(z1-z2)){
                    if(x1<x2 && z1<z2){
                        for (int i = 1; i < x2-x1; i++) {
                            if(s.getGame().getBord()[x1+i][z1+i]!=null){
                                return false;
                            }
                        }
                    } else if(x1<x2 && z1>z2){
                        for (int i = 1; i < x2-x1; i++) {
                            if(s.getGame().getBord()[x1+i][z1-i]!=null){
                                return false;
                            }
                        }
                    } else if(x1>x2 && z1<z2){
                        for (int i = 1; i < x1-x2; i++) {
                            if(s.getGame().getBord()[x1-i][z1+i]!=null){
                                return false;
                            }
                        }
                    } else if(x1>x2 && z1>z2){
                        for (int i = 1; i < x1-x2; i++) {
                            if(s.getGame().getBord()[x1-i][z1-i]!=null){
                                return false;
                            }
                        }
                    }
                    if(s.getGame().getBord()[x2][z2]==null || s.getGame().getBord()[x2][z2].isWhite()!=s.getGame().getBord()[x1][z1].isWhite())return true;
                }
                break;
            case "Koenigin":
                if(x1==x2){
                    if(z1>z2){
                        for (int i = z1-1; i > z2; i--) {
                            if(s.getGame().getBord()[x1][i]!=null){
                                return false;
                            }
                        }
                    } else {
                        for (int i = z1+1; i < z2; i++) {
                            if(s.getGame().getBord()[x1][i]!=null){
                                return false;
                            }
                        }
                    }
                    if(s.getGame().getBord()[x2][z2]==null || s.getGame().getBord()[x2][z2].isWhite()!=s.getGame().getBord()[x1][z1].isWhite())return true;
                }
                else if(z1==z2){
                    if(x1>x2){
                        for (int i = x1-1; i > x2; i--) {
                            if(s.getGame().getBord()[i][z1]!=null){
                                return false;
                            }
                        }
                    } else {
                        for (int i = x1+1; i < x2; i++) {
                            if(s.getGame().getBord()[i][z1]!=null){
                                return false;
                            }
                        }
                    }
                    if(s.getGame().getBord()[x2][z2]==null || s.getGame().getBord()[x2][z2].isWhite()!=s.getGame().getBord()[x1][z1].isWhite())return true;
                }
                else if(Math.abs(x1-x2)==Math.abs(z1-z2)){
                    if(x1<x2 && z1<z2){
                        for (int i = 1; i < x2-x1; i++) {
                            if(s.getGame().getBord()[x1+i][z1+i]!=null){
                                return false;
                            }
                        }
                    } else if(x1<x2 && z1>z2){
                        for (int i = 1; i < x2-x1; i++) {
                            if(s.getGame().getBord()[x1+i][z1-i]!=null){
                                return false;
                            }
                        }
                    } else if(x1>x2 && z1<z2){
                        for (int i = 1; i < x1-x2; i++) {
                            if(s.getGame().getBord()[x1-i][z1+i]!=null){
                                return false;
                            }
                        }
                    } else if(x1>x2 && z1>z2){
                        for (int i = 1; i < x1-x2; i++) {
                            if(s.getGame().getBord()[x1-i][z1-i]!=null){
                                return false;
                            }
                        }
                    }
                    if(s.getGame().getBord()[x2][z2]==null || s.getGame().getBord()[x2][z2].isWhite()!=s.getGame().getBord()[x1][z1].isWhite())return true;
                }
                break;
            case "Koenig":
                if(Math.abs(x1-x2)<=1 && Math.abs(z1-z2)<=1){
                    if(s.getGame().getBord()[x2][z2]==null || s.getGame().getBord()[x2][z2].isWhite()!=s.getGame().getBord()[x1][z1].isWhite())return true;
                }
                //hohade
                if(z1+2==z2 || z1-2==z2 && x1==x2) {
                    if (z1 + 2 == z2 && (s.getGame().getBord()[x1][x2].isWhite() ? s.getGame().isWhite_kingside() : s.getGame().isBlack_kingside()) && s.getGame().getBord()[x1][z1 + 1] == null && s.getGame().getBord()[x1][z1 + 2] == null && s.getGame().getBord()[x1][z1 + 3] != null && s.getGame().getBord()[x1][z1 + 3].getClass().getSimpleName().equals("Turm") && s.getGame().getBord()[x1][z1 + 3].isWhite() == s.getGame().getBord()[x1][z1].isWhite()) {
                        return true;
                    }
                    if (z1 - 2 == z2 && (s.getGame().getBord()[x1][x2].isWhite() ? s.getGame().isWhite_queenside() : s.getGame().isBlack_queenside()) && s.getGame().getBord()[x1][z1 - 1] == null && s.getGame().getBord()[x1][z1 - 2] == null && s.getGame().getBord()[x1][z1 - 3] == null && s.getGame().getBord()[x1][z1 - 4] != null && s.getGame().getBord()[x1][z1 - 4].getClass().getSimpleName().equals("Turm") && s.getGame().getBord()[x1][z1 - 4].isWhite() == s.getGame().getBord()[x1][z1].isWhite()) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public static void savebords(){
        File cosfile = new File("plugins/KMS Plugins/DingeDiePassierenMussen","Chessbords.yml");
        FileConfiguration cosmetiks= YamlConfiguration.loadConfiguration(cosfile);
        for (int i = 0; i < schachliste.size(); i++) {
            cosmetiks.set(i+".Location",schachliste.get(i).getFeltstart());
            cosmetiks.set(i+".Name",schachliste.get(i).getName());
        }

        try {
            cosmetiks.save(cosfile);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static void loadbords(){
        File cosfile = new File("plugins/KMS Plugins/DingeDiePassierenMussen","Chessbords.yml");
        FileConfiguration cosmetiks= YamlConfiguration.loadConfiguration(cosfile);
        schachliste.clear();
        for (int i = 0; i < 300; i++) {
            if(cosmetiks.get(i+".Name")==null)break;
            schachliste.add(new Chessbord(
                    cosmetiks.getLocation(i+".Location")
                    ,cosmetiks.getString(i+".Name")
            ));
        }
    }
}

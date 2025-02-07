package me.marc3308.monsterplugin.spiele.chess;

import me.marc3308.monsterplugin.Monsterplugin;
import me.marc3308.monsterplugin.spiele.chess.objekts.Chessbord;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class chessutilitys {

    public static ArrayList<Chessbord> schachliste = new ArrayList<>();

    public static void movefigure(ArmorStand ar, Location newloc, int time){

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
        }.runTaskTimer(Monsterplugin.getPlugin(), 0L, time);
    }

    public static boolean darferdas(Chessbord s, int x1, int z1, int x2, int z2){

        switch (s.getGame().getBord()[x1][z1].getClass().getSimpleName()){
            case "Bauer":
                if(s.getGame().getBord()[x1][z1].isWhite()){
                    if(s.getGame().getBord()[x2][z2]==null && (x1+1==x2 || (x1+2==x2 && x1==1 && s.getGame().getBord()[x2+1][z2]==null)) && z1==z2)return true;
                    if(s.getGame().getBord()[x2][z2]!=null && !s.getGame().getBord()[x2][z2].isWhite() && x1+1==x2 && (z1+1==z2 || z1-1==z2))return true;

                    if(s.getGame().getLastturn()!=null && s.getGame().getBord()[x2][z2]==null && x2==5 && (z2+1==z1 || z2-1==z1) && x1==4
                            && s.getGame().getLastturn().split(":")[0].equals(String.valueOf(x1))
                            && Integer.valueOf(s.getGame().getLastturn().split(":")[1])==z2
                            && s.getGame().getBord()[Integer.valueOf(s.getGame().getLastturn().split(":")[0])][Integer.valueOf(s.getGame().getLastturn().split(":")[1])].getClass().getSimpleName().equals("Bauer")
                            && Integer.valueOf(s.getGame().getLastturn().split(":")[2])-2==4)return true;
                } else {
                    if(s.getGame().getBord()[x2][z2]==null && (x1-1==x2 || (x1-2==x2 && x1==6 && s.getGame().getBord()[x2-1][z2]==null)) && z1==z2)return true;
                    if(s.getGame().getBord()[x2][z2]!=null && s.getGame().getBord()[x2][z2].isWhite() && x1-1==x2 && (z1+1==z2 || z1-1==z2))return true;
                    if(s.getGame().getLastturn()!=null && s.getGame().getBord()[x2][z2]==null && x2==2 && (z2+1==z1 || z2-1==z1) && x1==3
                            && s.getGame().getLastturn().split(":")[0].equals(String.valueOf(x1))
                            && Integer.valueOf(s.getGame().getLastturn().split(":")[1])==z2
                            && s.getGame().getBord()[Integer.valueOf(s.getGame().getLastturn().split(":")[0])][Integer.valueOf(s.getGame().getLastturn().split(":")[1])].getClass().getSimpleName().equals("Bauer")
                            && Integer.valueOf(s.getGame().getLastturn().split(":")[2])+2==3)return true;
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
            case "Dame":
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
                    if (z1 + 2 == z2 && (s.getGame().getBord()[x1][z1].isWhite() ? s.getGame().isWhite_kingside() : s.getGame().isBlack_kingside()) && s.getGame().getBord()[x1][z1 + 1] == null && s.getGame().getBord()[x1][z1 + 2] == null && s.getGame().getBord()[x1][z1 + 3] != null && s.getGame().getBord()[x1][z1 + 3].getClass().getSimpleName().equals("Turm") && s.getGame().getBord()[x1][z1 + 3].isWhite() == s.getGame().getBord()[x1][z1].isWhite()) {
                        return true;
                    }
                    if (z1 - 2 == z2 && (s.getGame().getBord()[x1][z1].isWhite() ? s.getGame().isWhite_queenside() : s.getGame().isBlack_queenside()) && s.getGame().getBord()[x1][z1 - 1] == null && s.getGame().getBord()[x1][z1 - 2] == null && s.getGame().getBord()[x1][z1 - 3] == null && s.getGame().getBord()[x1][z1 - 4] != null && s.getGame().getBord()[x1][z1 - 4].getClass().getSimpleName().equals("Turm") && s.getGame().getBord()[x1][z1 - 4].isWhite() == s.getGame().getBord()[x1][z1].isWhite()) {
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

        //remove
        for (int i=0;i<300;i++)cosmetiks.set(String.valueOf(i),null);
        for (int i = 0; i < schachliste.size(); i++) {
            cosmetiks.set(i+".Location",schachliste.get(i).getFeltstart());
            cosmetiks.set(i+".Name",schachliste.get(i).getName());
            cosmetiks.set(i+".Help",schachliste.get(i).isHelp());
            cosmetiks.set(i+".Time",schachliste.get(i).getTime());
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
                    ,cosmetiks.getBoolean(i+".Help")
                    ,cosmetiks.getInt(i+".Time")
            ));
        }
    }

    public static void spawnhelp(Chessbord s,int x,int z){
        new BukkitRunnable(){
            @Override
            public void run() {
                if(s.getGame()==null || s.getGame().getBord()[x][z]==null){
                    cancel();
                    return;
                }

                if(!s.getGame().getTurn().equals((s.getGame().getBord()[x][z].isWhite() ? "white:" : "black:")+x+":"+z)){
                    cancel();
                    return;
                }
                switch (s.getGame().getBord()[x][z].getClass().getSimpleName()){
                    case "Bauer":
                        int is = s.getGame().getBord()[x][z].isWhite() ? 1 : -1;
                        if(x+is<=7 && x+is>=0 && s.getGame().getBord()[x+is][z]==null)armorhelp(s.getFeltstart().clone().add(x+is,0,z), Material.GREEN_CONCRETE);
                        if(x==1 && s.getGame().getBord()[3][z]==null && s.getGame().getBord()[2][z]==null &&  s.getGame().getBord()[x][z].isWhite())armorhelp(s.getFeltstart().clone().add(x+2,0,z), Material.GREEN_CONCRETE);
                        if(x==6 && s.getGame().getBord()[4][z]==null && s.getGame().getBord()[5][z]==null && !s.getGame().getBord()[x][z].isWhite())armorhelp(s.getFeltstart().clone().add(x-2,0,z), Material.GREEN_CONCRETE);
                        if(z+1<=7 && s.getGame().getBord()[x+is][z+1]!=null && s.getGame().getBord()[x+is][z+1].isWhite()!=s.getGame().getBord()[x][z].isWhite())armorhelp(s.getFeltstart().clone().add(x+is,0,z+1),Material.RED_CONCRETE);
                        if(z-1>=0 && s.getGame().getBord()[x+is][z-1]!=null && s.getGame().getBord()[x+is][z-1].isWhite()!=s.getGame().getBord()[x][z].isWhite())armorhelp(s.getFeltstart().clone().add(x+is,0,z-1),Material.RED_CONCRETE);

                        //onpassond
                        if(s.getGame().getLastturn()!=null && s.getGame().getBord()[x][z].isWhite() && x==4 && Integer.valueOf(s.getGame().getLastturn().split(":")[0])==x && Integer.valueOf(s.getGame().getLastturn().split(":")[1])==z+1
                                && s.getGame().getBord()[x][z+1].getClass().getSimpleName().equals("Bauer") && s.getGame().getLastturn().split(":")[2].equals(String.valueOf(x+2))
                        )armorhelp(s.getFeltstart().clone().add(x+1,0,z+1),Material.RED_CONCRETE);
                        if(s.getGame().getLastturn()!=null && s.getGame().getBord()[x][z].isWhite() && x==4 && Integer.valueOf(s.getGame().getLastturn().split(":")[0])==x && Integer.valueOf(s.getGame().getLastturn().split(":")[1])==z-1
                                && s.getGame().getBord()[x][z-1].getClass().getSimpleName().equals("Bauer") && s.getGame().getLastturn().split(":")[2].equals(String.valueOf(x+2))
                        )armorhelp(s.getFeltstart().clone().add(x+1,0,z-1),Material.RED_CONCRETE);
                        if(s.getGame().getLastturn()!=null && !s.getGame().getBord()[x][z].isWhite() && x==3 && Integer.valueOf(s.getGame().getLastturn().split(":")[0])==x && Integer.valueOf(s.getGame().getLastturn().split(":")[1])==z+1
                                && s.getGame().getBord()[x][z+1].getClass().getSimpleName().equals("Bauer") && s.getGame().getLastturn().split(":")[2].equals(String.valueOf(x-2))
                        )armorhelp(s.getFeltstart().clone().add(x-1,0,z+1),Material.RED_CONCRETE);
                        if(s.getGame().getLastturn()!=null && !s.getGame().getBord()[x][z].isWhite() && x==3 && Integer.valueOf(s.getGame().getLastturn().split(":")[0])==x && Integer.valueOf(s.getGame().getLastturn().split(":")[1])==z-1
                                && s.getGame().getBord()[x][z-1].getClass().getSimpleName().equals("Bauer") && s.getGame().getLastturn().split(":")[2].equals(String.valueOf(x-2))
                        )armorhelp(s.getFeltstart().clone().add(x-1,0,z-1),Material.RED_CONCRETE);

                        break;
                    case "Turm":
                        for (int i = x+1; i < 8; i++) {
                            if(s.getGame().getBord()[i][z]==null)armorhelp(s.getFeltstart().clone().add(i,0,z),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[i][z].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(i,0,z),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = x-1; i >= 0; i--) {
                            if(s.getGame().getBord()[i][z]==null)armorhelp(s.getFeltstart().clone().add(i,0,z),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[i][z].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(i,0,z),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = z+1; i < 8; i++) {
                            if(s.getGame().getBord()[x][i]==null)armorhelp(s.getFeltstart().clone().add(x,0,i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x][i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x,0,i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = z-1; i >= 0; i--) {
                            if(s.getGame().getBord()[x][i]==null)armorhelp(s.getFeltstart().clone().add(x,0,i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x][i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x,0,i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        break;
                    case "Springer":
                        if(x+2<8 && z+1<8 && (s.getGame().getBord()[x+2][z+1]==null || s.getGame().getBord()[x+2][z+1].isWhite()!=s.getGame().getBord()[x][z].isWhite()))armorhelp(s.getFeltstart().clone().add(x+2,0,z+1),s.getGame().getBord()[x+2][z+1]==null ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
                        if(x+2<8 && z-1>=0 && (s.getGame().getBord()[x+2][z-1]==null || s.getGame().getBord()[x+2][z-1].isWhite()!=s.getGame().getBord()[x][z].isWhite()))armorhelp(s.getFeltstart().clone().add(x+2,0,z-1),s.getGame().getBord()[x+2][z-1]==null ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
                        if(x-2>=0 && z+1<8 && (s.getGame().getBord()[x-2][z+1]==null || s.getGame().getBord()[x-2][z+1].isWhite()!=s.getGame().getBord()[x][z].isWhite()))armorhelp(s.getFeltstart().clone().add(x-2,0,z+1),s.getGame().getBord()[x-2][z+1]==null ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
                        if(x-2>=0 && z-1>=0 && (s.getGame().getBord()[x-2][z-1]==null || s.getGame().getBord()[x-2][z-1].isWhite()!=s.getGame().getBord()[x][z].isWhite()))armorhelp(s.getFeltstart().clone().add(x-2,0,z-1),s.getGame().getBord()[x-2][z-1]==null ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
                        if(x+1<8 && z+2<8 && (s.getGame().getBord()[x+1][z+2]==null || s.getGame().getBord()[x+1][z+2].isWhite()!=s.getGame().getBord()[x][z].isWhite()))armorhelp(s.getFeltstart().clone().add(x+1,0,z+2),s.getGame().getBord()[x+1][z+2]==null ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
                        if(x+1<8 && z-2>=0 && (s.getGame().getBord()[x+1][z-2]==null || s.getGame().getBord()[x+1][z-2].isWhite()!=s.getGame().getBord()[x][z].isWhite()))armorhelp(s.getFeltstart().clone().add(x+1,0,z-2),s.getGame().getBord()[x+1][z-2]==null ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
                        if(x-1>=0 && z+2<8 && (s.getGame().getBord()[x-1][z+2]==null || s.getGame().getBord()[x-1][z+2].isWhite()!=s.getGame().getBord()[x][z].isWhite()))armorhelp(s.getFeltstart().clone().add(x-1,0,z+2),s.getGame().getBord()[x-1][z+2]==null ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
                        if(x-1>=0 && z-2>=0 && (s.getGame().getBord()[x-1][z-2]==null || s.getGame().getBord()[x-1][z-2].isWhite()!=s.getGame().getBord()[x][z].isWhite()))armorhelp(s.getFeltstart().clone().add(x-1,0,z-2),s.getGame().getBord()[x-1][z-2]==null ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
                        break;
                    case "Laufer":
                        for (int i = 1; x+i<8 && z+i<8; i++) {
                            if(s.getGame().getBord()[x+i][z+i]==null)armorhelp(s.getFeltstart().clone().add(x+i,0,z+i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x+i][z+i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x+i,0,z+i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = 1; x+i<8 && z-i>=0; i++) {
                            if(s.getGame().getBord()[x+i][z-i]==null)armorhelp(s.getFeltstart().clone().add(x+i,0,z-i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x+i][z-i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x+i,0,z-i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = 1; x-i>=0 && z+i<8; i++) {
                            if(s.getGame().getBord()[x-i][z+i]==null)armorhelp(s.getFeltstart().clone().add(x-i,0,z+i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x-i][z+i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x-i,0,z+i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = 1; x-i>=0 && z-i>=0; i++) {
                            if(s.getGame().getBord()[x-i][z-i]==null)armorhelp(s.getFeltstart().clone().add(x-i,0,z-i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x-i][z-i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x-i,0,z-i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        break;
                    case "Dame":
                        for (int i = z+1; i < 8; i++) {
                            if(s.getGame().getBord()[x][i]==null)armorhelp(s.getFeltstart().clone().add(x,0,i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x][i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x,0,i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = z-1; i >= 0; i--) {
                            if(s.getGame().getBord()[x][i]==null)armorhelp(s.getFeltstart().clone().add(x,0,i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x][i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x,0,i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = x+1; i < 8; i++) {
                            if(s.getGame().getBord()[i][z]==null)armorhelp(s.getFeltstart().clone().add(i,0,z),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[i][z].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(i,0,z),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = x-1; i >= 0; i--) {
                            if(s.getGame().getBord()[i][z]==null)armorhelp(s.getFeltstart().clone().add(i,0,z),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[i][z].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(i,0,z),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = 1; x+i<8 && z+i<8; i++) {
                            if(s.getGame().getBord()[x+i][z+i]==null)armorhelp(s.getFeltstart().clone().add(x+i,0,z+i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x+i][z+i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x+i,0,z+i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = 1; x+i<8 && z-i>=0; i++) {
                            if(s.getGame().getBord()[x+i][z-i]==null)armorhelp(s.getFeltstart().clone().add(x+i,0,z-i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x+i][z-i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x+i,0,z-i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = 1; x-i>=0 && z+i<8; i++) {
                            if(s.getGame().getBord()[x-i][z+i]==null)armorhelp(s.getFeltstart().clone().add(x-i,0,z+i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x-i][z+i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x-i,0,z+i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        for (int i = 1; x-i>=0 && z-i>=0; i++) {
                            if(s.getGame().getBord()[x-i][z-i]==null)armorhelp(s.getFeltstart().clone().add(x-i,0,z-i),Material.GREEN_CONCRETE);
                            else if(s.getGame().getBord()[x-i][z-i].isWhite()!=s.getGame().getBord()[x][z].isWhite()){
                                armorhelp(s.getFeltstart().clone().add(x-i,0,z-i),Material.RED_CONCRETE);
                                break;
                            } else break;
                        }
                        break;
                    case "Koenig":
                        for (int i = -1; i<2;i++){
                            for (int j = -1; j < 2; j++) {
                                if(x+i>=0 && x+i<8 && z+j>=0 && z+j<8 && (s.getGame().getBord()[x+i][z+j]==null || s.getGame().getBord()[x+i][z+j].isWhite()!=s.getGame().getBord()[x][z].isWhite()))armorhelp(s.getFeltstart().clone().add(x+i,0,z+j),s.getGame().getBord()[x+i][z+j]==null ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
                            }
                        }
                        //hohade
                        if ((s.getGame().getBord()[x][z].isWhite() ? s.getGame().isWhite_kingside() : s.getGame().isBlack_kingside()) && s.getGame().getBord()[x][z + 1] == null && s.getGame().getBord()[x][z + 2] == null && s.getGame().getBord()[x][z + 3] != null && s.getGame().getBord()[x][z + 3].getClass().getSimpleName().equals("Turm") && s.getGame().getBord()[x][z + 3].isWhite() == s.getGame().getBord()[x][z].isWhite()) {
                            armorhelp(s.getFeltstart().clone().add(x,0,z+2),Material.GREEN_CONCRETE);
                        }
                        if ((s.getGame().getBord()[x][z].isWhite() ? s.getGame().isWhite_queenside() : s.getGame().isBlack_queenside()) && s.getGame().getBord()[x][z - 1] == null && s.getGame().getBord()[x][z - 2] == null && s.getGame().getBord()[x][z - 3] == null && s.getGame().getBord()[x][z - 4] != null && s.getGame().getBord()[x][z - 4].getClass().getSimpleName().equals("Turm") && s.getGame().getBord()[x][z - 4].isWhite() == s.getGame().getBord()[x][z].isWhite()){
                            armorhelp(s.getFeltstart().clone().add(x,0,z-2),Material.GREEN_CONCRETE);
                        }
                        break;
                }
            }
        }.runTaskTimer(Monsterplugin.getPlugin(),0,3);
    }

    public static void armorhelp(Location loc,Material mat){

        ArmorStand ar = loc.getWorld().spawn(loc.clone().add(0.5,-1.9,0.5),ArmorStand.class);
        ar.setInvulnerable(true);
        ar.setCustomNameVisible(false);
        ar.setGravity(false);
        ar.setVisible(false);
        ar.setMarker(true);
        ItemStack helmet = new ItemStack(mat);
        ItemMeta meta = helmet.getItemMeta();
        helmet.setItemMeta(meta);
        ar.setHelmet(helmet);

        Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(),() -> ar.remove(),3);
    }

    public static void makeamove(Chessbord s, Player p, Location blocklock){
        switch (s.getGame().getTurn().split(":")[0]){
            case "white":
                if(s.getGame().getSpieler1().equals(p)){
                    int x = blocklock.getBlockX()-s.getFeltstart().getBlockX();
                    int z = blocklock.getBlockZ()-s.getFeltstart().getBlockZ();
                    if(x<0 || x>7 || z<0 || z>7)return;
                    if(s.getGame().getTurn().equals("netfinished"))return;
                    if(s.getGame().getTurn().split(":").length>1
                            && (s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])]==null
                            || s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].isWhite())
                            && chessutilitys.darferdas(s,Integer.valueOf(s.getGame().getTurn().split(":")[1]),Integer.valueOf(s.getGame().getTurn().split(":")[2]),x,z)){

                        movefigure(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand(),s.getFeltstart().clone().add(x,0,z).clone().add(0.5,-1.9,0.5),s.getTime());
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

                        //caseling
                        if(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Koenig")
                                || s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Turm")){
                            if(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Koenig")){
                                if(Integer.valueOf(s.getGame().getTurn().split(":")[2])+2==z){
                                    movefigure(s.getGame().getBord()[0][7].getArmorStand(),s.getFeltstart().clone().add(0,0,5).clone().add(0.5,-1.9,0.5),s.getTime());
                                    s.getGame().getBord()[0][5]=s.getGame().getBord()[0][7];
                                    s.getGame().getBord()[0][7]=null;
                                } else if(Integer.valueOf(s.getGame().getTurn().split(":")[2])-2==z){
                                    movefigure(s.getGame().getBord()[0][0].getArmorStand(),s.getFeltstart().clone().add(0,0,3).clone().add(0.5,-1.9,0.5),s.getTime());
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
                        //dalay
                        double delay = s.getFeltstart().add(x,0,z).distance(
                                s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand().getLocation());

                        //on passond
                        if(s.getGame().getBord()[x][z]==null
                                && s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Bauer")
                                && z!=Integer.valueOf(s.getGame().getTurn().split(":")[2])){
                            // Create the firework entity at the specified location
                            Firework firework = (Firework) s.getGame().getBord()[x-1][z].getArmorStand().getWorld().spawnEntity(
                                    s.getGame().getBord()[x-1][z].getArmorStand().getLocation(), EntityType.FIREWORK_ROCKET);

                            // Get the firework meta data
                            FireworkMeta fireworkMeta = firework.getFireworkMeta();

                            // Create a firework effect
                            FireworkEffect effect = FireworkEffect.builder()
                                    .withColor(Color.BLACK)
                                    .withFade(Color.BLACK)
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

                            s.getGame().getBord()[x-1][z].getArmorStand().remove();
                            s.getGame().getBord()[x-1][z]=null;
                        }

                        s.getGame().getBord()[x][z]=s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])];
                        s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])]=null;

                        s.getGame().setLastturn(x+":"+z+":"+s.getGame().getTurn().split(":")[1]+":"+s.getGame().getTurn().split(":")[2]);
                        s.getGame().setTurn("netfinished");
                        Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () ->{
                            if(!s.hasGame())return;
                            //promotion
                            if(x==7 && s.getGame().getBord()[x][z].getClass().getSimpleName().equals("Bauer")){
                                s.getGame().setTurn("netfinished:white:"+x+":"+z);
                                s.getGame().createpromolist(p);
                            } else s.getGame().setTurn("black");
                        }, (long) (delay*(9*s.getTime())));

                    } else {
                        if(s.getGame().getBord()[x][z]!=null && s.getGame().getBord()[x][z].isWhite()){
                            s.getGame().getBord()[x][z].getArmorStand().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1));
                            if(s.getGame().getTurn().split(":").length>1){
                                s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand().removePotionEffect(PotionEffectType.GLOWING);
                            }
                            s.getGame().setTurn(s.getGame().getTurn().split(":").length>1 && Integer.valueOf(s.getGame().getTurn().split(":")[1]).equals(x) && Integer.valueOf(s.getGame().getTurn().split(":")[2]).equals(z)
                                    ? "white" : "white:"+x+":"+z);

                            if(s.isHelp()) chessutilitys.spawnhelp(s,x,z);

                        }
                    }
                }
                break;
            case "black":
                if(s.getGame().getSpieler2().equals(p)){
                    int x = blocklock.getBlockX()-s.getFeltstart().getBlockX();
                    int z = blocklock.getBlockZ()-s.getFeltstart().getBlockZ();
                    if(x<0 || x>7 || z<0 || z>7)return;
                    if(s.getGame().getTurn().split(":").length>1
                            && (s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])]==null
                            || !s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].isWhite())
                            && chessutilitys.darferdas(s,Integer.valueOf(s.getGame().getTurn().split(":")[1]),Integer.valueOf(s.getGame().getTurn().split(":")[2]),x,z)){
                        movefigure(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand(),s.getFeltstart().clone().add(x,0,z).clone().add(0.5,-1.9,0.5),s.getTime());
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

                        //hohade
                        if(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Koenig")
                                ||s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Turm")){
                            if(s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Koenig")){
                                if(Integer.valueOf(s.getGame().getTurn().split(":")[2])+2==z){
                                    movefigure(s.getGame().getBord()[7][7].getArmorStand(),s.getFeltstart().clone().add(7,0,5).clone().add(0.5,-1.9,0.5),s.getTime());
                                    s.getGame().getBord()[7][5]=s.getGame().getBord()[7][7];
                                    s.getGame().getBord()[7][7]=null;
                                } else if(Integer.valueOf(s.getGame().getTurn().split(":")[2])-2==z){
                                    movefigure(s.getGame().getBord()[7][0].getArmorStand(),s.getFeltstart().clone().add(7,0,3).clone().add(0.5,-1.9,0.5),s.getTime());
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

                        //on passond
                        if(s.getGame().getBord()[x][z]==null
                                && s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getClass().getSimpleName().equals("Bauer")
                                && z!=Integer.valueOf(s.getGame().getTurn().split(":")[2])){
                            // Create the firework entity at the specified location
                            Firework firework = (Firework) s.getGame().getBord()[x+1][z].getArmorStand().getWorld().spawnEntity(
                                    s.getGame().getBord()[x+1][z].getArmorStand().getLocation(), EntityType.FIREWORK_ROCKET);

                            // Get the firework meta data
                            FireworkMeta fireworkMeta = firework.getFireworkMeta();

                            // Create a firework effect
                            FireworkEffect effect = FireworkEffect.builder()
                                    .withColor(Color.WHITE)
                                    .withFade(Color.WHITE)
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

                            s.getGame().getBord()[x+1][z].getArmorStand().remove();
                            s.getGame().getBord()[x+1][z]=null;
                        }

                        s.getGame().getBord()[x][z]=s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])];
                        s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])]=null;

                        s.getGame().setLastturn(x+":"+z+":"+s.getGame().getTurn().split(":")[1]+":"+s.getGame().getTurn().split(":")[2]);
                        s.getGame().setTurn("netfinished");
                        Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () ->{
                            if(!s.hasGame())return;
                            if(x==0 && s.getGame().getBord()[x][z].getClass().getSimpleName().equals("Bauer")){
                                s.getGame().setTurn("netfinished:black:"+x+":"+z);
                                s.getGame().createpromolist(p);
                            } else s.getGame().setTurn("white");
                        }, (long) (delay*(9*s.getTime())));
                    } else {
                        if(s.getGame().getBord()[x][z]!=null && !s.getGame().getBord()[x][z].isWhite()){
                            s.getGame().getBord()[x][z].getArmorStand().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1));
                            if(s.getGame().getTurn().split(":").length>1){
                                s.getGame().getBord()[Integer.valueOf(s.getGame().getTurn().split(":")[1])][Integer.valueOf(s.getGame().getTurn().split(":")[2])].getArmorStand().removePotionEffect(PotionEffectType.GLOWING);
                            }
                            s.getGame().setTurn(s.getGame().getTurn().split(":").length>1 && Integer.valueOf(s.getGame().getTurn().split(":")[1]).equals(x) && Integer.valueOf(s.getGame().getTurn().split(":")[2]).equals(z)
                                    ? "black" : "black:"+x+":"+z);

                            if(s.isHelp()) chessutilitys.spawnhelp(s,x,z);

                        }
                    }
                }
                break;
            case "farbauswal":
                int x = blocklock.getBlockX()-s.getFeltstart().getBlockX();
                int z = blocklock.getBlockZ()-s.getFeltstart().getBlockZ();
                if(x==3 || x==4 && z==3 || z==4){
                    Player p1 = s.getGame().getSpieler1();
                    Player p2 = s.getGame().getSpieler2();
                    s.getGame().setSpieler1(z==3 ? p : p1.equals(p) ? p2 : p1);
                    s.getGame().setSpieler2(z==4 ? p : p1.equals(p) ? p2 : p1);
                    s.getGame().setTurn("white");
                    ArmorStand ar1 = s.getGame().getBord()[0][0].getArmorStand();
                    ArmorStand ar2 = s.getGame().getBord()[0][1].getArmorStand();

                    chessutilitys.movefigure(ar1,ar1.getLocation().clone().add(0,-3,0),s.getTime());
                    chessutilitys.movefigure(ar2,ar2.getLocation().clone().add(0,-3,0),s.getTime());
                    Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> {
                        ar1.remove();
                        ar2.remove();
                    },20*4);

                    s.getGame().spawnbordside("white");
                    s.getGame().spawnbordside("black");
                }
                break;
            case "promo":
                if(s.getGame().getTurn().split(":")[1].equals("white")){
                    if(s.getGame().getSpieler1().equals(p)){
                        int x1 = blocklock.getBlockX()-s.getFeltstart().getBlockX();
                        int z1 = blocklock.getBlockZ()-s.getFeltstart().getBlockZ();
                        if(x1==7 && z1>1 && z1<6){
                            s.getGame().promote(x1,z1);
                        }
                    }
                } else {
                    if(s.getGame().getSpieler2().equals(p)){
                        int x1 = blocklock.getBlockX()-s.getFeltstart().getBlockX();
                        int z1 = blocklock.getBlockZ()-s.getFeltstart().getBlockZ();
                        if(x1==0 && z1>1 && z1<6){
                            s.getGame().promote(x1,z1);
                        }
                    }
                }
                break;
        }

    }
}

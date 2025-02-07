package me.marc3308.monsterplugin.spiele.chess.objekts;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import me.marc3308.monsterplugin.Monsterplugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;

import static me.marc3308.monsterplugin.spiele.chess.chessutilitys.darferdas;
import static me.marc3308.monsterplugin.spiele.chess.chessutilitys.movefigure;

public class chessgame {
    private Chessbord chessbord;
    private Player spieler1;
    private Player spieler2;
    private boolean gamehasstarted;
    private Figuren[][] bord = new Figuren[8][8];
    private String lastturn;
    private String turn;
    private boolean white_kingside;
    private boolean black_kingside;
    private boolean black_queenside;
    private boolean white_queenside;
    private ArmorStand whitstand;
    private ArmorStand blackstand;
    private ArrayList<Figuren> promolist = new ArrayList<>();
    private ArrayList<Player> zuschauerlist=new ArrayList<>();

    public ArmorStand getWhitstand() {
        return whitstand;
    }

    public ArmorStand getBlackstand() {
        return blackstand;
    }

    public ArrayList<Player> getZuschauerlist() {
        return zuschauerlist;
    }

    public void setLastturn(String lastturn) {
        this.lastturn = lastturn;
    }

    public ArrayList<Figuren> getPromolist() {
        return promolist;
    }

    public String getLastturn() {
        return lastturn;
    }

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
        spieler1=p;
        spieler2=p2;
        final int[] i = {0};

        //cammera
        Location cameraLocation = chessbord.getFeltstart().add(4, 6, 4);
        whitstand = p.getWorld().spawn(cameraLocation, ArmorStand.class);
        whitstand.setVisible(false);
        whitstand.setGravity(false);
        whitstand.setMarker(true);
        whitstand.setRotation(-90, 90); // Look down

        blackstand = p.getWorld().spawn(cameraLocation, ArmorStand.class);
        blackstand.setInvulnerable(true);
        blackstand.setGravity(false);
        blackstand.setVisible(false);
        blackstand.setMarker(true);
        blackstand.setRotation(90, 90); // Look down

        new BukkitRunnable(){
            @Override
            public void run() {
                //cancl
                if(i[0] >=30 || !gamehasstarted){
                    zuschauerlist.forEach(p -> {
                        // Packet
                        PacketContainer cameraPacket2 = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
                        cameraPacket2.getIntegers().write(0, p.getEntityId());
                        ProtocolLibrary.getProtocolManager().sendServerPacket(p, cameraPacket2);
                        if(p.equals(spieler1) || p.equals(spieler2))Bukkit.getOnlinePlayers().forEach(p1 -> p.showPlayer(Monsterplugin.getPlugin(), p1));
                    });
                    whitstand.remove();
                    blackstand.remove();
                    if(gamehasstarted)gameend("none");
                    chessbord.setGame(null);
                    cancel();
                    return;
                }
                Chessbord s = chessbord;
                if(!spieler1.isOnline() || !spieler2.isOnline()
                        || !((s.getFeltend().getX()<s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())-2<=spieler1.getX()
                        && (s.getFeltend().getX()>s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())+2>=spieler1.getX()
                        && (s.getFeltend().getY()<s.getFeltstart().getY() ? s.getFeltend().getY() : s.getFeltstart().getY())+2>=spieler1.getY()
                        && (s.getFeltend().getY()<s.getFeltstart().getY() ? s.getFeltend().getY() : s.getFeltstart().getY())-2<=spieler1.getY()
                        && (s.getFeltend().getZ()<s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())-2<=spieler1.getZ()
                        && (s.getFeltend().getZ()>s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())+2>=spieler1.getZ())
                        || !((s.getFeltend().getX()<s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())-2<=spieler2.getX()
                        && (s.getFeltend().getX()>s.getFeltstart().getX() ? s.getFeltend().getX() : s.getFeltstart().getX())+2>=spieler2.getX()
                        && (s.getFeltend().getY()<s.getFeltstart().getY() ? s.getFeltend().getY() : s.getFeltstart().getY())+2>=spieler2.getY()
                        && (s.getFeltend().getY()<s.getFeltstart().getY() ? s.getFeltend().getY() : s.getFeltstart().getY())-2<=spieler2.getY()
                        && (s.getFeltend().getZ()<s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())-2<=spieler2.getZ()
                        && (s.getFeltend().getZ()>s.getFeltstart().getZ() ? s.getFeltend().getZ() : s.getFeltstart().getZ())+2>=spieler2.getZ()))i[0]++;
                else i[0]=0;

                //out timmer
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

                //camera?
                Iterator<Player> iterator = zuschauerlist.iterator();
                while (iterator.hasNext()) {
                    Player p = iterator.next();

                    // Camera logic
                    if (!p.isOnline()) {
                        // Packet
                        PacketContainer cameraPacket2 = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
                        cameraPacket2.getIntegers().write(0, p.getEntityId());
                        ProtocolLibrary.getProtocolManager().sendServerPacket(p, cameraPacket2);

                        if (p.equals(spieler1) || p.equals(spieler2)) {
                            Bukkit.getOnlinePlayers().forEach(p1 -> p.showPlayer(Monsterplugin.getPlugin(), p1));
                        }

                        // Remove Zuschauer safely
                        iterator.remove();
                        continue;
                    }

                    // Vanish logic
                    if (p.equals(spieler1) || p.equals(spieler2)) {
                        Bukkit.getOnlinePlayers().forEach(p1 -> p.hidePlayer(Monsterplugin.getPlugin(), p1));
                    }

                    boolean schwarzeseite =chessbord.getFeltstart().add(3,0,3.5).distance(p.getLocation())>=chessbord.getFeltstart().add(4,0,3.5).distance(p.getLocation());
                    // Packet camera
                    PacketContainer cameraPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
                    cameraPacket.getIntegers().write(0, schwarzeseite  ? blackstand.getEntityId() : whitstand.getEntityId());
                    ProtocolLibrary.getProtocolManager().sendServerPacket(p, cameraPacket);

                    // Auto update logic
                    p.sendMessage(ChatColor.DARK_GREEN + ("------Spielstand------"));
                    if(getTurn().contains("promo")
                            && !promolist.isEmpty()
                            && promolist.size()==4
                            && ((p.equals(spieler1) && getTurn().split(":")[1].equals("white")) || (p.equals(spieler2) && getTurn().split(":")[1].equals("black")))){

                        Component row = Component.text("[" + (getTurn().split(":")[1].equals("white") ? 8 : 1) + "]").color(NamedTextColor.DARK_GREEN); // Row label
                        int j = (getTurn().split(":")[1].equals("white") ? 7 : 0);
                        for (int i = (!schwarzeseite ? 0 : 7);
                             !schwarzeseite ? i < 8 : i >= 0;
                             i = !schwarzeseite ? i + 1 : i - 1){

                            ChatColor squareColor = (i == 0 && j == 0) || (i + j) % 2 == 0 ? ChatColor.DARK_GRAY : ChatColor.WHITE;
                            String name = i>1 && i<6 ? promolist.get(i-2).getArmorStand().getName().substring(0, 3) : "_";
                            String columnLetter = switch (i) {
                                case 0 -> "A";
                                case 1 -> "B";
                                case 2 -> "C";
                                case 3 -> "D";
                                case 4 -> "E";
                                case 5 -> "F";
                                case 6 -> "G";
                                default -> "H";
                            };

                            Component boardCell = Component.text(squareColor+"["+name+squareColor+"]")
                                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/cpe " + columnLetter + (j+1)))
                                    .hoverEvent(HoverEvent.showText(Component.text(columnLetter + (j+1) + (name.equals("_")
                                            ? "" : " "+promolist.get(i-2).getArmorStand().getName().substring(0, 2) + " [" + promolist.get(i-2).getArmorStand().getName().substring(2) + "]"))));
                            row = row.append(boardCell);
                        }

                        p.sendMessage(row);

                    } else {
                        for (int i = (schwarzeseite ? 0 : 7);
                             schwarzeseite ? i < 8 : i >= 0;
                             i = schwarzeseite ? i + 1 : i - 1) {

                            Component row = Component.text("[" + (i + 1) + "]").color(NamedTextColor.DARK_GREEN); // Row label

                            for (int j = (!schwarzeseite ? 0 : 7);
                                 !schwarzeseite ? j < 8 : j >= 0;
                                 j = !schwarzeseite ? j + 1 : j - 1) {

                                // Determine board cell color
                                ChatColor squareColor = s.getGame().getTurn().split(":").length > 1
                                        && !s.getGame().getTurn().split(":")[0].equals("promo")
                                        && !s.getGame().getTurn().split(":")[0].equals("netfinished")
                                        && darferdas(chessbord, Integer.valueOf(s.getGame().getTurn().split(":")[1]), Integer.valueOf(s.getGame().getTurn().split(":")[2]), i, j)
                                        ? s.getGame().getBord()[i][j] != null ? ChatColor.RED : ChatColor.GREEN : (i == 0 && j == 0) || (i + j) % 2 == 0 ? ChatColor.DARK_GRAY : ChatColor.WHITE;

                                // Default empty cell component
                                String columnLetter = switch (j) {
                                    case 0 -> "A";
                                    case 1 -> "B";
                                    case 2 -> "C";
                                    case 3 -> "D";
                                    case 4 -> "E";
                                    case 5 -> "F";
                                    case 6 -> "G";
                                    default -> "H";
                                };

                                String name = "";
                                String hover = "";
                                String command = columnLetter + (i + 1);
                                Component boardCell;

                                if (s.getGame().getBord()[i][j] != null) {
                                    String pieceName = s.getGame().getBord()[i][j].getArmorStand().getName().substring(2, 3);
                                    ChatColor pieceColor = s.getGame().getTurn().split(":").length > 1
                                            && !s.getGame().getTurn().split(":")[0].equals("promo")
                                            && !s.getGame().getTurn().split(":")[0].equals("netfinished")
                                            && Integer.valueOf(s.getGame().getTurn().split(":")[1]) == i
                                            && Integer.valueOf(s.getGame().getTurn().split(":")[2]) == j ? ChatColor.GOLD : s.getGame().getBord()[i][j].isWhite() ? ChatColor.WHITE : ChatColor.DARK_GRAY;

                                    name = squareColor + "[" + pieceColor + pieceName + squareColor + "]";
                                    hover = command + s.getGame().getBord()[i][j].getArmorStand().getName().substring(0, 2) + " [" + s.getGame().getBord()[i][j].getArmorStand().getName().substring(2) + "]";
                                    boardCell = Component.text(name)
                                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/cpe " + command))
                                            .hoverEvent(HoverEvent.showText(Component.text(hover)));
                                } else {
                                    name = squareColor + "[_]";
                                    hover = command;
                                    boardCell = Component.text(name)
                                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/cpe " + command))
                                            .hoverEvent(HoverEvent.showText(Component.text(hover)));
                                }
                                Component normalcell = Component.text(name)
                                        .hoverEvent(HoverEvent.showText(Component.text(hover)));
                                row = row.append(!p.equals(spieler1) && !p.equals(spieler2)
                                        && ((p.equals(spieler1) && getTurn().split(":")[0].equals("white")) || (p.equals(spieler2) && getTurn().split(":")[0].equals("black"))) ? normalcell : boardCell);
                            }

                            // Send the row component to the player
                            p.sendMessage(row);
                        }
                    }
                    p.sendMessage(Component.text(schwarzeseite ? "[X][H][G][F][E][D][C][B][A]" : "[X][A][B][C][D][E][F][G][H]")
                            .color(NamedTextColor.DARK_GREEN));
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
        promolist.forEach(f -> {
            movefigure(f.getArmorStand(), f.getArmorStand().getLocation().clone().add(0,-3,0),3);
            Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> f.getArmorStand().remove(),20*4);
        });
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(bord[i][j]==null)continue;
                movefigure(bord[i][j].getArmorStand(),bord[i][j].getArmorStand().getLocation().clone().add(0,-3,0),3);
                int finalI = i;
                int finalJ = j;
                Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> bord[finalI][finalJ].getArmorStand().remove(),20*4);

                if(!winner.equals("none")){
                    Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () ->{
                        // Create the firework entity at the specified location
                        Firework firework = (Firework) chessbord.getFeltstart().getWorld().spawnEntity(
                                 chessbord.getFeltstart().add(winner.equals("white") ? finalI : 7-finalI ,4,finalJ), EntityType.FIREWORK_ROCKET);

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
                        fireworkMeta.setPower(0);

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
                            : j==3 ? new Dame(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+5,true)
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
                                    : j==3 ? new Dame(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+15,false)
                                    : new Koenig(chessbord.getFeltstart().add(i,0,j), Material.PAPER, modeldata+16,false); //black
                }
            }
        }
    }

    public void promote(int x ,int z){
        promolist.get(z-2).getArmorStand().teleport(bord[Integer.valueOf(lastturn.split(":")[0])][Integer.valueOf(lastturn.split(":")[1])].getArmorStand().getLocation());
        bord[Integer.valueOf(lastturn.split(":")[0])][Integer.valueOf(lastturn.split(":")[1])].getArmorStand().remove();
        bord[Integer.valueOf(lastturn.split(":")[0])][Integer.valueOf(lastturn.split(":")[1])] = promolist.get(z-2);
        promolist.remove(z-2);
        promolist.forEach(f -> movefigure(f.getArmorStand(), f.getArmorStand().getLocation().clone().add(0,-2,0),3));
        Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () ->{
            moveall(2);
            promolist.forEach(f -> f.getArmorStand().remove());
            promolist.clear();
        },20);
        Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> setTurn(getTurn().split(":")[1].equals("white") ? "black" : "white"),20*3);
    }

    public void createpromolist(Player p){
        int modeldata=spieler1.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(),"chessskin"))
                ? spieler1.getPersistentDataContainer().get(new NamespacedKey(Monsterplugin.getPlugin(),"chessskin"), PersistentDataType.INTEGER)
                : 10000;
        Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> moveall(-2),20);
        promolist.forEach(f -> f.getArmorStand().remove());
        promolist.clear();
        promolist.add(new Turm(chessbord.getFeltstart().add(getTurn().split(":")[1].equals("white") ? 7 : 0,0,2), Material.PAPER, getTurn().split(":")[1].equals("white") ? modeldata+2 : modeldata+12,getTurn().split(":")[1].equals("white")));
        promolist.add(new Springer(chessbord.getFeltstart().add(getTurn().split(":")[1].equals("white") ? 7 : 0,0,3), Material.PAPER, getTurn().split(":")[1].equals("white") ? modeldata+3 : modeldata+13,getTurn().split(":")[1].equals("white")));
        promolist.add(new Laufer(chessbord.getFeltstart().add(getTurn().split(":")[1].equals("white") ? 7 : 0,0,4), Material.PAPER, getTurn().split(":")[1].equals("white") ? modeldata+4 : modeldata+14,getTurn().split(":")[1].equals("white")));
        promolist.add(new Dame(chessbord.getFeltstart().add(getTurn().split(":")[1].equals("white") ? 7 : 0,0,5), Material.PAPER, getTurn().split(":")[1].equals("white") ? modeldata+5 : modeldata+15,getTurn().split(":")[1].equals("white")));
        Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () ->setTurn("promo:"+getTurn().split(":")[1]+":"+getTurn().split(":")[2]+":"+getTurn().split(":")[3]),20*4);
    }

    public void moveall(double y){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(bord[i][j]==null)continue;
                movefigure(bord[i][j].getArmorStand(),bord[i][j].getArmorStand().getLocation().clone().add(0,y,0), 3);
            }
        }
    }

    public boolean isGamehasstarted() {
        return gamehasstarted;
    }
}

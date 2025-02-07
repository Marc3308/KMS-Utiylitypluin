package me.marc3308.monsterplugin.cosmetiks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static me.marc3308.monsterplugin.Monsterplugin.cosmetikslist;

public class events implements Listener {

    @EventHandler
    public void oninv(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();

        //update inv
        if(e.getSlotType().equals(InventoryType.SlotType.ARMOR) || (e.getSlotType().equals(InventoryType.SlotType.QUICKBAR) && e.getSlot() == 40)){
            sendpack(p);
        }


        //cosmikinf
        if(e.getView().getTitle().equalsIgnoreCase("§lCosmetic-Menü")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            switch (e.getSlot()){
                case 13:
                    opencosmikmenu2(p,"Kopf",1);
                    break;
                case 22:
                    opencosmikmenu2(p,"Körper",1);
                    break;
                case 31:
                    opencosmikmenu2(p,"Beine",1);
                    break;
                case 40:
                    opencosmikmenu2(p,"Füße",1);
                    break;
                case 29:
                    opencosmikmenu2(p,"Mainhand",1);
                    break;
                case 33:
                    opencosmikmenu2(p,"Offhand",1);
                    break;
            }
            return;
        }

        if(e.getView().getTitle().split(" >")[0].equalsIgnoreCase("§lCosmetic-Menü")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            String sorter = " ";
            switch (e.getView().getTitle().split("> ")[1]){
                case "Kopf":
                    sorter="comichead";
                    break;
                case "Körper":
                    sorter="comicchest";
                    break;
                case "Beine":
                    sorter="comiclegs";
                    break;
                case "Füße":
                    sorter="comicfeet";
                    break;
                case "Mainhand":
                    sorter="commicmainhand";
                    break;
                case "Offhand":
                    sorter="comicoffhand";
                    break;
            }
            switch (e.getCurrentItem().getType()){
                case ARROW:
                    opencosmikmenu2(p,e.getView().getTitle().split("> ")[1],Integer.parseInt(e.getView().getItem(51).getItemMeta().getDisplayName())+1);
                    break;
                case BARRIER:
                    Bukkit.dispatchCommand(p,"cosmetic");
                    sendpack(p);
                    break;
                case BOOK:
                    opencosmikmenu2(p,e.getView().getTitle().split("> ")[1],1);
                    break;
                default:
                    for (Cosmetikobjekt c : cosmetikslist){
                        if(c.getName().equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName())){
                            if(p.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(), sorter))
                                    && p.getPersistentDataContainer().get(new NamespacedKey(Monsterplugin.getPlugin(), sorter), PersistentDataType.INTEGER).equals(c.getCustommodeldata())){
                                p.getPersistentDataContainer().remove(new NamespacedKey(Monsterplugin.getPlugin(), sorter));
                            } else{
                                p.getPersistentDataContainer().set(new NamespacedKey(Monsterplugin.getPlugin(), sorter), PersistentDataType.INTEGER, c.getCustommodeldata());
                            }
                            Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> Bukkit.dispatchCommand(p,"cosmetic"), 1L);
                            sendpack(p);
                            break;
                        }
                    }
                    break;
            }
        }

    }

    @EventHandler
    public void plresp(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        sendpack(p);
    }

    @EventHandler
    public void onchanche(PlayerArmorChangeEvent e){
        Player p = e.getPlayer();
        sendpack(p);
    }
    @EventHandler
    public void onjoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        sendpack(p);
        for (Player other : Bukkit.getOnlinePlayers()){
            Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> sendFakeArmor(p,other), 1L);
        }
    }

    @EventHandler
    public void onswitch(PlayerItemHeldEvent e){
        Player p = e.getPlayer();
        if(e.getPlayer().getInventory().getItem(e.getNewSlot())==null && p.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(), "commicmainhand"), PersistentDataType.INTEGER)){
            p.updateInventory();
            Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> sendpack(p), 3L);
        }
    }

    private void sendpack(Player p){
        for (Player other : Bukkit.getOnlinePlayers()){
            Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> sendFakeArmor(other,p), 1L);
        }
    }

    private void sendFakeArmor(Player viewer, Player target) {
        //if u mod or so
        if(viewer.equals(target) && !target.getGameMode().equals(GameMode.SURVIVAL))return;

        try {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);

            // Set the target entity ID
            packet.getIntegers().write(0, target.getEntityId());

            // Create fake armor
            List<Pair<EnumWrappers.ItemSlot, ItemStack>> equipment = new ArrayList<>();
            equipment.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, builder(target,target.getInventory().getHelmet(), "comichead")));
            equipment.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, builder(target,target.getInventory().getChestplate(), "comicchest")));
            equipment.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, builder(target,target.getInventory().getLeggings(), "comiclegs")));
            equipment.add(new Pair<>(EnumWrappers.ItemSlot.FEET, builder(target,target.getInventory().getBoots(), "comicfeet")));
            //testthis
            if(target.getInventory().getItemInOffHand().getType().equals(Material.AIR))equipment.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, builder(target,target.getInventory().getItemInOffHand(), "comicoffhand")));
            if(target.getInventory().getItemInMainHand().getType().equals(Material.AIR))equipment.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, builder(target,target.getInventory().getItemInMainHand(), "commicmainhand")));


            // Write the fake armor to the packet
            packet.getSlotStackPairLists().write(0, equipment);

            // Send the packet to the viewer
            ProtocolLibrary.getProtocolManager().sendServerPacket(viewer, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ItemStack builder(Player p,ItemStack oldstack, String sorter){
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(), sorter), PersistentDataType.INTEGER))return oldstack;
        String custem ="testit";
        switch (sorter){
            case "comichead":
                custem="Kopf";
                break;
            case "comicchest":
                custem="Körper";
                break;
            case "comiclegs":
                custem="Beine";
                break;
            case "comicfeet":
                custem="Füße";
                break;
            case "commicmainhand":
                custem="Mainhand";
                break;
            case "comicoffhand":
                custem="Offhand";
                break;
        }
        for (Cosmetikobjekt c : cosmetikslist){
            if(c.getCustommodeldata()==p.getPersistentDataContainer().get(new NamespacedKey(Monsterplugin.getPlugin(), sorter), PersistentDataType.INTEGER) && c.getKörperteil().equals(custem)){
                if(c.getBedingung()!=null
                        && !p.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(), c.getBedingung()), PersistentDataType.BOOLEAN)
                        && !c.getBedingung().equals(p.getPersistentDataContainer().get(new NamespacedKey("rassensystem","rasse"), PersistentDataType.STRING)))return oldstack;
                ItemStack newstack = new ItemStack(Material.valueOf(c.getMaterial()));
                ItemMeta meta = newstack.getItemMeta();
                meta.setDisplayName(c.getName());
                meta.setLore(c.getBeschreibung());
                meta.setCustomModelData(c.getCustommodeldata());
                newstack.setItemMeta(meta);
                return newstack;
            }
        }
        return oldstack;
    }

    private void opencosmikmenu2(Player p,String sorter, int Seite){

        Inventory Loginventar = Bukkit.createInventory(p,54,"§lCosmetic-Menü > "+sorter);

        //creat the allways components
        ItemStack vorpfeil=new ItemStack(Material.ARROW);
        ItemMeta vorpfeil_meta=vorpfeil.getItemMeta();
        vorpfeil_meta.setDisplayName(""+Seite);
        vorpfeil.setItemMeta(vorpfeil_meta);

        ItemStack buch=new ItemStack(Material.BOOK);
        ItemMeta buch_meta= buch.getItemMeta();
        buch_meta.setDisplayName(ChatColor.YELLOW+"§lSeite: 1");
        buch.setItemMeta(buch_meta);

        ItemStack bestenbuch=new ItemStack(Material.BARRIER);
        ItemMeta bestenbuch_meta= bestenbuch.getItemMeta();
        bestenbuch_meta.setRarity(ItemRarity.EPIC);
        bestenbuch_meta.setDisplayName(ChatColor.GRAY+"§lZurück");
        bestenbuch.setItemMeta(bestenbuch_meta);

        Loginventar.setItem(51,vorpfeil);
        Loginventar.setItem(49,buch);
        Loginventar.setItem(45,bestenbuch);


        ArrayList<Cosmetikobjekt> cosmiklist = new ArrayList<>();
        for (Cosmetikobjekt c : cosmetikslist){
            if(c.getKörperteil().equalsIgnoreCase(sorter)){
                if(c.getBedingung()==null
                        || p.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(), c.getBedingung()), PersistentDataType.BOOLEAN)
                        || c.getBedingung().equals(p.getPersistentDataContainer().get(new NamespacedKey("rassensystem","rasse"), PersistentDataType.STRING))){
                    cosmiklist.add(c);
                }
            }
        }

        switch (sorter){
            case "Kopf":
                sorter="comichead";
                break;
            case "Körper":
                sorter="comicchest";
                break;
            case "Beine":
                sorter="comiclegs";
                break;
            case "Füße":
                sorter="comicfeet";
                break;
            case "Mainhand":
                sorter="commicmainhand";
                break;
            case "Offhand":
                sorter="comicoffhand";
                break;
        }

        for (Cosmetikobjekt c : cosmiklist){
            if(cosmiklist.indexOf(c)>=44*(Seite-1) && cosmiklist.indexOf(c)<=44*Seite){

                boolean aktivecosmetic = p.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(), sorter), PersistentDataType.INTEGER)
                        && p.getPersistentDataContainer().get(new NamespacedKey(Monsterplugin.getPlugin(), sorter), PersistentDataType.INTEGER).equals(c.getCustommodeldata());

                ItemStack item = new ItemStack(Material.valueOf(c.getMaterial()));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(c.getName());
                meta.setLore(new ArrayList<>(){{
                    c.getBeschreibung().forEach(this::add);
                    add("");
                    add(aktivecosmetic ? ChatColor.YELLOW+"Linksklick zum Absetzen" : ChatColor.YELLOW+"Linksklick zum Wechseln");
                }});
                if(aktivecosmetic){
                    meta.addEnchant(Enchantment.MENDING,1,true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                meta.setCustomModelData(c.getCustommodeldata());
                item.setItemMeta(meta);
                Loginventar.setItem(Loginventar.firstEmpty(),item);
            }
        }
        p.openInventory(Loginventar);
    }

}

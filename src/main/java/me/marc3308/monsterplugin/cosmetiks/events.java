package me.marc3308.monsterplugin.cosmetiks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
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

        //cosmikinf
        if(e.getView().getTitle().equalsIgnoreCase("§lCosmetik Menu")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            switch (e.getCurrentItem().getItemMeta().getDisplayName()){
                case "Kopf":
                    opencosmikmenu2(p,"Kopf",1);
                    break;
            }
        }

        if(e.getView().getTitle().equalsIgnoreCase("§lCosmetik Menu > "+"Kopf")) {
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
                    Bukkit.dispatchCommand(p,"cosmetikmenu");
                    p.getPersistentDataContainer().remove(new NamespacedKey(Monsterplugin.getPlugin(), sorter));
                    sendpack(p);
                    break;
                case BOOK:
                    opencosmikmenu2(p,e.getView().getTitle().split("> ")[1],1);
                    break;
                default:
                    for (Cosmetikobjekt c : cosmetikslist){
                        if(c.getName().equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName())){
                            p.getPersistentDataContainer().set(new NamespacedKey(Monsterplugin.getPlugin(), sorter), PersistentDataType.INTEGER, c.getCustommodeldata());
                            Bukkit.dispatchCommand(p,"cosmetikmenu");
                            sendpack(p);
                            break;
                        }
                    }
                    break;
            }
        }

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
    }

    private void sendpack(Player p){
        for (Player other : Bukkit.getOnlinePlayers()){
            Bukkit.getScheduler().runTaskLater(Monsterplugin.getPlugin(), () -> sendFakeArmor(other,p), 1L);
        }
    }

    private void sendFakeArmor(Player viewer, Player target) {
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
            equipment.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, builder(target,target.getInventory().getItemInMainHand(), "commicmainhand")));
            equipment.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, builder(target,target.getInventory().getItemInOffHand(), "comicoffhand")));

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
        for (Cosmetikobjekt c : cosmetikslist){
            if(c.getCustommodeldata()==p.getPersistentDataContainer().get(new NamespacedKey(Monsterplugin.getPlugin(), sorter), PersistentDataType.INTEGER)){
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

        Inventory Loginventar = Bukkit.createInventory(p,54,"§lCosmetik Menu > "+sorter);

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
        bestenbuch_meta.setDisplayName(ChatColor.GRAY+"§l Zurück");
        bestenbuch.setItemMeta(bestenbuch_meta);

        Loginventar.setItem(51,vorpfeil);
        Loginventar.setItem(49,buch);
        Loginventar.setItem(45,bestenbuch);


        ArrayList<Cosmetikobjekt> cosmiklist = new ArrayList<>();
        for (Cosmetikobjekt c : cosmetikslist){
            if(c.getKörperteil().equalsIgnoreCase(sorter)){
                if(c.getBedingung()==null || p.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(), c.getBedingung()), PersistentDataType.BOOLEAN)){
                    cosmiklist.add(c);
                }
            }
        }
        for (Cosmetikobjekt c : cosmiklist){
            if(cosmiklist.indexOf(c)>=44*(Seite-1) && cosmiklist.indexOf(c)<=44*Seite){
                ItemStack item = new ItemStack(Material.valueOf(c.getMaterial()));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(c.getName());
                meta.setLore(c.getBeschreibung());
                meta.setCustomModelData(c.getCustommodeldata());
                item.setItemMeta(meta);
                Loginventar.setItem(Loginventar.firstEmpty(),item);
            }
        }
        p.openInventory(Loginventar);
    }

}

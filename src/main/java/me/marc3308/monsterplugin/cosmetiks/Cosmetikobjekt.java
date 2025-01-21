package me.marc3308.monsterplugin.cosmetiks;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static me.marc3308.monsterplugin.Monsterplugin.cosmetikslist;

public class Cosmetikobjekt {

    private String name;
    private List<String> beschreibung;
    private int custommodeldata;
    private String material;
    private String Körperteil;
    private String bedingung;

    public Cosmetikobjekt(String name, List<String> beschreibung, int custommodeldata, String material,String Körperteil,String bedingung) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.custommodeldata = custommodeldata;
        this.material = material;
        this.Körperteil=Körperteil;
        this.bedingung=bedingung;
    }

    public String getBedingung() {
        return bedingung;
    }

    public String getKörperteil() {
        return Körperteil;
    }

    public String getName() {
        return name;
    }

    public List<String> getBeschreibung() {
        return beschreibung;
    }

    public int getCustommodeldata() {
        return custommodeldata;
    }

    public String getMaterial() {
        return material;
    }


    public void setBedingung(String bedingung) {
        this.bedingung = bedingung;
    }

    public void setKörperteil(String körperteil) {
        Körperteil = körperteil;
    }

    public void setBeschreibung(List<String> beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setCustommodeldata(int custommodeldata) {
        this.custommodeldata = custommodeldata;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void loadlist(){
        File cosfile = new File("plugins/KMS Plugins/DingeDiePassierenMussen","Cosmetiks.yml");
        FileConfiguration cosmetiks= YamlConfiguration.loadConfiguration(cosfile);
        cosmetikslist.clear();
        for (int i = 0; i < 300; i++) {
            if(cosmetiks.get(i+".Name")==null)break;
            cosmetikslist.add(new Cosmetikobjekt(
                    cosmetiks.getString(i+".Name")
                    ,cosmetiks.getStringList(i+".Beschreibung")
                    ,cosmetiks.getInt(i+".CustomModelData")
                    ,cosmetiks.getString(i+".Type")
                    ,cosmetiks.getString(i+".Körperteil")
                    ,cosmetiks.getString(i+".Bedingung")
            ));
        }
    }

    public static void savecosmis(){
        File cosfile = new File("plugins/KMS Plugins/DingeDiePassierenMussen","Cosmetiks.yml");
        FileConfiguration cosmetiks= YamlConfiguration.loadConfiguration(cosfile);
        for (int i = 0; i < cosmetikslist.size(); i++) {
            cosmetiks.set(i+".Name",cosmetikslist.get(i).getName());
            cosmetiks.set(i+".Beschreibung",cosmetikslist.get(i).getBeschreibung());
            cosmetiks.set(i+".CustomModelData",cosmetikslist.get(i).getCustommodeldata());
            cosmetiks.set(i+".Type",cosmetikslist.get(i).getMaterial());
            cosmetiks.set(i+".Körperteil",cosmetikslist.get(i).getKörperteil());
            cosmetiks.set(i+".Bedingung",cosmetikslist.get(i).getBedingung());
        }

        try {
            cosmetiks.save(cosfile);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}

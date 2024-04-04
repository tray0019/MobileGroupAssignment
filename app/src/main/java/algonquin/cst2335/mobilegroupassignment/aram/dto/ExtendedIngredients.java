package algonquin.cst2335.mobilegroupassignment.aram.dto;

import java.util.Arrays;

public class ExtendedIngredients {
    private long id;
    private String aisle;
    private String image;
    private String consistency;
    private String name;
    private String nameClean;
    private String original;
    private String originalName;
    private float amount;
    private String unit;
    private String[] meta;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getConsistency() {
        return consistency;
    }

    public void setConsistency(String consistency) {
        this.consistency = consistency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameClean() {
        return nameClean;
    }

    public void setNameClean(String nameClean) {
        this.nameClean = nameClean;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String[] getMeta() {
        return meta;
    }

    public void setMeta(String[] meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "ExtendedIngredients{" +
               "id=" + id +
               ", aisle='" + aisle + '\'' +
               ", image='" + image + '\'' +
               ", consistency='" + consistency + '\'' +
               ", name='" + name + '\'' +
               ", nameClean='" + nameClean + '\'' +
               ", original='" + original + '\'' +
               ", originalName='" + originalName + '\'' +
               ", amount=" + amount +
               ", unit='" + unit + '\'' +
               ", meta=" + Arrays.toString(meta) +
               '}';
    }
}

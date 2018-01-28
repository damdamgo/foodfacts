package com.villiers.foodfacts.Service.Modele;

/**
 * Classe du modele du produit
 * @author villiers
 */
public class Product {
    /**
     * barcode
     */
    private String barcodeNumber = null;
    /**
     * nom du produit
     */
    private String name = null;
    /**
     * ingrédients
     */
    private String ingredients = null;
    /**
     * energie
     */
    private String energyValue = null;
    /**
     * path de l'image
     */
    private String image = null;
    /**
     * url de l'image
     */
    private String imageURL = null;

    /**
     *
     *
     *
     * getter et setter
     *
     *
     *
     */

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(String barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getEnergyValue() {
        return energyValue;
    }

    public void setEnergyValue(String energyValue) {
        this.energyValue = energyValue;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * toString
     * @return
     */
    @Override
    public String toString() {
        return "Product{" +
                "barcodeNumber='" + barcodeNumber + '\'' +
                ", name='" + name + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", energyValue='" + energyValue + '\'' +
                ", image='" + image + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }

    /**
     * verifie si il manque des informations dans le modele
     * @return boolean
     */
    public boolean missingInformation() {
        return name==null || ingredients==null || energyValue == null || imageURL==null;
    }
}

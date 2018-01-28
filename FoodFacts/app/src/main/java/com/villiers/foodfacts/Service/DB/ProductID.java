package com.villiers.foodfacts.Service.DB;

import com.villiers.foodfacts.Service.Modele.Product;

import java.util.Observable;


/**
 * Classe qui permet de gérer un produit avec son ID dans la base de donneés
 * @author villiers
 */
public class ProductID extends Observable{
    /**
     * ID dans la base
     */
    public String ID;
    /**
     * produit
     */
    public Product product;

    /**
     * constructeur
     * @param ID
     * @param product
     */
    public ProductID(String ID, Product product) {
        this.ID = ID;
        this.product = product;
    }

    /**
     * getter de l'ID
     * @return ID ( String)
     */
    public String getID() {
        return ID;
    }

    /**
     * getter du produit
     * @return produit
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Fixe le nom du fichier contenant l'image du produit
     * @param path nom du fichier
     */
    public void setImagePath(String path){
        this.product.setImage(path);
        setChanged();
    }
}

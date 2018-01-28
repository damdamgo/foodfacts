package com.villiers.foodfacts.Service.Modele;

import android.databinding.ObservableField;

import com.villiers.foodfacts.Service.DB.ProductID;


/**
 * callback pour l'insertion d'un nouveau produit
 * @author villiers
 */
public interface CallbackAskProduct {
    /**
     * erreur pendant le traitement
     * @param code (code erreur)
     */
    void error(int code);

    /**
     * Le nouveau produit a été ajouté
     * @param product ProductID du nouveau produit
     */
    void success(ProductID product);

    /**
     * Produit existe déjà dans la base
     * @param product ProductID du produit déjà existant
     */
    void productExist(ProductID product);
}

package com.villiers.foodfacts.Service.API;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.villiers.foodfacts.Service.Modele.Product;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Permet de transformer un element JSON en une instance de classe Product
 * @author villiers
 */
public class ProductDeserializer implements JsonDeserializer<Product> {

    /**
     * Permet de passer d'un JSON à un objet
     * @param json
     * @param typeOfT
     * @param context
     * @return le produit crée
     * @throws JsonParseException
     */
    @Override
    public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Product product = null;
        if(json.isJsonObject()){
            JsonObject jsonOb = json.getAsJsonObject();
            if(jsonOb.has("status") && jsonOb.get("status").getAsInt()==1){

                product = new Product();
                JsonObject productJson = jsonOb.getAsJsonObject("product");

                if(productJson.has("product_name")) product.setName(productJson.get("product_name").getAsString());

                if(productJson.has("nutriments")){
                    JsonObject nutrimentsJson = productJson.getAsJsonObject("nutriments");
                    if(nutrimentsJson.has("energy")) product.setEnergyValue(nutrimentsJson.get("energy").getAsString());
                }

                if(productJson.has("ingredients_text")) product.setIngredients(productJson.get("ingredients_text").getAsString().replace('_',' '));

                if(productJson.has("selected_images")){
                    JsonObject imageProduct = productJson.getAsJsonObject("selected_images");
                    if(imageProduct.has("front")){
                        imageProduct = imageProduct.get("front").getAsJsonObject();
                        JsonObject typeImage = null;
                        if(imageProduct.has("display"))typeImage = (imageProduct.get("display").getAsJsonObject());
                        else if(imageProduct.has("small"))typeImage = (imageProduct.get("small").getAsJsonObject());
                        else if(imageProduct.has("thumb"))typeImage = (imageProduct.get("thumb").getAsJsonObject());
                        if (typeImage != null) {
                            Set<Map.Entry<String, JsonElement>> imageLoop = typeImage.entrySet();
                            if(!imageLoop.isEmpty()){
                                Iterator<Map.Entry<String, JsonElement>> itImage = imageLoop.iterator();
                                product.setImageURL(itImage.next().getValue().getAsString());
                                product.setImage(null);
                            }
                        }
                    }
                }
            }
        }
        if(product!=null && product.missingInformation())product=null;
        return product;
    }

    /**
     * Personnalisation d'un convertisseur
     * @return convertisseur pour retrofit
     */
    public static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Product.class, new ProductDeserializer());
        Gson myGson = gsonBuilder.create();
        return GsonConverterFactory.create(myGson);
    }
}
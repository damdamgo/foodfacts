package com.villiers.foodfacts.Service.API;

import com.villiers.foodfacts.Service.Modele.Product;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * API pour acceder à l'API de openFoodFacts
 * @author villiers
 */
public interface OpenFoodFactsService {

    @GET("product/{barcodeNumber}.json")
    Call<Product> productInformation(@Path("barcodeNumber") String barcodeNumber);
}

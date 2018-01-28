package com.villiers.foodfacts.View.Fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.villiers.foodfacts.R;
import com.villiers.foodfacts.Service.DB.ProductID;
import com.villiers.foodfacts.ViewModel.ViewModelProduct;
import com.villiers.foodfacts.databinding.ShowProductFragmentBinding;


/**
 * Fragment qui montre un produit spécifique
 * @author villiers
 */
public class FragmentShowProduct extends Fragment {

    /**
     * vue binding
     */
    private ShowProductFragmentBinding showProductFragmentBinding;
    /**
     * model
     */
    private ViewModelProduct model;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(ViewModelProduct.class);
        setUpObservalbe(model);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        showProductFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.show_product_fragment,container,false);
        showProductFragmentBinding.setProduct(null);
        return showProductFragmentBinding.getRoot();
    }

    /**
     * observe le produit qui est sélectionné
     * @param model
     */
    private void setUpObservalbe(ViewModelProduct model ){
        model.getProduct().observe(this, new Observer<ProductID>() {
            @Override
            public void onChanged(@Nullable ProductID productIDToShow) {
                showProductFragmentBinding.setProduct(productIDToShow);
                if(productIDToShow!=null && productIDToShow.getProduct().getImage()!=null && productIDToShow.getProduct().getImage().length()>0){
                    Bitmap bitmap = FragmentShowProduct.this.model.getBitmapByProductID(productIDToShow);
                    showProductFragmentBinding.imageViewProductImage.setImageBitmap(bitmap);
                }else{
                    showProductFragmentBinding.imageViewProductImage.setImageBitmap(null);
                }
            }
        });
    }
}

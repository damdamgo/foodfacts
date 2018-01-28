package com.villiers.foodfacts.View.Fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.villiers.foodfacts.R;
import com.villiers.foodfacts.Service.DB.ProductID;
import com.villiers.foodfacts.View.Adapter.ListProductsAdapter;
import com.villiers.foodfacts.ViewModel.ViewModelProduct;
import com.villiers.foodfacts.databinding.ListProductsFragmentBinding;

import java.util.List;


/**
 *Fragment de la liste des produits
 * @author villiers
 */
public class FragmentListProduct extends Fragment implements ListProductsAdapter.ClickOnItem{

    /**
     * vue binding
     */
    private ListProductsFragmentBinding listProductsFragmentBinding;
    /**
     * adapteur
     */
    private ListProductsAdapter listProductsAdapter;
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
        listProductsFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.list_products_fragment,container,false);
        listProductsFragmentBinding.setIsLoading(true);
        listProductsAdapter = new ListProductsAdapter(this);
        listProductsFragmentBinding.recyclerViewListProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        listProductsFragmentBinding.recyclerViewListProduct.setAdapter(listProductsAdapter);
        listProductsFragmentBinding.recyclerViewListProduct.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        listProductsFragmentBinding.setMoreData(model.getStillDataToLoad());
        if(model.getListProducts().getValue()!=null && model.getListProducts().getValue().size()>0){
            listProductsAdapter.updateData(model.getListProducts().getValue());
            listProductsFragmentBinding.setIsLoading(false);
        }
        listProductsFragmentBinding.buttonLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.loadMoreData();
            }
        });
        return listProductsFragmentBinding.getRoot();
    }

    /**
     * Observation de la liste des produits
     * @param model
     */
    private void setUpObservalbe(ViewModelProduct model ){
        model.getListProducts().observe(this, new Observer<List<ProductID>>() {
            @Override
            public void onChanged(@Nullable List<ProductID> productIDS) {
                if(productIDS!=null){
                    listProductsAdapter.updateData(productIDS);
                    listProductsFragmentBinding.setIsLoading(false);
                }
            }
        });
    }

    @Override
    public void itemSelected(ProductID productID) {
        model.setProductToShow(productID);
    }
}

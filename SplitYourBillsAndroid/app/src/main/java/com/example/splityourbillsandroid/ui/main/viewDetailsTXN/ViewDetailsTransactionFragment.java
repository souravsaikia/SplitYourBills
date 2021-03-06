package com.example.splityourbillsandroid.ui.main.viewDetailsTXN;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.splityourbillsandroid.R;
import com.example.splityourbillsandroid.data.models.spaces.response.AddNewSpaceResponse;
import com.example.splityourbillsandroid.data.models.transactions.PersonDetailsTXN;
import com.example.splityourbillsandroid.data.models.transactions.TransactionDetailsResponse;
import com.example.splityourbillsandroid.ui.main.MainViewModel;
import com.example.splityourbillsandroid.ui.main.spaces.SpacesAdapter;
import com.example.splityourbillsandroid.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class ViewDetailsTransactionFragment extends Fragment {

    private static final String TAG = "ViewDetailsTransactionF";

    Long spaceId = Long.valueOf(0);

    RecyclerView recyclerView;

    TransactionDetailsResponse txnDetails;

    @Inject
    ViewTXNDetailsAdapter adapter;

    @Inject
    MainViewModel viewModel;

    ImageView imageView;
    TextView totalAmount,perPersonAmount;


    @Inject
    public ViewDetailsTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_details_transaction, container, false);

        AndroidSupportInjection.inject(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        totalAmount = view.findViewById(R.id.total_amount);
        perPersonAmount = view.findViewById(R.id.per_person_amount);
        imageView = view.findViewById(R.id.image_view);


      //  spaceId = getArguments().getString(Constants.SPACE_ID);
      //  Long spaceIdLong = Long.valueOf(spaceId);


        subscribeForSpaceId();


        subscribeObserverForDetails();

        setUpRecyclerView(recyclerView, adapter);



        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        subscribeForSpaceId();
    }

    private void subscribeObserverForDetails() {
        viewModel.getTXNDetailsForSpace().observe(this, new Observer<TransactionDetailsResponse>() {
            @Override
            public void onChanged(TransactionDetailsResponse transactionDetailsResponse) {
                txnDetails = transactionDetailsResponse;
                List<PersonDetailsTXN> mList = txnDetails.getPersonDetailsTXN();
                Log.d(TAG, "onChanged: " + mList.size());
                adapter.setPerPersonAmount(txnDetails.getPerPerson());
                adapter.updateListData(mList);
                totalAmount.setText("Total Amount : " + txnDetails.getTotalAmount());
                perPersonAmount.setText("Everyone needs to pay : " + txnDetails.getPerPerson());
            }
        });
    }


    @Override
    public void onAttach(Context context) {

        AndroidSupportInjection.inject(this);

        super.onAttach(context);
    }

    private void setUpRecyclerView(RecyclerView recyclerView, ViewTXNDetailsAdapter adapter) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }

    private void subscribeForSpaceId() {
        viewModel.getSpaceDetails().observe(this, new Observer<AddNewSpaceResponse>() {
            @Override
            public void onChanged(AddNewSpaceResponse addNewSpaceResponse) {
                spaceId = addNewSpaceResponse.getSpaceId();
                Log.d(TAG, "onChanged: spaceId : " + spaceId);
                //TODO doing the network call here guyms
                viewModel.getTXNDetailsForSpaceId(spaceId);
            }
        });
    }



}
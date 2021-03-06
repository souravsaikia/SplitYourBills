package com.example.splityourbillsandroid.ui.main.spaces;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.splityourbillsandroid.R;
import com.example.splityourbillsandroid.data.models.spaces.response.AddNewSpaceResponse;
import com.example.splityourbillsandroid.data.models.spaces.response.SpaceResponse;
import com.example.splityourbillsandroid.ui.main.MainViewModel;
import com.example.splityourbillsandroid.ui.main.createSpace.CreateSpaceFragment;
import com.example.splityourbillsandroid.ui.main.spaceDetails.SpaceDetailsFragment;
import com.example.splityourbillsandroid.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class SpacesFragment extends Fragment {


    private static final String TAG = "SpacesFragment";


    @Inject
    MainViewModel viewModel;

    @Inject
    SpacesAdapter adapter;

    @Inject
    SpaceDetailsFragment spaceDetailsFragment;

    @Inject
    CreateSpaceFragment createSpaceFragment;


    List<SpaceResponse> mList;

    RecyclerView recyclerView;
    RelativeLayout linearLayout;
    FloatingActionButton fabNewSpace;
    ImageView imageView;




    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            Log.d(TAG, "onClick: POsition ::: " + position);


            //Convert it to long in the next fragment
            String spaceId = String.valueOf(mList.get(position).getSpaceId());

            Bundle bundle = new Bundle();
            bundle.putString(Constants.SPACE_ID, spaceId);

            spaceDetailsFragment.setArguments(bundle);

            initializeFragments(spaceDetailsFragment);

            String spaceIdTemp =  mList.get(position).getSpaceId();
            String spaceName = mList.get(position).getSpaceName();
            AddNewSpaceResponse response = new AddNewSpaceResponse(Long.valueOf(spaceIdTemp),spaceName);
            viewModel.setSpaceDetails(response);


        }
    };


    private void initializeFragments(Fragment frag) {
        String backStateName = frag.getClass().toString();
        //Log.d(TAG, "onBtnOtpLoginClicked: " + backStateName);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        //   transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.replace(R.id.frame_layout_main, frag);
        transaction.addToBackStack(backStateName);
        transaction.commit();
    }




    @Inject
    public SpacesFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_spaces, container, false);

        AndroidSupportInjection.inject(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        linearLayout = view.findViewById(R.id.linear_layout);
        fabNewSpace = view.findViewById(R.id.fab);
        imageView = view.findViewById(R.id.image_view);


        mList = new ArrayList<>();

        Log.d(TAG, "onCreateView: Token : " + viewModel.getAccessToken());

        viewModel.getSpaceById();

        setUpRecyclerView(recyclerView, adapter);

        //
        subscribeForSpacesStatus();
        subscribeForSpacesData();

        fabNewSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeFragments(createSpaceFragment);
            }
        });





        return view;
    }


    private void subscribeForSpacesData() {
        viewModel.getSpaceResponse().observe(this, new Observer<List<SpaceResponse>>() {
            @Override
            public void onChanged(List<SpaceResponse> addNewSpaceRespons) {

                mList = addNewSpaceRespons;
                if (mList.size()==0){
                    recyclerView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                }
                Log.d(TAG, "onChanged: " + mList.size());
                adapter.updateListData(mList);

            }
        });
    }

    private void subscribeForSpacesStatus() {

        viewModel.getStatusSpaceResponse().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                int x = integer;
                if (x == 200) {
                  //  showToast("Data Retrieved");
                 //   progressBar.setVisibility(View.GONE);
                } else if (x == 401){
                    showToast("Not Authenticated");
                    //TODO Redirect to the authentication page
                }
                else if (x == 500)
                    showToast("Somewhere,Somehow Something went show");
               // progressBar.setVisibility(View.GONE);
            }
        });

    }


    private void setUpRecyclerView(RecyclerView recyclerView, SpacesAdapter adapter) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(onClickListener);


    }

    private void showToast(String msg) {
        Snackbar snackbar = Snackbar.make(linearLayout, msg, Snackbar.LENGTH_INDEFINITE).
                setDuration(2000);
        snackbar.show();
    }




    @Override
    public void onAttach(Context context) {

        AndroidSupportInjection.inject(this);

        super.onAttach(context);
    }
}
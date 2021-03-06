package com.example.splityourbillsandroid.ui.main.SpaceMembers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splityourbillsandroid.R;
import com.example.splityourbillsandroid.data.models.spaces.response.SpaceMembersResponse;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SpaceMembersAdapter extends RecyclerView.Adapter<SpaceMembersAdapter.viewholder> {


    private ArrayList<SpaceMembersResponse> mList;
    private View.OnClickListener onItemClickListener;

    private static final String TAG = "HomeAdapter";
    int invites = 0;


    @Inject
    public SpaceMembersAdapter() {
        mList = new ArrayList<>();
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_space_members, parent, false);
            return new viewholder(view);

    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getUserId()!=-1)
            return 0;
        return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

//
//        String str = mList.get(position).getUserDetails().getUserName() + " paid " + mList.get(position).getAmount().toString();
//        holder.name.setText(str);
//        holder.description.setText(mList.get(position).getDescription());
//
            if (mList.get(position).getUserId()!=-1) {
                holder.name.setText(mList.get(position).getUserDetails().getUserName());
                holder.description.setText(mList.get(position).getUserDetails().getUserPhone());
            }else if (mList.get(position).getInviteId()!=-1){
                holder.name.setText(mList.get(position).getInvites().getName());
                holder.description.setText(mList.get(position).getInvites().getPhone());
                invites++;
                Log.d(TAG, "onBindViewHolder: invites : " + invites);
                holder.chip.setVisibility(View.VISIBLE);
            }
    }

    @Override
    public int getItemCount() {

//        int size  = 0 ;
//        for (SpaceMembersResponse s : mList){
//            if (s.getUserId()!=-1)
//                size++;
//        }
//        invites = mList.size()-size;
//        return size;
        return mList.size();
    }

    public int getInviteCount(){
        return invites;
    }


    public void updateListData(List<SpaceMembersResponse> data) {
        mList.clear();
        this.mList.addAll(data);
        notifyDataSetChanged();

    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView name,description;
        Chip chip;

        public viewholder(@NonNull View itemView) {

            super(itemView);

            itemView.setTag(this);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.phone);
            chip = itemView.findViewById(R.id.chip);

            itemView.setOnClickListener(onItemClickListener);

        }

    }
}

package com.kopotron.bafvideoplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.kopotron.bafvideoplayer.Utilities.deleteRecursive;
import static com.kopotron.bafvideoplayer.Utilities.loadUserInfo;
import static com.kopotron.bafvideoplayer.Utilities.saveAircraftData;


public class AircraftViewAdapter extends RecyclerView.Adapter<AircraftViewAdapter.ViewHolder> {
    private ArrayList<AircraftData> aircraftDataArrayList;
    private Context mcontext;

    public AircraftViewAdapter(ArrayList<AircraftData> recyclerDataArrayList, Context mcontext) {
        this.aircraftDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
        Log.d("resume","aircraft view adapter");
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_aircraft_item, parent, false);
//        return new AircraftViewHolder(view);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem=layoutInflater.inflate(R.layout.view_aircraft_item,parent,false);
        AircraftViewAdapter.ViewHolder viewHolder=new AircraftViewAdapter.ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserInfo userInfo=loadUserInfo(mcontext);
        if(userInfo.getLoggedIn())
            holder.floatingActionButtonCross.setVisibility(View.VISIBLE);
        else
            holder.floatingActionButtonCross.setVisibility(View.INVISIBLE);

        // Set the data to textview and imageview.
        AircraftData aircraftDataData = aircraftDataArrayList.get(position);
        holder.iconTV.setText(aircraftDataData.getTitle());

        if(aircraftDataData.getAircraftImage()!=null)
        holder.iconIV.setImageURI(Uri.fromFile(aircraftDataData.getAircraftImage()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AircraftContentActivity.class);
                intent.putExtra("aircraft",aircraftDataData.getTitle() );
                view.getContext().startActivity(intent);

            }
        });

        holder.floatingActionButtonCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecursive(aircraftDataData.getAircraftDirectories().getMovieDirectory());
                deleteRecursive(aircraftDataData.getAircraftDirectories().getPictureDirectory());
                aircraftDataArrayList.remove(position);
                saveAircraftData(mcontext,aircraftDataArrayList);
                AircraftViewAdapter.this.notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return aircraftDataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        private TextView iconTV;
        private ImageView iconIV;
        private FloatingActionButton floatingActionButtonCross=null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconTV = itemView.findViewById(R.id.TV_AC_TITLE);
            iconIV = itemView.findViewById(R.id.IV_AC_THUMB);
            cardView=itemView.findViewById(R.id.CV_AIRCRAFT);
            floatingActionButtonCross=itemView.findViewById(R.id.FAB_CROSS);

        }
    }
}

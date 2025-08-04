package com.kopotron.bafvideoplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import static com.kopotron.bafvideoplayer.Utilities.saveImageList;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ViewHolder> {
    private ArrayList<ImageData> imageDataArrayList;
    private Context mContext;
    ArrayList<String> imageList=new ArrayList<>();;

    public ImageViewAdapter(ArrayList<ImageData> imageDataArrayList,Context mContext){
        this.imageDataArrayList=imageDataArrayList;
        this.mContext=mContext;
        for(int i=0;i<imageDataArrayList.size();i++)
        {
            this.imageList.add(imageDataArrayList.get(i).getImagePath());
        }
    }

    @NonNull
    @Override
    public ImageViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.view_aircraft_image,parent,false);
        ImageViewAdapter.ViewHolder viewHolder = new ImageViewAdapter.ViewHolder(listItem);
        //imageList = loadImageList(mContext);
       // Log.d("load from shared",imageList.toString());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewAdapter.ViewHolder holder, int position) {
        ImageData imageData = imageDataArrayList.get(position);

        holder.tvTitle.setText(imageData.getTitle());
        UserInfo userInfo=loadUserInfo(mContext);
        if(userInfo.getLoggedIn())
            holder.floatingActionButtonCross.setVisibility(View.VISIBLE);
        else
            holder.floatingActionButtonCross.setVisibility(View.INVISIBLE);

        Bitmap bitmap = BitmapFactory.decodeFile(imageData.getImagePath());
        holder.ivImage.setImageBitmap(bitmap);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), VideoPlayer.class);
//                    intent.putExtra("video",videoData.getVideoPath() );
//                    v.getContext().startActivity(intent);
                Intent imageIntent = new Intent(v.getContext(), ImagePlayerPlayList.class);
                imageIntent.putStringArrayListExtra("imageList", (ArrayList<String>) imageList);
                imageIntent.putExtra("index",imageList.indexOf(imageData.getImagePath()));
                v.getContext().startActivity(imageIntent);
            }
        });

        holder.floatingActionButtonCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageList.remove(imageList.indexOf(imageData.getImagePath()));
                deleteRecursive(imageData.getImageFile());
                imageDataArrayList.remove(position);
                saveImageList(mContext,imageList);
                ImageViewAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageDataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        private TextView tvTitle;
        private ImageView ivImage;
        private FloatingActionButton floatingActionButtonCross=null;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView=itemView.findViewById(R.id.CV_IMAGE);
            tvTitle=itemView.findViewById(R.id.TV_IMAGE_TITLE);
            ivImage=itemView.findViewById(R.id.IV_IMAGE_THUMB);
            floatingActionButtonCross=itemView.findViewById(R.id.FAB_CROSS_IMAGE);

        }
    }
}

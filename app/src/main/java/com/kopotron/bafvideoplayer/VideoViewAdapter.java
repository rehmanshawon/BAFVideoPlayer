package com.kopotron.bafvideoplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
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
import static com.kopotron.bafvideoplayer.Utilities.loadMovieList;
import static com.kopotron.bafvideoplayer.Utilities.loadUserInfo;
import static com.kopotron.bafvideoplayer.Utilities.saveMovieList;

public class VideoViewAdapter extends RecyclerView.Adapter<VideoViewAdapter.ViewHolder> {
    private ArrayList<VideoData> videoDataArrayList;
    private Context mContext;
    ArrayList<String> movieList;

    public VideoViewAdapter(ArrayList<VideoData> videoDataArrayList,Context mContext){
        this.videoDataArrayList=videoDataArrayList;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public VideoViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.view_aircraft_video,parent,false);
        VideoViewAdapter.ViewHolder viewHolder = new VideoViewAdapter.ViewHolder(listItem);
        movieList = loadMovieList(mContext);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewAdapter.ViewHolder holder, int position) {
            VideoData videoData = videoDataArrayList.get(position);
            holder.tvTitle.setText(videoData.getTitle());
            UserInfo userInfo=loadUserInfo(mContext);
            if(userInfo.getLoggedIn())
                holder.floatingActionButtonCross.setVisibility(View.VISIBLE);
            else
                holder.floatingActionButtonCross.setVisibility(View.INVISIBLE);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoData.getVideoPath());

            Bitmap bitmap = retriever.getFrameAtTime(2000000,MediaMetadataRetriever.OPTION_NEXT_SYNC);
            holder.ivVideo.setImageBitmap(bitmap);


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), VideoPlayer.class);
//                    intent.putExtra("video",videoData.getVideoPath() );
//                    v.getContext().startActivity(intent);
                    Intent videoIntent = new Intent(v.getContext(), VideoPlayerPlayList.class);
                    videoIntent.putStringArrayListExtra("videoList", (ArrayList<String>) movieList);
                    videoIntent.putExtra("index",movieList.indexOf(videoData.getVideoPath()));
                    v.getContext().startActivity(videoIntent);
                }
            });

        holder.floatingActionButtonCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieList.remove(movieList.indexOf(videoData.getVideoPath()));
               deleteRecursive(videoData.getVideoFile());
               videoDataArrayList.remove(position);
               saveMovieList(mContext,movieList);
                VideoViewAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoDataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        private TextView tvTitle;
        private ImageView ivVideo;
        private FloatingActionButton floatingActionButtonCross=null;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView=itemView.findViewById(R.id.CV_VIDEO);
            tvTitle=itemView.findViewById(R.id.TV_VIDEO_TITLE);
            ivVideo=itemView.findViewById(R.id.IV_VIDEO_THUMB);
            floatingActionButtonCross=itemView.findViewById(R.id.FAB_CROSS_VIDEO);

        }
    }
}

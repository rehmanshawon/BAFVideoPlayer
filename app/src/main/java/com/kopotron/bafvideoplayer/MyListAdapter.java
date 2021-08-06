package com.kopotron.bafvideoplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private MyListData[] listdata;

    // RecyclerView recyclerView;
    public MyListAdapter(MyListData[] myListData) {
        this.listdata = myListData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem=layoutInflater.inflate(R.layout.list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyListData myListData = listdata[position];
        holder.textView.setText(listdata[position].getDescription());
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
        // Set data source to retriever.
        // From your code, you might want to use your 'String path' here.

        retriever.setDataSource(listdata[position].getImgPath());
       // MediaPlayer mp = MediaPlayer.create(MainActivity.getAppContext(), Uri.parse(listdata[position].getImgPath()));
       // int millis = mp.getDuration();
        //mp.seekTo(2000);
        Log.d("Frmae", listdata[position].getImgPath());
       // mp.release();
        Bitmap bitmap = retriever.getFrameAtTime(2000000,MediaMetadataRetriever.OPTION_NEXT_SYNC);
        //Bitmap bitmap = retriever.getFrameAtIndex(0);
        //Bitmap bitmap = createVideoThumbNail(Uri.parse(listdata[position].getImgPath()));
        holder.imageView.setImageBitmap(bitmap);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VideoPlayer.class);
                intent.putExtra("video",myListData.getImgPath() );
                view.getContext().startActivity(intent);
                //Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public Bitmap createVideoThumbNail(Uri path){
        return ThumbnailUtils.createVideoThumbnail(path.toString(), MediaStore.Video.Thumbnails.MINI_KIND);
        // return loadThumbnail(path, Size, null);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}

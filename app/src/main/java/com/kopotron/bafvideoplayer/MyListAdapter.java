package com.kopotron.bafvideoplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        View listItem=layoutInflater.inflate(R.layout.card_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyListData myListData = listdata[position];

        holder.textView.setText(listdata[position].getDescription());

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(listdata[position].getImgPath());

        Bitmap bitmap = retriever.getFrameAtTime(2000000,MediaMetadataRetriever.OPTION_NEXT_SYNC);
        holder.imageView.setImageBitmap(bitmap);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VideoPlayer.class);
                intent.putExtra("video",myListData.getImgPath() );
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public Bitmap createVideoThumbNail(Uri path){
        return ThumbnailUtils.createVideoThumbnail(path.toString(), MediaStore.Video.Thumbnails.MINI_KIND);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.id_VideoImage);
            this.textView = (TextView) itemView.findViewById(R.id.id_Title);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linerLayout);

        }
    }
}

package com.allan.kostku.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allan.kostku.R;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapterKost extends SliderViewAdapter<SliderAdapterKost.SliderAdapterVH>{

    private Context context;
    private String img1, img2;


    public SliderAdapterKost(Context context, String img1, String img2) {
        this.context = context;
        this.img1 = img1;
        this.img2 = img2;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        if (img1 != null && img2 != null) {
            switch (position) {
                case 0:
                    viewHolder.textViewDescription.setText("Boarding House");
                    viewHolder.textViewDescription.setTextSize(16);
                    viewHolder.textViewDescription.setTextColor(Color.WHITE);
                    viewHolder.imageGifContainer.setVisibility(View.GONE);
                    Glide.with(viewHolder.itemView)
                            .load(img1)
                            .fitCenter()
                            .into(viewHolder.imageViewBackground);
                    break;
                case 1:
                    viewHolder.textViewDescription.setText("Car Parking Area");
                    viewHolder.textViewDescription.setTextSize(16);
                    viewHolder.textViewDescription.setTextColor(Color.WHITE);
                    viewHolder.imageGifContainer.setVisibility(View.GONE);
                    Glide.with(viewHolder.itemView)
                            .load(img2)
                            .fitCenter()
                            .into(viewHolder.imageViewBackground);
                    break;
            }
        } else if (img1 == null && img2 !=null) {
            switch (position) {
                case 1:
                    viewHolder.textViewDescription.setText("Car Parking Area");
                    viewHolder.textViewDescription.setTextSize(16);
                    viewHolder.textViewDescription.setTextColor(Color.WHITE);
                    viewHolder.imageGifContainer.setVisibility(View.GONE);
                    Glide.with(viewHolder.itemView)
                            .load(img2)
                            .fitCenter()
                            .into(viewHolder.imageViewBackground);
                    break;
                default:
                    viewHolder.textViewDescription.setTextSize(29);
                    viewHolder.textViewDescription.setTextColor(Color.WHITE);
                    viewHolder.textViewDescription.setText("No Image");
                    viewHolder.imageGifContainer.setVisibility(View.VISIBLE);
                    Glide.with(viewHolder.itemView)
                            .load(R.drawable.ic_wrong)
                            .fitCenter()
                            .into(viewHolder.imageViewBackground);
                    Glide.with(viewHolder.itemView)
                            .asGif()
                            .load(R.drawable.alfamart)
                            .into(viewHolder.imageGifContainer);
                    break;
            }

        } else if (img1!= null && img2 == null) {
            switch (position) {
                case 0:
                    viewHolder.textViewDescription.setText("Boarding House");
                    viewHolder.textViewDescription.setTextSize(16);
                    viewHolder.textViewDescription.setTextColor(Color.WHITE);
                    viewHolder.imageGifContainer.setVisibility(View.GONE);
                    Glide.with(viewHolder.itemView)
                            .load(img1)
                            .fitCenter()
                            .into(viewHolder.imageViewBackground);
                    break;
                default:
                    viewHolder.textViewDescription.setTextSize(29);
                    viewHolder.textViewDescription.setTextColor(Color.WHITE);
                    viewHolder.textViewDescription.setText("No Image");
                    viewHolder.imageGifContainer.setVisibility(View.VISIBLE);
                    Glide.with(viewHolder.itemView)
                            .load(R.drawable.ic_wrong)
                            .fitCenter()
                            .into(viewHolder.imageViewBackground);
                    Glide.with(viewHolder.itemView)
                            .asGif()
                            .load(R.drawable.alfamart)
                            .into(viewHolder.imageGifContainer);
                    break;
            }
        } else if(img1 == null && img2 ==null){
            switch (position) {
                default:
                    viewHolder.textViewDescription.setTextSize(29);
                    viewHolder.textViewDescription.setTextColor(Color.WHITE);
                    viewHolder.textViewDescription.setText("No Image");
                    viewHolder.imageGifContainer.setVisibility(View.VISIBLE);
                    Glide.with(viewHolder.itemView)
                            .load(R.drawable.ic_wrong)
                            .fitCenter()
                            .into(viewHolder.imageViewBackground);
                    Glide.with(viewHolder.itemView)
                            .asGif()
                            .load(R.drawable.alfamart)
                            .into(viewHolder.imageGifContainer);
                    break;
            }
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}

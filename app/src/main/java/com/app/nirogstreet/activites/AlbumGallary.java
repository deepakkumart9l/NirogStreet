package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.ImageLoader;
import com.app.nirogstreet.uttil.TouchImageViewComplex;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Preeti on 07-11-2017.
 */
public class AlbumGallary extends Activity { List<String> listImages;
        int pos;
        ProgressBar progressBar;
        GridView horizontalListView;
        ViewPager mViewPager;
        HorizontalScrollView horizontalListView2;


public static AlbumGallary newInstance(ArrayList<Image> listImages,
        int position) {
        AlbumGallary fullImageFrag = new AlbumGallary();
        Bundle bundle = new Bundle();
        bundle.putSerializable("listImages", listImages);
        bundle.putInt("pos", position);
        // fullImageFrag.setArguments(bundle);
        return fullImageFrag;
        }

  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            // dish = bundle.getParcelable("dish");
            pos = bundle.getInt("pos");
            listImages = (ArrayList<Image>) bundle.getSerializable("listImages");
        }
    }*/

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        Intent intent = getIntent();

        pos = intent.getIntExtra("position", -1);
        listImages = (List<String>) intent.getSerializableExtra("images");
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        finish();
        }
        });
        try {
        ImageView cancel = (ImageView) findViewById(R.id.cancel);
        DrawableCompat.setTint(cancel.getDrawable(), ContextCompat.getColor(AlbumGallary.this, R.color.gray_2nd));
        } catch (Exception e) {
        e.printStackTrace();
        }
        //progressBar = (ProgressBar) findViewById(R.id.feedProgress);
        horizontalListView = (GridView)
        findViewById(R.id.horizontalListView);
        horizontalListView2 = (HorizontalScrollView)
        findViewById(R.id.horizontalListViewTop);
        if (listImages.size() > 1) {
        int filterCount = listImages.size();
        int width = (int) (filterCount * getResources().getDimension(
        R.dimen.width_filter_icon));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
        LinearLayout.LayoutParams.WRAP_CONTENT);

        horizontalListView.setLayoutParams(params);
        horizontalListView.setNumColumns(filterCount);

        horizontalListView.setAdapter(new ImageAAdapter(AlbumGallary.this,
        listImages));

        } else {
        horizontalListView2.setVisibility(View.GONE);
        }

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TouchImageAdapter());

        mViewPager.setCurrentItem(pos);

        }

/* @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.full_image_, container, false);

    // ((GalleryActivity) context).setButtons(false);
     view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
             getFragmentManager().popBackStack();

         }
     });
     imageLoader = new ImageLoader(context);
     progressBar = (ProgressBar) view.findViewById(R.id.feedProgress);
     horizontalListView = (GridView) view
             .findViewById(R.id.horizontalListView);
     horizontalListView2 = (HorizontalScrollView) view
             .findViewById(R.id.horizontalListViewTop);
     if (listImages.size() > 1) {
         int filterCount = listImages.size();
         int width = (int) (filterCount * getResources().getDimension(
                 R.dimen.width_filter_icon));

         LayoutParams params = new LayoutParams(width,
                 LayoutParams.WRAP_CONTENT);

         horizontalListView.setLayoutParams(params);
         horizontalListView.setNumColumns(filterCount);

         horizontalListView.setAdapter(new ImageAAdapter(getActivity(),
                 listImages));

     } else {
         horizontalListView2.setVisibility(View.GONE);
     }

     mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
     mViewPager.setAdapter(new TouchImageAdapter());

     mViewPager.setCurrentItem(pos);

//		((AppCompatActivity)getActivity()).getSupportActionBar().hide();
     return view;
 }
*/
class TouchImageAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return listImages.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        TouchImageViewComplex img = new TouchImageViewComplex(
                container.getContext());
        Glide.with(AlbumGallary.this)
                .load(listImages.get(position)) // Uri of the picture
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .crossFade()
                .override(100, 100)
                .into( img);

        container.addView(img,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}

public class ImageAAdapter extends BaseAdapter {

    Context mContext;
    List<String> data;

    public ImageAAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.data = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }


    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class ViewHolder {

        public ImageView imgView;
        public ProgressBar progressBar;

    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // Utilities.log("convert null " + position);
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.image_, null);
            //    holder.progressBar = (ProgressBar) convertView.findViewById(R.id.feedProgress1);
            holder.imgView = (ImageView) convertView

                    .findViewById(R.id.imageView1);

            convertView.setTag(holder);
        } else {
            // Utilities.log("convert not null " + position);
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position);
            }
        });


            /*imageLoader.DisplayImage(AlbumGallary.this
                    ,data.get(position), holder.imgView,null , 150, 150, R.drawable.bannerdummy);*/
        Glide.with(AlbumGallary.this)
                .load(data.get(position)) // Uri of the picture
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .crossFade()
                .override(100, 100)
                .into( holder.imgView);
        holder.imgView.setVisibility(View.VISIBLE);

        return convertView;
    }
}

}


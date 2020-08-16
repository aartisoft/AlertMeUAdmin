package in.alertmeu.a4a.adapter;

import android.content.Context;
import android.os.Parcelable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import in.alertmeu.a4a.R;
import in.alertmeu.a4a.imageUtils.ImageLoader;
import in.alertmeu.a4a.models.ImageModel;


public class SlidingImage_Adapter extends PagerAdapter {


    private List<ImageModel> data;
    private LayoutInflater inflater;
    private Context context;
    ImageModel current;

    public SlidingImage_Adapter(Context context, List<ImageModel> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
        current = data.get(position);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final TextView imageDescription = (TextView) imageLayout.findViewById(R.id.imageDescription);
        ImageLoader imageLoader = new ImageLoader(context);
        imageLoader.DisplayImage(current.getImage_path(), imageView);
        imageDescription.setText(current.getImage_description());
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
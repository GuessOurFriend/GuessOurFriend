package inc.guessourfriend.SupportingClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Laura on 11/14/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] imageURLs = null;
    private List<MutualFriend> poolFriends = null;

    public ImageAdapter(Context c, String[] imageURLs) {
        mContext = c;
        this.imageURLs = imageURLs;
    }

    public ImageAdapter(Context c, List<MutualFriend> poolFriends) {
        mContext = c;
        this.poolFriends = poolFriends;
    }

    @Override
    public int getCount() {
        return (imageURLs != null) ? imageURLs.length : poolFriends.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(width/5 - 10, width/5 - 10));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setCropToPadding(true);
            imageView.setBackgroundColor(Color.parseColor("#80ffffff"));
            if (poolFriends != null && poolFriends.get(position).isGrayedOut) {
                imageView.setColorFilter(Color.parseColor("#88000000"));
            }
        } else {
            imageView = (ImageView) convertView;
        }

        String imageUrl = (imageURLs != null) ? imageURLs[position] : poolFriends.get(position).profilePicture;
        new DownloadImageTask(imageView).execute(imageUrl);
        return imageView;
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

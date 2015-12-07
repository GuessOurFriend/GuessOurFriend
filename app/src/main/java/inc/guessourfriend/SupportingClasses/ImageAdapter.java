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

/**
 * Created by Laura on 11/14/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] imageURLs;
//    private Bitmap[] mThumbIds;

    public ImageAdapter(Context c, String[] imageURLs) {
        mContext = c;
        this.imageURLs = imageURLs;
//        mThumbIds = urlsToBitmaps(imageURLs);
    }

    @Override
    public int getCount() {
//        return mThumbIds.length;
        return imageURLs.length;
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
            imageView.setBackgroundColor(Color.parseColor("#80ffffff"));
        } else {
            imageView = (ImageView) convertView;
        }


//        imageView.setImageResource(mThumbIds[position]);
//        imageView.setImageBitmap(mThumbIds[position]);
        new DownloadImageTask(imageView).execute(imageURLs[position]);
        return imageView;
    }

//    public Bitmap[] urlsToBitmaps(String[] urlList) {
//        Bitmap[] bitmaps = new Bitmap[urlList.length];
//        for (int i = 0; i < urlList.length; i++) {
//            Bitmap addBitmap = new DownloadImageTask()
//        }
//        return null;
//    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}

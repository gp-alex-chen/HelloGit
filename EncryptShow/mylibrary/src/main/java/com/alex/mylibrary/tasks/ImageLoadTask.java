package com.alex.mylibrary.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.alex.mylibrary.cache.FileCache;
import com.alex.mylibrary.utils.HttpTools;

import java.lang.ref.WeakReference;

/**
 * Created by
 * Author: alex
 * Email:
 * Date: 15-10-12.
 */
public class ImageLoadTask extends AsyncTask<String,Integer,Bitmap> {
    private int requestWidth;
    private int requestHeight;
    /**
     * 使用弱引用来进行 ImageView 对象的引用，当UI销毁 任务不再使用 ImageView
     */
    private final WeakReference<ImageView> imageViewWeakReference;

    /**
     * 异步任务的构造
     * @param imageView 需要显示的图片
     * @param reqWidth 请求的宽  0代表显示原始图片
     * @param reqHeight 请求的高度 0代表显示原始图片
     */
    public ImageLoadTask(ImageView imageView,int reqWidth,int reqHeight){
        imageViewWeakReference = new WeakReference<>(imageView);
        requestWidth = reqWidth;
        requestHeight = reqHeight;
    }




    @Override
    protected Bitmap doInBackground(String... params) {

        Bitmap ret = null;
        if (params != null && params.length > 0){
            String url = params[0];
            // 获取 url 对应的文件缓存
            byte[] data = FileCache.getInstance().load(url);
            if (data != null) {
                // 有文件数据 不需要联网
                ret = getBitmapUseOption(null, data);

            } else {
                data = HttpTools.doGet(url);
                ret = getBitmapUseOption(null, data);
                FileCache.getInstance().save(url, data);
                data = null;
                // 联网下载图片
//                try {
//                    URL u = new URL(url);
//                    HttpURLConnection connection = (HttpURLConnection) u.openConnection();
//                    connection.connect();
//                    int code = connection.getResponseCode();
//                    if (code == 200){
//

//                        // 给 data 赋值
//                            ByteArrayOutputStream bout = null;
//                            InputStream fin = null;
//                            try {
//                                fin = connection.getInputStream();
//
//                                bout = new ByteArrayOutputStream();
//
//                                byte[] buf = new byte[128];
//                                int len;
//                                while (true){
//                                    len = fin.read(buf);
//                                    if (len == -1){
//                                        break;
//                                    }
//                                    bout.write(buf,0,len);
//                                }
//                                // buf 比需要进行 = null 的操作
//                                // 减少内存溢出的可能性， 让 gc 可以回收
//                                buf = null;
//
//                                data = bout.toByteArray();
//
//                                // 创建 options
//                                ret = getBitmapUseOption(null, data);
//
//
//
//                                FileCache.getInstance().save(url,data);
//                                data = null;
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            } finally {
//                                if (fin != null){
//                                    try {
//                                        fin.close();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                                if (bout != null){
//                                    try {
//                                        bout.close();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }

//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }

        }
        return ret;
    }

    private Bitmap getBitmapUseOption(Bitmap ret, byte[] data) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置 inJustDecodeBounds 来控制解码器 只进行图片宽高的获取 不加载，不占内存
        options.inJustDecodeBounds = true;
        // 使用Options 参数设置解码方式
        ret = BitmapFactory.decodeByteArray(data,0,data.length,options);


        // ----- 根据图片真是尺寸雨当前需要显示的尺寸进行计算生成采样率

        // 准备显示在手机上
        int reqW = requestWidth;
        int reqH = requestHeight;

        // 计算设置图片采样率
        options.inSampleSize = calculateInSampleSize(options,reqW,reqH);// 宽度的 1／32

        // 开放解码 实际生成 Bitmap
        options.inJustDecodeBounds = false;
        // 使用 565 方式解码
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        ret = BitmapFactory.decodeByteArray(data,0,data.length,options);
        return ret;
    }

    /**
     * 计算图片二次采样的采样率，使用获取图片宽高之后的 option参数 作为第一个参数
     * @param options options
     * @param reqWidth 请求宽度
     * @param reqHeight 请求高度
     * @return int 采样率
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        // 请求高度宽度 > 0 进行缩放
        if (reqHeight > 0 && reqWidth > 0){

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
        }
        return inSampleSize;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {

            //获取弱引用包含的对象，可能为 null
            ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                //每一个图片都可以包含 AsyncDrawable 对象
                Drawable drawable = imageView.getDrawable();

                if (drawable != null && drawable instanceof AsyncDrawable){
                    // 用于检测图片错位
                    AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                    ImageLoadTask task = asyncDrawable.getImageLoadTask();
                    // 当前的 ImageView 内部包含的 AsyncDrawable
                    if (this == task){
                        imageView.setImageBitmap(bitmap);
                    }

                }else {
                    // 不用检测图片错位情况
                    imageView.setImageBitmap(bitmap);
                }

                imageView.setImageBitmap(bitmap);
            }

        }
    }
}

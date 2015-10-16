package com.alex.mylibrary.tasks;

/**
 * Created by
 * Author: alex
 * Email:
 * Date: 15-10-12.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * 异步下载的 Drawable， 解决图片错位问题
 * 自身不需要设置图片
 */
public class AsyncDrawable extends BitmapDrawable {
    /**
     * 真正下载图片的部分
     */

    private final WeakReference<ImageLoadTask> taskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap, ImageLoadTask task) {
        super(res, bitmap);
        taskReference = new WeakReference<>(task);
    }

    /**
     * 获取当前 Drawable 包含的异步任务
     * @return 返回 ImageLoaderTask
     */
    public ImageLoadTask getImageLoadTask(){
        ImageLoadTask ret;
        ret = taskReference.get();
        return ret;
    }

}

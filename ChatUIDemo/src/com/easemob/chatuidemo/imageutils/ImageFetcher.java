package com.easemob.chatuidemo.imageutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
 * 图片获取者，单例
 * 
 * @author hugo
 * 
 */
public class ImageFetcher {

    private static ImageFetcher mImageFetcher;
    private static int type = 0;

    // 图片存放地址
    public final static String IAMGES_PATH = Environment
            .getExternalStorageDirectory().getPath()
            + "/linkGroup/record/images";

    public static ImageFetcher Instance(Context context, int bo) {
        type = bo;
        int memorySize = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        // 硬引用缓存容量，这里的参数根据应用本身环境重新设定，这里设定为为1/8
        int cacheSize = 1024 * 1024 * memorySize / 2;
        mImageFetcher = ImageFetcher.init(IAMGES_PATH, cacheSize);
        mImageFetcher.unlock();
        return mImageFetcher;
    }

    private final static String TAG = "ImageFetcher";
    /**
	 * 
	 */
    private static ImageFetcher instance;
    /**
     * 执行取图片任务的线程池
     */
    private ExecutorService executorService;

    private List<Fetchable> fetchers;

    /**
     * 是否锁住，如果锁住，则不允许加载图片
     */
    private boolean isLock;

    private Map<String, ImageView> tasks;
    private int flag;

    /**
     * 
     * @param sdPath
     *            SDCache缓存的路径
     * @param cacheSize
     *            硬引用缓存的大小
     */
    private ImageFetcher(String sdPath, int cacheSize) {
        // 取得CPU的核数
        int cpuCount = Runtime.getRuntime().availableProcessors();
        // 也可以根据网络状况，譬如wifi，gprs等决定初始化线程池数
        // 根据CPU的核数初始化线程池
        this.executorService = Executors.newFixedThreadPool(cpuCount + 1);
        // 单线程池，主要是为了观察效果，测试用
        // this.executorService = Executors.newSingleThreadExecutor();
        this.fetchers = new ArrayList<Fetchable>();
        // 加载三个图片加载器，注意顺序，优先从内存缓存中取，其次SD卡缓存中存，最后从网络中获取
        this.fetchers.add(new MemoryCahceFetcher(cacheSize));
        this.fetchers.add(new SDFileFetcher(sdPath));
        this.fetchers.add(new NetworkFetcher());
        // 可能出现几个view同时请求图片，故用ConcurrentHashMap
        tasks = new ConcurrentHashMap<String, ImageView>();
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                ImageInfoHolder holder = (ImageInfoHolder) msg.obj;
                if (holder.bitmap != null) {
                    holder.bitmap = toRoundCorner(holder.bitmap, type);
                    if (flag == 0) {// 为0时，剪切图片为圆形
                        holder.imageView
                                .setImageBitmap(toRoundBitmap(holder.bitmap));
                    } else {
                        holder.imageView.setImageBitmap(holder.bitmap);
                    }

                }
            }
        }
    };

    /**
     * 带参数取得实例，加上锁，以免多线程同时调用，造成多个实例的存在
     * 
     * @param context
     * @param sdPath
     */
    public static ImageFetcher init(String sdPath, int cacheSize) {
        synchronized (ImageFetcher.class) {
            if (instance == null) {
                instance = new ImageFetcher(sdPath, cacheSize);
            }
        }
        return instance;
    }

    /**
     * 取得ImageFetcher的实例对象，可能返回null
     * 
     * @param context
     * @return
     */
    public static ImageFetcher getInstance() {
        if (instance == null) {
            Log.w(TAG, "Please init the ImageFetcher before use it!");
        }
        return instance;
    }

    /**
     * ImageFetcher销毁
     */
    public static void destory() {
        if (instance != null) {
            instance.clear();
            instance = null;
        }
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);

            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;

            final Paint paint = new Paint();

            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            final RectF rectF = new RectF(rect);

            final float roundPx = pixels;

            paint.setAntiAlias(true);

            canvas.drawARGB(0, 0, 0, 0);

            paint.setColor(color);

            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 清理工作
     */
    private void clear() {
        this.executorService.shutdown();
        this.fetchers.clear();
    }

    /**
     * 锁住
     */
    public void lock() {
        this.isLock = true;
    }

    /**
     * 解锁
     */
    public void unlock() {
        this.isLock = false;
        doTasks();
    }

    /**
     * 加载一个取图片任务，这个方法由主线程调用
     * 
     * @param imageView
     * @param photoUrl
     */
    public void addTask(ImageView imageView, String photoUrl, int flag) {
        this.flag = flag;
        // 传入数据不合法，为null则立即返回
        if (imageView == null || photoUrl == null) {
            return;
        }

        // 如果从内存中直接取到图片
        Bitmap bitmap = null;
        try {
            bitmap = this.fetchers.get(0).fetch(photoUrl);
            if (null != bitmap) {
                bitmap = toRoundCorner(bitmap, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            // 还在UI线程中，可直接设置图片
            if (flag == 0)// 0 剪切成圆形
            {
                imageView.setImageBitmap(toRoundBitmap(bitmap));
            } else {
                imageView.setImageBitmap(bitmap);
            }
            return;
        }

        imageView.setTag(photoUrl);
        // 由于Android中listView中的item是复用的，这个tasks里面存的都是能看到的ImageView
        this.tasks.put(Integer.toString(imageView.hashCode()), imageView);

        // 如果不是锁住的话，立即加载图片
        if (!this.isLock) {
            doTasks();
        }
    }

    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 执行加载图片任务
     */
    public synchronized void doTasks() {
        for (Map.Entry<String, ImageView> entry : this.tasks.entrySet()) {
            ImageView iv = entry.getValue();
            // 以防ImageView给垃圾回收器回收
            if (iv != null) {
                ImageInfoHolder holder = new ImageInfoHolder();
                holder.imageView = entry.getValue();
                holder.photoUrl = (String) entry.getValue().getTag();
                this.executorService.execute(new FetchRunnable(holder));
            }
        }
        // 请空task列表
        this.tasks.clear();
    }

    /**
     * 在取图片整个任务中传输的对象
     * 
     * @author hugo
     * 
     */
    class ImageInfoHolder {
        ImageView imageView;
        String photoUrl;
        Bitmap bitmap;
    }

    /**
     * 取图片Runnable
     * 
     * @author hugo
     * 
     */
    class FetchRunnable implements Runnable {
        private ImageInfoHolder holder;

        FetchRunnable(ImageInfoHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            Bitmap bitmap = null;
            // 记录取得图片的Fetch的下标
            int fetchIndex = 0;
            for (int i = 0; i < fetchers.size(); i++) {
                Fetchable fetcher = fetchers.get(i);
                try {
                    bitmap = fetcher.fetch(this.holder.photoUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    fetchIndex = i;
                    break;
                }
            }

            if (bitmap == null) {
                return;
            }

            // 在下标fetchIndex之前的Fetcher都得执行存缓存动作
            for (int i = 0; i < fetchers.size(); i++) {
                if (i == fetchIndex) {
                    break;
                }
                Fetchable fetcher = fetchers.get(i);
                try {
                    fetcher.cache(this.holder.photoUrl, bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (bitmap != null) {
                this.holder.bitmap = bitmap;
                Message msg = mHandler.obtainMessage();
                msg.obj = this.holder;
                mHandler.sendMessage(msg);
            }

        }
    }

}

package androidUtils.imageio;


import android.os.Handler;
import android.os.Looper;

public class HandlerAction {
    Handler mainHandler;


    public HandlerAction()
    {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void post(Runnable r) {
        mainHandler.post(r);
    }


}

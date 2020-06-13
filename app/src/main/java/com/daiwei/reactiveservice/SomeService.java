package com.daiwei.reactiveservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SomeService extends Service {

  static private final String TAG = SomeService.class.getSimpleName();

  static final int MSG_SUB = 1;

  static class IncomingHandler extends Handler {
    private Context mContext;

    IncomingHandler(Context context) {
      mContext = context;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      switch (msg.what) {
        case MSG_SUB:
          Log.d(TAG, "MSG_SUB");
          break;
        default:
          super.handleMessage(msg);
      }
    }
  }

  @Nullable Messenger mMessenger;

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "onCreate");
  }

  @Override
  public IBinder onBind(Intent intent) {
    mMessenger = new Messenger(new IncomingHandler(this));
    return mMessenger.getBinder();
  }

  @Override
  public boolean onUnbind(Intent intent) {
    return super.onUnbind(intent);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy");
  }
}

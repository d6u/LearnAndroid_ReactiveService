package com.daiwei.reactiveservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SomeService extends Service {

  private static final String TAG = SomeService.class.getSimpleName();

  static final int MSG_SUB = 1;

  static class IncomingHandler extends Handler {
    private SomeService mService;

    IncomingHandler(SomeService service) {
      mService = service;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      switch (msg.what) {
        case MSG_SUB:
          Log.d(TAG, "MSG_SUB");
          mService.mClient = msg.replyTo;

          break;
        default:
          super.handleMessage(msg);
      }
    }
  }

  @Nullable Messenger mMessenger;
  @Nullable Timer mTimer;
  private int mCounter = 0;
  @Nullable private Messenger mClient;

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "onCreate");

    mTimer = new Timer();
    mTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            Log.d(TAG, "run " + mCounter);
            mCounter++;

            if (mClient != null) {
              Message message = Message.obtain(null, 123);
              try {
                mClient.send(message);
              } catch (RemoteException e) {
                e.printStackTrace();
              }
            }
          }
        },
        0,
        1000);

    mMessenger = new Messenger(new IncomingHandler(this));
  }

  @Override
  public IBinder onBind(Intent intent) {
    return Objects.requireNonNull(mMessenger).getBinder();
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

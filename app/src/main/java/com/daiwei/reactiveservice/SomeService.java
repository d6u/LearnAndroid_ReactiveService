package com.daiwei.reactiveservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class SomeService extends Service {

  static class IncomingHandler extends Handler {

    IncomingHandler(SomeService service) {
      mService = new WeakReference<>(service);
    }

    private final WeakReference<SomeService> mService;

    @Override
    public void handleMessage(@NonNull Message msg) {
      switch (msg.what) {
        case SUB_TO_SERVICE:
          {
            Log.d(TAG, "SUB_TO_SERVICE");
            SomeService service = mService.get();
            if (service != null) {
              service.mClients.add(msg.replyTo);
            }
            break;
          }
        case UNSUB_TO_SERVICE:
          {
            Log.d(TAG, "UNSUB_TO_SERVICE");
            SomeService service = mService.get();
            if (service != null) {
              service.mClients.remove(msg.replyTo);
            }
            break;
          }
        default:
          super.handleMessage(msg);
      }
    }
  }

  static final int SUB_TO_SERVICE = 0;
  static final int UNSUB_TO_SERVICE = 1;
  private static final String TAG = SomeService.class.getSimpleName();
  final Set<Messenger> mClients = new HashSet<>();
  @Nullable Messenger mMessenger;
  @Nullable Timer mTimer;
  private Counter mCounter = new Counter();

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "onCreate");

    mTimer = new Timer();
    mTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            mCounter.setCount(mCounter.getCount() + 1);

            for (Messenger client : mClients) {
              Message message = Message.obtain(null, ServiceClient.COUNTER_UPDATE);

              Bundle bundle = new Bundle();
              bundle.putParcelable(ServiceClient.KEY_COUNTER, mCounter);

              message.setData(bundle);

              try {
                client.send(message);
              } catch (RemoteException e) {
                e.printStackTrace();
              }
            }
          }
        },
        1000,
        1000);

    mMessenger = new Messenger(new IncomingHandler(this));
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy");

    if (mTimer != null) {
      mTimer.cancel();
      mTimer = null;
    }

    super.onDestroy();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return Objects.requireNonNull(mMessenger).getBinder();
  }

  @Override
  public boolean onUnbind(Intent intent) {
    return super.onUnbind(intent);
  }
}

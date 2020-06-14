package com.daiwei.reactiveservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class ServiceClient {

  static final int COUNTER_UPDATE = 0;

  static final String KEY_COUNTER = "KEY_COUNTER";

  private static final String TAG = ServiceClient.class.getSimpleName();

  private static class SomeHandler extends Handler {

    @Override
    public void handleMessage(@NonNull Message msg) {
      switch (msg.what) {
        case COUNTER_UPDATE:
          {
            Bundle bundle = msg.getData();
            int counter = bundle.getInt(KEY_COUNTER);
            Log.d(TAG, "counter = " + counter);
            break;
          }
        default:
          {
            super.handleMessage(msg);
            break;
          }
      }
    }
  }

  ServiceClient(Context context) {
    mContext = context;
  }

  private Context mContext;
  @Nullable private Messenger mServiceMessenger;
  @Nullable private Messenger mClientMessenger = new Messenger(new SomeHandler());

  private ServiceConnection mConnection =
      new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
          mServiceMessenger = new Messenger(binder);

          Message message = Message.obtain(null, SomeService.SUB_TO_SERVICE);
          message.replyTo = mClientMessenger;

          try {
            mServiceMessenger.send(message);
          } catch (RemoteException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
          Message message = Message.obtain(null, SomeService.UNSUB_TO_SERVICE);
          message.replyTo = mClientMessenger;

          try {
            mServiceMessenger.send(message);
          } catch (RemoteException e) {
            e.printStackTrace();
          }
        }
      };

  void subscribeService() {
    mContext.bindService(
        new Intent(mContext, SomeService.class), mConnection, Context.BIND_AUTO_CREATE);
  }

  void unsubscribeService() {
    mContext.unbindService(mConnection);
  }
}

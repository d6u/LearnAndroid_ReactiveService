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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import java.lang.ref.WeakReference;

class ServiceClient {

  private static class SomeHandler extends Handler {

    SomeHandler(ServiceClient serviceClient) {
      mServiceClient = new WeakReference<>(serviceClient);
    }

    private final WeakReference<ServiceClient> mServiceClient;

    @Override
    public void handleMessage(@NonNull Message msg) {
      if (msg.what == COUNTER_UPDATE) {
        Bundle bundle = msg.getData();
        Counter counter = bundle.getParcelable(KEY_COUNTER);
        ServiceClient serviceClient = mServiceClient.get();
        if (serviceClient != null) {
          serviceClient.mCounterSubject.onNext(counter);
        }
      } else {
        super.handleMessage(msg);
      }
    }
  }

  static final int COUNTER_UPDATE = 0;
  static final String KEY_COUNTER = "KEY_COUNTER";

  ServiceClient(Context context) {
    mContext = context;
    bindService();
  }

  private void bindService() {
    mContext.bindService(
        new Intent(mContext, SomeService.class), mConnection, Context.BIND_AUTO_CREATE);
  }

  private Context mContext;
  private Subject<Counter> mCounterSubject = BehaviorSubject.create();
  @Nullable private Messenger mServiceMessenger;
  @Nullable private Messenger mReceivingMessenger = new Messenger(new SomeHandler(this));

  private ServiceConnection mConnection =
      new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
          mServiceMessenger = new Messenger(binder);

          Message message = Message.obtain(null, SomeService.SUB_TO_SERVICE);
          message.replyTo = mReceivingMessenger;

          try {
            mServiceMessenger.send(message);
          } catch (RemoteException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
          Message message = Message.obtain(null, SomeService.UNSUB_TO_SERVICE);
          message.replyTo = mReceivingMessenger;

          try {
            mServiceMessenger.send(message);
          } catch (RemoteException e) {
            e.printStackTrace();
          }
        }
      };

  Observable<Counter> getCounter() {
    return mCounterSubject;
  }
}

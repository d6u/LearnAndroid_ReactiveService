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
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  static private final String TAG = MainActivity.class.getSimpleName();

  private ServiceConnection mConnection =
      new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
          Messenger messenger = new Messenger(binder);
          Message message = Message.obtain(null, SomeService.MSG_SUB);

          try {
            messenger.send(message);
          } catch (RemoteException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  static class TestHandler extends Handler {

    @Override
    public void handleMessage(@NonNull Message msg) {
      Log.d(TAG, "handleMessage msg.what = " + msg.what);
      super.handleMessage(msg);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    bindService(new Intent(this, SomeService.class), mConnection, Context.BIND_AUTO_CREATE);

    Messenger messenger1 = new Messenger(new TestHandler());

    Messenger messenger2 = new Messenger(messenger1.getBinder());
    try {
      messenger2.send(Message.obtain(null, 100));
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onStop() {
    unbindService(mConnection);
    super.onStop();
  }
}

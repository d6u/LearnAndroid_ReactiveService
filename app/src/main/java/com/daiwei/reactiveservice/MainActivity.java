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

  static private class SomeHandler extends Handler {

    @Override
    public void handleMessage(@NonNull Message msg) {
      super.handleMessage(msg);
      Log.d(TAG, "handleMessage " + msg.what);
    }
  }

  private Messenger mMessenger = new Messenger(new SomeHandler());

  private ServiceConnection mConnection =
      new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
          Messenger messenger = new Messenger(binder);
          Message message = Message.obtain(null, SomeService.MSG_SUB);
          message.replyTo = mMessenger;

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
    Log.d(TAG, "onCreate");

    setContentView(R.layout.activity_main);
    bindService(new Intent(this, SomeService.class), mConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    Log.d(TAG, "onDestroy");
    unbindService(mConnection);
    super.onDestroy();
  }
}

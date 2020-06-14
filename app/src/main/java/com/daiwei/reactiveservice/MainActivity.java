package com.daiwei.reactiveservice;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Nullable private ServiceClient mServiceClient;
  @Nullable private Disposable mDisposable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mServiceClient = new ServiceClient(getApplicationContext());
  }

  @Override
  protected void onStart() {
    Log.d(TAG, "onStart");
    super.onStart();
    if (mServiceClient != null) {
      mDisposable = mServiceClient
          .getCounter()
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
              counter -> Log.d(TAG, "The counter is " + counter));
    }
  }

  @Override
  protected void onStop() {
    Log.d(TAG, "onStop");
    super.onStop();
    if (mDisposable != null) {
      mDisposable.dispose();
      mDisposable = null;
    }
  }

  @Override
  protected void onDestroy() {
    Log.d(TAG, "onDestroy");
    super.onDestroy();
  }
}

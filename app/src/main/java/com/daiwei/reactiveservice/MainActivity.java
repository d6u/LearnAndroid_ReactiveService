package com.daiwei.reactiveservice;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.daiwei.reactiveservice.databinding.ActivityMainBinding;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Nullable private ServiceClient mServiceClient;
  @Nullable private Disposable mDisposable;
  @Nullable private CounterViewModel mCounterViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");

    super.onCreate(savedInstanceState);

    final ActivityMainBinding binding =
        ActivityMainBinding.inflate(getLayoutInflater(), findViewById(android.R.id.content), true);
    binding.setLifecycleOwner(this);

    mCounterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);
    binding.setLiveDataCount(mCounterViewModel);

    mServiceClient = new ServiceClient(getApplicationContext());
  }

  @Override
  protected void onStart() {
    Log.d(TAG, "onStart");
    super.onStart();

    if (mServiceClient != null) {
      mDisposable =
          mServiceClient
              .getCounter()
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(
                  counter ->
                      Objects.requireNonNull(mCounterViewModel)
                          .getCount()
                          .setValue(counter.getCount()));
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

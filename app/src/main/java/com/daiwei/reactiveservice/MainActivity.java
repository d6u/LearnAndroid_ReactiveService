package com.daiwei.reactiveservice;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daiwei.reactiveservice.databinding.ActivityMainBinding;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Nullable private ServiceClient mServiceClient;
  @Nullable private Disposable mDisposable;
  private RecyclerView mRecyclerView;
  private RecyclerView.Adapter mAdapter;
  private boolean mIsRecyclerViewLoading;
  private ArrayList<ITableCell> mDataList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate");

    mServiceClient = new ServiceClient(getApplicationContext());

    final ActivityMainBinding binding =
        ActivityMainBinding.inflate(getLayoutInflater(), findViewById(android.R.id.content), true);
    binding.setLifecycleOwner(this);

    mRecyclerView = binding.recyclerView;
    mRecyclerView.setHasFixedSize(true);

    mDataList.addAll(
        Arrays.asList(
            new Item("a"),
            new Item("b"),
            new Item("c"),
            new Item("d"),
            new Item("e"),
            new Item("f"),
            new Item("g"),
            new Item("h"),
            new Item("i")));

    mAdapter = new MyRecyclerViewAdapter(mDataList);
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override
  protected void onStart() {
    super.onStart();

    Log.d(TAG, "onStart");

    mRecyclerView.addOnScrollListener(
        new RecyclerView.OnScrollListener() {
          @Override
          public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager linearLayoutManager =
                (LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager());

            if (!mIsRecyclerViewLoading
                && linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    == mDataList.size() - 1) {
              mIsRecyclerViewLoading = true;
              loadMore();
            }
          }
        });
  }

  private void loadMore() {
    mDataList.add(new LoadingItem());
    mAdapter.notifyItemInserted(mDataList.size());

    Handler handler = new Handler();
    handler.postDelayed(
        () -> {
          int start = mDataList.size() - 1;

          mDataList.remove(start);
          mAdapter.notifyItemRemoved(start);

          mDataList.addAll(
              Arrays.asList(new Item("j"), new Item("k"), new Item("l"), new Item("m")));
          mAdapter.notifyItemRangeInserted(start, 4);

          mIsRecyclerViewLoading = false;
        },
        1000);
  }

  @Override
  protected void onStop() {
    Log.d(TAG, "onStop");

    mRecyclerView.clearOnScrollListeners();

    super.onStop();
  }

  @Override
  protected void onDestroy() {
    Log.d(TAG, "onDestroy");

    if (mDisposable != null) {
      mDisposable.dispose();
      mDisposable = null;
    }

    super.onDestroy();
  }
}

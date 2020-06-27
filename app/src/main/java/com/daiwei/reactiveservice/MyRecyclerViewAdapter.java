package com.daiwei.reactiveservice;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.daiwei.reactiveservice.databinding.LoadingIndicatorBinding;
import com.daiwei.reactiveservice.databinding.RecyclerCellBinding;
import java.util.ArrayList;

final class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

  static class MyViewHolder extends RecyclerView.ViewHolder {
    MyViewHolder(ViewBinding binding) {
      super(binding.getRoot());
      mBinding = binding;
    }

    ViewBinding mBinding;
  }

  private static final int REGULAR_ITEM = 0;
  private static final int LOADING_ITEM = 1;

  MyRecyclerViewAdapter(ArrayList<ITableCell> myDataset) {
    mDataset = myDataset;
  }

  private ArrayList<ITableCell> mDataset;

  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ViewBinding binding;

    if (viewType == REGULAR_ITEM) {
      binding =
          RecyclerCellBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    } else {
      binding =
          LoadingIndicatorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    ITableCell cell = mDataset.get(position);
    if (cell instanceof Item) {
      ((RecyclerCellBinding) holder.mBinding).setText(((Item) cell).getTitle());
    }
  }

  @Override
  public int getItemViewType(int position) {
    return mDataset.get(position) instanceof Item ? REGULAR_ITEM : LOADING_ITEM;
  }

  @Override
  public int getItemCount() {
    return mDataset.size();
  }
}

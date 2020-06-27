package com.daiwei.reactiveservice;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.daiwei.reactiveservice.databinding.LoadingIndicatorBinding;
import com.daiwei.reactiveservice.databinding.RecyclerCellBinding;

public final class MyRecyclerViewAdapter
    extends ListAdapter<ITableCell, MyRecyclerViewAdapter.MyViewHolder> {

  static class MyViewHolder extends RecyclerView.ViewHolder {
    MyViewHolder(ViewBinding binding) {
      super(binding.getRoot());
      mBinding = binding;
    }

    ViewBinding mBinding;
  }

  private static final DiffUtil.ItemCallback<ITableCell> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<ITableCell>() {
        @Override
        public boolean areItemsTheSame(@NonNull ITableCell oldCell, @NonNull ITableCell newCell) {
          return oldCell == newCell;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(
            @NonNull ITableCell oldCell, @NonNull ITableCell newCell) {
          // NOTE: if you use equals, your object must properly override Object#equals()
          // Incorrectly returning false here will result in too many animations.
          return oldCell == newCell;
        }
      };

  private static final int REGULAR_ITEM = 0;
  private static final int LOADING_ITEM = 1;

  MyRecyclerViewAdapter() {
    super(DIFF_CALLBACK);
  }

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
    ITableCell cell = getItem(position);
    if (cell instanceof Item) {
      ((RecyclerCellBinding) holder.mBinding).setText(((Item) cell).getTitle());
    }
  }

  @Override
  public int getItemViewType(int position) {
    return getItem(position) instanceof Item ? REGULAR_ITEM : LOADING_ITEM;
  }
}

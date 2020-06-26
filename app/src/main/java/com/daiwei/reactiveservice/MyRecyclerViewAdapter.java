package com.daiwei.reactiveservice;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.daiwei.reactiveservice.databinding.RecyclerCellBinding;
import java.util.ArrayList;

final class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

  static class MyViewHolder extends RecyclerView.ViewHolder {
    MyViewHolder(RecyclerCellBinding binding) {
      super(binding.getRoot());
      mBinding = binding;
    }

    RecyclerCellBinding mBinding;
  }

  MyRecyclerViewAdapter(ArrayList<String> myDataset) {
    mDataset = myDataset;
  }

  private ArrayList<String> mDataset;

  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    RecyclerCellBinding binding =
        RecyclerCellBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    holder.mBinding.setText(mDataset.get(position));
  }

  @Override
  public int getItemCount() {
    return mDataset.size();
  }
}

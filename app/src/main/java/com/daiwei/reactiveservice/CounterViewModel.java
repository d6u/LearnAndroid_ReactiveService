package com.daiwei.reactiveservice;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public final class CounterViewModel extends ViewModel {

  public CounterViewModel() {
    this.count = new MutableLiveData<>();
    this.count.setValue(0);
  }

  private MutableLiveData<Integer> count;

  public MutableLiveData<Integer> getCount() {
    return count;
  }
}

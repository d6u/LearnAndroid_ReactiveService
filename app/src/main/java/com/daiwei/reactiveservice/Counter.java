package com.daiwei.reactiveservice;

import android.os.Parcel;
import android.os.Parcelable;

final class Counter implements Parcelable {

  public static final Creator<Counter> CREATOR =
      new Creator<Counter>() {
        @Override
        public Counter createFromParcel(Parcel in) {
          return new Counter(in);
        }

        @Override
        public Counter[] newArray(int size) {
          return new Counter[size];
        }
      };

  Counter() {}

  private Counter(Parcel in) {
    mCountBox = in.readParcelable(CountBox.class.getClassLoader());
  }

  private CountBox mCountBox = new CountBox();

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(mCountBox, flags);
  }

  int getCount() {
    return mCountBox.getCount();
  }

  void setCount(int count) {
    mCountBox.setCount(count);
  }
}

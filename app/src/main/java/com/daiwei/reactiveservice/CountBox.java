package com.daiwei.reactiveservice;

import android.os.Parcel;
import android.os.Parcelable;

final class CountBox implements Parcelable {

  public static final Creator<CountBox> CREATOR =
      new Creator<CountBox>() {
        @Override
        public CountBox createFromParcel(Parcel in) {
          return new CountBox(in);
        }

        @Override
        public CountBox[] newArray(int size) {
          return new CountBox[size];
        }
      };

  CountBox() {}

  private CountBox(Parcel in) {
    mCount = in.readInt();
  }

  private int mCount = 0;

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mCount);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  int getCount() {
    return mCount;
  }

  void setCount(int count) {
    mCount = count;
  }
}

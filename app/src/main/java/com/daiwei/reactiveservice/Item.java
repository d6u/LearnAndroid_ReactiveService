package com.daiwei.reactiveservice;

final class Item implements ITableCell {

  Item(String title) {
    mTitle = title;
  }

  private String mTitle;

  String getTitle() {
    return mTitle;
  }
}

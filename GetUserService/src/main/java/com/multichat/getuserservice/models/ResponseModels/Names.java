package com.multichat.getuserservice.models.ResponseModels;

import java.util.List;

import com.multichat.getuserservice.models.Message;

public class Names {

  List<String> names;
  Message firstText;
  

  public List<String> getNames() {
    return names;
  }

  public void setNames(List<String> names) {
    this.names = names;
  }

  public Message getFirstText() {
    return firstText;
  }

  public void setFirstText(Message firstText) {
    this.firstText = firstText;
  }

  @Override
  public String toString() {
    return "Names [names=" + names + ", firstText=" + firstText + "]";
  }


}
  

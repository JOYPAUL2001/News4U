package com.example.dome;

import com.example.dome.Models.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListner<NewsApiResponse> {
    void onFetchData(List<NewsHeadlines> list, String message);
    void onError(String message);


}

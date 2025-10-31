package com.khetigaadi.commons.utils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.commons.model.Testimonial;
import com.khetigaadi.implement.newImplement.util.NewImp;
import com.khetigaadi.tractor.newTractor.model.NewTractor;

import java.util.ArrayList;
import java.util.List;

public class HomeData {
    @SerializedName("popularTractors")
    @Expose
    private List<NewTractor> tractors;
    @SerializedName("implements")
    @Expose
    private List<NewImp> implement;
    @SerializedName("news")
    @Expose
    private List<News> news;

    @SerializedName("testimonial")
    @Expose
    private List<Testimonial> testimonials;


    public List<NewTractor> getTractors() {
        return tractors;
    }

    public void setTractors(ArrayList<NewTractor> tractors) {
        this.tractors = tractors;
    }


    public List<News> getNews() {
        return news;
    }


    public void setTractors(List<NewTractor> tractors) {
        this.tractors = tractors;
    }

    public List<NewImp> getImplement() {
        return implement;
    }

    public void setImplement(List<NewImp> implement) {
        this.implement = implement;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public List<Testimonial> getTestimonials() {
        return testimonials;
    }

    public void setTestimonials(List<Testimonial> testimonials) {
        this.testimonials = testimonials;
    }

    @NonNull
    @Override
    public String toString() {
        return "HomeList{" +
                "tractors=" + tractors +
                ", imps=" + implement +
                ", news=" + news +
                '}';
    }
}

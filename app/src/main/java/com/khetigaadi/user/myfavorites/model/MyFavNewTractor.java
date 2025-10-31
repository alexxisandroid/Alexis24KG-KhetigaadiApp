package com.khetigaadi.user.myfavorites.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
public class MyFavNewTractor {
   // @SerializedName("id")
 //   @Expose
    private int id;
   // @SerializedName("newtractor")
   // @Expose
    private NewTractor newtractor;
    public NewTractor getNewTractor() {
        return newtractor;
    }
    public void setNewTractor(NewTractor newtractor) {
        this.newtractor = newtractor;
    }
}

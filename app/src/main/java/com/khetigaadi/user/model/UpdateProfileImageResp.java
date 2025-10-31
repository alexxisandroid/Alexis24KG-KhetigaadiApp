package com.khetigaadi.user.model;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.model.agri.Others;
import java.util.List;
public class UpdateProfileImageResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private UpdateProfileImage updateProfileImage;
    @SerializedName("others")
    @Expose
    private Others others;
    public UpdateProfileImage getUpdateProfileImage() {
        return updateProfileImage;
    }
    public void setUpdateProfileImage(UpdateProfileImage updateProfileImage) {
        this.updateProfileImage = updateProfileImage;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
    @NonNull
    @Override
    public String toString() {
        return "UpdateProfileImageResp{" +
                "updateProfileImage=" + updateProfileImage +
                ", others=" + others +
                '}';
    }
//    private List<String> msg;
//    public  List<String> getMsg() {
//        return msg;
//    }
}

//
//
//
//
//package com.khetigaadi.user.model;
//import java.util.List;
//public class UpdateProfileImageResp {
//    //getUpdateProfileImage
//    public UpdateProfileImage getUpdateProfileImage() {
//        return updateProfileImage;
//    }
//    public void setUpdateProfileImage(UpdateProfileImage updateProfileImage) {
//        this.updateProfileImage = updateProfileImage;
//    }
//    private UpdateProfileImage updateProfileImage;
//
//}

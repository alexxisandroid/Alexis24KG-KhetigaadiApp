package com.khetigaadi.implement.compare.util;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.implement.newImplement.util.ImplementImage;
public class PopularImpComp {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("first_implement")
    @Expose
    private PopularImpCompDetail firstImplement;
    @SerializedName("secound_implement")
    @Expose
    private PopularImpCompDetail secondImplement;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public PopularImpCompDetail getFirstImplement() {
        return firstImplement;
    }
    public void setFirstImplement(PopularImpCompDetail firstImplement) {
        this.firstImplement = firstImplement;
    }
    public PopularImpCompDetail getSecondImplement() {
        return secondImplement;
    }
    public void setSecondImplement(PopularImpCompDetail secondImplement) {
        this.secondImplement = secondImplement;
    }
    public static class PopularImpCompDetail {
        @SerializedName("implement_id")
        @Expose
        private String implId;
        @SerializedName("implement_name")
        @Expose
        private String implName;
        @SerializedName("brand_id")
        @Expose
        private String implBrandId;
        @SerializedName("implement_type_id")
        @Expose
        private String implTypeId;
        @SerializedName("implement_model_id")
        @Expose
        private String implModelId;
        @SerializedName("implement_image")
        @Expose
        private ImplementImage implImage;
        public String getImplId() {
            return implId;
        }
        public void setImplId(String implId) {
            this.implId = implId;
        }
        public String getImplName() {
            return implName;
        }
        public void setImplName(String implName) {
            this.implName = implName;
        }
        public ImplementImage getImplImage() {
            return implImage;
        }
        public void setImplImage(ImplementImage implImage) {
            this.implImage = implImage;
        }
        public String getImplBrandId() {
            return implBrandId;
        }
        public void setImplBrandId(String implBrandId) {
            this.implBrandId = implBrandId;
        }
        public String getImplTypeId() {
            return implTypeId;
        }
        public void setImplTypeId(String implTypeId) {
            this.implTypeId = implTypeId;
        }
        public String getImplModelId() {
            return implModelId;
        }
        public void setImplModelId(String implModelId) {
            this.implModelId = implModelId;
        }
        @NonNull
        @Override
        public String toString() {
            return "PopularTractorComp{" +
                    ", name='" + implName + '\'' +
                    ", image='" + implImage + '\'' +
                    ", brand='" + implBrandId + '\'' +
                    ", type='" + implTypeId + '\'' +
                    ", model='" + implModelId + '\'' +
                    '}';
        }
    }
}

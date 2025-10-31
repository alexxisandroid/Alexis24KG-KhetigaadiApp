package com.khetigaadi.shop.model;
import android.os.Parcel;

import java.util.List;
public class ProductListRespS{
    protected ProductListRespS(Parcel in) {
        error = in.readByte() != 0;
        msg = in.createStringArrayList();
    }
      public boolean isError() {
        return error;
    }
    public void setError(boolean error) {
        this.error = error;
    }
    private boolean error;
    public List<String> getMsg() {
        return msg;
    }
    public void setMsg(List<String> msg) {
        this.msg = msg;
    }
    private List<String> msg;
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
    private Others others;
}

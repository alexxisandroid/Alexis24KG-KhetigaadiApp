package com.khetigaadi.user.model;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
public class OtpResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private MobileRegisterOTP verificationCode = new MobileRegisterOTP();
    public MobileRegisterOTP getVerificationCode() {
        return verificationCode;
    }
    public void setVerificationCode(MobileRegisterOTP verificationCode) {
        this.verificationCode = verificationCode;
    }
    @NonNull
    @Override
    public String toString() {
        return "OtpResp{" +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
    public static class MobileRegisterOTP {
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("follower_status")
        @Expose
        private String follower_status;
        @SerializedName("is_register")
        @Expose
        private boolean is_register=true;
        public String getMobile() {
            return mobile;
        }
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
        public String getOtp() {
            return otp;
        }
        public void setOtp(String otp) {
            this.otp = otp;
        }
        public String getFollower_status() {
            return follower_status;
        }
        public void setFollower_status(String follower_status) {
            this.follower_status = follower_status;
        }
        public boolean isIs_register() {
            return is_register;
        }
        public void setIs_register(boolean is_register) {
            this.is_register = is_register;
        }
    }
}

package com.khetigaadi.tractor.sellTractorNewUi.fileuploadservice.utils;
public enum MIMEType {
    IMAGE("image/*"), VIDEO("video/*");
    public String value;
    MIMEType(String value) {
        this.value = value;
    }
}

package com.khetigaadi.base.retrofit;
import android.net.Uri;
import com.khetigaadi.base.service.CountingRequestBody;
import java.io.File;
import java.util.Objects;
import io.reactivex.rxjava3.core.FlowableEmitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
public class MultiPartHelper {
    public RequestBody createRequestBodyFromText(String mText) {
        return RequestBody.create(MediaType.parse("text/plain"),mText);
    }
    public MultipartBody.Part createMultipartBody(Uri url,String key,FlowableEmitter<Double> emitter) {
        try {
            File file = new File(Objects.requireNonNull(url.getPath()));
            return MultipartBody.Part.createFormData(key,file.getName(),createCountingRequestBody(file,emitter));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private RequestBody createCountingRequestBody(File file,final FlowableEmitter<Double> emitter) {
        RequestBody requestBody = createRequestBodyFromFile(file);
        return new CountingRequestBody(requestBody, (bytesWritten, contentLength) -> {
            double progress = (1.0 * bytesWritten) / contentLength;
            emitter.onNext(progress);
        });
    }
    private RequestBody createRequestBodyFromFile(File file) {
        return RequestBody.create(MediaType.parse("image/*"),file);
    }
}

package com.khetigaadi.news.activity;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.commons.utils.NewsDetailResp;
import com.khetigaadi.news.model.CommentResp;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.NewsRepository;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;
@HiltViewModel
public class NewsDetailsViewModel extends ViewModel {
    private static final String TAG = "NewsDetailsViewModel";
    private final StateMutableLiveData<MyResponse> commentResp = new StateMutableLiveData<>();
    private final KhetiAPIs khetiAPIs;
    private final NewsRepository newsRepository;
    private final SavedStateHandle savedStateHandle;
    private final StateMutableLiveData<NewsDetailResp> newsResp = new StateMutableLiveData<>();
    private final StateMutableLiveData<MyResponse> newsLikeResp = new StateMutableLiveData<>();
    private final StateMutableLiveData<CommentListResp> commentListResp = new StateMutableLiveData<>();
    private final StateMutableLiveData<MyResponse> shareNewsResp = new StateMutableLiveData<>();
    @Inject
    public NewsDetailsViewModel(KhetiAPIs khetiAPIs, NewsRepository newsRepository, SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
        this.newsRepository = newsRepository;
        this.savedStateHandle = savedStateHandle;
    }
    public StateMutableLiveData<MyResponse> callLikeCommentApi(String commentId) {
        commentResp.postLoading();
        HashMap<String, Object> params = new HashMap<>();
        params.put("news_comment_id", commentId);
        khetiAPIs.commentLike(params).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                if (response.isSuccessful())
                    commentResp.postSuccess(response.body());
                else
                    commentResp.postError(response.message());
            }
            @Override
            public void onFailure(@NotNull Call<MyResponse> call, @NotNull Throwable t) {
                Timber.d("onFailure() returned: " + t);
                commentResp.postError(t.getMessage());
            }
        });
        return commentResp;
    }
    public StateMutableLiveData<NewsDetailResp> getNewsDetails() {
        return newsRepository.getNewsDetails(savedStateHandle.get("news_id"));
    }
    public StateMutableLiveData<MyResponse> callLikeNewsApi(String id) {
        newsLikeResp.postLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("news_id", id);
        khetiAPIs.newsLike(params).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                if (response.isSuccessful())
                    newsLikeResp.postSuccess(response.body());
                else
                    newsLikeResp.postError(response.message());
            }
            @Override
            public void onFailure(@NotNull Call<MyResponse> call, @NotNull Throwable t) {
                Timber.e(t, "onFailure: ");
                newsLikeResp.postError(t.getMessage());
            }
        });
        return newsLikeResp;
    }
    public StateMutableLiveData<CommentListResp> getComments(String newsId) {
        commentListResp.postLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("news_id", newsId);
        khetiAPIs.getNewsComments(params).enqueue(new Callback<CommentListResp>() {
            @Override
            public void onResponse(@NotNull Call<CommentListResp> call, @NotNull Response<CommentListResp> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!response.body().isError()) {
                            commentListResp.postSuccess(response.body());
                        } else {
                            commentListResp.postError(response.body().getMsg().get(0));
                        }
                    }
                } else
                    commentListResp.postError(response.message());
            }
            @Override
            public void onFailure(@NotNull Call<CommentListResp> call, @NotNull Throwable t) {
                Timber.e(t, "onFailure: ");
                commentListResp.postError(t.getMessage());
            }
        });
        return commentListResp;
    }
    public StateMutableLiveData<CommentResp> addComment(String newsId, String content) {
        StateMutableLiveData<CommentResp> addCommentResp = new StateMutableLiveData<>();
        addCommentResp.postLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("news_id", newsId);
        params.put("comment", content);
        khetiAPIs.addNewsComment(params).enqueue(new Callback<CommentResp>() {
            @Override
            public void onResponse(@NotNull Call<CommentResp> call, @NotNull Response<CommentResp> response) {
                if (response.isSuccessful())
                    if (response.body() != null) {
                        if (!response.body().isError())
                            addCommentResp.postSuccess(response.body());
                        else
                            addCommentResp.postError(response.body().getMsg().get(0));
                    } else
                        addCommentResp.postError(response.message());
            }
            @Override
            public void onFailure(@NotNull Call<CommentResp> call, @NotNull Throwable t) {
                Timber.e(t, "onFailure: ");
                addCommentResp.postError(t.getMessage());
            }
        });
        return addCommentResp;
    }
    public StateMutableLiveData<MyResponse> shareNews(String newsId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("news_id", newsId);
        khetiAPIs.shareNews(params).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                if (response.isSuccessful())
                    if (response.body() != null) {
                        if (!response.body().isError())
                            shareNewsResp.postSuccess(response.body());
                        else
                            shareNewsResp.postError(response.body().getMsg().get(0));
                    } else
                        shareNewsResp.postError(response.message());
            }
            @Override
            public void onFailure(@NotNull Call<MyResponse> call, @NotNull Throwable t) {
                Timber.e(t, "onFailure: ");
                shareNewsResp.postError(t.getMessage());
            }
        });
        return shareNewsResp;
    }
}

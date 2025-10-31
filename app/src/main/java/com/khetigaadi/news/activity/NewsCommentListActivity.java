package com.khetigaadi.news.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.khetigaadi.AppController;
import com.khetigaadi.R;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityNewsCommentListBinding;
import com.khetigaadi.news.adapter.CommentListAdapter;
import com.khetigaadi.news.model.Comment;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.profile.ProfileActivity;
import org.json.JSONObject;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class NewsCommentListActivity extends BaseActivity {
    private static final String TAG = "NewsCommentListActivity";
    private CommentListAdapter mAdapter;
    private String newsId;
    private int page = 1;
    private CommentListResp commentListResp;
    @Inject
    SharedPreferenceUtils pref;
    @Inject
    SessionManager sessionManager;
    @Inject
    AppController appController;
    private ActivityNewsCommentListBinding binding;
    public static void startActivity(Activity activity,String newsId) {
        Intent intent = new Intent(activity, NewsCommentListActivity.class);
        intent.putExtra("newsId", newsId);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("cvbcbcvbcv====","");
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ActivityNewsCommentListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        user = sessionManager.getUser();
        findViewById();
        initToolbar();
        newsId = getIntent().getStringExtra("newsId");
        initUI();
        if(Constants.isConnectionAvailable(NewsCommentListActivity.this)) {
            getComments(newsId, user.getId(), page);
        }
        else {
            noInternetConnection(binding.parent);
        }
    }
    private void getComments(String newsId,String userId,int page) {
   /* try {
        if (page == 1) {
            binding.llProgress.llProgress.setVisibility(View.VISIBLE);
            binding.rvReply.setVisibility(View.GONE);
        }
        JSONObject params = new JSONObject();
        //   params.put("user_id", userId);
        params.put("news_id", newsId);
        // params.put("page", page);
        Timber.e("getComments: " + params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_NEW + "news/comments", params, response -> {
            Timber.e("onResponse: " + response);
            try {
                commentListResp = new Gson().fromJson(response.toString(), CommentListResp.class);
                if (!commentListResp.isError()) {
                    mAdapter.addItems(commentListResp.getData().getComments());
                    binding.llProgress.llProgress.setVisibility(View.GONE);
                    binding.rvReply.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(NewsCommentListActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            binding.llProgress.llProgress.setVisibility(View.GONE);
            binding.rvReply.setVisibility(View.VISIBLE);
        }) {
            *//**
         * Passing some request headers
         *//*
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                headers.put("Authorization", SharedPreferenceUtils.getInstance(NewsCommentListActivity.this).getStringValue(Constants.TOKEN_ID, ""));
                Timber.d(SharedPreferenceUtils.getInstance(NewsCommentListActivity.this).getStringValue(Constants.TOKEN_ID, ""));
                headers.put("user_id", userId);
                return headers;
            }
        };
        appController.addToRequestQueue(jsonObjectRequest, TAG_GET_REPLYS);
    } catch (Exception e) {
        e.printStackTrace();
    }*/
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.comments));
    }
    private void initUI() {
        binding.rvReply.setHasFixedSize(true);
        binding.rvReply.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommentListAdapter(this,binding.rvReply,sessionManager);
        binding.rvReply.setAdapter(mAdapter);
        mAdapter.setClickListener(new CommentListAdapter.CommentListClickListener() {
            @Override
            public void onItemClick(Comment post,int adapterPosition,View v) {
            }
            @Override
            public void onLikeClick(Comment post,int adapterPosition,View v) {
                callLikeApi(post.getCommentId(),user.getId(),"comment",adapterPosition);
            }
            @Override
            public void onProfileClik(Comment comment,int adapterPosition,View v) {
                Intent intent = new Intent(NewsCommentListActivity.this,ProfileActivity.class);
                intent.putExtra("userId",comment.getUser().getId());
                startActivity(intent);
            }
            @Override
            public void onReplyClick(Comment comment,int adapterPosition,View v) {
                NewsCommentReplyListActivity.startActivity(NewsCommentListActivity.this,newsId,comment);
            }
        });
        mAdapter.setOnLoadMoreListener(() -> {
            if (commentListResp != null)
                if ((++page) <= commentListResp.getData().getLastPage())
                    getComments(newsId,user.getId(), page);
        });
        if (user != null && user.getImage() != null)
            Glide.with(NewsCommentListActivity.this).load(Constants.decodeURL(sessionManager.getUserOthers().getUserPath()+user.getImage()))
                    .into(binding.inInputComment.civLogUserProfile);
        binding.inInputComment.civLogUserProfile.setOnClickListener(v -> {
            Intent intent = new Intent(NewsCommentListActivity.this,ProfileActivity.class);
            intent.putExtra("userId",user.getId());
            startActivity(intent);
        });
        binding.inInputComment.ivSend.setOnClickListener(v -> {
            if (replyValidate()) {
                if (Constants.isConnectionAvailable(NewsCommentListActivity.this)) {
                    callAddCommentApi(binding.inInputComment.etComment.getText().toString(),user.getId(),newsId);
                } else {
                    noInternetConnection(binding.parent);
                }
            }
        });
    }
    private void callLikeApi(String id,String userId,String type,int position) {
        likeStateChange(type,position);
        JSONObject params = new JSONObject();
        /*try {
            params.put("news_comment_id", id);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_NEW + "news/comments/like", params, response -> {
                dismissProgressDialog();
                try {
                    if (response.getBoolean("error")) {
                        likeStateChange(type, position);
                        JSONObject jObj = new JSONObject(response.toString());
                        JSONArray jsonArrayMsg = jObj.getJSONArray("message");
                        for (int i = 0; i < jsonArrayMsg.length(); i++) {
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Toast.makeText(NewsCommentListActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                likeStateChange(type, position);
            }) {
                *//**
         * Passing some request headers
         *//*
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", SharedPreferenceUtils.getInstance(NewsCommentListActivity.this).getStringValue(Constants.TOKEN_ID, ""));
                    Log.d("token", SharedPreferenceUtils.getInstance(NewsCommentListActivity.this).getStringValue(Constants.TOKEN_ID, ""));
                    headers.put("user_id", userId);
                    return headers;
                }
            };
            jsonObjectRequest.setShouldCache(false);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, TAG_LIKE_POST);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
    private void likeStateChange(String type,int position) {
        mAdapter.getItem(position).setLikes(mAdapter.getItem(position).isLike() ? mAdapter.getItem(position).getLikes() - 1 : mAdapter.getItem(position).getLikes() + 1);
        mAdapter.getItem(position).setLike(!mAdapter.getItem(position).isLike());
        mAdapter.updateItem(position);
    }
    private void callAddCommentApi(String replyText,String userId,String newsId) {
        binding.inInputComment.ivSend.setEnabled(false);
        showProgressDialog(getString(R.string.loading));
        JSONObject params = new JSONObject();
        /*try {
            // params.put("user_id", userId);
            params.put("news_id", newsId);
            params.put("comment", replyText);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_NEW + "news/comments/create", params, response -> {
                dismissProgressDialog();
                binding.inInputComment. ivSend.setEnabled(true);
                CommentResp commentResp = new Gson().fromJson(response.toString(), CommentResp.class);
                mAdapter.addItem(0, commentResp.getComment());
                binding.rvReply.scrollToPosition(0);
                if (!commentResp.isError()) {
                    binding.inInputComment.etComment.setText("");
                }

            }, error -> {
                dismissProgressDialog();
                binding.inInputComment.  ivSend.setEnabled(true);
                Toast.makeText(NewsCommentListActivity.this, getString(R.string.loading), Toast.LENGTH_SHORT).show();
            }) {
                *//**
         * Passing some request headers
         *//*
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", SharedPreferenceUtils.getInstance(NewsCommentListActivity.this).getStringValue(Constants.TOKEN_ID, ""));
                    Log.d("token", SharedPreferenceUtils.getInstance(NewsCommentListActivity.this).getStringValue(Constants.TOKEN_ID, ""));
                    // headers.put("user_id", user.getId());
                    return headers;
                }
            };
            jsonObjectRequest.setShouldCache(false);
            appController.addToRequestQueue(jsonObjectRequest, TAG_ADD_REPLY);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    private boolean replyValidate() {
        if (binding.inInputComment.etComment.getText().length() < 1) {
            Toast.makeText(this,getString(R.string.please_enter_reply),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void findViewById() {
        Glide.with(NewsCommentListActivity.this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
        binding.inInputComment.etComment.setCompoundDrawables(null,null,null,null);
    }
}

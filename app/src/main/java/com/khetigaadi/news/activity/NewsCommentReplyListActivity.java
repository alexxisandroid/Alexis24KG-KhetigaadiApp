package com.khetigaadi.news.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityNewsCommentReplyListBinding;
import com.khetigaadi.news.adapter.ReplyListAdapter;
import com.khetigaadi.news.model.Comment;
import com.khetigaadi.news.model.ReplyResp;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.profile.ProfileActivity;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class NewsCommentReplyListActivity extends BaseActivity {
    private static final String TAG = "NewsCommentReplyListAct";
    private ReplyListAdapter mAdapter;
    private Comment comment;
    private String newsId;
    private final String TAG_ADD_REPLY = "addReply";
    private final String TAG_GET_REPLYS = "getReplys";
    private int page = 1;
    private ReplyResp replys;
    @Inject
    SessionManager sessionManager;
    private ActivityNewsCommentReplyListBinding binding;
    public static void startActivity(Activity newsDetailsActivity, String newsId, Comment comment) {
        Intent intent = new Intent(newsDetailsActivity, NewsCommentReplyListActivity.class);
        intent.putExtra("newsId", newsId);
        intent.putExtra("comment", comment);
        newsDetailsActivity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ActivityNewsCommentReplyListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);

        user=sessionManager.getUser();
        findViewById();
        initToolbar();
        comment = getIntent().getParcelableExtra("comment");
        newsId = getIntent().getStringExtra("newsId");
        initUI();
        if (Constants.isConnectionAvailable(NewsCommentReplyListActivity.this)) {
            getReplys(newsId,comment.getCommentId(),user.getId(),page);
        } else {
            noInternetConnection(binding.parent);
        }
    }
    private void getReplys(String newsId,String commentId,String userId,int page) {
        /*try {
            if (page == 1) {
                binding.llProgress.llProgress.setVisibility(View.VISIBLE);
                binding.rvReply.setVisibility(View.GONE);
            }
            JSONObject params = new JSONObject();
            params.put("news_comment_id", commentId);
            //params.put("page", page);
            Timber.e("getReplys: " + params);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_NEW + "news/comments/reply/show", params, response -> {
                Timber.e("onResponse: " + response);
                try {
                    JSONObject jObj = new JSONObject(response.toString());
                    JSONObject jsonObjectData = jObj.getJSONObject("data");
                    replys = new Gson().fromJson(jsonObjectData.toString(), ReplyResp.class);
                    if (!replys.isError()) {
                        mAdapter.addItems(replys.getReplies());
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                        binding.rvReply.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> {
                Toast.makeText(NewsCommentReplyListActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.llProgress.llProgress.setVisibility(View.GONE);
                binding.rvReply.setVisibility(View.VISIBLE);
            }) {
                *//**
                 * Passing some request headers
                 *//*
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", SharedPreferenceUtils.getInstance(NewsCommentReplyListActivity.this).getStringValue(Constants.TOKEN_ID, ""));
                    Timber.d(SharedPreferenceUtils.getInstance(NewsCommentReplyListActivity.this).getStringValue(Constants.TOKEN_ID, ""));
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
        binding.toolbars.toolbar.setTitle(getString(R.string.reply));
    }
    private void initUI() {
        binding.rvReply.setHasFixedSize(true);
        binding.rvReply.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ReplyListAdapter(this, binding.rvReply, sessionManager);
        binding.rvReply.setAdapter(mAdapter);
        mAdapter.setClickListener((reply, adapterPosition, v) -> {
            if (reply.getUser() != null) {
                Intent intent = new Intent(NewsCommentReplyListActivity.this, ProfileActivity.class);
                intent.putExtra("userId", reply.getUser().getId());
                startActivity(intent);
            } else {
                Toast.makeText(NewsCommentReplyListActivity.this, "Data not available", Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.setOnLoadMoreListener(() -> {
            if (replys != null)
                if ((++page) <= replys.getPages())
                    getReplys(newsId, comment.getCommentId(), user.getId(), page);
        });
        if (user != null && user.getImage() != null)
            Glide.with(NewsCommentReplyListActivity.this).load(Constants.decodeURL(user.getImage()))
                    .into(binding.inInputComment.civLogUserProfile);
        binding.inInputComment.civLogUserProfile.setOnClickListener(v -> {
            Intent intent = new Intent(NewsCommentReplyListActivity.this, ProfileActivity.class);
            intent.putExtra("userId", user.getId());
            startActivity(intent);
        });
        binding.inInputComment.ivSend.setOnClickListener(v -> {
            if (replyValidate()) {
                if (Constants.isConnectionAvailable(NewsCommentReplyListActivity.this)) {
                    callAddReplyApi(binding.inInputComment.etComment.getText().toString(), user.getId(), newsId, comment.getCommentId());
                } else {
                    noInternetConnection(binding.parent);
                }
            }
        });
    }
    private void callAddReplyApi(String replyText,String userId,String newsId,String
            commentId) {
        binding.inInputComment.ivSend.setEnabled(false);
        showProgressDialog(getString(R.string.loading));
        JSONObject params = new JSONObject();
       /* try {
            params.put("news_comment_id", commentId);
            params.put("content", replyText);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_NEW + "news/comments/reply/create", params, response -> {
                dismissProgressDialog();
                binding.inInputComment.ivSend.setEnabled(true);
                AddReplyResp replyResponse = new Gson().fromJson(response.toString(), AddReplyResp.class);
                if (!replyResponse.isError()) {
                    mAdapter.addItem(0, replyResponse.getReply());
                    binding.rvReply.scrollToPosition(0);
                    binding.inInputComment.etComment.setText("");
                }
            }, error -> {
                dismissProgressDialog();
                binding.inInputComment.ivSend.setEnabled(true);
                Toast.makeText(NewsCommentReplyListActivity.this, getString(R.string.loading), Toast.LENGTH_SHORT).show();
            }) {
                *//**
                 * Passing some request headers
                 *//*
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", SharedPreferenceUtils.getInstance(NewsCommentReplyListActivity.this).getStringValue(Constants.TOKEN_ID, ""));
                    Log.d("token", SharedPreferenceUtils.getInstance(NewsCommentReplyListActivity.this).getStringValue(Constants.TOKEN_ID, ""));
                    headers.put("user_id", userId);
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
            Toast.makeText(this, getString(R.string.please_enter_reply), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void findViewById() {
        Glide.with(NewsCommentReplyListActivity.this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
        binding.inInputComment.etComment.setHint(getString(R.string.write_a_reply));
        binding.inInputComment.etComment.setCompoundDrawables(null, null, null, null);
    }
    /*@Override
    protected void onStop() {
        super.onStop();
        if (appController.getRequestQueue() != null) {
            appController.cancelPendingRequests(TAG_ADD_REPLY);
            appController.cancelPendingRequests(TAG_GET_REPLYS);
        }
    }*/
}

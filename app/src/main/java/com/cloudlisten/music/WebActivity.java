package com.cloudlisten.music;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;

import com.cloudlisten.music.databinding.ActivityMainBinding;
import com.cloudlisten.music.utils.AppConfig;
import com.cloudlisten.music.utils.okhttp3.OKHttpCallBack;
import com.cloudlisten.music.utils.okhttp3.OKHttpUtils;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.statelayout.*;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleUtils;

import java.io.IOException;

import okhttp3.Call;

public class WebActivity extends BaseActivity {

    private boolean nointernet = false;
    private  LinearLayout webactivity;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(WebActivity.this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_webpage);
        /*setSupportActionBar(binding.toolbar);*/

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_exit);
        }

        webactivity = (LinearLayout) findViewById(R.id.weblayout);

        showAsInternet();
        Intent intent = getIntent();

        TitleUtils.initTitleBar(WebActivity.this,R.id.xui_titlebar,intent.getStringExtra("titled")).setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_from_right);
                finish();
            }
        });

    }

    private void showAsInternet() {
        OKHttpUtils.newBuilder().url(new AppConfig().apiurl_hitoko)
                .get()
                .build()
                .enqueue(new OKHttpCallBack<UserInfoBean>() {

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        if (nointernet == true) {
                            nointernet = false;
                            webactivity.removeViewAt(0);
                            showWebsite(WebActivity.this,getIntent().getStringExtra("weburl"));
                        } else {
                            showWebsite(WebActivity.this,getIntent().getStringExtra("weburl"));
                        }
                    }

                    @Override
                    public void onError(int code) {
                        if (nointernet != true) {
                            nointernet = true;
                            View netView = getLayoutInflater().inflate(R.layout.content_nonet, null);
                            webactivity.addView(netView);
                            StatefulLayout statefulllayout = (StatefulLayout) findViewById(R.id.netlayout);
                            statefulllayout.showOffline("123", "1234", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    XToastUtils.error(R.string.error_notinternet);
                                }
                            });
                        }

                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.netlayout);
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showAsInternet();
                            }
                        });
                        XToastUtils.error(R.string.error_notinternet);
                        /*Toast.makeText(MainActivity.this,"网络请求异常！",Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (nointernet != true) {
                            nointernet = true;
                            View netView = getLayoutInflater().inflate(R.layout.content_nonet, null);
                            webactivity.addView(netView);
                            StatefulLayout statefulllayout = (StatefulLayout) findViewById(R.id.netlayout);
                            statefulllayout.showOffline("123", "1234", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    XToastUtils.error(R.string.error_notinternet);
                                }
                            });
                        }

                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.netlayout);
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showAsInternet();
                            }
                        });
                        XToastUtils.error(R.string.error_notinternet);
                        /*Toast.makeText(MainActivity.this,"网络请求失败！",Toast.LENGTH_SHORT).show();*/
                    }

                });
    }

    private void showWebsite(final Activity activity, final String weburl) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final WebView webview = new WebView(activity);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.loadUrl(weburl);
                webview.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view,String weburl) {
                        if (weburl.matches("http[s]://.*/app-release.apk")) {
                            XToastUtils.info(R.string.open_in_browser_to_download);
                            Handler handler = new Handler();
                            new Handler().postDelayed(new Runnable()  {
                                @Override
                                public void run() {
                                    Uri uri = Uri.parse(weburl);
                                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                    startActivity(intent);
                                }
                            },500);
                        } else
                        if (weburl.matches("http[s]://.*.lanzou[a-z].com/.*")) {
                            XToastUtils.info(R.string.open_in_browser_to_download);
                            Handler handler = new Handler();
                            new Handler().postDelayed(new Runnable()  {
                                @Override
                                public void run() {
                                    Uri uri = Uri.parse(weburl);
                                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                    startActivity(intent);
                                }
                            },500);
                        } else
                        if (weburl.matches("http[s]://www.123pan.com/.*")) {
                            XToastUtils.info(R.string.open_in_browser_to_download);
                            Handler handler = new Handler();
                            new Handler().postDelayed(new Runnable()  {
                                @Override
                                public void run() {
                                    Uri uri = Uri.parse(weburl);
                                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                    startActivity(intent);
                                }
                            },500);
                        } else
                        if (weburl.matches("http[s]://.*..*")) {
                            Handler handler = new Handler();
                            new Handler().postDelayed(new Runnable()  {
                                @Override
                                public void run() {
                                    Uri uri = Uri.parse(weburl);
                                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                    startActivity(intent);
                                }
                            },500);
                        }

                        return true;
                    }
                });

                webactivity.addView(webview);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_from_right);
        finish();
    }
}

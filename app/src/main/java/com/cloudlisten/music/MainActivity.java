package com.cloudlisten.music;

import static com.cloudlisten.music.Toolutils.ROOT;
import static com.cloudlisten.music.Toolutils.getInt;
import static com.cloudlisten.music.Toolutils.set;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cloudlisten.music.databinding.ActivityMainBinding;
import com.cloudlisten.music.utils.AppConfig;
import com.cloudlisten.music.utils.EnCode;
import com.cloudlisten.music.utils.okhttp3.OKHttpCallBack;
import com.cloudlisten.music.utils.okhttp3.OKHttpUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.king.app.dialog.AppDialog;
import com.king.app.updater.AppUpdater;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListAdapter;
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListItem;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends BaseActivity {

    private long firstBackTime;
    private MediaPlayer mediaPlayer;
    private DrawerLayout drawerlayout;
    private ActivityMainBinding binding;
    private NavigationView navigationview;

    private final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(MainActivity.this);
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        startFunctions();

        /**********150***************************************************************************************************************************************************************/

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,R.string.java_asdeveloper,Snackbar.LENGTH_LONG).setAnchorView(R.id.fab).setAction(R.string.i_am_aware,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });

        binding.fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String apiurl_hitoko = new AppConfig().apiurl_hitoko;
                OKHttpUtils.newBuilder().url(apiurl_hitoko)
                        .get()
                        .build()
                        .enqueue(new OKHttpCallBack<UserInfoBean>() {

                            @Override
                            public void onSuccess(UserInfoBean userInfoBean) {
                                String hitokoto = userInfoBean.getHitokoto().toString();
                                new MaterialDialog.Builder(MainActivity.this)
                                        .customView(R.layout.dialog_contennt_main,false)
                                        .show();
                                XToastUtils.info(hitokoto);
                            }

                            @Override
                            public void onError(int code) {
                                XToastUtils.error(R.string.reqtips_exception);
                                /*Toast.makeText(MainActivity.this,"网络请求异常！",Toast.LENGTH_SHORT).show();*/
                            }

                            @Override
                            public void onFailure(Call call,IOException e) {
                                XToastUtils.error(R.string.reqtips_failed);
                                /*Toast.makeText(MainActivity.this,"网络请求失败！",Toast.LENGTH_SHORT).show();*/
                            }

                        });
                return true;
            }
        });

        /**********150***************************************************************************************************************************************************************/

    }

    private void startFunctions() {
        initWindow();
        checkUpdates();
    }

    private void getUpdatelogs() {
        String appnum = new AppConfig().appnum;
        String system_path = new AppConfig().system_path;
        long system = getInt(ROOT + system_path + "/" + "CheckLogs" + ".cld",appnum,0);
        if (system != 1) {
            new MaterialDialog.Builder(MainActivity.this)
                    .iconRes(R.mipmap.ic_package)
                    .title(R.string.aaa_bbb)
                    .content(R.string.app_updatelogs)
                    .positiveText(R.string.button_beok)
                    .show();
            set(ROOT + system_path + "/" + "CheckLogs" + ".cld",appnum,1);
        } else {
            Calendar rightNow = Calendar.getInstance();
            int year = rightNow.get(Calendar.YEAR);
            int month = rightNow.get(Calendar.MONTH);
            int dates = rightNow.get(Calendar.DATE);
            int value = year + month + dates;
            long systemstate = getInt(ROOT + system_path + "/" + "WebDialogs" + ".cld","state",0);
            if (systemstate != value) {
                set(ROOT + system_path + "/" + "WebDialogs" + ".cld","state",value);
                String siteurl = new AppConfig().siteurl;
                showWebsite(MainActivity.this,siteurl);
            }
        }
    }

    private void checkUpdates() {
        String appver = new AppConfig().appver;
        String apikey = new AppConfig().apikey;
        String appnum = new AppConfig().appnum;
        String apiurl_update = new AppConfig().apiurl_update;
        String checkurl = apiurl_update + "updateapi.php" + "?" + "type" + "=" + "2" + "&" + "serverkey" + "=" + EnCode.Base64Jie(apikey) + "&" + "version" + "=" + appver + "&" + "appnumber" + "=" + appnum;
        OKHttpUtils.newBuilder().url(checkurl)
                .get()
                .build()
                .enqueue(new OKHttpCallBack<UserInfoBean>() {

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        String code = userInfoBean.getCode().toString();
                        String apiver = userInfoBean.getMsg().toString();
                        if (code.matches("203")) {
                            getUpdate(apiver);
                        } else {
                            getUpdatelogs();
                            XToastUtils.success(R.string.tips_lastversion);
                            /*Toast.makeText(MainActivity.this,"当前已是最新版本！",Toast.LENGTH_SHORT).show();*/
                        }
                    }

                    @Override
                    public void onError(int code) {
                        XToastUtils.error(R.string.reqtips_exception);
                        /*Toast.makeText(MainActivity.this,"网络请求异常！",Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onFailure(Call call,IOException e) {
                        XToastUtils.error(R.string.reqtips_failed);
                        /*Toast.makeText(MainActivity.this,"网络请求失败！",Toast.LENGTH_SHORT).show();*/
                    }

                });
    }

    private void getUpdate(String newver) {
        String apikey = new AppConfig().apikey;
        String appnum = new AppConfig().appnum;
        String apiurl_update = new AppConfig().apiurl_update;
        String updateurl = apiurl_update + "updateapi.php" + "?" + "type" + "=" + "1" + "&" + "serverkey" + "=" + EnCode.Base64Jie(apikey) + "&" + "version" + "=" + newver + "&" + "appnumber" + "=" + appnum;
        OKHttpUtils.newBuilder().url(updateurl)
                .get()
                .build()
                .enqueue(new OKHttpCallBack<UserInfoBean>() {

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        String code = userInfoBean.getCode().toString();
                        String updatelog = userInfoBean.getVersion_logs().toString();
                        if (code.matches("200")) {
                            String downloadurl = userInfoBean.getDownload_urls().toString();
                            new MaterialDialog.Builder(MainActivity.this)
                                    .iconRes(R.mipmap.ic_upload)
                                    .title(R.string.app_have_update)
                                    .content(updatelog)
                                    .positiveText(R.string.button_getupdate)
                                    .negativeText(R.string.button_cancel)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            new AppUpdater.Builder(MainActivity.this)
                                                    .setUrl(downloadurl)
                                                    .build()
                                                    .start();
                                            AppDialog.INSTANCE.dismissDialog();
                                        }
                                    })
                                    .show();
                        }
                        if (code.matches("202")) {
                            String downloadurl = userInfoBean.getDownload_browser().toString();
                            new MaterialDialog.Builder(MainActivity.this)
                                    .iconRes(R.mipmap.ic_upload)
                                    .title(R.string.app_have_update)
                                    .content(updatelog)
                                    .positiveText(R.string.button_getupdate)
                                    .negativeText(R.string.button_cancel)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            XToastUtils.info(R.string.open_in_browser_to_download);
                                            Handler handler = new Handler();
                                            new Handler().postDelayed(new Runnable()  {
                                                @Override
                                                public void run() {
                                                    Uri uri = Uri.parse(downloadurl);
                                                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                                    startActivity(intent);
                                                }
                                            },500);
                                            AppDialog.INSTANCE.dismissDialog();
                                        }
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onError(int code) {
                        XToastUtils.error(R.string.reqtips_exception);
                        /*Toast.makeText(MainActivity.this,"网络请求异常！",Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onFailure(Call call,IOException e) {
                        XToastUtils.error(R.string.reqtips_failed);
                        /*Toast.makeText(MainActivity.this,"网络请求失败！",Toast.LENGTH_SHORT).show();*/
                    }

                });
    }

    private void initWindow() {
        Toolbar toolbar = binding.toolbar;
        toolbar.setNavigationIcon(R.mipmap.ic_menu);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } catch (Exception e) {
                MenuAndFunctions(0);
            }
        }

        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationview = (NavigationView) findViewById(R.id.navigationview);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.app_name, R.string.app_name);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem navigationsitem) {
                int navigationsid = navigationsitem.getItemId();

                if (navigationsid == R.id.n_action_signin) {
                    userSignin();
                }
                if (navigationsid == R.id.navigation_items1_1) {
                    String siteurl = new AppConfig().siteurl;
                    showWebsite(MainActivity.this,siteurl);
                }
                if (navigationsid == R.id.navigation_items1_2) {
                    getAppnotice();
                }
                if (navigationsid == R.id.navigation_items1_3) {
                    startActivity(new Intent(MainActivity.this,AboutActivity.class));
                    overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
                }
                if (navigationsid == R.id.navigation_items2_1) {
                    XToastUtils.warning(R.string.stay_tunned);
                }
                if (navigationsid == R.id.navigation_items2_2) {
                    MenuAndFunctions(1);
                }
                if (navigationsid == R.id.navigation_items2_3) {
                    MenuAndFunctions(2);
                }
                if (navigationsid == R.id.navigation_items2_4) {
                    AskUserAbout(1);
                }
                return true;
            }
        });
    }

    private void showWebsite(final Activity activity,final String weburl) {
        /*XToastUtils.warning(R.string.www_ttt);*/
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

                final MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(activity)
                        .setView(webview)
                        .setPositiveButton(R.string.i_am_aware,null)
                        .setNegativeButton(R.string.openbrowser, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String siteurl = new AppConfig().siteurl;
                                XToastUtils.info(R.string.jumpbrowser);
                                Handler handler = new Handler();
                                new Handler().postDelayed(new Runnable()  {
                                    @Override
                                    public void run() {
                                        Uri uri = Uri.parse(siteurl);
                                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                        startActivity(intent);
                                    }
                                },500);
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void userSignin() {
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH);
        int dates = rightNow.get(Calendar.DATE);
        String signin_path = new AppConfig().signin_path;
        long system = getInt(ROOT + signin_path + year + "年" + "/" + (month + 1) + "月" + dates + "日" + ".cld","1" + (month + 1) + dates,0);
        if (system == 1) {
            XToastUtils.info(R.string.loginin_already);
            /*Toast.makeText(MainActivity.this,"您今日已经领取过了，明天再来吧！",Toast.LENGTH_SHORT).show();*/
        } else {
            String apiurl_hitoko = new AppConfig().apiurl_hitoko;
            int frequency = getInt(ROOT + "signin/signdata","totalsign",0);
            OKHttpUtils.newBuilder().url(apiurl_hitoko)
                    .get()
                    .build()
                    .enqueue(new OKHttpCallBack<UserInfoBean>() {

                        @Override
                        public void onSuccess(UserInfoBean userInfoBean) {
                            String hitokoto = userInfoBean.getHitokoto().toString();
                            long frequency = getInt(ROOT + signin_path + "frequency" + ".cld","ststusnum",0);
                            new MaterialDialog.Builder(MainActivity.this)
                                    .iconRes(R.mipmap.ic_calendar)
                                    .title(R.string.loginin_success)
                                    .content("恭喜您今日签到成功，您已累计在这里签到了" + (frequency + 1) + "天，再接再厉，为您赠上一言：\n\n" + "『" + hitokoto + "』")
                                    .positiveText(R.string.button_beok)
                                    .show();
                            set(ROOT + signin_path + "frequency" + ".cld","ststusnum",(frequency + 1));
                            set(ROOT + signin_path + year + "年" + "/" + (month + 1) + "月" + dates + "日" + ".cld","1" + (month + 1) + dates,1);
                        }

                        @Override
                        public void onError(int code) {
                            XToastUtils.error(R.string.reqtips_exception);
                            /*Toast.makeText(MainActivity.this,"网络请求异常！",Toast.LENGTH_SHORT).show();*/
                        }

                        @Override
                        public void onFailure(Call call,IOException e) {
                            XToastUtils.error(R.string.reqtips_failed);
                            /*Toast.makeText(MainActivity.this,"网络请求失败！",Toast.LENGTH_SHORT).show();*/
                        }

                    });
        }
    }

    private void getAppnotice() {
        String apikey = new AppConfig().apikey;
        String apiurl = new AppConfig().apiurl_notice;
        String noticeurl = apiurl + "noticeapi.php" + "?" + "type" + "=" + "1" + "&" + "serverkey" + "=" + EnCode.Base64Jie(apikey);
        OKHttpUtils.newBuilder().url(noticeurl)
                .get()
                .build()
                .enqueue(new OKHttpCallBack<UserInfoBean>() {

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        String code = userInfoBean.getCode().toString();
                        String appnotice = userInfoBean.getAppnotice().toString();
                        if (code.matches("200")) {
                            new MaterialDialog.Builder(MainActivity.this)
                                    .iconRes(R.mipmap.ic_bell)
                                    .title(R.string.app_notice)
                                    .content(appnotice)
                                    .positiveText(R.string.button_beok)
                                    .negativeText(R.string.jumpwwwsite)
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            String siteurl = new AppConfig().siteurl;
                                            Handler handler = new Handler();
                                            new Handler().postDelayed(new Runnable()  {
                                                @Override
                                                public void run() {
                                                    Uri uri = Uri.parse(siteurl);
                                                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                                    startActivity(intent);
                                                }
                                            },200);
                                        }
                                    })
                                    .show();
                        } else {
                            XToastUtils.error(R.string.app_notice_error);
                            /*Toast.makeText(MainActivity.this,"获取APP公告时遇到错误！",Toast.LENGTH_SHORT).show();*/
                        }
                    }

                    @Override
                    public void onError(int code) {
                        XToastUtils.error(R.string.reqtips_exception);
                        /*Toast.makeText(MainActivity.this,"网络请求异常！",Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onFailure(Call call,IOException e) {
                        XToastUtils.error(R.string.reqtips_failed);
                        /*Toast.makeText(MainActivity.this,"网络请求失败！",Toast.LENGTH_SHORT).show();*/
                    }

                });
    }

    private void MenuAndFunctions(int value) {
        switch (value) {
            case 1:
                showOpenList();
                break;
            case 2:
                XToastUtils.info(R.string.start_to_checkupdates);
                /*Toast.makeText(MainActivity.this,"正在开始检查更新！",Toast.LENGTH_SHORT).show();*/
                new Handler().postDelayed(new Runnable()  {
                    @Override
                    public void run() {
                        checkUpdates();
                    }
                },1000);
                break;
            default:
                XToastUtils.error(R.string.appstatus_error);
                /*Toast.makeText(MainActivity.this,"APP状态异常，即将退出！",Toast.LENGTH_SHORT).show();*/
                Handler handler = new Handler();
                new Handler().postDelayed(new Runnable()  {
                    @Override
                    public void run() {
                        finish();
                    }
                },1000);
                break;
        }
    }

    private void showOpenList() {
        String[] openlist = {
                "GOOGLE/GSON",
                "GrenderG/Toasty",
                "OKHTTP/OKHTTPS 3",
                "Material Design 3",
                "JENLY1314/APPUPDATER",
                "HyperionS/SweetAlert",
                "Android Typeface Helper",
                "XUEXIANGJYS/Author Librarys",
                "GetActivity/Author Librarys"
        };

        Map<String, String> openlistUrl = new HashMap<>();

        openlistUrl.put("GOOGLE/GSON", "https://github.com/google/gson");
        openlistUrl.put("GrenderG/Toasty", "https://github.com/");
        openlistUrl.put("OKHTTP/OKHTTPS 3", "https://github.com/square/okhttp");
        openlistUrl.put("Material Design 3", "https://github.com/google/material-design-icons");
        openlistUrl.put("JENLY1314/APPUPDATER", "https://github.com/jenly1314/AppUpdater");
        openlistUrl.put("HyperionS/SweetAlert", "https://github.com/");
        openlistUrl.put("Android Typeface Helper", "https://gitee.com/mirrors/android-typeface-helper?_from=gitee_search");
        openlistUrl.put("XUEXIANGJYS/Author Librarys", "https://github.com/xuexiangjys");
        openlistUrl.put("GetActivity/Author Librarys", "https://github.com/getActivity");

        List<MaterialSimpleListItem> list = new ArrayList<>();
        for (int i = 0; i < openlist.length; i++) {
            list.add(new MaterialSimpleListItem.Builder(MainActivity.this)
                    .content(openlist[i])
                    .build());
        }

        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(list)
                .setOnItemClickListener(new MaterialSimpleListAdapter.OnItemClickListener() {
                    @Override
                    public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(openlistUrl.get(openlist[index]))));
                    }
                });
        new MaterialDialog.Builder(MainActivity.this).adapter(adapter, null)
                .title(R.string.action_openreas)
                .show();
    }

    private void AskUserAbout(int value){
        switch (value) {
            case 1:
                new MaterialDialog.Builder(MainActivity.this)
                        .iconRes(R.mipmap.ic_exit)
                        .title(R.string.ask_about_exit)
                        .positiveText(R.string.button_cancel)
                        .negativeText(R.string.button_beok)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new Handler().postDelayed(new Runnable()  {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                },500);
                            }
                        })
                        .show();
                break;
            default:
                XToastUtils.error(R.string.appstatus_error);
                /*Toast.makeText(MainActivity.this,"APP状态异常，即将退出！",Toast.LENGTH_SHORT).show();*/
                Handler handler = new Handler();
                new Handler().postDelayed(new Runnable()  {
                    @Override
                    public void run() {
                        finish();
                    }
                },1000);
                break;
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId,menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

        }

        if (id == R.id.action_cheknews) {
            MenuAndFunctions(2);
        }

        if (id == R.id.action_deveices) {
            String site_url = new AppConfig().siteurl;
            startActivity(new Intent(MainActivity.this,WebActivity.class).putExtra("weburl",site_url).putExtra("titled",getResources().getString(R.string.about_quick_1)));
            overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
        }

        if (id == R.id.action_openreas) {
            MenuAndFunctions(1);
        }

        if (id == R.id.action_sponsors) {
            String sponsor_url = new AppConfig().sponsor_url;
            startActivity(new Intent(MainActivity.this,WebActivity.class).putExtra("weburl",sponsor_url).putExtra("titled",getResources().getString(R.string.about_quick_3)));
            overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this,AboutActivity.class));
            overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
        }

        if (id == R.id.action_exitapps) {
            AskUserAbout(1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstBackTime > 2000) {
            XToastUtils.toast(R.string.action_exitted);
            /*Toast.makeText(MainActivity.this,"再按一次返回键退出程序", Toast.LENGTH_SHORT).show();*/
            firstBackTime = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
    }
}
package com.cloudlisten.music;

import static com.cloudlisten.music.Toolutils.ROOT;
import static com.cloudlisten.music.Toolutils.getInt;
import static com.cloudlisten.music.Toolutils.set;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.cloudlisten.music.databinding.ActivityMainBinding;
import com.cloudlisten.music.utils.AppConfig;
import com.cloudlisten.music.utils.EnCode;
import com.cloudlisten.music.utils.okhttp3.OKHttpCallBack;
import com.cloudlisten.music.utils.okhttp3.OKHttpUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.king.app.dialog.AppDialog;
import com.king.app.updater.AppUpdater;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.LoadingDialog;
import com.xuexiang.xui.widget.dialog.*;
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListAdapter;
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

public class AboutActivity extends BaseActivity {

    private int imgClick = 0;
    private ListView listview;
    private ImageView imageView;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(AboutActivity.this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_about);
        /*setSupportActionBar(binding.toolbar);*/

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_exit);
        }

        TitleUtils.initTitleBar(AboutActivity.this,R.id.xui_titlebar,getResources().getString(R.string.action_settings)).setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_from_right);
                finish();
            }
        });

        imageView = (ImageView) findViewById(R.id.about_logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgClick == 2) {
                    showSystemInfo(imgClick);
                } else {
                    imgClick ++;
                }
                new Handler().postDelayed(new Runnable()  {
                    @Override
                    public void run() {
                        imgClick = 0;
                    }
                },1000);
            }
        });

        listview = (ListView) findViewById(R.id.about_list);

        List<String> data = Arrays.asList(
                "   " + getResources().getString(R.string.about_item_0),
                "   " + getResources().getString(R.string.about_item_1),
                "   " + getResources().getString(R.string.about_item_2),
                "   " + getResources().getString(R.string.about_item_3),
                "   " + getResources().getString(R.string.about_item_4)
        );
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AboutActivity.this, android.R.layout.simple_list_item_activated_1, data);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 这里处理点击事件
                // 你可以使用 position 获取点击的列表项的位置
                // 也可以使用 parent.getAdapter().getItem(position) 获取点击的列表项的数据
                switch (Long.toString(id)) {
                    case "0":
                        showUpdatelog();
                        break;
                    case "1":
                        subscriptionUpdates();
                        break;
                    case "2":
                        getdeveloper();
                        break;
                    case "3":
                        quickJumpModel();
                        break;
                    case "4":
                        XToastUtils.info(R.string.java_asdeveloper);
                        break;
                    case "5":
                        XToastUtils.info(R.string.start_to_checkupdates);
                        /*Toast.makeText(AboutActivity.this,"正在开始检查更新！",Toast.LENGTH_SHORT).show();*/
                        new Handler().postDelayed(new Runnable()  {
                            @Override
                            public void run() {
                                checkUpdates();
                            }
                        },1000);
                        break;
                }
            }
        });

        TextView textview = (TextView) findViewById(R.id.textview);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String privacy_url = new AppConfig().privacy_url;
                startActivity(new Intent(AboutActivity.this,WebActivity.class).putExtra("weburl",privacy_url).putExtra("titled",getResources().getString(R.string.action_privacys)));
                overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
            }
        });
    }

    private void quickJumpModel() {
        String[] openlist = {
                getResources().getString(R.string.about_quick_0),
                getResources().getString(R.string.about_quick_1),
                getResources().getString(R.string.about_quick_2),
                getResources().getString(R.string.about_quick_3),
                getResources().getString(R.string.about_quick_4),
                getResources().getString(R.string.about_quick_5),
        };

        List<MaterialSimpleListItem> list = new ArrayList<>();
        for (int i = 0; i < openlist.length; i++) {
            list.add(new MaterialSimpleListItem.Builder(AboutActivity.this)
                    .content(openlist[i])
                    .build());
        }

        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(list)
                .setOnItemClickListener(new MaterialSimpleListAdapter.OnItemClickListener() {
                    @Override
                    public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                        switch (index) {
                            case 0:
                                toJunmTheSite(new AppConfig().groupurl);
                                break;
                            case 1:
                                String site_url = new AppConfig().siteurl;
                                startActivity(new Intent(AboutActivity.this,WebActivity.class).putExtra("weburl",site_url).putExtra("titled",getResources().getString(R.string.about_quick_1)));
                                overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
                                break;
                            case 2:
                                toJunmTheSite(new AppConfig().author_blog);
                                break;
                            case 3:
                                String sponsor_url = new AppConfig().sponsor_url;
                                startActivity(new Intent(AboutActivity.this,WebActivity.class).putExtra("weburl",sponsor_url).putExtra("titled",getResources().getString(R.string.about_quick_3)));
                                overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
                                break;
                            case 4:
                                toJunmTheSite(new AppConfig().operation_url);
                                break;
                            case 5:
                                String site_url_2 = new AppConfig().siteurl;
                                startActivity(new Intent(AboutActivity.this,WebActivity.class).putExtra("weburl",site_url_2).putExtra("titled",getResources().getString(R.string.about_quick_1)));
                                overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
                                break;
                        }
                    }
                });

        new MaterialDialog.Builder(AboutActivity.this).adapter(adapter, null)
                .title(R.string.about_item_3)
                .show();
    }

    private void showSystemInfo(int imgClick) {
        if (imgClick == 2) {
            new MaterialDialog.Builder(AboutActivity.this)
                    .customView(R.layout.dialog_contennt_main,false)
                    .show();
        }
    }

    private void subscriptionUpdates() {
        new MaterialDialog.Builder(AboutActivity.this)
                .title(R.string.about_item_1)
                .content(R.string.subscription_updates)
                .input(R.string.aaa_aaa, R.string.app_null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String system_path = new AppConfig().system_path;
                        String submail = dialog.getInputEditText().getText().toString();
                        long system = getInt(ROOT + system_path + "/" + "SubmailState" + ".cld",submail,328938);
                        if (system != 14154) {
                            if (submail == "") {
                                XToastUtils.error(R.string.aaa_eee);
                                /*Toast.makeText(MainActivity.this,"订阅邮箱有效性检验失败！",Toast.LENGTH_SHORT).show();*/
                            } else
                            if (!submail.matches(".*@.*..*")) {
                                XToastUtils.error(R.string.aaa_eee);
                                /*Toast.makeText(MainActivity.this,"订阅邮箱有效性检验失败！",Toast.LENGTH_SHORT).show();*/
                            } else {
                                String apikey = new AppConfig().apikey;
                                String apiurl = new AppConfig().apiurl_subscribe;

                                String noticeurl = apiurl + "subscribe.php" + "?" + "subsrmail" + "=" + submail + "&" + "serverkey" + "=" + EnCode.Base64Jie(apikey);
                                OKHttpUtils.newBuilder().url(noticeurl)
                                        .get()
                                        .build()
                                        .enqueue(new OKHttpCallBack<UserInfoBean>() {

                                            @Override
                                            public void onSuccess(UserInfoBean userInfoBean) {
                                                String code = userInfoBean.getCode().toString();
                                                String msg = userInfoBean.getMsg().toString();
                                                XToastUtils.success(msg);
                                                if (code.matches("200")) {
                                                    set(ROOT + system_path + "/" + "SubmailState" + ".cld",submail,14154);
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
                        } else {
                            XToastUtils.error(R.string.aaa_fff);
                        }
                    }
                })
                .positiveText(R.string.button_submit)
                .negativeText(R.string.button_cancel)
                .cancelable(false)
                .show();
    }

    private void toJunmTheSite(String siteurl) {
        Uri uri = Uri.parse(siteurl);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }

    private void showUpdatelog() {
        new MaterialDialog.Builder(AboutActivity.this)
                .title(R.string.about_item_0)
                .content(R.string.app_updatelogs)
                .positiveText(R.string.button_beok)
                .show();
    }

    private void getdeveloper() {
        String apikey = new AppConfig().apikey;
        String apiurl = new AppConfig().apiurl_notice;
        String noticeurl = apiurl + "noticeapi.php" + "?" + "type" + "=" + "2" + "&" + "serverkey" + "=" + EnCode.Base64Jie(apikey);
        OKHttpUtils.newBuilder().url(noticeurl)
                .get()
                .build()
                .enqueue(new OKHttpCallBack<UserInfoBean>() {

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        String code = userInfoBean.getCode().toString();
                        String appnotice = userInfoBean.getAuthorsay().toString();
                        if (code.matches("200")) {
                            MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(AboutActivity.this)
                                    .setIcon(R.mipmap.ic_incognito)
                                    .setTitle(R.string.about_item_1)
                                    .setMessage(appnotice)
                                    .setPositiveButton(R.string.button_beok,null);
                            alertDialog.show();
                        } else
                        if (code.matches("202")) {
                            XToastUtils.info(R.string.developer_nosay);
                            /*Toast.makeText(MainActivity.this,"开发者暂时还不想说什么！！",Toast.LENGTH_SHORT).show();*/
                        } else {
                            XToastUtils.error(R.string.error_developer_say);
                            /*Toast.makeText(MainActivity.this,"获取开发者说时遇到错误！",Toast.LENGTH_SHORT).show();*/
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
                            new MaterialDialog.Builder(AboutActivity.this)
                                    .iconRes(R.mipmap.ic_upload)
                                    .title(R.string.app_have_update)
                                    .content(updatelog)
                                    .positiveText(R.string.button_getupdate)
                                    .negativeText(R.string.button_cancel)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            new AppUpdater.Builder(AboutActivity.this)
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
                            new MaterialDialog.Builder(AboutActivity.this)
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
                                                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(downloadurl)));
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
                        /*Toast.makeText(AboutActivity.this,"网络请求异常！",Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        XToastUtils.error(R.string.reqtips_failed);
                        /*Toast.makeText(AboutActivityy.this,"网络请求失败！",Toast.LENGTH_SHORT).show();*/
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

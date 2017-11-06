package com.example.gsl.anotherapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;

import com.example.gsl.startservicefromanotherapp.IAppServiceRemoteBinder;

public class MainActivity extends Activity implements View.OnClickListener, ServiceConnection {

    private Intent intent;
    private EditText editInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editInput = (EditText) findViewById(R.id.editInput);
        intent = new Intent();
        intent.setComponent(new ComponentName("com.example.gsl.startservicefromanotherapp","com.example.gsl.startservicefromanotherapp.AppService"));
        findViewById(R.id.btnStartService).setOnClickListener(this);
        findViewById(R.id.btnStopService).setOnClickListener(this);
        findViewById(R.id.btnBindService).setOnClickListener(this);
        findViewById(R.id.btnUnbindService).setOnClickListener(this);
        findViewById(R.id.btnSync).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStartService:
                startService(intent);
                break;
            case R.id.btnStopService:
                stopService(intent);
                break;
            case R.id.btnBindService:
                bindService(intent,this, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btnUnbindService:
                unbindService(this);
                binder = null;
                break;
            case R.id.btnSync:
                if (binder != null){
                    try {
                        binder.setData(editInput.getText().toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        System.out.println("bind service");
        System.out.println(service);
        binder = IAppServiceRemoteBinder.Stub.asInterface(service);//此处不能强制类型转换。因为IAppServiceRemoteBinder不是同一应用中，内存地址分配不一样。
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
    private IAppServiceRemoteBinder binder = null;
}

package com.digital.ayaz.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.digital.ayaz.R;
import com.digital.ayaz.app.MainApplication;
import com.digital.ayaz.brodcast_n_service.ConnectivityReceiver;

public class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    public ProgressDialog mProgress;
    private TextView noInternetMsg;

    public void showProgress() {
        showProgress(getString(R.string.progress_msg));
    }

    public void showProgress(String msg) {
        if (mProgress == null) {
            mProgress = new ProgressDialog(this);
            mProgress.setCancelable(false);
            mProgress.getWindow().setGravity(Gravity.CENTER);
            mProgress.setMessage(msg);
            mProgress.setIndeterminate(true);
        }

        if (!mProgress.isShowing()) {
            mProgress.show();
        }
    }

    public void hideProgress() {
        if (getMainLooper().getThread().equals(Thread.currentThread())) {

            hideProgressInternal();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgressInternal();
                }
            });
        }

    }
    protected void showSnack(Context context, int stringID) {

        Snackbar.make(((Activity) context).findViewById(android.R.id.content), stringID, Snackbar.LENGTH_LONG)
                .show();
    }

    private void hideProgressInternal() {
        if (mProgress != null && mProgress.isShowing() && !isFinishing()) {
            mProgress.dismiss();
        }
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showToast(final String msg) {

        if (getMainLooper().getThread().equals(Thread.currentThread())) {

            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        hideProgress();
        super.onDestroy();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    protected void setToolBar(Toolbar toolBar,String title){
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }
    protected void setInternetConnectionListner(TextView noInternetMsg) {
        MainApplication.getInstance().setConnectivityListener(this);
        this.noInternetMsg = noInternetMsg;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (noInternetMsg != null) {
            if (isConnected) {
                noInternetMsg.setVisibility(View.GONE);
            }
            else {
                noInternetMsg.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(noInternetMsg!=null)
        if(!isNetworkConnected()){
            noInternetMsg.setVisibility(View.VISIBLE);
        }else {
            noInternetMsg.setVisibility(View.GONE);
        }
    }
}

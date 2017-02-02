package com.digital.ayaz.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.digital.ayaz.R;
import com.digital.ayaz.app.MainApplication;
import com.digital.ayaz.brodcast_n_service.ConnectivityReceiver;

public class BaseFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    protected Context mContext;
    protected Toolbar toolbar;
    private ProgressDialog mProgress;
    private TextView noInternetMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (noInternetMsg != null)
            if (!isNetworkConnected()) {
                noInternetMsg.setVisibility(View.VISIBLE);
            } else {
                noInternetMsg.setVisibility(View.GONE);
            }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    protected void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showSnack(Context context, int stringID) {

        Snackbar.make(((Activity) context).findViewById(android.R.id.content), stringID, Snackbar.LENGTH_LONG)
                .show();
    }

    protected void showProgress() {
        showProgress(getString(R.string.progress_msg));
    }

    protected void showProgress(String msg) {
        if (mProgress == null) {
            mProgress = new ProgressDialog(mContext);
            mProgress.setCancelable(false);
            mProgress.getWindow().setGravity(Gravity.CENTER);
            mProgress.setMessage(msg);
            mProgress.setIndeterminate(true);
        }

        if (!mProgress.isShowing()) {
            mProgress.show();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    protected void hideProgress() {
        if (mContext.getMainLooper().getThread().equals(Thread.currentThread())) {

            hideProgressInternal();
        } else {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgressInternal();
                }
            });
        }
    }

    protected void hideProgressInternal() {
        if (mProgress != null && mProgress.isShowing() && !((Activity) mContext).isFinishing()) {
            mProgress.dismiss();
        }
    }

    protected void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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
            } else {
                noInternetMsg.setVisibility(View.VISIBLE);
            }
        }
    }
}
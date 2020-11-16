package com.qinglianyun.loginintercept;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 登陆装载器
 * Created by tang_xqing on 2020/11/12.
 */
public class LoginCarrier implements Parcelable {
    public String mTargetAction;
    public Bundle mbundle;

    public LoginCarrier(String target, Bundle bundle) {
        mTargetAction = target;
        mbundle = bundle;
    }

    /**
     * 目标activity
     *
     * @param ctx
     */
    public void invoke(Context ctx) {
        try {
            Class<?> aClass = Class.forName(mTargetAction);
            Intent intent = new Intent(ctx, aClass);
            if (mbundle != null) {
                intent.putExtras(mbundle);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ctx.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LoginCarrier(Parcel parcel) {
        // 按变量定义的顺序读取
        mTargetAction = parcel.readString();
        mbundle = parcel.readParcelable(Bundle.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // 按变量定义的顺序写入
        parcel.writeString(mTargetAction);
        parcel.writeParcelable(mbundle, flags);
    }

    public static final Parcelable.Creator<LoginCarrier> CREATOR = new Parcelable.Creator<LoginCarrier>() {

        @Override
        public LoginCarrier createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new LoginCarrier(source);
        }

        @Override
        public LoginCarrier[] newArray(int arg0) {
            // TODO Auto-generated method stub
            return new LoginCarrier[arg0];
        }
    };
}

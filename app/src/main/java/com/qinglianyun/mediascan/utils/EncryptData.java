package com.qinglianyun.mediascan.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by tang_xqing on 2020/12/9.
 */
public class EncryptData implements Serializable, Parcelable {

    private static final long serialVersionUID = 6325772086960229602L;
    /**
     * 别名，用于保存，查找
     */
    String alias;

    /**
     * 加密后的内容
     */
    String encryptString;

    /**
     * 初始向量
     */
    byte[] iv;

    public EncryptData(String alias, String encryptString, byte[] iv) {
        this.alias = alias;
        this.encryptString = encryptString;
        this.iv = iv;
    }

    protected EncryptData(Parcel in) {
        alias = in.readString();
        encryptString = in.readString();
        iv = in.createByteArray();
    }

    public static final Creator<EncryptData> CREATOR = new Creator<EncryptData>() {
        @Override
        public EncryptData createFromParcel(Parcel in) {
            return new EncryptData(in);
        }

        @Override
        public EncryptData[] newArray(int size) {
            return new EncryptData[size];
        }
    };

    public String getAlias() {
        return alias;
    }

    public String getEncryptString() {
        return encryptString;
    }

    public byte[] getIv() {
        return iv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alias);
        dest.writeString(encryptString);
        dest.writeByteArray(iv);
    }

    @Override
    public String toString() {
        return "EncryptData{" +
                "alias='" + alias + '\'' +
                ", encryptString='" + encryptString + '\'' +
                ", iv=" + Arrays.toString(iv) +
                '}';
    }
}

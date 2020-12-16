package com.qinglianyun.mediascan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.qinglianyun.mediascan.utils.AESUtils;
import com.qinglianyun.mediascan.utils.CryptoUtils;
import com.qinglianyun.mediascan.utils.CryptoUtilsCopy;
import com.qinglianyun.mediascan.utils.EncryptData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String china = AESUtils.encrypt("china");
        Log.e("测试","密文 = "+china);
        Log.e("测试","明文 = "+AESUtils.decrypt(china));

        CryptoUtils instance = CryptoUtils.getInstance(this);
        EncryptData encrypt = instance.encrypt("user", "china");
        String decrypt = instance.decrypt(encrypt);
        Log.e("测试","Keystore 密文 = "+encrypt);
        Log.e("测试","Keystore 明文 = "+decrypt);

//        CryptoUtilsCopy instance = CryptoUtilsCopy.getInstance(this);
//        EncryptData encrypt = instance.aesEncrypt("user", "china");
//        String decrypt = instance.aesDecrypt(encrypt);
//        Log.e("测试","Keystore 密文 = "+encrypt);
//        Log.e("测试","Keystore 明文 = "+decrypt);

    }
}

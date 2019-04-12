package qst.com.coordinatorlayoutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.stetho.common.LogUtil;

import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;

import qst.com.coordinatorlayoutdemo.exception.CipherException;
import qst.com.coordinatorlayoutdemo.org.tron.common.crypto.ECKey;
import qst.com.coordinatorlayoutdemo.org.tron.common.utils.ByteArray;
import qst.com.coordinatorlayoutdemo.org.tron.common.utils.Utils;
import qst.com.coordinatorlayoutdemo.org.tron.utils.Wallet;
import qst.com.coordinatorlayoutdemo.utils.ResourceUtil;
import qst.com.coordinatorlayoutdemo.utils.StatusBarCompatUtil2;

public class MainActivity extends AppCompatActivity {

    private Button btnCity;
    private Button btnHuadong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarCompatUtil2.setStatusBarFontIconDark(this, true);
        StatusBarCompatUtil2.setStatusBarFontIconDark(this, true, ResourceUtil.getColor(R.color.colorAccent));
        btnCity = (Button) findViewById(R.id.btn_city);
        btnHuadong = (Button) findViewById(R.id.btn_huadong);
        btnHuadong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoordinatorLayoutActivity.open(MainActivity.this);
            }
        });
        btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Provider sc = Security.getProvider("SC");
                LogUtil.e("是否等于null" + sc + "");

                Wallet wallet = new Wallet(true);
                wallet.setWalletName("11");
                wallet.setColdWallet(true);
                byte[] privateKey = wallet.getPrivateKey();
                String privateKeyString = ByteArray.toHexString(privateKey);
                LogUtil.e("私钥为:" + privateKeyString);
                try {
                    String address = private2Address(privateKey);
                    Log.e("pppppp", "store: 地址为:" + address);
                } catch (CipherException e) {


                }
//                ECKey ecKey = ECKey.fromPrivate(privateKey);
//                byte[] address = ecKey.getAddress();
//                byte[] address = public2Address(wallet.getPublicKey());
//                String address = wallet.getAddress();//地址

//                WalletManager.store(wallet, "111111");
            }
        });
    }

    private static String private2Address(byte[] privateKey) throws CipherException {
        ECKey eCkey;
        if (privateKey == null || privateKey.length == 0) {
            eCkey = new ECKey(Utils.getRandom());  //Gen new Keypair
        } else {
            eCkey = ECKey.fromPrivate(privateKey);
        }

        byte[] publicKey0 = eCkey.getPubKey();
        byte[] publicKey1 = private2Public(eCkey.getPrivKeyBytes());
        if (!Arrays.equals(publicKey0, publicKey1)) {
            throw new CipherException("publickey error");
        }

        byte[] address0 = eCkey.getAddress();
        Log.e("pppppp:", " Address:未编码之前 " + ByteArray.toHexString(address0));
        String base58checkAddress0 = Wallet.encode58Check(address0);
        return base58checkAddress0;
    }


    private static byte[] private2Public(byte[] privateKey) {
        BigInteger privKey = new BigInteger(1, privateKey);
        ECPoint point = ECKey.CURVE.getG().multiply(privKey);
        return point.getEncoded(false);
    }

    //    private static byte[] public2Address(byte[] publicKey) {
//        byte[] hash = Hash.sha3(copyOfRange(publicKey, 1, publicKey.length));
//        byte[] address = copyOfRange(hash, 11, hash.length);
//        address[0] = Wallet.getAddressPreFixByte();
//        return address;
//    }
    ArrayList<String> imgList = new ArrayList<String>();

    public void upLoadAllImg() {

    }
}

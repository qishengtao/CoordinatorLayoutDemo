package qst.com.coordinatorlayoutdemo.utils;

import org.bitcoinj.core.Sha256Hash;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Arrays;

import qst.com.coordinatorlayoutdemo.exception.CipherException;
import qst.com.coordinatorlayoutdemo.org.tron.common.crypto.ECKey;
import qst.com.coordinatorlayoutdemo.org.tron.common.crypto.Hash;
import qst.com.coordinatorlayoutdemo.org.tron.common.utils.Base58;
import qst.com.coordinatorlayoutdemo.org.tron.common.utils.ByteArray;
import qst.com.coordinatorlayoutdemo.org.tron.common.utils.Utils;

import static java.util.Arrays.copyOfRange;

/**
 * @updateDts 2019/4/1
 */
public class TRXUtils {
    public static String getTRXAddress(String privKey) {
        String address = null;
        try {
            address = private2Address(ByteArray.fromHexString(privKey));
        } catch (CipherException e) {
            e.printStackTrace();
            return address;
        }
        return address;
    }

    private static String private2Address(byte[] privateKey) throws CipherException {
        ECKey eCkey;//注意  包路径
        if (privateKey == null) {
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
        byte[] address1 = public2Address(publicKey0);
        if (!Arrays.equals(address0, address1)) {
            throw new CipherException("address error");
        }
        System.out.println("Address: " + ByteArray.toHexString(address0));

        String base58checkAddress0 = encode58Check(address0);
        String base58checkAddress1 = address2Encode58Check(address0);
        if (!base58checkAddress0.equals(base58checkAddress1)) {
            throw new CipherException("base58checkAddress error");
        }

        return base58checkAddress1;
    }

    //aaaaaa
    public static String encode58Check(byte[] input) {
        byte[] hash0 = Sha256Hash.hash(input);
        byte[] hash1 = Sha256Hash.hash(hash0);
        byte[] inputCheck = new byte[input.length + 4];
        System.arraycopy(input, 0, inputCheck, 0, input.length);
        System.arraycopy(hash1, 0, inputCheck, input.length, 4);
        return Base58.encode(inputCheck);
    }

    private static byte[] private2Public(byte[] privateKey) {
        BigInteger privKey = new BigInteger(1, privateKey);
        ECPoint point = ECKey.CURVE.getG().multiply(privKey);
        return point.getEncoded(false);
    }

    public static String address2Encode58Check(byte[] input) {
        byte[] hash0 = Sha256Hash.hash(input);

        byte[] hash1 = Sha256Hash.hash(hash0);

        byte[] inputCheck = new byte[input.length + 4];

        System.arraycopy(input, 0, inputCheck, 0, input.length);
        System.arraycopy(hash1, 0, inputCheck, input.length, 4);

        return Base58.encode(inputCheck);
    }

    private static byte[] public2Address(byte[] publicKey) {
        byte[] hash = Hash.sha3(copyOfRange(publicKey, 1, publicKey.length));
        byte[] address = copyOfRange(hash, 11, hash.length);
        //  private static byte addressPreFixByte = 65;  源码
//        address[0] = Wallet.getAddressPreFixByte();
        address[0] = 65;
        return address;
    }

}

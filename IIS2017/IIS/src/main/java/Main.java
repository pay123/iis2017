import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.common.hash.*;

public class Main
{
    public static final BigInteger E = BigInteger.valueOf(65537);
    public static final BigInteger N = new BigInteger("581696833723949335177");
    public static final String CIPHERTEXT = "GVHIUXNYQXWUGB";

    public static void main(String[] args)
    {
        //----------------------------------------------------------------------
        // 1 Public-Key Cryptography - RSA
        //
        Cryptography c = new Cryptography();
        BigInteger cipher = c.encode(CIPHERTEXT);

        BigInteger p = c.factor(N);
        System.out.println("p: " + p);

        BigInteger q = N.divide(p);
        System.out.println("q: " + q);

        BigInteger phi_n = (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)));
        BigInteger d = E.modInverse(phi_n);
        System.out.println("d: " + d);

        BigInteger m = cipher.modPow(d, N);
        System.out.println("m: " + m);

        System.out.println("Decoding " + m + " ... " + c.decode(m));

        //----------------------------------------------------------------------
        // 2 Hash-Collision Search
        //
        HashCode referenceHash = Hashing.sha256().hashString("10301231030456", StandardCharsets.UTF_8);
        System.out.println(referenceHash.toString());

        byte[] hashBytes = referenceHash.asBytes();
        System.out.println(hashBytes.length);

        byte[] upper = new byte[4];
        byte[] lower = new byte[4];

        List<byte[]> blocks = new ArrayList<>();
        for (int i = 0; i < 8; i++)
        {
            byte[] dest = new byte[4];
//            System.arraycopy();
            for (int j = 0; j < 4; j++)
            {
                dest[j] = hashBytes[i+j];
            }
//            System.out.println(referenceHash.writeBytesTo(dest, 0, 4));
            blocks.add(dest);
        }

        for (int i = 0; i < 4; i++)
        {
            int b1 = (int)blocks.get(4)[i];
            int b2 = (int)blocks.get(5)[i];
            int b3 = (int)blocks.get(6)[i];
            int b4 = (int)blocks.get(7)[i];

            upper[i] = (byte)(0xff & (b1 ^ b2 ^ b3 ^ b4));
        }

        for (int i = 0; i < 4; i++)
        {
            int b1 = (int)blocks.get(0)[i];
            int b2 = (int)blocks.get(1)[i];
            int b3 = (int)blocks.get(2)[i];
            int b4 = (int)blocks.get(3)[i];

            lower[i] = (byte)(0xff & (b1 ^ b2 ^ b3 ^ b4));
        }

//        int i = 0;
//        for (byte b1 : blocks.get(4))
//        {
//            for (byte b2 : blocks.get(5))
//            {
//                for (byte b3 : blocks.get(6))
//                {
//                    for (byte b4 : blocks.get(7))
//                    {
//                        int xor = (int)b1 ^ (int)b2 ^ (int)b3 ^ (int)b4;
//
////                        upper[i] = (byte)(0xff & xor);
//                        i++;
//                    }
//                }
//            }
//        }
//
//        int j = 0;
//        for (byte b1 : blocks.get(0))
//        {
//            for (byte b2 : blocks.get(1))
//            {
//                for (byte b3 : blocks.get(2))
//                {
//                    for (byte b4 : blocks.get(3))
//                    {
//                        int xor = (int)b1 ^ (int)b2 ^ (int)b3 ^ (int)b4;
//
////                        lower[j] = (byte)(0xff & xor);
//                        j++;
//                    }
//                }
//            }
//        }

        byte[] bytes = new byte[upper.length + lower.length];
        System.arraycopy(upper, 0, bytes, 0, upper.length);
        System.arraycopy(lower, 0, bytes, upper.length, lower.length);

        System.out.println(HashCode.fromBytes(bytes).toString());
    }
}

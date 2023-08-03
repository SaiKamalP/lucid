package com.kamal.lucid;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/* loaded from: classes.dex */
public class ObjectSerializer {
    public static String serialize(Serializable serializable) throws IOException {
        if (serializable == null) {
            return "";
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
            return encodeBytes(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new IOException("Serialization error: " + e.getMessage(), e);
        }
    }

    public static Object deserialize(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            return new ObjectInputStream(new ByteArrayInputStream(decodeBytes(str))).readObject();
        } catch (Exception e) {
            throw new IOException("Deserialization error: " + e.getMessage(), e);
        }
    }

    public static String encodeBytes(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bArr.length; i++) {
            stringBuffer.append((char) (((bArr[i] >> 4) & 15) + 97));
            stringBuffer.append((char) ((bArr[i] & 15) + 97));
        }
        return stringBuffer.toString();
    }

    public static byte[] decodeBytes(String str) {
        byte[] bArr = new byte[str.length() / 2];
        for (int i = 0; i < str.length(); i += 2) {
            int i2 = i / 2;
            bArr[i2] = (byte) ((str.charAt(i) - 'a') << 4);
            bArr[i2] = (byte) (bArr[i2] + (str.charAt(i + 1) - 'a'));
        }
        return bArr;
    }
}

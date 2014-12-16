package com.example.sdaapp01.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.net.Uri;

public class Util {
	private static String dumpHEX(final byte[] sendData) {
		String StrHex = "";
		for (int i = 0; i < sendData.length; i++)
			// StrHex += Integer.toHexString(sendData[i]) + ":";
			StrHex += String.format("%02x:", sendData[i] & 0x0ff);

		L.e("@@@@@ [" + StrHex + "]");

		return StrHex;
	}

	public String readStringFromUri(Context context, final String uri) {
		L.e("@@@@@ readStringFromUri()");

		String str = null;
		byte[] data = null;
		try {
			// コンテンツプロバイダが提供するファイルへのアクセス
			InputStream in = context.getContentResolver().openInputStream(
					Uri.parse(uri));
			data = in2data(in);
			L.e("@@@@@ readStringFromUri() available=" + in.available());
			L.e("@@@@@ readStringFromUri() str=_" + Uri.parse(uri) + "_" + str
					+ "_");
		} catch (Exception e) {
			L.e("@@@@@ readStringFromUri() error??");
		}

		if (data.length > 0) {
			L.e("@@@@@ readStringFromUri() data=");
			dumpHEX(data);
			str = new String(data);
		}
		return str;
	}

	public static String readHexFromUri(Context context, final String uri) {
		L.e("@@@@@ readStringFromUri()");

		String str = null;
		byte[] data = null;
		try {
			// コンテンツプロバイダが提供するファイルへのアクセス
			InputStream in = context.getContentResolver().openInputStream(
					Uri.parse(uri));
			data = in2data(in);
			L.e("@@@@@ readStringFromUri() available=" + in.available());
			L.e("@@@@@ readStringFromUri() str=_" + Uri.parse(uri) + "_" + str
					+ "_");
		} catch (Exception e) {
			L.e("@@@@@ readStringFromUri() error??");
		}

		if (data.length > 0) {
			L.e("@@@@@ readStringFromUri() data=");
			str = dumpHEX(data);
		}
		return str;
	}

	public static byte[] readBytesFromUri(Context context, final String uri) {
		byte[] data = null;
		try {
			// コンテンツプロバイダが提供するファイルへのアクセス
			InputStream in = context.getContentResolver().openInputStream(
					Uri.parse(uri));
			data = in2data(in);
			L.i("@@@@@ readStringFromUri() available=" + in.available());
			L.i("@@@@@ readStringFromUri() str=_" + Uri.parse(uri));
		} catch (Exception e) {
			L.i("@@@@@ readStringFromUri() error??");
		}

		return data;
	}

	// 入力ストリーム→データ
	public static byte[] in2data(InputStream in) throws Exception {
		byte[] w = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			while (true) {
				int size = in.read(w);
				if (size <= 0)
					break;
				out.write(w, 0, size);
				L.i("@@@@@ in2data() size=" + size);
			}
			out.close();
			in.close();
			return out.toByteArray();
		} catch (Exception e) {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (Exception e2) {
			}
			throw e;
		}
	}

}

package co.ue.edu.huertaconectaapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Comprime imágenes para guardarlas como BLOB en SQLite (tamaño razonable).
 */
public final class ImageUtils {

    private static final int MAX_DIMENSION = 1280;
    private static final int JPEG_QUALITY = 82;

    private ImageUtils() {}

    public static byte[] uriToJpegBlob(Context context, Uri uri) throws IOException {
        Bitmap decoded;
        try (InputStream in = context.getContentResolver().openInputStream(uri)) {
            if (in == null) return null;
            decoded = BitmapFactory.decodeStream(in);
        }
        if (decoded == null) return null;

        int w = decoded.getWidth();
        int h = decoded.getHeight();
        float scale = 1f;
        if (w > MAX_DIMENSION || h > MAX_DIMENSION) {
            scale = Math.min((float) MAX_DIMENSION / w, (float) MAX_DIMENSION / h);
        }
        Bitmap scaled = scale < 1f
            ? Bitmap.createScaledBitmap(decoded, Math.round(w * scale), Math.round(h * scale), true)
            : decoded;
        if (scaled != decoded) {
            decoded.recycle();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out);
        scaled.recycle();
        return out.toByteArray();
    }
}

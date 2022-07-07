package com.android.zbarandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.zbarandroid.utils.YUVUtils;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;

public class ImageActivity extends AppCompatActivity {
    private ImageScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        scanner = new ImageScanner();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qr_test9).copy(Bitmap.Config.ARGB_8888, true);
        decode(bitmap.getWidth(),bitmap.getHeight(),YUVUtils.getNV21(bitmap.getWidth(),bitmap.getHeight(),bitmap));
    }

    private void decode(int width, int height, byte[] data) {
        Image barcode = new Image(width, height, "Y800");
        barcode.setData(data);
        int result = scanner.scanImage(barcode);
        if (result != 0) {
            StringBuilder stringBuilder = new StringBuilder();

            for (Symbol sym : scanner.getResults()) {

                Log.d("QL", "GOT DATA: \n" + sym.getData() + "\n" + "Type: " + sym.getType());
                stringBuilder.append(sym.getData());
                stringBuilder.append("\n");
            }

            if (stringBuilder.length() > 0) {
                TextView tvResult = findViewById(R.id.tv_result);
                tvResult.setText(stringBuilder.toString());
            }
        }
    }
}
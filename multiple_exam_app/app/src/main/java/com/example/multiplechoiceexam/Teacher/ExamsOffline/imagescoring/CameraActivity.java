package com.example.multiplechoiceexam.Teacher.ExamsOffline.imagescoring;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.multiplechoiceexam.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.pqpo.smartcropperlib.view.CropImageView;
import pub.devrel.easypermissions.EasyPermissions;

public class CameraActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String EXTRA_FROM_ALBUM = "extra_from_album";
    public static final String EXTRA_CROPPED_FILE = "extra_cropped_file";
    public static final String EXTRA_CROPPED_FILES = "extra_cropped_files";
    private static final int REQUEST_CODE_TAKE_PHOTO = 100;
    private static final int REQUEST_CODE_SELECT_ALBUM = 200;

    CropImageView ivCrop;
    Button btnCancel;
    Button btnOk;
    List<File> mCroppedFiles;
    boolean mFromAlbum;
    File mCroppedFile;

    File tempFile;

    public static Intent getJumpIntent(Context context, boolean fromAlbum, File croppedFile) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra(EXTRA_FROM_ALBUM, fromAlbum);
        intent.putExtra(EXTRA_CROPPED_FILE, croppedFile);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ivCrop = findViewById(R.id.iv_crop);
        btnCancel = findViewById(R.id.btn_cancel);
        btnOk = findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        btnOk.setOnClickListener(v -> {
            if (ivCrop.canRightCrop()) {
                Bitmap crop = ivCrop.crop();
                if (crop != null) {
                    saveImage(crop, mCroppedFile);
                    // Gửi kết quả trở lại màn hình gọi
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EXTRA_CROPPED_FILE, mCroppedFile);
                    setResult(RESULT_OK, resultIntent);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            } else {
                Toast.makeText(CameraActivity.this, "cannot crop correctly", Toast.LENGTH_SHORT).show();
            }
        });
        mFromAlbum = getIntent().getBooleanExtra(EXTRA_FROM_ALBUM, true);
        mCroppedFile = (File) getIntent().getSerializableExtra(EXTRA_CROPPED_FILE);
        if (mCroppedFile == null) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        tempFile = new File(getExternalFilesDir("img"), "temp.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            EasyPermissions.requestPermissions(
                    CameraActivity.this,
                    "申请权限",
                    0,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA);
        } else {
            selectPhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> list) {
        // Some permissions have been granted
        // ...
        selectPhoto();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> list) {
        // Some permissions have been denied
        // ...
    }

    private void selectPhoto() {
        if (mFromAlbum) {
            Intent selectIntent = new Intent(Intent.ACTION_PICK);
            selectIntent.setType("image/*");
            selectIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            if (selectIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(selectIntent, REQUEST_CODE_SELECT_ALBUM);
            }
        } else {
            Intent startCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this, "com.example.multiplechoiceexam.fileprovider", tempFile);
            } else {
                uri = Uri.fromFile(tempFile);
            }
            startCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (startCameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(startCameraIntent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        Bitmap selectedBitmap = null;
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && tempFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(tempFile.getPath(), options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = calculateSampleSize(options);
            selectedBitmap = BitmapFactory.decodeFile(tempFile.getPath(), options);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(selectedBitmap, 0, 0, selectedBitmap.getWidth(), selectedBitmap.getHeight(), matrix, true);
            selectedBitmap = rotatedBitmap;
        } else if (requestCode == REQUEST_CODE_SELECT_ALBUM && data != null) {
            handleMultipleImages(data);
            return;
        }
        if (selectedBitmap != null) {
            ivCrop.setImageToCrop(selectedBitmap);
        }
    }
    private void handleMultipleImages(Intent data) {
        mCroppedFiles = new ArrayList<>();
        List<Uri> croppedUris = new ArrayList<>();

        if (data.getClipData() != null) {
            int numberImages = data.getClipData().getItemCount();
            for (int i = 0; i < numberImages; i++) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                Bitmap selectedBitmap = decodeUriToBitmap(imageUri);
                if (selectedBitmap != null) {
                    mCroppedFile = new File(getExternalFilesDir("img"), "cropped_image_" + i + ".jpg");
                    mCroppedFiles.add(mCroppedFile);
                    croppedUris.add(imageUri);
                }
            }
        } else if (data.getData() != null) {
            Uri imageUri = data.getData();
            Bitmap selectedBitmap = decodeUriToBitmap(imageUri);
            if (selectedBitmap != null) {
                mCroppedFile = new File(getExternalFilesDir("img"), "cropped_image.jpg");
                mCroppedFiles.add(mCroppedFile);
                croppedUris.add(imageUri);
                ivCrop.setImageToCrop(selectedBitmap);
            }
        }

        // Gửi danh sách các Uri trở lại
        Intent resultIntent = new Intent();
        resultIntent.putParcelableArrayListExtra(EXTRA_CROPPED_FILES, (ArrayList<Uri>) croppedUris);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    private void saveImage(Bitmap bitmap, File saveFile) {
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Bitmap decodeUriToBitmap(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = calculateSampleSize(options);
            return BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private int calculateSampleSize(BitmapFactory.Options options) {
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int sampleSize = 1;
        int destHeight = 1500;
        int destWidth = 1500;
        if (outHeight > destHeight || outWidth > destHeight) {
            if (outHeight > outWidth) {
                sampleSize = outHeight / destHeight;
            } else {
                sampleSize = outWidth / destWidth;
            }
        }
        if (sampleSize < 1) {
            sampleSize = 1;
        }
        return sampleSize;
    }
}


//
//package com.example.multiplechoiceexam.Teacher.ExamsOffline.imagescoring;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.FileProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.Manifest;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.multiplechoiceexam.Api.ApiService;
//import com.example.multiplechoiceexam.R;
//import com.example.multiplechoiceexam.Api.RetrofitClient;
//import com.example.multiplechoiceexam.Utils.Utility;
//import com.example.multiplechoiceexam.dto.upload.TestImageResponse;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;
//import com.yalantis.ucrop.UCrop;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
//public class UploadImageActivity extends AppCompatActivity {
//    private RecyclerView imgRecyclerview;
//    private Button pickImgBtn;
//    private TextView imgCount;
//    private ArrayList<Uri> imageList;
//    private ImageAdapter imageAdapter;
//    private FloatingActionButton done_img;
//    private String currentPhotoPath;
//    private final int REQUESTCODE_GALLEY =  123;
//    private final int REQUEST_IMAGE_CAPTURE =  115;
//    private EditText classCode_edt;
//    private String permissions[] = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA,
//            Manifest.permission.READ_MEDIA_IMAGES
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_image);
//        imageList = new ArrayList<>();
//        pickImgBtn =findViewById(R.id.pickImage);
//        imgCount = findViewById(R.id.imageCount_tv);
//        imageAdapter = new ImageAdapter(imageList);
//        imgRecyclerview = findViewById(R.id.recycler_image);
//        done_img = findViewById(R.id.upload_img);
//        classCode_edt = findViewById(R.id.classCode_edt);
//        // set layout
//        /*imgRecyclerview.setLayoutManager(new GridLayoutManager(this,2));*/
//        imgRecyclerview.setLayoutManager(new LinearLayoutManager(this));
//        imgRecyclerview.setAdapter(imageAdapter);
//        ProgressDialog progressDialog = new ProgressDialog(UploadImageActivity.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Uploading...");
//
//
//        pickImgBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog dialog = new Dialog(UploadImageActivity.this);
//                dialog.setContentView(R.layout.dialog_option_select_img);
//                ImageView camera = (ImageView) dialog.findViewById(R.id.camera_img);
//                ImageView galley = (ImageView) dialog.findViewById(R.id.galley_img);
//                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                galley.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // check permission for galley
//                        if (ActivityCompat.checkSelfPermission(
//                                UploadImageActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
//                                ActivityCompat.checkSelfPermission(
//                                        UploadImageActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
//                                ActivityCompat.checkSelfPermission(
//                                        UploadImageActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED &&
//                                ActivityCompat.checkSelfPermission(
//                                        UploadImageActivity.this, permissions[3]) != PackageManager.PERMISSION_GRANTED) {
//
//                            // if permission not granted then request
//                            ActivityCompat.requestPermissions(UploadImageActivity.this,permissions, REQUESTCODE_GALLEY);
//                        }
//                        else {
//                            openGalley();
//                            dialog.dismiss();
//                        }
//
//                    }
//                });
//                // click camera
//                camera.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // check permission for camera
//                        if (ActivityCompat.checkSelfPermission(
//                                UploadImageActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
//                                ActivityCompat.checkSelfPermission(
//                                        UploadImageActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
//                                ActivityCompat.checkSelfPermission(
//                                        UploadImageActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
//
//                            // if permission not granted then request
//                            requestPermissions(permissions, REQUESTCODE_GALLEY);
//                        }
//                        else {
//                            String fileName = "photo";
//                            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                            try {
//                                File imageFile= File.createTempFile(fileName,".jpg",storageDir);
//                                currentPhotoPath = imageFile.getAbsolutePath();
//                                Uri imageUri =  FileProvider.getUriForFile(UploadImageActivity.this,
//                                        "com.example.multiplechoiceexam.fileprovider",imageFile);
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//                                startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
//                                dialog.dismiss();
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//
//
//                    }
//                });
//                dialog.show();
//            }
//        });
//
//
//
//        // click upload
//        done_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String examClassCode = classCode_edt.getText().toString().trim();
//                if(examClassCode.isEmpty()){
//                    classCode_edt.setError("Enter classCode");
//                    return;
//                }
//                progressDialog.show();
//                List<MultipartBody.Part> partList = new ArrayList<>();
//                // Thực hiện chuyển đổi cho mỗi URI
//                for (Uri imageUri : imageList) {
//                    // Chuyển đổi Uri thành File
//                    String newFileName = generateFileName();
//
//                    // Chuyển đổi Uri thành đường dẫn mới với tên tệp mới
//                    String newPath = getRealPathFromURI(imageUri);
//
//                    // Tạo File mới từ đường dẫn mới
//                    File file = new File(newPath);
//                    // Tạo RequestBody từ File
//                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", newFileName, requestFile);
//                    partList.add(filePart);
//                }
//
//                // get api service
//                ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
//
//
//                Call<Void> call = apiService.uploadStudentTestImages(examClassCode,partList);
//                call.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(@NotNull Call<Void> call,@NotNull Response<Void> response) {
//                        progressDialog.dismiss();
//                        if (response.isSuccessful()) {
//                            // Xử lý phản hồi thành công
//                            Utility.showToast(UploadImageActivity.this,"Upload Successfully !");
//                            finish();
//                            // ...
//                        } else {
//                            Utility.showToast(UploadImageActivity.this,"Error: " + response.code() + response.message() );
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
//                        Utility.showToast(UploadImageActivity.this, t.getMessage());
//                        Log.e("LOG_BUG", "onFailure: " + t.getMessage() );
//
//                        progressDialog.dismiss();
//
//                    }
//                });
//
//
//
//            }
//        });
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUESTCODE_GALLEY) { // Kiểm tra mã yêu cầu quyền
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Utility.showToast(UploadImageActivity.this,"Permission granted !!");
//            } else {
//                Utility.showToast(UploadImageActivity.this,"The app needs permission to access gallery");
//            }
//        }
//    }
//
//
//    private void openGalley() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_PICK);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);
//    }
//    private String generateFileName() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
//        String timeStamp = sdf.format(new Date());
//        return "IMG_" + timeStamp +  getRandomString(6) +".jpg"; // Thay ".jpg" bằng phần mở rộng của file ảnh thích hợp.
//    }
//    private String getRandomString(int length) {
//        // Chuỗi ký tự để tạo mã ngẫu nhiên
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        StringBuilder sb = new StringBuilder();
//
//        // Tạo mã ngẫu nhiên bằng cách chọn ký tự ngẫu nhiên từ chuỗi characters
//        for (int i = 0; i < length; i++) {
//            int index = (int) (Math.random() * characters.length());
//            sb.append(characters.charAt(index));
//        }
//
//        return sb.toString();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUESTCODE_GALLEY && resultCode == RESULT_OK) {
//            assert data != null;
//            if (data.getClipData() != null) {
//                int numberImage = data.getClipData().getItemCount();
//                for (int i = 0; i < numberImage; i++) {
//                    imageList.add(data.getClipData().getItemAt(i).getUri());
//                }
//
//            } else if (data.getData() != null) {
//                String imgurl = data.getData().getPath();
//                imageList.add(Uri.parse(imgurl));
//            }
//            // Thông báo cho Adapter cập nhật lại dữ liệu
//            imageAdapter.notifyDataSetChanged();
//            imgCount.setText("Image(" + imageList.size() + ")");
//            done_img.setVisibility(View.VISIBLE);
//            imgCount.setVisibility(View.VISIBLE);
//            classCode_edt.setVisibility(View.VISIBLE);
//            classCode_edt.requestFocus();
//
//        }
//
//        // result capture
//        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
//            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
//            if (bitmap != null) {
//                int rotationDegrees = 90;
//                Matrix matrix = new Matrix();
//                matrix.postRotate(rotationDegrees);
//                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                if (rotatedBitmap != null) {
//                    // Lấy thời gian hiện tại dưới dạng chuỗi
//                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//
//                    // Tạo tên file hình ảnh từ thời gian hiện tại
//                    String imageFileName = "Image_" + timeStamp + ".jpg";
//
//                    // Tiếp tục với mã lưu hình ảnh bên trên, nhưng thay "Image Title" bằng imageFileName
//                    String path = MediaStore.Images.Media.insertImage(UploadImageActivity.this.getContentResolver(), rotatedBitmap, imageFileName, null);
//                    if (path != null) {
//                        Uri imageUri = Uri.parse(path);
//                        imageList.add(imageUri);
//                        imageAdapter.notifyDataSetChanged();
//                        imgCount.setText("Image(" + imageList.size() + ")");
//                        done_img.setVisibility(View.VISIBLE);
//                        imgCount.setVisibility(View.VISIBLE);
//                        classCode_edt.setVisibility(View.VISIBLE);
//                        classCode_edt.requestFocus();
//                    } else {
//                        Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(this, "Unable to rotate bitmap", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "Failed to load bitmap", Toast.LENGTH_SHORT).show();
//            }}
//    }
//
//
//    private String getRealPathFromURI(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        if (cursor == null) return null;
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String path = cursor.getString(column_index);
//        cursor.close();
//        return path;
//    }
//
//}


package com.example.multiplechoiceexam.Teacher.ExamsOffline.imagescoring;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.FileProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.Manifest;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.multiplechoiceexam.Api.ApiService;
//import com.example.multiplechoiceexam.R;
//import com.example.multiplechoiceexam.Api.RetrofitClient;
//import com.example.multiplechoiceexam.Utils.Utility;
//import com.example.multiplechoiceexam.dto.upload.TestImageResponse;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;
//import com.yalantis.ucrop.UCrop;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
//public class UploadImageActivity extends AppCompatActivity {
//    private RecyclerView imgRecyclerview;
//    private Button pickImgBtn;
//    private TextView imgCount;
//    private ArrayList<Uri> imageList;
//    private ImageAdapter imageAdapter;
//    private FloatingActionButton done_img;
//    private String currentPhotoPath;
//    private final int REQUESTCODE_GALLEY =  123;
//    private final int REQUEST_IMAGE_CAPTURE =  115;
//    private EditText classCode_edt;
//    private String permissions[] = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA,
//            Manifest.permission.READ_MEDIA_IMAGES
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_image);
//        imageList = new ArrayList<>();
//        pickImgBtn =findViewById(R.id.pickImage);
//        imgCount = findViewById(R.id.imageCount_tv);
//        imageAdapter = new ImageAdapter(imageList);
//        imgRecyclerview = findViewById(R.id.recycler_image);
//        done_img = findViewById(R.id.upload_img);
//        classCode_edt = findViewById(R.id.classCode_edt);
//        // set layout
//        /*imgRecyclerview.setLayoutManager(new GridLayoutManager(this,2));*/
//        imgRecyclerview.setLayoutManager(new LinearLayoutManager(this));
//        imgRecyclerview.setAdapter(imageAdapter);
//        ProgressDialog progressDialog = new ProgressDialog(UploadImageActivity.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Uploading...");
//
//
//        pickImgBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog dialog = new Dialog(UploadImageActivity.this);
//                dialog.setContentView(R.layout.dialog_option_select_img);
//                ImageView camera = (ImageView) dialog.findViewById(R.id.camera_img);
//                ImageView galley = (ImageView) dialog.findViewById(R.id.galley_img);
//                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                galley.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // check permission for galley
//                        if (ActivityCompat.checkSelfPermission(
//                                UploadImageActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
//                                ActivityCompat.checkSelfPermission(
//                                        UploadImageActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
//                                ActivityCompat.checkSelfPermission(
//                                        UploadImageActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED &&
//                                ActivityCompat.checkSelfPermission(
//                                        UploadImageActivity.this, permissions[3]) != PackageManager.PERMISSION_GRANTED) {
//
//                            // if permission not granted then request
//                            ActivityCompat.requestPermissions(UploadImageActivity.this,permissions, REQUESTCODE_GALLEY);
//                        }
//                        else {
//                            openGalley();
//                            dialog.dismiss();
//                        }
//
//                    }
//                });
//                // click camera
//                camera.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // check permission for camera
//                        if (ActivityCompat.checkSelfPermission(
//                                UploadImageActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
//                                ActivityCompat.checkSelfPermission(
//                                        UploadImageActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
//                                ActivityCompat.checkSelfPermission(
//                                        UploadImageActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
//
//                            // if permission not granted then request
//                            requestPermissions(permissions, REQUESTCODE_GALLEY);
//                        }
//                        else {
//                            String fileName = "photo";
//                            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                            try {
//                                File imageFile= File.createTempFile(fileName,".jpg",storageDir);
//                                currentPhotoPath = imageFile.getAbsolutePath();
//                                Uri imageUri =  FileProvider.getUriForFile(UploadImageActivity.this,
//                                        "com.example.multiplechoiceexam.fileprovider",imageFile);
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//                                startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
//                                dialog.dismiss();
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//
//
//                    }
//                });
//                dialog.show();
//            }
//        });
//
//
//
//        // click upload
//        done_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String examClassCode = classCode_edt.getText().toString().trim();
//                if(examClassCode.isEmpty()){
//                    classCode_edt.setError("Enter classCode");
//                    return;
//                }
//                progressDialog.show();
//                List<MultipartBody.Part> partList = new ArrayList<>();
//                // Thực hiện chuyển đổi cho mỗi URI
//                for (Uri imageUri : imageList) {
//                    // Chuyển đổi Uri thành File
//                    String newFileName = generateFileName();
//
//                    // Chuyển đổi Uri thành đường dẫn mới với tên tệp mới
//                    String newPath = getRealPathFromURI(imageUri);
//
//                    // Tạo File mới từ đường dẫn mới
//                    File file = new File(newPath);
//                    // Tạo RequestBody từ File
//                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", newFileName, requestFile);
//                    partList.add(filePart);
//                }
//
//                // get api service
//                ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
//
//
//                Call<Void> call = apiService.uploadStudentTestImages(examClassCode,partList);
//                call.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(@NotNull Call<Void> call,@NotNull Response<Void> response) {
//                        progressDialog.dismiss();
//                        if (response.isSuccessful()) {
//                            // Xử lý phản hồi thành công
//                            Utility.showToast(UploadImageActivity.this,"Upload Successfully !");
//                            finish();
//                            // ...
//                        } else {
//                            Utility.showToast(UploadImageActivity.this,"Error: " + response.code() + response.message() );
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
//                        Utility.showToast(UploadImageActivity.this, t.getMessage());
//                        Log.e("LOG_BUG", "onFailure: " + t.getMessage() );
//
//                        progressDialog.dismiss();
//
//                    }
//                });
//
//
//
//            }
//        });
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUESTCODE_GALLEY) { // Kiểm tra mã yêu cầu quyền
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Utility.showToast(UploadImageActivity.this,"Permission granted !!");
//            } else {
//                Utility.showToast(UploadImageActivity.this,"The app needs permission to access gallery");
//            }
//        }
//    }
//
//
//    private void openGalley() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_PICK);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);
//    }
//    private String generateFileName() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
//        String timeStamp = sdf.format(new Date());
//        return "IMG_" + timeStamp +  getRandomString(6) +".jpg"; // Thay ".jpg" bằng phần mở rộng của file ảnh thích hợp.
//    }
//    private String getRandomString(int length) {
//        // Chuỗi ký tự để tạo mã ngẫu nhiên
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        StringBuilder sb = new StringBuilder();
//
//        // Tạo mã ngẫu nhiên bằng cách chọn ký tự ngẫu nhiên từ chuỗi characters
//        for (int i = 0; i < length; i++) {
//            int index = (int) (Math.random() * characters.length());
//            sb.append(characters.charAt(index));
//        }
//
//        return sb.toString();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Existing code: Handle the result of capturing an image using the camera or selecting from the gallery
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            // Result from capturing an image using the camera
//
//            // Get the captured image's URI
//            Uri imageUri = Uri.fromFile(new File(currentPhotoPath));
//
//            // Start image cropping activity for the captured image
//            startCropActivity(imageUri);
//        }
//
//        if (requestCode == REQUESTCODE_GALLEY && resultCode == RESULT_OK) {
//
//            assert data != null;
//
//            if (data.getClipData() != null) {
//                int numberImage = data.getClipData().getItemCount();
//
//                for (int i = 0; i < numberImage; i++) {
//                    Uri selectedImageUri = data.getClipData().getItemAt(i).getUri();
//                    startCropActivity(selectedImageUri);
//                }
//            } else if (data.getData() != null) {
//                Uri selectedImageUri = data.getData();
//
//                startCropActivity(selectedImageUri);
//            }
//
////            // Notify the Adapter to update the data
////            imageAdapter.notifyDataSetChanged();
////
////            // Update UI elements
////            imgCount.setText("Image(" + imageList.size() + ")");
////            done_img.setVisibility(View.VISIBLE);
////            imgCount.setVisibility(View.VISIBLE);
////            classCode_edt.setVisibility(View.VISIBLE);
////            classCode_edt.requestFocus();
//        }
//
//    }
//
//    // Method to start image cropping activity
//    private void startCropActivity(Uri imageUri) {
//
//        CropImage.activity(imageUri)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .start(UploadImageActivity.this);
//        imageList.add(imageUri);
//
//        imageAdapter.notifyDataSetChanged();
//
//        // Update UI elements
//        imgCount.setText("Số lượng(" + imageList.size() + ")");
//        done_img.setVisibility(View.VISIBLE);
//        imgCount.setVisibility(View.VISIBLE);
//        classCode_edt.setVisibility(View.VISIBLE);
//        classCode_edt.requestFocus();
//    }
//
//    private String getRealPathFromURI(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        if (cursor == null) return null;
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String path = cursor.getString(column_index);
//        cursor.close();
//        return path;
//    }
//
//}


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Utils.FileStorage;
import com.example.multiplechoiceexam.Utils.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.pqpo.smartcropperlib.SmartCropper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageActivity extends AppCompatActivity {
    private RecyclerView imgRecyclerview;
    private Button pickImgBtn;
    private TextView imgCount;
    private String examClassCode;
    private ArrayList<Uri> imageList;
    private ImageAdapter imageAdapter;
    private FloatingActionButton done_img;
    private String currentPhotoPath;
    private final int REQUESTCODE_GALLEY = 123;
    private final int REQUEST_IMAGE_CAPTURE = 115;
    private final int REQUEST_IMAGE_GALLERY = 116;
    private EditText classCode_edt;
    private String permissions[] = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        imageList = new ArrayList<>();
        pickImgBtn = findViewById(R.id.pickImage);
        imgCount = findViewById(R.id.imageCount_tv);
        imageAdapter = new ImageAdapter(imageList,this);
        imgRecyclerview = findViewById(R.id.recycler_image);
        done_img = findViewById(R.id.upload_img);
        classCode_edt = findViewById(R.id.classCode_edt);
        // set layout
        /*imgRecyclerview.setLayoutManager(new GridLayoutManager(this,2));*/
        imgRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        imgRecyclerview.setAdapter(imageAdapter);
        ProgressDialog progressDialog = new ProgressDialog(UploadImageActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading...");
        SmartCropper.buildImageDetector(this);

        pickImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(UploadImageActivity.this);
                dialog.setContentView(R.layout.dialog_option_select_img);
                ImageView camera = (ImageView) dialog.findViewById(R.id.camera_img);
                ImageView galley = (ImageView) dialog.findViewById(R.id.galley_img);
                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                galley.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // check permission for galley
                        if (ActivityCompat.checkSelfPermission(
                                UploadImageActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(
                                        UploadImageActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(
                                        UploadImageActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(
                                        UploadImageActivity.this, permissions[3]) != PackageManager.PERMISSION_GRANTED) {

                            // if permission not granted then request
                            ActivityCompat.requestPermissions(UploadImageActivity.this, permissions, REQUESTCODE_GALLEY);
                        } else {
                            openGalley();
                            dialog.dismiss();

                        }

                    }
                });
                // click camera
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // check permission for camera
                        if (ActivityCompat.checkSelfPermission(
                                UploadImageActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(
                                        UploadImageActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(
                                        UploadImageActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(permissions, REQUESTCODE_GALLEY);
                        } else {
                            File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg");
                            startActivityForResult(CameraActivity.getJumpIntent(UploadImageActivity.this, false, storageDir), REQUEST_IMAGE_CAPTURE);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });


        // click upload
        done_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String examClassCode = classCode_edt.getText().toString().trim();
                if (examClassCode.isEmpty()) {
                    classCode_edt.setError("Nhập mã lớp");
                    return;
                }
                progressDialog.show();
                List<MultipartBody.Part> partList = new ArrayList<>();
                // Thực hiện chuyển đổi cho mỗi URI
                for (Uri imageUri : imageList) {
                    // Chuyển đổi Uri thành File
                    String newFileName = generateFileName();

                    // Chuyển đổi Uri thành đường dẫn mới với tên tệp mới
                    String newPath = FileStorage.getPathFromUri(UploadImageActivity.this,imageUri);

                    // Tạo File mới từ đường dẫn mới
                    File file = new File(newPath);
                    // Tạo RequestBody từ File
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", newFileName, requestFile);
                    partList.add(filePart);
                }

                // get api service
                ApiService apiService = RetrofitClient.getApiService(getApplicationContext());


                Call<Void> call = apiService.uploadStudentTestImages(examClassCode, partList);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call,@NotNull Response<Void> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            // Xử lý phản hồi thành công
                            Utility.showToast(UploadImageActivity.this, "Upload Successfully !");
                            finish();
                            // ...
                        } else {
                            Utility.showToast(UploadImageActivity.this, "Error: " + response.code() + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                        Utility.showToast(UploadImageActivity.this, t.getMessage());
                        Log.e("LOG_BUG", "onFailure: " + t.getMessage());

                        progressDialog.dismiss();

                    }
                });


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUESTCODE_GALLEY) { // Kiểm tra mã yêu cầu quyền
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utility.showToast(UploadImageActivity.this, "Permission granted !!");
            } else {
                Utility.showToast(UploadImageActivity.this, "The app needs permission to access gallery");
            }
        }
    }


    private void openGalley() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GALLERY);
    }

    private String generateFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timeStamp = sdf.format(new Date());
        return "IMG_" + timeStamp + getRandomString(6) + ".jpg"; // Thay ".jpg" bằng phần mở rộng của file ảnh thích hợp.
    }

    private String getRandomString(int length) {
        // Chuỗi ký tự để tạo mã ngẫu nhiên
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();

        // Tạo mã ngẫu nhiên bằng cách chọn ký tự ngẫu nhiên từ chuỗi characters
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File croppedFile = (File) data.getSerializableExtra(CameraActivity.EXTRA_CROPPED_FILE);

            if (croppedFile != null && croppedFile.exists()) {
                Bitmap croppedBitmap = BitmapFactory.decodeFile(croppedFile.getAbsolutePath());

                int rotationDegrees = 90;
                Matrix matrix = new Matrix();
                matrix.postRotate(rotationDegrees);
                Bitmap rotatedBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight(), matrix, true);

                Uri rotatedImageUri = getImageUri(rotatedBitmap);
                imageList.add(rotatedImageUri);

                imageAdapter.notifyDataSetChanged();
                imgCount.setText("Sô lượng(" + imageList.size() + ")");
                done_img.setVisibility(View.VISIBLE);
                imgCount.setVisibility(View.VISIBLE);
                classCode_edt.setVisibility(View.VISIBLE);
                classCode_edt.requestFocus();
            } else {
                Toast.makeText(this, "Failed to load cropped bitmap", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getClipData() != null) {
                int numberImage = data.getClipData().getItemCount();
                for (int i = 0; i < numberImage; i++) {
                    Uri selectedImageUri = data.getClipData().getItemAt(i).getUri();
                    startCropActivity(selectedImageUri);
                }
            } else if (data.getData() != null) {
                Uri selectedImageUri = data.getData();
                startCropActivity(selectedImageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri croppedImageUri = result.getUri();

                imageList.add(croppedImageUri);
                imageAdapter.notifyDataSetChanged();

                imgCount.setText("Số lượng(" + imageList.size() + ")");
                done_img.setVisibility(View.VISIBLE);
                imgCount.setVisibility(View.VISIBLE);
                classCode_edt.setVisibility(View.VISIBLE);
                classCode_edt.requestFocus();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void startCropActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(UploadImageActivity.this);
    }

    private Uri getImageUri(Bitmap bitmap) {
        try {
            File imageFile = createImageFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }
}
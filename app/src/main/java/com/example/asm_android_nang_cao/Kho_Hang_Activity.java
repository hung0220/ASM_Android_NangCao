package com.example.asm_android_nang_cao;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class   Kho_Hang_Activity extends AppCompatActivity {
    Spinner spinner;
    ImageView imgAdd, imgLoadHinh, imgCamera, imgFile, imgSua, imgLoadHinhSua, imgCameraSua, imgFileSua;
    Button btnDialogThem, btnDialogHuy, getBtnDialogSua, btnxoa;
    EditText edttenSp, edtgiaSp, edtghiChuSp, edttenSpSua, edtgiaSpSua, edtghiChuSpSua, edtTimKiemKhoHang;

    String textTimKiem;
    ListView lv;
    ArrayList<SanPham> mangSanPham = new ArrayList();
    SanPhamAdapter myAdapter;
    int vitri;
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://asm-android-a58d4-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference node = db.getReference("SanPham");
    int REQUEST_CODE_IMGE = 1;
    int REQUEST_IMAGE_OPEN = 99;
    int REQUEST_CODE_IMGE_PRO = 2;
    int REQUEST_IMAGE_OPEN_PRO = 88;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://asm-android-a58d4.appspot.com");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kho_hang);


        lv = findViewById(R.id.lvSp);
        myAdapter = new SanPhamAdapter(Kho_Hang_Activity.this, R.layout.layout_mot_dong_san_pham, mangSanPham);
        lv.setAdapter(myAdapter);
        AnhXa();
        LoadData();

        edtTimKiemKhoHang.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                textTimKiem = edtTimKiemKhoHang.getText().toString().trim();
                LoadData();
                return false;
            }
        });



        ArrayList<String> arrLuaChon = new ArrayList<>();
        arrLuaChon.add("Tất cả");
        arrLuaChon.add("Giá cao");
        arrLuaChon.add("Gia thấp");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrLuaChon);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

//Thêm
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vitri = i;

                openDialogSua();
            }
        });


    }

    private void AnhXa() {
        spinner = findViewById(R.id.spinner);
        imgAdd = findViewById(R.id.ImgAdd);
        edtTimKiemKhoHang = findViewById(R.id.editTimKiemKhoHang);


    }

    private void openDialogSua() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_sua);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        String ten = mangSanPham.get(vitri).getTenSp();
        String gia = mangSanPham.get(vitri).getGiaSp();
        String ghichu = mangSanPham.get(vitri).getGhiChu();
        String linkhinh = mangSanPham.get(vitri).getLinkHinhSp();

        imgLoadHinhSua = dialog.findViewById(R.id.imgAddHinhSua);
        imgCameraSua = dialog.findViewById(R.id.ImgOpenCameraSua);
        imgFileSua = dialog.findViewById(R.id.ImgOpenPictureSua);
        getBtnDialogSua = dialog.findViewById(R.id.btnSuaSp);
        btnxoa = dialog.findViewById(R.id.btnXoaSua);


        edttenSpSua = dialog.findViewById(R.id.editTenSpSua);
        edtgiaSpSua = dialog.findViewById(R.id.editGiaSpSua);
        edtghiChuSpSua = dialog.findViewById(R.id.editGhiChuSpSua);


        edttenSpSua.setText(ten);
        edtgiaSpSua.setText(gia);
        edtghiChuSpSua.setText(ghichu);

        Picasso.get().load(linkhinh).into(imgLoadHinhSua);


        imgCameraSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_IMGE_PRO);
            }
        });
        imgFileSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
                startActivityForResult(intent, REQUEST_IMAGE_OPEN_PRO);
            }


        });


        getBtnDialogSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");
                // Get the data from an ImageView as bytes
                imgLoadHinhSua.setDrawingCacheEnabled(true);
                imgLoadHinhSua.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgLoadHinhSua.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                String TenSPNew = edttenSpSua.getText().toString().trim();
                String GiaSPNew = edtgiaSpSua.getText().toString().trim();
                String GhiChuSPNew = edtghiChuSpSua.getText().toString().trim();

                if (TenSPNew.equals("")) {
                    Toast.makeText(Kho_Hang_Activity.this, "Vui lòng nhập Tên Sản Phẩm", Toast.LENGTH_SHORT).show();
                    return;
                } else   if (GiaSPNew.equals("")) {
                    Toast.makeText(Kho_Hang_Activity.this, "Vui lòng nhập Giá Sản Phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }
                else   if (GhiChuSPNew.equals("")) {
                    Toast.makeText(Kho_Hang_Activity.this, "Vui lòng nhập Ghi Chú Sản Phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }



                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(Kho_Hang_Activity.this, "Loi up hinh", Toast.LENGTH_SHORT).show();
                    }

                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("cccccc", uri + "");

                                SanPham sanPham = new SanPham();

                                sanPham.setTenSp(TenSPNew);
                                sanPham.setGhiChu(GhiChuSPNew);
                                sanPham.setGiaSp(GiaSPNew);
                                sanPham.setLinkHinhSp("" + uri + "");


                                node.child(mangSanPham.get(vitri).getId()).updateChildren(sanPham.toMap());
                                Toast.makeText(Kho_Hang_Activity.this, "Sửa Thành Công", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                });


            }
        });

        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = mangSanPham.get(vitri).getId();
                XoaSanPham(key);
                dialog.dismiss();

            }
        });


        dialog.show();

    }

    private void openDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_add);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        imgLoadHinh = dialog.findViewById(R.id.imgAddHinh);
        imgCamera = dialog.findViewById(R.id.ImgOpenCamera);
        imgFile = dialog.findViewById(R.id.ImgOpenPicture);
        btnDialogThem = dialog.findViewById(R.id.btnthemsp);

        edttenSp = dialog.findViewById(R.id.editTenSp);
        edtgiaSp = dialog.findViewById(R.id.editGiaSp);
        edtghiChuSp = dialog.findViewById(R.id.editGhiChuSp);


        btnDialogThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String TenSP = edttenSp.getText().toString().trim();
                String GiaSP = edtgiaSp.getText().toString().trim();
                String GhiChuSP = edtghiChuSp.getText().toString().trim();

                if (TenSP.equals("")) {
                    Toast.makeText(Kho_Hang_Activity.this, "Vui lòng nhập Tên Sản Phẩm", Toast.LENGTH_SHORT).show();
                    return;
                } else   if (GiaSP.equals("")) {
                    Toast.makeText(Kho_Hang_Activity.this, "Vui lòng nhập Giá Sản Phẩm", Toast.LENGTH_SHORT).show();
                    return;

            } else   if (!GiaSP.matches( "^[0-9]*$")) {
                Toast.makeText(Kho_Hang_Activity.this, "Vui lòng nhập Giá Sản Phẩm Là số mà", Toast.LENGTH_SHORT).show();
                return;
            }
                else   if (GhiChuSP.equals("")) {
                    Toast.makeText(Kho_Hang_Activity.this, "Vui lòng nhập Ghi Chú Sản Phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }

                UUID Id = UUID.randomUUID();
                String id = Id.toString();

                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");
                // Get the data from an ImageView as bytes
                imgLoadHinh.setDrawingCacheEnabled(true);
                imgLoadHinh.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgLoadHinh.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(Kho_Hang_Activity.this, "Loi up hinh", Toast.LENGTH_SHORT).show();
                    }

                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                              //  Log.d("cccccc", uri + "");

                                SanPham sanPham = new SanPham(id, TenSP, GiaSP, GhiChuSP, "" + uri + "");
                                node.child(Id.toString()).setValue(sanPham, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error == null) {
                                            Toast.makeText(Kho_Hang_Activity.this, "Luu DU Lieu Thanh Cong", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                });

            }
        });


        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_IMGE);
            }
        });
        imgFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
                startActivityForResult(intent, REQUEST_IMAGE_OPEN);
            }
        });


        dialog.show();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        AnhXa();
        if (requestCode == REQUEST_CODE_IMGE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgLoadHinh.setImageBitmap(bitmap);


        }
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri full = data.getData();

            imgLoadHinh.setImageURI(full);


        }
        if (requestCode == REQUEST_CODE_IMGE_PRO && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgLoadHinhSua.setImageBitmap(bitmap);


        }
        if (requestCode == REQUEST_IMAGE_OPEN_PRO && resultCode == RESULT_OK) {
            Uri full1 = data.getData();

            imgLoadHinhSua.setImageURI(full1);


        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void LoadData() {
        mangSanPham.clear();
        node.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {


                SanPham sanPham = snapshot.getValue(SanPham.class);
                if(sanPham != null){




                    if(textTimKiem != null){
                        if(sanPham.getTenSp().contains(textTimKiem)){
                            mangSanPham.add(sanPham);
                        }
                    }else {
                        mangSanPham.add(sanPham);
                    }




                    myAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                SanPham sp = snapshot.getValue(SanPham.class);
                if (sp == null || mangSanPham == null || mangSanPham.isEmpty()) {
                    return;
                }
                for (int i = 0; i < mangSanPham.size(); i++) {
                    if (sp.getId() == mangSanPham.get(i).getId()) {
                        mangSanPham.set(i, sp);
                        break;
                    }
                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {


                SanPham sp = snapshot.getValue(SanPham.class);
                if (sp == null || mangSanPham == null || mangSanPham.isEmpty()) {
                    return;
                }
                for (int i = 0; i < mangSanPham.size(); i++) {
                    if (sp.getId() == mangSanPham.get(i).getId()) {
                        mangSanPham.remove(mangSanPham.get(i));
                        break;
                    }
                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void XoaSanPham(String idsp) {


        AlertDialog.Builder myBuilder = new AlertDialog.Builder(Kho_Hang_Activity.this);
        myBuilder.setMessage("Bạn chắc chắn muốn xóa không ???");
        myBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {

                node.child(idsp).removeValue();
                Toast.makeText(Kho_Hang_Activity.this, "Xóa Thàng Công", Toast.LENGTH_SHORT).show();

            }
        });
        myBuilder.setNegativeButton("HỦy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        myBuilder.show();


    }
}
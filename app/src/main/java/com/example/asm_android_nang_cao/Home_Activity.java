package com.example.asm_android_nang_cao;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;


public class Home_Activity extends AppCompatActivity {
    GridView lvhome;
    ArrayList<SanPham> mangSanPham = new ArrayList();
    SanPhamAdapter myAdapter;
    String textTimKiemHome;
    EditText edttimKiemHome,edtSdtMua, sdtTenMua,edtDiaChiMua, edtSoLuongMua;
    ImageView hinhSpPopup, hinhSpMua;
    Button btnMua,btnMuaThat;
    TextView tenSpPopup, giaSpPopup, ghiChuSpPopup, tenSpMua, giaSpMua, ghiChuSpMua;
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://asm-android-a58d4-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference node = db.getReference("SanPham");
    DatabaseReference nodeMua = db.getReference("DonMua");
int vitri;
    SliderView sliderView;
    int[] images = {R.drawable.cf2,
            R.drawable.cf3,
            R.drawable.cf4,
            R.drawable.cf5,
            R.drawable.cf8,
            R.drawable.cf10};
    ImageView btnMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AnhXa();

       btnMore = findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });




        edttimKiemHome.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                textTimKiemHome = edttimKiemHome.getText().toString().trim();
                LoadData();
                return false;
            }
        });

        sliderView = findViewById(R.id.image_slider);

        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

LoadData();

        myAdapter = new SanPhamAdapter(Home_Activity.this, R.layout.layout_mot_dong_home, mangSanPham);
        lvhome.setAdapter(myAdapter);

        lvhome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vitri=i;

                openDialogThongTinSanPham();
            }
        });

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public void showMenu() {
        PopupMenu popupMenu = new PopupMenu(Home_Activity.this, btnMore);
        popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){

                    case  R.id.dangXuat:
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        break;
                    case  R.id.KhoHang:
                        Intent intent1 = new Intent(getApplicationContext(), com.example.asm_android_nang_cao.Kho_Hang_Activity.class);
                        startActivity(intent1);
                        break;

                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void LoadData() {
        mangSanPham.clear();
        node.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {


                SanPham sanPham = snapshot.getValue(SanPham.class);
                if(sanPham != null){


                    if(textTimKiemHome != null){
                        if(sanPham.getTenSp().contains(textTimKiemHome)){
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
    private void AnhXa() {
        lvhome = findViewById(R.id.lvhome);
        edttimKiemHome = findViewById(R.id.editTimKiemHome);

    }
    private void openDialogThongTinSanPham() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_popup_home);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        String TenSp = mangSanPham.get(vitri).getTenSp();
        String GiaSp = mangSanPham.get(vitri).getGiaSp();
        String GhiChuSp = mangSanPham.get(vitri).getGhiChu();
        String linkHinh = mangSanPham.get(vitri).getLinkHinhSp();

        tenSpPopup = dialog.findViewById(R.id.txtTenMotDongHome);
        giaSpPopup = dialog.findViewById(R.id.txtGiaMotDongHome);
        ghiChuSpPopup = dialog.findViewById(R.id.txtGhiChuMotDongHome);
        hinhSpPopup = dialog.findViewById(R.id.imgMotDongHome);
        btnMua = dialog.findViewById(R.id.btnMuaNgay);

        Picasso.get().load(linkHinh).into(hinhSpPopup);
        giaSpPopup.setText(GiaSp);
        tenSpPopup.setText(TenSp);
        ghiChuSpPopup.setText(GhiChuSp);

        btnMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogThongTinSanPhamHAi();
                dialog.dismiss();
            }
        });




        dialog.show();



    }
    private void openDialogThongTinSanPhamHAi() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_thong_tin_mua);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        String TenSp = mangSanPham.get(vitri).getTenSp();
        String GiaSp = mangSanPham.get(vitri).getGiaSp();
        String GhiChuSp = mangSanPham.get(vitri).getGhiChu();
        String linkHinh = mangSanPham.get(vitri).getLinkHinhSp();

        tenSpMua = dialog.findViewById(R.id.txtTenMotDongMua);
        giaSpMua = dialog.findViewById(R.id.txtGiaMotDongMua);
        ghiChuSpMua = dialog.findViewById(R.id.txtGhiChuMotDongMua);
        hinhSpMua = dialog.findViewById(R.id.imgMotDongMua);
        btnMuaThat = dialog.findViewById(R.id.btnMuaThat);

        sdtTenMua = dialog.findViewById(R.id.edtTenMua);
        edtSdtMua = dialog.findViewById(R.id.edtSDTMua);
        edtDiaChiMua = dialog.findViewById(R.id.edtDiaChiMua);
        edtSoLuongMua= dialog.findViewById(R.id.edtSoLuongMua);






        Picasso.get().load(linkHinh).into(hinhSpMua);
        giaSpMua.setText(GiaSp);
        tenSpMua.setText(TenSp);
        ghiChuSpMua.setText(GhiChuSp);

        btnMuaThat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenMua = sdtTenMua.getText().toString().trim();
                String sdtMua = edtSdtMua.getText().toString().trim();
                String diaChiMua = edtDiaChiMua.getText().toString().trim();
                String soluongMua = edtSoLuongMua.getText().toString().trim();
                String idSp = mangSanPham.get(vitri).getId();


                if(tenMua.equals("")){
                    Toast.makeText(Home_Activity.this, "Bạn CHưa Nhập Tên", Toast.LENGTH_SHORT).show();
                    return;
                } else if(sdtMua.equals("")){
                    Toast.makeText(Home_Activity.this, "Bạn CHưa Nhập SĐT", Toast.LENGTH_SHORT).show();
                    return;
                }else if(diaChiMua.equals("")){
                    Toast.makeText(Home_Activity.this, "Bạn CHưa Nhập Địa Chỉ", Toast.LENGTH_SHORT).show();
                    return;
                }else if(soluongMua.equals("")){
                    Toast.makeText(Home_Activity.this, "Bạn CHưa Nhập Số Lượng", Toast.LENGTH_SHORT).show();
                    return;
                }

                UUID Id = UUID.randomUUID();
                String id = Id.toString();
                Thong_Tin_Don_Hang thong_tin_don_hang = new Thong_Tin_Don_Hang(id,idSp,tenMua,sdtMua,soluongMua,diaChiMua);

                nodeMua.child(id).setValue(thong_tin_don_hang, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull  DatabaseReference ref) {
                        Toast.makeText(Home_Activity.this, "Mua Thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

            }
        });





        dialog.show();
    }


}
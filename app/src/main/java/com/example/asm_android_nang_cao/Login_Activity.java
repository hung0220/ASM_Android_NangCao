package com.example.asm_android_nang_cao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_Activity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    Button btnDangNhap;
    TextView txtDangKi;
    EditText edtEmail, edtPass, edtPassLai;
    CheckBox ckSavePassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("dataLogin",MODE_PRIVATE);

        AnhXa();
        mAuth = FirebaseAuth.getInstance();

        TextView txtDangKi = findViewById(R.id.txtDangKi);
        txtDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, com.example.asm_android_nang_cao.Register_Activity.class);
                startActivity(intent);
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangNhap();
            }
        });
        edtEmail.setText(sharedPreferences.getString("taiKhoan",""));
        edtPass.setText(sharedPreferences.getString("matKhau",""));
        ckSavePassWord.setChecked(sharedPreferences.getBoolean("ckbRemember",false));

    }

    private void AnhXa(){

        txtDangKi = findViewById(R.id.txtDangKi);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        edtEmail = findViewById(R.id.edtGmailDangNhap);
        edtPass = findViewById(R.id.edtPassDangNhap);
        ckSavePassWord = findViewById(R.id.checkBox);


    }

    private void DangNhap(){

        String taiKhoan = edtEmail.getText().toString();
        String matkhau = edtPass.getText().toString();
        if (taiKhoan.equals("")) {
            Toast.makeText(Login_Activity.this, "Vui lòng nhập Tên Tài Khoản", Toast.LENGTH_SHORT).show();
            return;
        } else if (checkGmail(taiKhoan) == false) {
            Toast.makeText(Login_Activity.this, "Sai Gmail, Nhập lại né!!", Toast.LENGTH_SHORT).show();
            return;
        }
        else   if (matkhau.equals("")) {
            Toast.makeText(Login_Activity.this, "Vui lòng nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(taiKhoan, matkhau)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Login_Activity.this, "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login_Activity.this, Home_Activity.class);
                            startActivity(intent);
                            if(ckSavePassWord.isChecked()){
                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                editor.putString("taiKhoan",taiKhoan);
                                editor.putString("matKhau",matkhau);
                                editor.putBoolean("ckbRemember",true);
                                editor.commit();
                            }else {
                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                editor.remove("taiKhoan");
                                editor.remove("matKhau");
                                editor.remove("ckbRemember");
                                editor.commit();

                            }

                        }else {
                            Toast.makeText(Login_Activity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static boolean checkGmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}
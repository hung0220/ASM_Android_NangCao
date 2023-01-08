package com.example.asm_android_nang_cao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Register_Activity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btnDangKi;
    TextView txtDangNhap;
    EditText edtEmail, edtPass, edtPassLai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AnhXa();
        mAuth = FirebaseAuth.getInstance();





        txtDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register_Activity.this,Login_Activity.class);
                startActivity(intent);
            }
        });

        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangKy();
            }
        });


    }

    private  void  DangKy(){
        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();
        String passwordlai = edtPass.getText().toString();


        if (email.equals("")) {
            Toast.makeText(Register_Activity.this, "Vui lòng nhập Tên Tài Khoản", Toast.LENGTH_SHORT).show();
            return;
        }  else if (checkGmail(email) == false) {
            Toast.makeText(Register_Activity.this, "Sai Gmail, Nhập lại né!!", Toast.LENGTH_SHORT).show();
            return;
        }
        else   if (password.equals("")) {
            Toast.makeText(Register_Activity.this, "Vui lòng nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
            return;
        }else if (password.length() < 6) {
            Toast.makeText(Register_Activity.this, "Mật khẩu phải trên 6 kí tự", Toast.LENGTH_SHORT).show();
            return;
        }else if (passwordlai.equals("")) {
            Toast.makeText(Register_Activity.this, "Vui lòng nhập Mật Khẩu Lại", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Register_Activity.this, "Đăng kí Thành Công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Register_Activity.this, "Thất Bại", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    private void AnhXa(){

        txtDangNhap = findViewById(R.id.txtDangNhap);
        btnDangKi = findViewById(R.id.btnDangki);
        edtEmail = findViewById(R.id.edtGmailDangKi);
        edtPass = findViewById(R.id.edtPassDangki);
        edtPassLai = findViewById(R.id.edtPassDangkiLai);


    }
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static boolean checkGmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
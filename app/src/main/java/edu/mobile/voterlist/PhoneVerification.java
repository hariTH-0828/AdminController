package edu.mobile.voterlist;

import static java.util.concurrent.TimeUnit.SECONDS;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.internal.bind.ObjectTypeAdapter;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

public class PhoneVerification extends AppCompatActivity {

    TextInputEditText phoneNumber, otp;
    String phone_number, verificationId, code;
    Button requestOtp, verifyOtp;
    LinearLayoutCompat verifyOtpLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        otp = findViewById(R.id.textOtp);
        verifyOtp = findViewById(R.id.verifyOtp);

        /*verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.activity_phone_verification, null);
                verifyOtp = popupView.findViewById(R.id.verifyOtp);

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });*/
        
       /* verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PhoneVerification.this, "Button clicked", Toast.LENGTH_SHORT).show();
            }
        });*/

        requestOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.requireNonNull(phoneNumber.getText()).toString().trim().isEmpty() ||
                        Objects.requireNonNull(phoneNumber.getText()).toString().trim().length() != 10) {
                    Toast.makeText(PhoneVerification.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                phone_number = Objects.requireNonNull(phoneNumber.getText()).toString().trim();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+ phone_number,
                        60,
                        SECONDS,
                        PhoneVerification.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(PhoneVerification.this, "Code sent", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(PhoneVerification.this, "Verification Failed : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String mVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = mVerificationId;
                            }
                        }
                );
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.requireNonNull(otp.getText()).toString().trim().isEmpty() || Objects.requireNonNull(otp.getText()).toString().trim().length() != 6) {
                    Toast.makeText(PhoneVerification.this, "Enter valid OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                code = Objects.requireNonNull(otp.getText()).toString().trim();

                if(verificationId != null) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(PhoneVerification.this, "Verification Code entered was invalid", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
    }

}
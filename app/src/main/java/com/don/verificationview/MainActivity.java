package com.don.verificationview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.don.verificationviewlibrary.VerificationView;

public class MainActivity extends AppCompatActivity {

    VerificationView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vv= (VerificationView) findViewById(R.id.vv);
        Button btnReset= (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vv.reset();
                Log.i("MyLog","验证码是="+vv.getText());
            }
        });
    }
}

package com.example.consumo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRequest = findViewById(R.id.btnRequest);
        TextView tvResponse = findViewById(R.id.tvResponse);

        btnRequest.setOnClickListener(v -> fetchMessage(tvResponse));
    }

    private void fetchMessage(TextView textView) {
        new Thread(() -> {
            String response = "";
            try {
                // URL del servicio
                URL url = new URL("http://192.168.0.104:3000/mensaje");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Leer la respuesta
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                }
                reader.close();
                response = responseBuilder.toString();
            } catch (Exception e) {
                response = "Error: " + e.getMessage();
            }

            // Actualizar la UI en el hilo principal
            String finalResponse = response;
            new Handler(Looper.getMainLooper()).post(() -> textView.setText(finalResponse));
        }).start();
    }
}

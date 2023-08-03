package com.example.canopi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button onButton1;
    private Button onButton2;
    private Button offButton;
    private TextView textView;
    private static final String ESP32_IP_ADDRESS = "192.168.1.113"; // Ganti dengan IP ESP32 Anda
    private static final int ESP32_PORT = 80; // Ganti dengan port yang digunakan pada ESP32 Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        onButton1 = findViewById(R.id.onButton1);
        onButton2 = findViewById(R.id.onButton2);
        offButton = findViewById(R.id.offButton);

        onButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendCommandTask().execute("ON1");
            }
        });

        onButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendCommandTask().execute("ON2");
            }
        });

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendCommandTask().execute("OFF");
            }
        });
    }

    private class SendCommandTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... commands) {
            try {
                Socket socket = new Socket(ESP32_IP_ADDRESS, ESP32_PORT);

                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println(commands[0]);
                out.flush();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String messageFromESP32 = in.readLine();

                socket.close();

                return messageFromESP32;
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText("Hasil koneksi: " + result);
        }
    }
}

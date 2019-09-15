package com.example.matchthemembers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class quizScreen extends AppCompatActivity {

    ImageView face;
    Button[] buttons;
    Button endGame;
    TextView scoreView;

    TextView timerView;
    CountDownTimer timer;

    int score;

    String[] names;
    Map<String, Integer> faceMap;

    String currName;

    final String rawNames = "Aady Pillai,Aarushi Agrawal,Aayush Tyagi,Abhinav Kejriwal,Aditya Yadav,Ajay Merchia,Anand Chandra,Andres Medrano,Andrew Santoso,Angela Dong,Anika Bagga,Anik Gupta,Anita Shen,Anjali Thakrar,Anmol Parande,Ashwin Aggarwal,Ashwin Kumar,Athena Leong,Austin Davis,Ayush Kumar,Brandon David,Candace Chiang,Candice Ye,Charles Yang,Daniel Andrews,Divya Tadimeti,DoHyun Cheon,Eric Kong,Ethan Wong,Imran Khaliq,Izzie Lau,Jacqueline Zhang,Japjot Singh,Joey Hejna,Justin Kim,Kanyes Thaker,Karen Alarcon,Katniss Lee,Kayli Jiang,Kiana Go,Leon Kwak,Lewis Zhang,Matthew Locayo,Max Emerling,Max Miranda,Melanie Cooray,Michelle Mao,Mohit Katyal,Mudabbir Khan,Natasha Wong,Neha Nagabothu,Nikhar Arora,Noah Pepper,Patrick Yin,Paul Shao,Radhika Dhomse,Sai Yandapalli,Salman Husain,Samantha Lee,Shaina Chen,Sharie Wang,Shaurya Sanghvi,Shiv Kushwah,Shomil Jain,Shubha Jagannatha,Shubham Gupta,Sinjon Santos,Srujay Korlakunta,Stephen Jayakar,Tyler Reinecke,Vaibhav Gattani,Varun Jhunjhunwalla,Victor Sun,Vidya Ravikumar,Vineeth Yeevani,Will Oakley,Xin Yi Chen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);

        names = rawNames.split(",");
        faceMap = new HashMap<>();

        Resources resources = getResources();

        for (String name : names) {
            faceMap.put(name, resources.getIdentifier(name.toLowerCase().replaceAll(" ", ""), "drawable", getPackageName()));
        }

        buttons = new Button[4];
        buttons[0] = findViewById(R.id.name1);
        buttons[1] = findViewById(R.id.name2);
        buttons[2] = findViewById(R.id.name3);
        buttons[3] = findViewById(R.id.name4);

        endGame = findViewById(R.id.endgame);

        final Intent exit = new Intent(quizScreen.this, MainActivity.class);

        //alert dialog


        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Return to start screen?");
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(exit);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        scoreView = findViewById(R.id.score);
        scoreView.setText("Score: 0");
        score = 0;


        //timer
        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerView.setText("" + (1 + (int) (millisUntilFinished/1000)));
            }

            @Override
            public void onFinish() {
                nextFace();
            }
        };


        timerView = findViewById(R.id.timer);

        for (final Button b : buttons) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (b.getText().equals(currName)) {
                        score ++;
                        scoreView.setText("Score: " + score);
                    } else {
                        (Toast.makeText(getApplicationContext(), "Oops! The correct name was " + currName, Toast.LENGTH_SHORT)).show();
                    }
                    nextFace();
                }
            });
        }

        face = findViewById(R.id.face);

        //create contact

        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, currName);
                startActivity(intent);
            }
        });


        nextFace();
    }

    private void nextFace() {
        timer.cancel();

        currName = names[(int) (Math.random() * names.length)];
        int correctAns = (int) (Math.random() * buttons.length);

        List<String> usedNames = new ArrayList<String>();
        for (int i = 0; i < buttons.length; i++) {
            if (i == correctAns) {
                buttons[i].setText(currName);
            } else {
                String fakeName;
                do {
                    fakeName = names[(int) (Math.random() * names.length)];
                } while (fakeName.equals(currName) || usedNames.contains(fakeName));
                usedNames.add(fakeName);
                buttons[i].setText(fakeName);
            }
        }

        face.setImageResource(faceMap.get(currName));

        timer.start();
    }
}
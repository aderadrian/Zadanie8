package com.example.adrianreda.zadanie8;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class odtwarzacz extends AppCompatActivity
{
    private static final int RECORD_REQUEST_CODE = 101;
    private static final String TAG = "MyActivity";
    private MediaRecorder mediaRecorder;
    private Button PlaySound;
    private Button StopSound;
    private String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.odtwarzacz);
        audioRecorderService();
        savingCleaningAndReadingData();

    }


    private MediaPlayer mediaPlayer;

    public void playSound(View view){
        createAlertDialogWithList().show();

    }
    public void stopSound(View view){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }


    }


    public void play(){
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.mik);
        mediaPlayer.start();
    }
    public void play2(){
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.dim);
        mediaPlayer.start();
    }



    private Dialog createAlertDialogWithList() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final String[] options = {"Mike Posner-I took pill in Ibiza", "Dimitri Vegas, MOGUAI & Like Mike - Mammoth"};
        dialogBuilder.setTitle("Lista utworow");
        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                Toast.makeText(getApplicationContext(), "Wybrałeś: " + options[position], Toast.LENGTH_LONG).
                        show();
                if (options[position].equals("Mike Posner-I took pill in Ibiza")) {
                    play();
                } else {
                    play2();
                }

            }
        });
        return dialogBuilder.create();
    }










    protected void audioRecorderService(){

        final Button button1 = (Button) findViewById(R.id.button3);
        final Button button2 = (Button) findViewById(R.id.button5);
        PlaySound = (Button)findViewById(R.id.button6);
        StopSound = (Button)findViewById(R.id.button7);

        button2.setEnabled(false);
        PlaySound.setEnabled(false);
        StopSound.setEnabled(false);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                startRecording();
                button1.setEnabled(false);
                button2.setEnabled(true);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopRecording();
                button2.setEnabled(false);
                PlaySound.setEnabled(true);
                StopSound.setEnabled(true);
                onPlayButtonClick();
                onStopButtonClick();
            }
        });
    }

    private void prepareRecording(){
        mediaRecorder = new MediaRecorder();
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(mFileName);
    }

    private void startRecording(){
        prepareRecording();
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private void stopRecording(){
        if(null != mediaRecorder) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
    public void onPlayButtonClick(){
        PlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    playSound();
                    PlaySound.setEnabled(false);
                    StopSound.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onStopButtonClick(){
        StopSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSound();
                PlaySound.setEnabled(true);
                StopSound.setEnabled(false);
            }
        });
    }

    private void playSound() throws IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(mFileName);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    private void stopSound(){
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.stop();
        mediaPlayer.release();
    }





    private Button saveInternalFile;
    private Button saveSDFile;
    private Button readInternalFile;
    private Button readSDFile;
    private Button clearTextField;

    private View view;


    private void savingCleaningAndReadingData() {
        saveTextToFileOnInternalStorage();
        saveTextToFileOnSDcard();
        readTextFromFileOnInternalStorage();
        readTextFromFileOnSDcard();
        clearTextFieldAndFile();
    }

    public void saveTextToFileOnInternalStorage(){
        
        saveInternalFile = (Button)findViewById(R.id.button8);
        saveInternalFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String filePath = "phoneNames.txt";
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filePath,MODE_APPEND));
                    EditText editText = (EditText) findViewById(R.id.editText);
                    String text = editText.getText().toString();
                    outputStreamWriter.write(text);
                    outputStreamWriter.write("\n");
                    outputStreamWriter.close();

                    Toast.makeText(getBaseContext(),"Zapisano tekst do pliku: " + filePath,Toast.LENGTH_SHORT).show();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    Toast.makeText(getBaseContext(),"Nie można zapisać tekstu do pliku",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void saveTextToFileOnSDcard(){
        
        saveSDFile = (Button) findViewById(R.id.button9);
        saveSDFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = "phoneNames.txt";
                File sdCard = Environment.getExternalStorageDirectory();
                File directory = new File(sdCard.getAbsolutePath() + "/PlikiAplikacji/");
                directory.mkdir();

                File file = new File(directory, "phoneNames.txt");
                EditText editText = (EditText) findViewById(R.id.editText);
                String text = editText.getText().toString();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(file,true);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
                try {
                    osw.write(text + "\r\n");
                    osw.flush();
                    osw.close();
                    Toast.makeText(getBaseContext(),"Zapisano tekst do pliku na karcie SD: " + filePath,Toast.LENGTH_SHORT).show();
                }
                catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Nie można zapisać tekstu do pliku", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void readTextFromFileOnInternalStorage(){
        readInternalFile = (Button)findViewById(R.id.button11);
        readInternalFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder text = new StringBuilder();
                try {
                    InputStream inputStream = openFileInput("phoneNames.txt");
                    if (inputStream != null) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader buffreader = new BufferedReader(inputStreamReader);
                        String line;
                        while (( line = buffreader.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                            Toast.makeText(getBaseContext(),"Odczytano plik z pamięci wewnętrznej",Toast.LENGTH_SHORT).show();
                        }}}
                catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Nie można odczytać pliku z pamięci wewnętrznej", Toast.LENGTH_LONG).show();
                }
                TextView tv = (TextView)findViewById(R.id. textView9);
                tv.setText(text);

            }
        });
    }

    public void readTextFromFileOnSDcard(){
        readSDFile = (Button)findViewById(R.id.button12);
        readSDFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File sdCard = Environment.getExternalStorageDirectory();
                File directory = new File(sdCard.getAbsolutePath() + "/PlikiAplikacji/");
                File file = new File(directory, "phoneNames.txt");
                int length = (int) file.length();
                byte[] bytes = new byte[length];
                FileInputStream in;
                try {
                    in = new FileInputStream(file);
                    in.read(bytes);
                    in.close();
                    Toast.makeText(getBaseContext(),"Odczytano plik z karty SD",Toast.LENGTH_SHORT).show();
                }
                catch (FileNotFoundException e) {
                    Toast.makeText(getBaseContext(), "Nie można odczytać pliku z karty SD", Toast.LENGTH_LONG).show();

                }
                catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Nie można odczytać pliku z karty SD", Toast.LENGTH_LONG).show();
                }

                String contents = new String(bytes);
                TextView tv = (TextView)findViewById(R.id. textView9);
                tv.setText(contents);

            }
        });
    }

    public void clearTextFieldAndFile(){
        clearTextField = (Button)findViewById(R.id.button10);
        clearTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emptyString = null;
                TextView tv = (TextView)findViewById(R.id. textView9);
                tv.setText(emptyString);

               
                File directory = getFilesDir();
                File file = new File(directory, "phoneNames.txt");

                
                File sdCard = Environment.getExternalStorageDirectory();
                File directorySD = new File(sdCard.getAbsolutePath() + "/PlikiAplikacji/");
                File fileSD = new File(directorySD, "phoneNames.txt");


                if (file.exists() && fileSD.exists()){
                    file.delete();
                    fileSD.delete();
                    Toast.makeText(getBaseContext(),"Usunięto pliki z pamięci wewnętrznej i karty SD",Toast.LENGTH_SHORT).show();
                }
                else if(file.exists()){
                    file.delete();
                    Toast.makeText(getBaseContext(),"Usunięto plik z pamięci wewnętrznej",Toast.LENGTH_SHORT).show();
                }
                else if (fileSD.exists()){
                    fileSD.delete();
                    Toast.makeText(getBaseContext(),"Usunięto plik z karty SD ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



















    public void Powrot(View view)
    {
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
        finish();
    }






}

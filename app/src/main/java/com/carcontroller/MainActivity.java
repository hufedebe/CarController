package com.carcontroller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.carcontroller.Retrofit.ControlCarroAdapter;
import com.carcontroller.Retrofit.Model.AudioPost;
import com.carcontroller.Retrofit.Model.CarroPost;
import com.carcontroller.Retrofit.Model.CarroResponse;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    String valor;
    private MediaRecorder myAudioRecorder;
    private String outputFile;

    //Variable Gologables
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;

    private MediaPlayer   mPlayer = null;
    private boolean mStartRecording = false;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Record to the external cache directory for visibility
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.mp3";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);



        ImageView btn_up = (ImageView) findViewById(R.id.btn_up);
        ImageView btn_right = (ImageView) findViewById(R.id.btn_right);
        ImageView btn_left = (ImageView) findViewById(R.id.btn_left);
        ImageView btn_down = (ImageView) findViewById(R.id.btn_down);
        ImageView btn_parar = (ImageView) findViewById(R.id.btn_parar);
        ImageView btn_ibm = (ImageView) findViewById(R.id.btn_ibm);
        ImageView btn_play = (ImageView) findViewById(R.id.btn_ibm2);



      btn_play.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              mPlayer = new MediaPlayer();
              try {
                  mPlayer.setDataSource(mFileName);
                  mPlayer.prepare();
                  mPlayer.start();
              } catch (IOException e) {
                  Log.e(LOG_TAG, "prepare() failed");
              }
          }
      });

        btn_ibm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getApplicationContext(), "Empezando a grabar", Toast.LENGTH_LONG).show();
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    //mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mRecorder.setOutputFile(mFileName);
                    //mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);


                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }

                    mRecorder.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    //Toast.makeText(getApplicationContext(),"Finalizó el Comando por voz", Toast.LENGTH_LONG).show();
                    File files = new File(mFileName);
                    //RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM,"prueba");
                    String descripcion = "objeto";
                    AudioPost audioPost = new AudioPost(descripcion);
                    RequestBody filePart= RequestBody.create(MediaType.parse("audio/mpeg"),files);

                    MultipartBody.Part file = MultipartBody.Part.createFormData("audio",files.getName(),filePart);
                    Call<ResponseBody> call1 = ControlCarroAdapter.getApiService().upload(audioPost, file);
                    call1.enqueue(new  UploadAudioCallback());

                }
                return true;
            }
        });


/*
        btn_ibm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Empezando a grabar", Toast.LENGTH_LONG).show();
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setOutputFile(mFileName);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                try {
                    mRecorder.prepare();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }

                mRecorder.start();
            }
        }); */




        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(getApplicationContext(),"Caminando en Línea Rect", Toast.LENGTH_LONG).show();
                valor = "Avanza";
                CarroPost carroPost = new CarroPost(valor);
                Call<CarroResponse> call = ControlCarroAdapter.getApiService().postRegistrarComando(carroPost);
                call.enqueue(new RegistrarVehiculoCallback());
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),"Girando a la derecha", Toast.LENGTH_LONG).show();
                valor = "Derecha";
                CarroPost carroPost = new CarroPost(valor);
                Call<CarroResponse> call = ControlCarroAdapter.getApiService().postRegistrarComando(carroPost);
                call.enqueue(new RegistrarVehiculoCallback());
            }
        });

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Girando a la izquierda", Toast.LENGTH_LONG).show();
                valor = "Izquierda";
                CarroPost carroPost = new CarroPost(valor);
                Call<CarroResponse> call = ControlCarroAdapter.getApiService().postRegistrarComando(carroPost);
                call.enqueue(new RegistrarVehiculoCallback());
            }
        });

        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Caminando hacia atrás", Toast.LENGTH_LONG).show();
                valor = "Retrocede";
                CarroPost carroPost = new CarroPost(valor);
                Call<CarroResponse> call = ControlCarroAdapter.getApiService().postRegistrarComando(carroPost);
                call.enqueue(new RegistrarVehiculoCallback());
            }
        });

        btn_parar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Caminando hacia atrás", Toast.LENGTH_LONG).show();


                valor = "Para";

                CarroPost carroPost = new CarroPost(valor);
                Call<CarroResponse> call = ControlCarroAdapter.getApiService().postRegistrarComando(carroPost);
                call.enqueue(new RegistrarVehiculoCallback());


                /*
                File files = new File(mFileName);
                //RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM,"prueba");
                String descripcion = "objeto";
                AudioPost audioPost = new AudioPost(descripcion);
                RequestBody filePart= RequestBody.create(MediaType.parse("audio/mpeg"),files);

                MultipartBody.Part file = MultipartBody.Part.createFormData("audio",files.getName(),filePart);
                Call<ResponseBody> call1 = ControlCarroAdapter.getApiService().upload(audioPost, file);
                call1.enqueue(new  UploadAudioCallback());

            */

            }
        });




    }

    class UploadAudioCallback implements Callback<ResponseBody>{


        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Toast.makeText(getApplicationContext(),"Envío exitoso ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast.makeText(getApplicationContext(),"Problemas Conexion", Toast.LENGTH_SHORT).show();
        }
    }


    class RegistrarVehiculoCallback implements Callback<CarroResponse> {


        @Override
        public void onResponse(Call<CarroResponse> call, Response<CarroResponse> response) {
            if(response.isSuccessful()){
                CarroResponse vehiculoResponse = response.body();
                if (vehiculoResponse.getStatus().equals("ok")){

                    Toast.makeText(getApplicationContext(),"Envío exitoso ", Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(getApplicationContext(),"Problemas  del Servicio", Toast.LENGTH_SHORT).show();
                }


            }else{
                Toast.makeText(getApplicationContext(),"Problemas Conexion", Toast.LENGTH_SHORT).show();


            }

        }

        @Override
        public void onFailure(Call<CarroResponse> call, Throwable t) {
            Toast.makeText(getApplicationContext(),"Error en la estructura", Toast.LENGTH_SHORT).show();
        }


    }
}

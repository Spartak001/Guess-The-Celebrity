package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    public class DownloadImage extends AsyncTask<String,Void,Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
                URL url =new URL(urls[0]);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream in=httpURLConnection.getInputStream();
                Bitmap bitmap= BitmapFactory.decodeStream(in);
                return bitmap;

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class DownloadContent extends AsyncTask<String ,Void,String>
    {
        @Override
        protected  String doInBackground(String... urls) {
            String reslut="";
            try{
                URL url=new URL(urls[0]);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.connect();
                InputStream in=httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(in);
                int data=inputStreamReader.read();
                while(data!=-1)
                {
                    reslut+=(char)data;
                    data=inputStreamReader.read();
                }
                return reslut;
            }catch (Exception e)
            {
                return "Error";
            }

        }
    }

    public void pressName(View v)
    {
        if(v.getTag().toString().equals(Integer.toString(rTru)))
        {
            Toast.makeText(MainActivity.this,"True!!!",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(MainActivity.this,"Wrong!!",Toast.LENGTH_LONG).show();
        }
        Game();
    }
    public void Game()
    {
        if(count<address.size()) {
            rTru = random.nextInt(4);//0,1,2,3
            switch (rTru) {
                case 0: {
                    b1.setText(names.get(position));
                    randForOthers(rTru, b2, b3, b4);
                    break;
                }
                case 1: {
                    b2.setText(names.get(position));
                    randForOthers(rTru, b1, b3, b4);
                    break;
                }
                case 2: {
                    b3.setText(names.get(position));
                    randForOthers(rTru, b2, b1, b4);
                    break;
                }
                case 3: {
                    b4.setText(names.get(position));
                    randForOthers(rTru, b2, b3, b1);
                    break;
                }
            }

            try {

                    bitmap = new DownloadImage().execute(address.get(position)).get();
                    imageView.setImageBitmap(bitmap);
                    position++;
                    count++;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "End", Toast.LENGTH_LONG).show();
            b1.setEnabled(false);
            b2.setEnabled(false);
            b3.setEnabled(false);
            b4.setEnabled(false);
        }


    }
    public void randForOthers(int k,Button b1,Button b2,Button b3)
    {
        int r=k;
        while (r==rTru)
        {
            r=random.nextInt(names.size());
        }
        b1.setText(names.get(r));
        r=k;
        while (r==rTru)
        {
            r=random.nextInt(names.size());
        }
        b2.setText(names.get(r));
        r=k;
        while (r==rTru)
        {
            r=random.nextInt(names.size());
        }
        b3.setText(names.get(r));
    }


    DownloadContent downloadContent;
    DownloadImage downloadImage;
    Bitmap bitmap;
    ArrayList<String> address=new ArrayList<>();
    ArrayList<String> names=new ArrayList<>();
    Random random=new Random();
    ImageView imageView;
    Button b1,b2,b3,b4;
    int rTru=0;
    int position;
    int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> arrayList=new ArrayList<>();
        imageView=(ImageView)findViewById(R.id.imageView);
        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        b3=(Button)findViewById(R.id.button3);
        b4=(Button)findViewById(R.id.button4);
        downloadContent=new DownloadContent();


        try {
            String str = downloadContent.execute("http://www.posh24.se/kandisar").get();
            Pattern pattern=Pattern.compile("img src=\"(.*?)\" alt");
            Matcher matcher=pattern.matcher(str);

            while(matcher.find())
            {
               address.add(matcher.group(1));
                Log.i("Textttt",matcher.group(1));
            }

            pattern=Pattern.compile("alt=\"(.*?)\"/>");
            matcher=pattern.matcher(str);
            while(matcher.find())
            {
                names.add(matcher.group(1));
                Log.i("Textttt",matcher.group(1));
            }
            Game();



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}

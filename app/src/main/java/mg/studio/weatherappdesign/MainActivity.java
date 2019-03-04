package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnClick(View view) {
        new DownloadUpdate().execute();
    }
    //
    public class main {
        public float temp;
    }
    public class weather {
        public String description;
    }

    public class list {
        //public int dt;
        public String dt_txt;
        public main main;
        public weather[] weather;
    }
    public class  city {
        public int id;
        public String name;
    }

    public class WeatherRec {
        public String cod;
        //public int message;
        //public int cnt;
        public list[] list;
        public city city;
    }

//
    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "https://mpianatra.com/Courses/forecast.json";
            //https://www.jianshu.com/p/886f7b7fca7d
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                //Log.d("string：",inputStream.toString());

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    Log.d("TAG", line);
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature

                Gson gson = new Gson();
                String jsonWeather = buffer.toString();
                WeatherRec weatherRec = gson.fromJson(jsonWeather, WeatherRec.class);
                Log.d("string：",weatherRec.list[0].weather[0].description);
                String Newjson = gson.toJson(weatherRec);
                return Newjson;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed
            Gson gson_layout = new Gson();
            WeatherRec weather_layout = gson_layout.fromJson(temperature, WeatherRec.class);
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(Integer.toString((int)(weather_layout.list[0].main.temp - 273)));
            ((TextView)findViewById(R.id.tv_location)).setText(weather_layout.city.name);
            //((ImageView)findViewById(R.id.img_weather_condition)).setImageResource();
        }
    }
}

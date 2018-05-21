package ro.pub.cs.systems.eim.lab06.clientservercommunication.network;

import android.renderscript.Element;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ro.pub.cs.systems.eim.lab06.clientservercommunication.general.Constants;
import ro.pub.cs.systems.eim.lab06.clientservercommunication.general.Utilities;


// firul de executie care gestioneaza comunicatia dintreserver si client

public class CommunicationThread extends Thread {

    private Socket socket;
    private EditText serverTextEditText;
    ServerThread serverThread;


    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    // b
    @Override
    public void run() {
        HashMap data = new HashMap<String, WeatherForecastInformation>();
        data.put("craiova", new WeatherForecastInformation(29.9, 25.6, "Clear", 90, 34));


        // socketul serverului
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket); // ia fluxul de intrare
            PrintWriter printWriter = Utilities.getWriter(socket); // ia fluxul de iesire
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");
            String city = bufferedReader.readLine();
            String informationType = bufferedReader.readLine();

            Log.v(Constants.TAG, "[COMMUNICATION THREAD:] INFORMATION TYPE:::::::::::::::: " + informationType);
            if (city == null || city.isEmpty() || informationType == null || informationType.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }

            Log.v(Constants.TAG, "[COMMUNICATION THREAD:] AJUNGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

            // caut orasul pe serverul local
            if (data.get(city) != null) {
                //  Log.v(Constants.TAG, "SA VEDDDEEEEEM:::: " + data.get(city).toString());

                switch (informationType) {
                    case Constants.ALL:
                        printWriter.println(data.get(city));
                        printWriter.flush(); // trimit clientului rezultatul
                        return;
                    case Constants.TEMPERATURE:
                        printWriter.println(((WeatherForecastInformation) data.get(city)).getTemperature());
                        printWriter.flush(); // trimit clientului rezultatul
                        return;
                    case Constants.WIND_SPEED:
                        printWriter.println(((WeatherForecastInformation) data.get(city)).getWindSpeed());
                        printWriter.flush(); // trimit clientului rezultatul
                        return;
                    case Constants.CONDITION:
                        printWriter.println(((WeatherForecastInformation) data.get(city)).getCondition());
                        printWriter.flush(); // trimit clientului rezultatul
                        return;
                    case Constants.HUMIDITY:
                        printWriter.println(((WeatherForecastInformation) data.get(city)).getHumidity());
                        printWriter.flush(); // trimit clientului rezultatul
                        return;
                    case Constants.PRESSURE:
                        printWriter.println(((WeatherForecastInformation) data.get(city)).getPressure());
                        printWriter.flush(); // trimit clientului rezultatul
                        return;
                    default:
                        printWriter.println("[COMMUNICATION THREAD] Wrong information type (all / temperature / wind_speed / condition / humidity / pressure)!");
                        printWriter.flush();
                        return;
                }
                
            }

            // in cazul in care nu am orasul local fac un request la serverul online
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            Log.v(Constants.TAG, "CITYYYYYYYYYYYYYYYYYYYYYYYYYY: " + city);
            String responseString = null;
            response = httpclient.execute(new HttpGet("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=7327013ba37bb2f07c2ba8186937b8f8&units=metric"));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                Log.v(Constants.TAG, "::::::::::::::::::::" + responseString);
                out.close();
            }

            // parsez json ul
            JSONObject jObject = new JSONObject(responseString);
            String cityName = jObject.getString("name");
            JSONArray conditionObj = jObject.getJSONArray("weather");
            List<String> listCondition = new ArrayList<String>();
            for (int i = 0; i < conditionObj.length(); i++) {
                listCondition.add(conditionObj.getJSONObject(i).getString("description"));
            }

            String condition = listCondition.get(0);
            Log.v(Constants.TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ conition =  " + condition);

            JSONObject mainObj = jObject.getJSONObject("main");
            Double temperature = mainObj.getDouble("temp");
            Log.v(Constants.TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Temperatura::: " + temperature);

            int humidity = mainObj.getInt("humidity");
            Log.v(Constants.TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Humidity::: " + humidity);

            int pressure = mainObj.getInt("pressure");
            Log.v(Constants.TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Pressure::: " + pressure);

            JSONObject windSpeed = jObject.getJSONObject("wind");
            Double wind = windSpeed.getDouble("speed");
            Log.v(Constants.TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Wind Speed::: " + wind);
            data.put(cityName, new WeatherForecastInformation(temperature, wind, condition, pressure, humidity));

            switch (informationType) {
                case Constants.ALL:
                    printWriter.println(data.get(cityName));
                    printWriter.flush(); // trimit clientului rezultatul
                    return;
                case Constants.TEMPERATURE:
                    printWriter.println(((WeatherForecastInformation) data.get(cityName)).getTemperature());
                    printWriter.flush(); // trimit clientului rezultatul
                    return;
                case Constants.WIND_SPEED:
                    printWriter.println(((WeatherForecastInformation) data.get(cityName)).getWindSpeed());
                    printWriter.flush(); // trimit clientului rezultatul
                    return;
                case Constants.CONDITION:
                    printWriter.println(((WeatherForecastInformation) data.get(cityName)).getCondition());
                    printWriter.flush(); // trimit clientului rezultatul
                    return;
                case Constants.HUMIDITY:
                    printWriter.println(((WeatherForecastInformation) data.get(cityName)).getHumidity());
                    printWriter.flush(); // trimit clientului rezultatul
                    return;
                case Constants.PRESSURE:
                    printWriter.println(((WeatherForecastInformation) data.get(cityName)).getPressure());
                    printWriter.flush(); // trimit clientului rezultatul
                    return;
                default:
                    printWriter.println("[COMMUNICATION THREAD] Wrong information type (all / temperature / wind_speed / condition / humidity / pressure)!");
                    printWriter.flush();
                    return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }

        }
    }
}


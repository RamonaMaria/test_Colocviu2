package ro.pub.cs.systems.eim.lab06.clientservercommunication.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import ro.pub.cs.systems.eim.lab06.clientservercommunication.general.Constants;
import ro.pub.cs.systems.eim.lab06.clientservercommunication.general.Utilities;

public class ClientAsyncTask extends AsyncTask<String, String, Void> {

    private EditText clientAddressEditText;
    private EditText clientPortEditText;
    private EditText cityEditText;
    private Spinner informationTypeSpinner;
    private TextView weatherForecastTextView; //  2. unde afisezzzz ( ASTA PUI IN CONSTRUCTOR)


    public ClientAsyncTask(TextView weatherForecastTextView) {
        this.weatherForecastTextView = weatherForecastTextView;
    }

    // 4
   @Override
    protected Void doInBackground(String... params) {

        Log.v(Constants.TAG, "[ClientAsyncTask]: DoInBackGround ");
        Socket socket = null;
        try {

            // deschid un canal de comunicatie
            String clientAddress = params[0];
            int clientPort = Integer.parseInt(params[1]);
            socket = new Socket(clientAddress, clientPort);
            if (socket == null) {
                return null;
            }

            // deschid urm obiecte pentru realizarea operatiilor de citire si scriere
            // pe canalul de comunicatie
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return null;
            }

            String city = params[2];
            Log.v(Constants.TAG, "ORAS: " + city);

            printWriter.println(city);
            printWriter.flush(); // trimiterea efectiva a datelor la serverul meu - local

            String informationType = params[3];
            printWriter.println(informationType);
            printWriter.flush();

            // CE IMI AFISEAZA LA ECRAN
            String weatherInformation;
            Log.v(Constants.TAG, "AJUNGE 0");
            while ((weatherInformation = bufferedReader.readLine()) != null) {
                Log.v(Constants.TAG, "AJUNGE 1");
                final String finalizedWeateherInformation  =  weatherInformation;
                Log.v(Constants.TAG, "AJUNGE 2");

                Log.v(Constants.TAG, "weatherInformation::::::::::::: " + weatherInformation);

                weatherForecastTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        weatherForecastTextView.setText(finalizedWeateherInformation);
                    }
                });
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        Log.v(Constants.TAG, "::::::::::: onPreExecute");
        weatherForecastTextView.setText("");
    }

    // 3
    @Override
    protected void onProgressUpdate(String... progress) {
        Log.v(Constants.TAG, ":::::::::::::: onProgressUpdate");
        weatherForecastTextView.append(progress[0] + "\n");
    }

    @Override
    protected void onPostExecute(Void result) {}

}

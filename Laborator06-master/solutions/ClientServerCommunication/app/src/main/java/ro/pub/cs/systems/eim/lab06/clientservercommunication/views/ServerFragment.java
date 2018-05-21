package ro.pub.cs.systems.eim.lab06.clientservercommunication.views;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import ro.pub.cs.systems.eim.lab06.clientservercommunication.R;
import ro.pub.cs.systems.eim.lab06.clientservercommunication.general.Constants;
import ro.pub.cs.systems.eim.lab06.clientservercommunication.network.ClientAsyncTask;
import ro.pub.cs.systems.eim.lab06.clientservercommunication.network.ServerThread;

public class ServerFragment extends Fragment {

    private EditText serverPortEditText;
    private Button connectButton;
    private ServerThread serverThread;

    private ServerFragment.ConnectButtonClickListener buttonClickListener = new ServerFragment.ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Context context = getActivity();
                Toast.makeText(context, "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;

            }
            serverThread = new ServerThread(Integer.parseInt(serverPort)); // trimit portu la socket sa il deschid
            try {
                if (serverThread.getServerSocket() == null) {
                        Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                        return;
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverThread.start(); // pornesc serverul pe portul dat si astept sa ie invocat de catre clienti
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_server, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        Log.v(Constants.TAG, "SERVER: OnActivityCreated: ");


        serverPortEditText = (EditText)getActivity().findViewById(R.id.server_port_edit_text);
    //    serverPortEditText.addTextChangedListener(serverTextContentWatcher);

        connectButton = (Button)getActivity().findViewById(R.id.connect_button);
        connectButton.setOnClickListener(buttonClickListener);
    }

    @Override
    public void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

}

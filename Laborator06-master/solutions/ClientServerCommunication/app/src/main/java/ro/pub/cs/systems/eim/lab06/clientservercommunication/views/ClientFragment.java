package ro.pub.cs.systems.eim.lab06.clientservercommunication.views;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab06.clientservercommunication.R;
import ro.pub.cs.systems.eim.lab06.clientservercommunication.general.Constants;
import ro.pub.cs.systems.eim.lab06.clientservercommunication.network.ClientAsyncTask;

public class ClientFragment extends Fragment {

    private EditText clientAddressEditText, clientPortEditText, cityEditText;
    private TextView weatherForecattextView;
    private Spinner informationTypeSpinner;
    private Button getWeatherForecatButton;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Log.v(Constants.TAG, "[CLIENT] onClick");
            ClientAsyncTask clientAsyncTask = new ClientAsyncTask(weatherForecattextView); // ii dai unde trebuie sa afiseze
            clientAsyncTask.execute(clientAddressEditText.getText().toString(), clientPortEditText.getText().toString(),
                                    cityEditText.getText().toString(), informationTypeSpinner.getSelectedItem().toString()); // trimit parametrii introdusi in client
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_client, parent, false);
    }

    // 1
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        clientAddressEditText = (EditText)getActivity().findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)getActivity().findViewById(R.id.client_port_edit_text);
        cityEditText = (EditText)getActivity().findViewById(R.id.city_edit_text);
        informationTypeSpinner = (Spinner)getActivity().findViewById(R.id.information_type_spinner);
        getWeatherForecatButton = (Button)getActivity().findViewById(R.id.get_weather_forecast_button);
        getWeatherForecatButton.setOnClickListener(buttonClickListener);
        weatherForecattextView= (TextView)getActivity().findViewById(R.id.weather_forecast_text_view);
        informationTypeSpinner = getActivity().findViewById(R.id.information_type_spinner);
    }

}

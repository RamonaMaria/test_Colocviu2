package ro.pub.cs.systems.eim.lab06.clientservercommunication.general;

public interface Constants {

    public final static String SERVER_HOST = "localhost";
    public final static int SERVER_PORT = 2017;

    public final static String SERVER_START = "Start Server";
    public final static String SERVER_STOP = "Stop Server";
    public final static String TEMPERATURE = "temperature";
    public final static String HUMIDITY = "humidity";
    public final static String PRESSURE = "pressure";
    public final static String ALL = "all";
    public final static String WIND_SPEED = "wind speed";
    public final static String CONDITION = "condition";

    public final static boolean DEBUG = true;

    public final static String TAG = "Client Server Comm";
    public final static String WEB_SERVICE_ADDRESS = "http://www.wunderground.com/cgi-bin/findweather/getForecast";
    public final static String QUERY_ATTRIBUTE = "Post";

}

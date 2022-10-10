import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;

public class BreviumServer implements ServerInterface {

    private final String API_TOKEN = "8a044381-362f-4b21-9e96-80e323a48359"; // TODO would normally not store this here
    private final String API_URL = "http://scheduling-interview-2021-265534043.us-west-2.elb.amazonaws.com/api/Scheduling";
    private Gson gson;

    public BreviumServer() {
        gson = new Gson();
    }

    @Override
    public boolean start() {
        String response = sendRequest("Start", "POST", null);
        return response != null;
    }

    @Override
    public AppointmentInfo[] stop() {
        String response = sendRequest("Stop", "POST", null);
        if (response == null) {
            return new AppointmentInfo[] {};
        }
        AppointmentInfo[] appointments = gson.fromJson(response, AppointmentInfo[].class);
        return appointments;
    }

    @Override
    public AppointmentRequest[] getAppointmentRequests() {
        ArrayList<AppointmentRequest> requests = new ArrayList<>();

        String result = sendRequest("AppointmentRequest", "GET", null);
        while (result != null) {
            AppointmentRequest newRequest = gson.fromJson(result, AppointmentRequest.class);
            requests.add(newRequest);
            result = sendRequest("AppointmentRequest", "GET", null);
        }

        AppointmentRequest[] arrayRequests = new AppointmentRequest[requests.size()];
        return requests.toArray(arrayRequests);
    }

    @Override
    public AppointmentInfo[] getInitialSchedule() {
        String response = sendRequest("Schedule", "GET", null);
        if (response == null) {
            return new AppointmentInfo[] {};
        }
        AppointmentInfo[] appointments = gson.fromJson(response, AppointmentInfo[].class);
        return appointments;
    }

    @Override
    public boolean markAppointment(AppointmentInfoRequest appointment) {
        String body = gson.toJson(appointment);
        String response = sendRequest("Schedule", "POST", body);
        return response != null;
    }

    private String sendRequest(String endpoint, String method, String jsonBody) {
        String urlString = String.format("%s/%s?token=%s", API_URL, endpoint, API_TOKEN);
        URL url;
        try {
            url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);

            if (jsonBody != null) {
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonBody.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }
            con.connect();

            int status = con.getResponseCode();
            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();
                return content.toString();
            } else {
                System.out.printf("Got response code: %d for endpoint: %s\n", status, endpoint);
                con.disconnect();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

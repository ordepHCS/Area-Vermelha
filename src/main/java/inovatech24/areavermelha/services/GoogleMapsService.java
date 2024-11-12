package inovatech24.areavermelha.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleMapsService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    public double[] getCoordinates(String address) {
        try {
            String url = "" //waiting api key
                    + address.replace(" ", "+") + "&key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            JSONObject jsonObject = new JSONObject(response);

            JSONArray results = jsonObject.getJSONArray("results");
            if(results.length() > 0) {
                JSONObject location = results.getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");
                return new double[]{location.getDouble("latitude"), location.getDouble("longitude")};
            }
        }catch(Exception exception) {
            exception.printStackTrace();
        }
        return new double[]{0.0, 0.0};
    }
}

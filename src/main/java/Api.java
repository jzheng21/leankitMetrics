import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class Api {

    private String url;
    private String authorizationCode;

    public Api(String url, String authorizationCode) {
        this.url = url;
        this.authorizationCode = authorizationCode;
    }

    public JSONObject postRequest() throws IOException {
        JSONObject jsonObject;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("Authorization", authorizationCode)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return new JSONObject(response.body().string());
    }
}

package tulaa.io.fileprocessor.services;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import tulaa.io.fileprocessor.BuildConfig;

/**
 * Created by jmaina on 3/10/19.
 */

public interface WebAPIService {
    public static String BASE_URL= BuildConfig.SERVER_URL;
    @Multipart
    @POST("/files")
    Call<ResponseBody> postFile(@Part MultipartBody.Part csv, @Part("description") RequestBody description);
}

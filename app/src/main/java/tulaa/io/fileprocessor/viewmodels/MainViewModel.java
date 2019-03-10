package tulaa.io.fileprocessor.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tulaa.io.fileprocessor.services.WebAPIService;

/**
 * Created by jmaina on 3/10/19.
 */

public class MainViewModel extends ViewModel {
    //The gson builder
    Gson gson = new GsonBuilder().setLenient().create();
    //creating retrofit object
    Retrofit retrofit = new Retrofit.Builder().baseUrl(WebAPIService.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

    //creating our api
    WebAPIService webAPIService = retrofit.create(WebAPIService.class);

    public final ObservableField<Integer> showProgress =
            new ObservableField<>();
    public MutableLiveData<Boolean> successfullyUploaded = new MutableLiveData<>();


    public MainViewModel(){
        showProgress.set(View.GONE);
    }

    public boolean onClickUploadBtn(View view)  {
       try {
           showProgress.set(View.VISIBLE);
           Log.d("", "++++++++++btn clicked++++++++");
           File file = new File(view.getContext().getCacheDir(), "users.csv");
           copyInputStreamToFile(view.getContext().getAssets().open("users.csv"), file);
           RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
           MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

// add another part within the multipart request
           RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"),  ""+System.currentTimeMillis());

           webAPIService.postFile(body, description).enqueue(new Callback<ResponseBody>() {
               @Override
               public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                   //  data.setValue(response.body());
                   showProgress.set(View.GONE);
                   if (response.body() != null && response.isSuccessful()) {
                       successfullyUploaded.setValue(true);
                   }else {
                       successfullyUploaded.setValue(false);
                   }
               }

               @Override
               public void onFailure(Call<ResponseBody> call, Throwable t) {
                   showProgress.set(View.GONE);
                   successfullyUploaded.setValue(false);
               }

               // Error case is left out for brevity.
           });
       }catch(Exception e){
           Log.e("",e.getMessage());
       }
        return true;
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if ( out != null ) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
}

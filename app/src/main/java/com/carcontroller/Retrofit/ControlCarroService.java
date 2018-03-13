package com.carcontroller.Retrofit;

import com.carcontroller.Retrofit.Model.AudioPost;
import com.carcontroller.Retrofit.Model.CarroPost;
import com.carcontroller.Retrofit.Model.CarroResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Hudeya on 7/11/2017.
 */

public interface ControlCarroService {
    @POST("comandoCarro2")
    Call<CarroResponse> postRegistrarComando(@Body CarroPost carroPost);

    @Multipart
    @POST("upload2")
    Call<ResponseBody> upload(
            @Part("description") AudioPost audioPost,
            @Part MultipartBody.Part file
    );
}

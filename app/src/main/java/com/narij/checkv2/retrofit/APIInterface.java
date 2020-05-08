package com.narij.checkv2.retrofit;

import com.narij.checkv2.retrofit.model.RetrofitDuties;
import com.narij.checkv2.retrofit.model.RetrofitDuty;
import com.narij.checkv2.retrofit.model.RetrofitExperts;
import com.narij.checkv2.retrofit.model.RetrofitFriends;
import com.narij.checkv2.retrofit.model.RetrofitGroups;
import com.narij.checkv2.retrofit.model.RetrofitLogs;
import com.narij.checkv2.retrofit.model.RetrofitMessages;
import com.narij.checkv2.retrofit.model.RetrofitRecords;
import com.narij.checkv2.retrofit.model.RetrofitRegisteredUsers;
import com.narij.checkv2.retrofit.model.RetrofitUser;
import com.narij.checkv2.retrofit.model.WebServiceMessage;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIInterface {


    @GET("login/{phone}/{password}/{time}")
    Call<RetrofitUser> login(
            @Path("phone") String phone,
            @Path("password") String password,
            @Path("time") long time
    );

    @GET("users/fcm_token/change/{user}/{fcm_token}/{time}")
    Call<WebServiceMessage> changeFcmToken(
            @Path("user") int user,
            @Path("fcm_token") String fcm_token,
            @Path("time") long time
    );


    @GET("verify/{phone}/{verification_code}/{time}")
    Call<WebServiceMessage> verify(
            @Path("phone") String phone,
            @Path("verification_code") String verification_code,
            @Path("time") long time
    );

    @FormUrlEncoded
    @POST("register")
    Call<WebServiceMessage> register(
            @Field("name") String name,
            @Field("username") String username,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("email") String email,
            @Field("fcm_token") String fcm_token,
            @Field("time") long time
    );

    @FormUrlEncoded
    @POST("profile/edit")
    Call<WebServiceMessage> editProfile(
            @Field("id") int id,
            @Field("name") String name,
            @Field("username") String username,
            @Field("email") String email,
            @Field("time") long time
    );

    @Multipart
    @POST("upload")
    Call<WebServiceMessage> setProfilePhoto(
            @Part("user") int user,
            @Part MultipartBody.Part file,
            @Field("time") long time
    );


    @GET("remove/image/{user}/{time}")
    Call<WebServiceMessage> removeImage(
            @Path("user") int user,
            @Path("time") long time
    );

    @GET("duties/get/{creator}/{time}")
    Call<RetrofitDuties> getDuties(
            @Path("creator") int creator,
            @Path("time") long time
    );


    //    @GET("users/registered/{user}/{phones}")
//    Call<RetrofitRegisteredUsers> getRegisteredUsers(
//            @Path("user") int user,
//            @Path("phones") String users
//    );
    @FormUrlEncoded
    @POST("users/registered")
    Call<RetrofitRegisteredUsers> getRegisteredUsers(
            @Field("user") int user,
            @Field("phones") String users,
            @Field("time") long time
    );


    @GET("groups/get/{creator}/{time}")
    Call<RetrofitGroups> getDutiesAndGroups(
            @Path("creator") int creator,
            @Path("time") long time
    );

    @GET("duties/get/one/{duty}/{time}")
    Call<RetrofitDuty> getDuty(
            @Path("duty") int duty,
            @Path("time") long time
    );


    @GET("groups/just/get/{creator}/{time}")
    Call<RetrofitGroups> getGroups(
            @Path("creator") int creator,
            @Path("time") long time
    );

    @GET("duties/finish/{user}/{duty}/{finish_type}/{time}")
    Call<WebServiceMessage> finishDuty(
            @Path("user") int user,
            @Path("duty") int duty,
            @Path("finish_type") int finish_type,
            @Path("time") long time
    );


    @GET("logs/{duty}/{time}")
    Call<RetrofitLogs> getLogs(
            @Path("duty") int duty,
            @Path("time") long time
    );

    @FormUrlEncoded
    @POST("logs")
    Call<WebServiceMessage> addLog(
            @Field("user") int user,
            @Field("duty") int duty,
            @Field("log") String log,
            @Field("date") long date,
            @Field("time") long time
    );

//    Route::get('records/{record_type}/{duty}/{time}', "RecordController@getRecords")->name('records.get');
//    Route::post('records/{record_type}', "LogController@store")->name('records.store');

    @GET("records/{record_type}/{duty}/{time}")
    Call<RetrofitRecords> getRecoords(
            @Path("record_type") String record_type,
            @Path("duty") int duty,
            @Path("time") long time
    );

    @FormUrlEncoded
    @POST("records/{record_type}")
    Call<WebServiceMessage> addRecord(
            @Path("record_type") String record_type,
            @Field("user") int user,
            @Field("duty") int duty,
            @Field("log") String content,
            @Field("content_type") int content_type,
            @Field("date") long date,
            @Field("time") long time
    );


    @FormUrlEncoded
    @POST("duties")
    Call<WebServiceMessage> addDuty(
            @Field("title") String title,
            @Field("description") String description,
            @Field("start_date") long startDate,
            @Field("duration") long duration,
            @Field("parent") int parent,
            @Field("group") int group,
            @Field("priority") int priority,
            @Field("creator") int creator,
            @Field("users") String users,
            @Field("experts") String experts,
            @Field("exact_time") int exact_time,
            @Field("can_continue_after_timeout") int can_continue_after_timeout,
            @Field("time") long time

    );

    @FormUrlEncoded
    @POST("duties/edit")
    Call<WebServiceMessage> editDuty(
            @Field("id") int id,
            @Field("title") String title,
            @Field("description") String description,
            @Field("start_date") long startDate,
            @Field("duration") long duration,
            @Field("parent") int parent,
            @Field("group") int group,
            @Field("priority") int priority,
            @Field("creator") int creator,
            @Field("users") String users,
            @Field("experts") String experts,
            @Field("exact_time") int exact_time,
            @Field("can_continue_after_timeout") int can_continue_after_timeout,
            @Field("time") long time

    );


    @FormUrlEncoded
    @POST("groups")
    Call<WebServiceMessage> addGroup(
            @Field("creator") int creator,
            @Field("id") int id,
            @Field("title") String title,
            @Field("description") String description,
            @Field("users") String users,
            @Field("time") long time
    );


    @GET("groups/remove/{id}/{time}")
    Call<WebServiceMessage> removeGroup(
            @Path("id") int id,
            @Path("time") long time
    );


    @FormUrlEncoded
    @POST("friends/change")
    Call<WebServiceMessage> sendFriendshipSituation(
            @Field("user") int user,
            @Field("another") int another,
            @Field("command") int command,
            @Field("time") long time
    );


    @FormUrlEncoded
    @POST("friends/remove")
    Call<WebServiceMessage> removeFriendship(
            @Field("a") int a,
            @Field("b") int b,
            @Field("time") long time
    );


    @GET("friends/get/{id}/{time}")
    Call<RetrofitFriends> getFriends(
            @Path("id") int id,
            @Path("time") long time
    );


    @FormUrlEncoded
    @POST("messages")
    Call<WebServiceMessage> addMessage(
            @Field("user") int user,
            @Field("content") String content,
            @Field("duty") int duty,
            @Field("time") long time
    );


    @GET("messages/get/{duty}/{date}/{time}")
    Call<RetrofitMessages> getMessages(
            @Path("duty") int duty,
            @Path("date") long date,
            @Path("time") long time
    );

    @GET("experts/get/all/{time}")
    Call<RetrofitExperts> getAllExperts(
            @Path("time") long time
    );

    @GET("users/get/{user}/{time}")
    Call<RetrofitUser> getUser(
            @Path("user") int user,
            @Path("time") long time
    );


}

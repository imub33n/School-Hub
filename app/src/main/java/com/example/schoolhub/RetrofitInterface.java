package com.example.schoolhub;

import com.example.schoolhub.data.FacultyRequest;
import com.example.schoolhub.data.Fee;
import com.example.schoolhub.data.FeeStructure;
import com.example.schoolhub.data.Following;
import com.example.schoolhub.data.Image;
import com.example.schoolhub.data.Likes;
import com.example.schoolhub.data.LiveStreamRequests;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.Notification;
import com.example.schoolhub.data.PostResult;
import com.example.schoolhub.data.ReplyReview;
import com.example.schoolhub.data.SchoolData;
import com.example.schoolhub.data.SchoolHubReview;
import com.example.schoolhub.data.SchoolReviews;
import com.example.schoolhub.data.SearchFilters;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @POST("/user_management/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/user_management/signup")
    Call<HashMap<String, String>> executeSignup (@Body HashMap<String, String> map);

    @POST("/dashboard/post")
    Call<Void> executePost(@Body HashMap<String, String> map);

    @GET("/dashboard/home")
    Call<List<PostResult>> doGetListResources();

    @PATCH("/dashboard/updateComment/{id}")
    Call<PostResult> putComment(@Path("id") String id, @Body HashMap<String, HashMap<String, String>> map);

    @PATCH("/dashboard/updateLike/{id}")
    Call<PostResult> putLike(@Path("id") String id, @Body HashMap<String, Likes> map);

    @POST("/school/Create_School")
    Call<Void> createSchool(@Body SchoolData schoolData);

    @GET("/school/School_Details")
    Call<List<SchoolData>> getSchoolData();

    @PATCH("/school/Edit_School/{id}")
    Call<SchoolData> putSchoolData(@Path("id") String id, @Body SchoolData UpdatedSchoolData);

    @POST("/searchSchool/search/{yes}")
    Call<List<SchoolData>> searchSchools(@Path("yes") String yes, @Body SearchFilters searchFilters);

    @POST("/videoStreaming/addStream")
    Call<Void> postStreamRequest (@Body HashMap<String, String> map);

    @GET("/videoStreaming/getStreams")
    Call<List<LiveStreamRequests>> getStreams();

    @GET("/review/reviews")
    Call<List<SchoolReviews>> getReviews();
    
    @POST("/review/addReview")
    Call<Void> postReviews(@Body SchoolReviews schoolReviews);

    @PATCH("/review/updateReview/{id}")
    Call<Void> replyReview(@Path("id") String id, @Body ReplyReview reply);

    @GET("/user_management/userProfile/{id}")
    Call<List<LoginResult>> userData(@Path("id") String id);

    @PATCH("/user_management/userProfile/updateProfilePic/{id}")
    Call<Void> updateDp(@Path("id") String id,@Body HashMap<String, String> map);

    @PATCH("/user_management/userProfile/updateProfile/{id}")
    Call<Void> updateUserData(@Path("id") String id,@Body HashMap<String, String> map);

    @PATCH("/videoStreaming/updateStreamURI/{id}")
    Call<Void> streamURIPatch(@Path("id") String id,@Body LiveStreamRequests ohYes);

    @GET("/teacherRequest/getTeacherRequests")
    Call<List<FacultyRequest>> getFaculty();

    @POST("/teacherRequest/addTeacherRequest")
    Call<Void> postTeacherRequest(@Body FacultyRequest facultyRequest);

    @PATCH("/teacherRequest/updateTeacherRequest/{id}")
    Call<Void> teacherRequest(@Path("id") String id,@Body HashMap<String, String> map);

    @POST("/chat/chatPost")
    Call<Void> addChatFriend(@Body HashMap<String, String> map);

    @POST("/mainReview/schoolhubReviews")
    Call<Void> postSchoolhubReview(@Body SchoolHubReview schoolHubReview);

    @GET("/mainReview/schoolhubReviews")
    Call<List<SchoolHubReview>> getSchoolHubReviews();

    @POST("/school/delete_School/{id}")
    Call<Void> deleteSchool(@Path("id") String id,@Body HashMap<String, String> map);

    @POST("/school/delete_Teacher/{id}")
    Call<Void> deleteTeacher(@Path("id") String id,@Body HashMap<String, String> map);

    @GET("/dashboard/deletePost/{id}")
    Call<Void> deletePost(@Path("id") String id);

    @POST("/user_management/searchUser/{id}")
    Call<List<LoginResult>> getUser(@Path("id") String id,@Body HashMap<String, String> map);

    @POST("/school/delete_Image/{id}")
    Call<Void> deleteImage(@Path("id") String id,@Body HashMap<String, String> map);

    @POST("/school/addNewSchoolImages/{id}")
    Call<Void> addImage(@Path("id") String id,@Body HashMap<String, Image> map);

    @PATCH("/user_management/userProfile/updateFollowing/{id}")
    Call<Void> updateFollow(@Path("id") String id,@Body Following following);

    @PATCH("/notification/addNotification/{id}")
    Call<Void> sendNotification(@Path("id") String id, @Body Notification notification);

    @PATCH("/notification/deleteNotification/{id}")
    Call<Void> deleteNoti(@Path("id") String id, @Body HashMap<String, String> map);

    @PATCH("/school/Edit_SchoolFee/{id}")
    Call<Void> editFeeStructure(@Path("id") String id, @Body List<FeeStructure> feeStructures);
}

// 35 Total requests
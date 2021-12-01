package com.clyr.utils.utilshelper;

import android.app.Activity;
import android.database.Observable;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by M S I of clyr on 2019/5/17.
 */
public interface ORService {
    @GET("json/login.action")
    android.database.Observable<TestData<User>> reLogin(@Query("tel") String tel, @Query("password") String password);

    @GET
    Call<ResponseBody> get();

    @GET
    Call<ResponseBody> get(@Url String url);

    @GET
    Call<ResponseBody> get(@Url String url, @QueryMap Map<String, String> map);

    @POST
    Call<ResponseBody> post();

    @POST
    Call<ResponseBody> post(@Url String url);

    @POST
    Call<ResponseBody> post(@Url String url, @QueryMap Map<String, String> map);


    //TODO 测试
    @POST("common/air/getCityPM25Detail")
    Call<ResponseBody> post(@QueryMap Map<String, String> map);

    @POST("common/air/getCityPM25Detail")
    Call<ResponseBody> post(@Query("apiKey") String apiKey,
                            @Query("city") String city);
    @POST("common/air/getCityPM25Detail")
    Call<ResponseBody> get(@Query("apiKey") String apiKey,
                            @Query("city") String city);
    /**
     * CSDN原文 https://blog.csdn.net/qq_36699930/article/details/80564850
     */
    /**
     * 1.get无参请求
     * https://api.github.com/users/basil2style
     */
    @GET("basil2style")
    Call<ResponseBody> getbasil2style();

    //Call<String> getbasil2style2();

    /**
     * 2.get有参请求
     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
     */
    @GET("news/php/varcache_getnews.php")
    Call<ResponseBody> getNewsInfo(@Query("id") String id,
                                   @Query("page") String page,
                                   @Query("plat") String plat,
                                   @Query("version") String version);

    /**
     * 3.gson转换器
     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
     */

    @GET("news/php/varcache_getnews.php")
    Call<List<User>> getNewsInfo2(@Query("id") String id,
                                  @Query("page") String page,
                                  @Query("plat") String plat,
                                  @Query("version") String version);

    /**
     * 4.@Path:用于url中的占位符,所有在网址中的参数（URL的问号前面）
     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
     */
    @GET("news/{php}/varcache_getnews.php")
    Call<List<User>> getNewsInfoPath(@Path("php") String php,
                                     @Query("id") String id,
                                     @Query("page") String page,
                                     @Query("plat") String plat,
                                     @Query("version") String version);

    /**
     * 5.@QueryMap:参数太多时可以用@QueryMap封装参数,相当于多个@Query
     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
     */

    @GET("news/php/varcache_getnews.php")
    Call<List<User>> getNewsInfo3(@QueryMap Map<String, String> map);


    /**
     * 6.post请求;
     * url：http://zhushou.72g.com/app/game/game_list/
     * params:platform=2&page=1
     * FieldMap：多个参数时可以使用，类型@QueryMap
     * FormUrlEncoded：表示请求实体是一个Form表单，每个键值对需要使用@Field注解
     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
     */
    @FormUrlEncoded
    @POST("app/game/game_list/")
    Call<User> getGameInfo(@Field("platform") String platform,
                           @Field("page") String page);

    /**
     * 7.body注解：上传json格式的数据
     *
     * @param url
     * @param Body
     * @return
     */
    @POST()
    Call<ResponseBody> getNewsInfoByBody(@Url String url, @Body RequestBody Body);

    /**
     * 直接传入实体,它会自行转化成Json，这个转化方式是GsonConverterFactory定义的。
     *
     * @param url
     * @param newsInfo
     * @return
     */
    @POST("api/{url}/newsList")
    Call<List<User>> login(@Path("url") String url, @Body User newsInfo);

    /**
     * 8.若需要重新定义接口地址，可以使用@Url，将地址以参数的形式传入即可。如
     *
     * @param url
     * @param map
     * @return
     */
    @GET
    Call<List<Activity>> getActivityList(@Url String url, @QueryMap Map<String, String> map);

    /**
     * 9.使用@Headers添加多个请求头
     * 用于添加固定请求头，可以同时添加多个。通过该注解添加的请求头不会相互覆盖，而是共同存在
     *
     * @param url
     * @param map
     * @return
     */
    @Headers({
            "User-Agent:android",
            "apikey:123456789",
            "Content-Type:application/json",
    })
    @POST()
    Call<List<User>> post2(@Url String url, @QueryMap Map<String, String> map);


    /**
     * 10.@Header注解：
     * 作为方法的参数传入，用于添加不固定值的Header，该注解会更新已有的请求头
     *
     * @param token
     * @param activeId
     * @return
     */
    @GET("mobile/active")
    Call<List<User>> get(@Header("token") String token, @Query("id") int activeId);


    /**
     * 11.@HTTP注解：
     * method 表示请求的方法，区分大小写
     * path表示路径
     * hasBody表示是否有请求体
     */
    @HTTP(method = "GET", path = "blog/{id}", hasBody = false)
    Call<ResponseBody> getBlog(@Path("id") int id);

    /**
     * 12.Streaming注解:表示响应体的数据用流的方式返回，适用于返回的数据比较大，该注解在在下载大文件的特别有用
     */
    @Streaming
    @GET
    Call<List<User>> downloadPicture(@Url String fileUrl);


    ///////上传单张图片//////

    /**
     * Multipart：表示请求实体是一个支持文件上传的Form表单，需要配合使用@Part,适用于 有文件 上传的场景
     * Part:用于表单字段,Part和PartMap与Multipart注解结合使用,适合文件上传的情况
     * PartMap:用于表单字段,默认接受的类型是Map<String,REquestBody>，可用于实现多文件上传
     * Part 后面支持三种类型，{@link RequestBody}、{@link MultipartBody.Part} 、任意类型；
     *
     * @param file 服务器指定的上传图片的key值
     * @return
     */

    @Multipart
    @POST("upload/upload")
    Call<List<User>> upload1(@Part("file" + "\";filename=\"" + "test.png") RequestBody file);

    @Multipart
    @POST("xxxxx")
    Call<List<User>> upload2(@Part MultipartBody.Part file);

    //////////上传多张图片/////////

    /**
     * @param map
     * @return
     */
    @Multipart
    @POST("upload/upload")
    Call<List<User>> upload3(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("upload/upload")
    Call<List<User>> upload4(@PartMap Map<String, MultipartBody.Part> map);


    //////图文混传/////

    /**
     * @param params
     * @param files
     * @return
     */
    @Multipart
    @POST("upload/upload")
    Call<List<User>> upload5(@FieldMap() Map<String, String> params,
                             @PartMap() Map<String, RequestBody> files);

    /**
     * Part 后面支持三种类型，{@link RequestBody}、{@link MultipartBody.Part} 、任意类型；
     *
     * @param userName
     * @param passWord
     * @param file
     * @return
     */
    @Multipart
    @POST("xxxxx")
    Call<List<User>> upload6(@Part("username") RequestBody userName,
                             @Part("password") RequestBody passWord,
                             @Part MultipartBody.Part file);


    /**
     * 15.rxjava
     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
     */

    @GET("news/php/varcache_getnews.php")
    Observable<List<User>> getNewsInfoByRxJava(@Query("id") String id,
                                               @Query("page") String page,
                                               @Query("plat") String plat,
                                               @Query("version") String version);


}

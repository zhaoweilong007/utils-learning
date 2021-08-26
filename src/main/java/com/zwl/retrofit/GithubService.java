package com.zwl.retrofit;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * api client
 *
 * @author ZhaoWeiLong
 * @since 2021/8/26
 **/
public interface GithubService {

  @GET("user")
  @Headers(value = {"Accept: application/vnd.github.v3+json"})
  Map<String, Object> user();

  @GET("users/{user}/repos")
  Call<List<Map<String, Object>>> listRepos(@Path("user") String user);

}

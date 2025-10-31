package com.khetigaadi.retrofit;
import androidx.lifecycle.LiveData;
import com.khetigaadi.base.model.AllBannerResp;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.model.youtube.VideoListResp;
import com.khetigaadi.base.model.youtube.VideoResp;
import com.khetigaadi.base.room.repository.utils.ApiResponse;
import com.khetigaadi.commons.model.ModelResp;
import com.khetigaadi.commons.model.SearchResp;
import com.khetigaadi.commons.model.TypeResp;
import com.khetigaadi.commons.model.BrandResp;
import com.khetigaadi.commons.utils.HomeListResp;
import com.khetigaadi.commons.utils.HpResponse;
import com.khetigaadi.commons.utils.NewsDetailResp;
import com.khetigaadi.harvester.model.Harvester;
import com.khetigaadi.harvester.model.HarvesterListResp;
import com.khetigaadi.harvester.model.HarvesterResp;
import com.khetigaadi.harvester.model.SimilarHarvesterListResp;
import com.khetigaadi.implement.compare.model.CompImpReq;
import com.khetigaadi.implement.compare.model.CompImpResp;
import com.khetigaadi.implement.compare.model.CompareImplementListResp;
import com.khetigaadi.implement.compare.model.GetImpByModelReq;
import com.khetigaadi.implement.model.ImplementDetailsResp;
import com.khetigaadi.implement.model.SimilarImplementListResp;
import com.khetigaadi.implement.newImplement.model.NewImpParams;
import com.khetigaadi.implement.newImplement.util.ImplementList;
import com.khetigaadi.implement.newImplement.util.ImplementListResp;
import com.khetigaadi.implement.newImplement.util.NewImp;
import com.khetigaadi.isOwner.OwnerTractorResp;
import com.khetigaadi.mandi.model.MondiPriceResp;
import com.khetigaadi.model.SafetyTipsResp;
import com.khetigaadi.news.activity.CommentListResp;
import com.khetigaadi.news.model.CommentResp;
import com.khetigaadi.news.model.NewsListResp;
import com.khetigaadi.tractor.compare.model.CompTractResp;
import com.khetigaadi.tractor.compare.model.CompareTractorParam;
import com.khetigaadi.tractor.compare.model.CompareTractorsListResp;
import com.khetigaadi.tractor.dealer.model.DealerListResp;
import com.khetigaadi.tractor.newTractor.model.NewTrSearchByNameResp;
import com.khetigaadi.tractor.newTractor.model.NewTrSpecResp;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
import com.khetigaadi.tractor.newTractor.model.NewTractorOverviewResp;
import com.khetigaadi.tractor.newTractor.model.NewTractorPictureResp;
import com.khetigaadi.tractor.newTractor.model.NewTractorResp;
import com.khetigaadi.tractor.newTractor.model.SimilarNewTractorResp;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;
import com.khetigaadi.tractor.rent.model.RentTractor;
import com.khetigaadi.tractor.rent.model.RentTractorDetailsResp;
import com.khetigaadi.tractor.rent.model.RentTractorResp;
import com.khetigaadi.tractor.rent.model.SimilarRentTractorsResp;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentImpImageResp;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentTractorUploadResp;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.UploadRentTrReq;
import com.khetigaadi.tractor.usedTractor.model.GetLoan;
import com.khetigaadi.tractor.usedTractor.model.GetLoanResp;
import com.khetigaadi.tractor.usedTractor.model.SimilarParams;
import com.khetigaadi.tractor.usedTractor.model.SimilarUsedTractorsResp;
import com.khetigaadi.tractor.usedTractor.model.UsedTractorListResp;
import com.khetigaadi.tractor.usedTractor.model.UsedTractorResp;
import com.khetigaadi.tractor.usedTractor.util.UsedTrByName;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;
import com.khetigaadi.user.buyserEnquiry.model.InterestedBuyersResp;
import com.khetigaadi.user.model.DashboardResp;
import com.khetigaadi.user.model.UserResponse;
import com.khetigaadi.user.myRentTractor.MyRentTractorResp;
import com.khetigaadi.user.myUsedTractor.model.MyUploadedTrReq;
import com.khetigaadi.user.myUsedTractor.model.MyUsedTractorResp;
import com.khetigaadi.user.myfavorites.model.MyFavoritesResp;
import com.khetigaadi.weather.model.WeatherResp;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface KhetiAPIs {
    @GET("/mobile/api/v1/banner")
    Single<AllBannerResp> getBanner();
    @POST("mobile/api/v2/tractor/old/insert")
    Call<UsedTractorResp> insertTractor(@Body UsedTractor usedTractor);
    @Multipart
    @POST("mobile/api/v1/tractor/old/upload/images")
    Single<ResponseBody> uploadUsedTraImages(@Part("used_tractor_id") RequestBody id,@Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @Part MultipartBody.Part file3, @Part MultipartBody.Part file4, @Part MultipartBody.Part file5, @Part MultipartBody.Part file6);
    @GET("mobile/api/v2/tractor/old/{id}")
    Call<UsedTractorResp> getUsedTractor(@Path("id") String tractorId);
    @POST("mobile/api/v2/tractor/compare")
    Call<CompTractResp> compareTractor(@Body CompareTractorParam compareTractorParam);
    @POST("mobile/api/v1/news/comments/like")
    Call<MyResponse> commentLike(@Body HashMap<String,Object> params);
    @GET("/mobile/api/v1/wp/news/{id}")
    Single<NewsDetailResp> getNewsDetails(@Path("id") String tractorId);
    @POST("mobile/api/v1/news/like")
    Call<MyResponse> newsLike(@Body HashMap<String,String> params);
    @POST("mobile/api/v1/news/comments")
    Call<CommentListResp> getNewsComments(@Body HashMap<String,String> params);
    @POST("mobile/api/v1/news/comments/create")
    Call<CommentResp> addNewsComment(@Body HashMap<String,String> params);
    @POST("mobile/api/v1/news/share")
    Call<MyResponse> shareNews(@Body HashMap<String,String> params);
    @POST("mobile/api/v1/tractor/new/filter")
    Single<NewTractorResp> getNewTractors(@Body TractorFilter tractorFilter,@Query("page") int page);
    @POST("mobile/api/v1/tractor/new/similar")
    Single<SimilarNewTractorResp> getSimilarNewTractors(@Body SimilarParams tractor);
    @POST("mobile/api/v1/tractor/rent/filter")
    Single<RentTractorResp> getRentTractors(@Body TractorFilter tractorFilter,@Query("page") int page);
    @GET("mobile/api/v1/tractor/rent")
    Single<MyRentTractorResp> getMyRentTractors();
    @POST("mobile/api/v1/tractor/rent/delete-request")
    Single<MyResponse> rentTrDeleteReq(@Body Map<String,Object> params);
    @POST("mobile/api/v2/tractor/rent")
    Single<RentTractorDetailsResp> getRentTractorDetails(@Body HashMap<String,String> params);
    @POST("mobile/api/v1/tractor/rent/similar")
    Single<SimilarRentTractorsResp> getSimilarRentTractors(@Body SimilarParams rentTractor);
    @POST("mobile/api/v1/tractor/old/search")
    Single<UsedTractorListResp> getUsedTractors(@Body TractorFilter fromJson, @Query("page") int page);
    @POST("mobile/api/v1/tractor/old/similar")
    Single<SimilarUsedTractorsResp> getSimilarUsedTractors(@Body UsedTractor fromJson);
    @POST("mobile/api/v1/implement/new/filter")
    Single<ImplementListResp> getImplements(@Body TractorFilter tractorFilter, @Query("page") int page);
    @POST("mobile/api/v1/implement/new/brand/type/model")
    Single<ImplementDetailsResp> getNewImplementDetails(@Body NewImpParams params);
    @POST("mobile/api/v1/implement/new/similar")
    Single<SimilarImplementListResp> getSimilarNewImplements(@Body NewImp params);
    @POST("mobile/api/v1/tractor/new/bookmark/status")
    Single<MyResponse> newTractorFavoriteStateChange(@Body NewTractor tractor);
    @POST("mobile/api/v1/tractor/old/bookmark/status")
    Single<MyResponse> usedTractorFavoriteStateChange(@Body UsedTractor params);
    @POST("mobile/api/v1/tractor/rent/bookmark/status")
    Single<MyResponse> rentTractorFavoriteStateChange(@Body RentTractor params);
    @POST("mobile/api/v1/tractor/my-favourite")
    Single<MyFavoritesResp> getMyFavorites();
    @POST("mobile/api/v1/tractor/compare/popular")
    Single<CompareTractorsListResp> getCompareTractors(@Query("page") int page);
    @POST("mobile/api/v1/implement/compare/popular")
    Single<CompareImplementListResp> getCompareImplements(@Query("page") int page);
    @POST("mobile/api/v1/implement/new/brand/type/model")
    Single<ImplementDetailsResp> getImpByBrandTypeModel(@Body GetImpByModelReq impByModelReq);
    @GET("mobile/api/v1/tractor/new/{id}")
    Single<NewTractorOverviewResp> getNewTractorOverView(@Path("id") String tractorId);
    @POST("mobile/api/v1/tractor/new/specification")
    Single<NewTrSpecResp> getNewTractorSpecifications(@Body HashMap<String, String> params);
    @GET("mobile/api/v1/tractor/new/picture/{id}")
    Single<NewTractorPictureResp> getNewTractorPicture(@Path("id") String tractorId);
    @POST("mobile/api/v1/dealer")
    Single<DealerListResp> getDealers(@Body TractorFilter usedTraFilter,@Query("page") int page);
    @GET("mobile/api/v1/brands")
    Single<BrandResp> getBrands();
    @GET("mobile/api/v1/categories")
    LiveData<ApiResponse<BrandResp>> getCategories();
    @GET("mobile/api/v1/hpRange")
    Single<HpResponse> getHPs();
    @GET("mobile/api/v1/implement/new/type")
    Single<TypeResp> getTypes();
    @GET("mobile/api/v1/safety-tips")
    Single<SafetyTipsResp> getSafetyTips();
    @POST("mobile/api/v1/harvester/new/filter")
    Single<HarvesterListResp> getHarvesters(@Body TractorFilter tractorFilter,@Query("page") int page);
    @POST("/mobile/api/v1/harvester/new/{id}")
    Single<HarvesterResp> getHarvester(@Path("id") String harvesterId);
    @POST("mobile/api/v1/harvester/new/similar/model")
    Single<SimilarHarvesterListResp> getSimilarHarvesters(@Body Harvester params);
    @GET("mobile/api/v1/new-tractor/brand/model/{id}")
    Single<ModelResp> getNewTractorModels(@Path("id") int brandId);
    @GET("mobile/api/v1/tractor/old/brand/model/{id}")
    Single<ModelResp> getOldTractorModels(@Path("id") int brandId);
    @GET("https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd0000015374953136dd496a602572da505855bc&format=json&limit=10")
    Single<MondiPriceResp> getMandiPrice(@Query("offset") int page,@Query("filters[state]") String state,@Query("filters[commodity]") String commodity);
    @GET("https://api.weatherapi.com/v1/forecast.json?key=bf93b38658ba4cc788b55817212706&days=7&aqi=no&alerts=no")
    Single<WeatherResp> getWeather(@Query("q") String city);
    @POST("/mobile/api/v1/wp/news")
    Single<NewsListResp> getNews(@Body Map<String,String> params,@Query("page") int page);
    @POST("mobile/api/v1/common/tractor/price")
    Single<MyResponse> getNewTrPriceByBrandModel(@Body Map<String,String> params);
    @POST("/mobile/api/v1/new/tractor/price")
    Single<MyResponse> getNewTractorPrice(@Body Map<String,String> params);
    @POST("/mobile/api/v1/new/harvester/price")
    Single<MyResponse> getHarvesterPrice(@Body Map<String,String> params);
    @POST("/mobile/api/v1/new/implement/price")
    Single<MyResponse> getImplementPrice(@Body Map<String,String> params);
    @POST("mobile/api/v1/new/tractor/demo")
    Single<MyResponse> getTractorGetDemo(@Body Map<String,String> params);
    @POST("mobile/api/v1/new/implement/demo")
    Single<MyResponse> getImplementGetDemo(@Body Map<String,String> params);
    @POST("mobile/api/v1/new/harvester/demo")
    Single<MyResponse> getHarvesterGetDemo(@Body Map<String,String> params);
    @POST("mobile/api/v1/seller/interested")
    Single<InterestedBuyersResp> getBuyEnq();
    @POST("mobile/api/v1/dealership")
    Single<MyResponse> dealershipRequest(@Body Map<String,Object> params);
    @POST("mobile/api/v1/user/feedback")
    Single<MyResponse> feedback(@Body Map<String,Object> params);
    @GET("mobile/api/v1/user/dashboard")
    Single<DashboardResp> getDashBoard();
    @POST("mobile/api/v1/seller/contact")
    Single<MyResponse> getSellerContact(@Body Map<String,Object> params);
    @Headers("isAuthorizable: false")
    @GET("https://www.googleapis.com/youtube/v3/search?order=date&part=snippet&maxResults=10")
    Single<VideoListResp> getYouTubeVideoList(@Query("channelId") String channelId, @Query("key") String key, @Query("pageToken") String pageToken);
    @Headers("isAuthorizable: false")
    @GET("https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics,status")
    Single<VideoResp> getYouTubeVideo(@Query("id") String id,@Query("key") String key);
    @POST("mobile/api/v5/khetigaadi/search")
    Single<SearchResp> search(@Body Map<String,String> params);
    @POST("mobile/api/v1/loan/enquiry")
    Single<GetLoanResp> getLoanEnq(@Body GetLoan params);
    @Multipart
    @POST("mobile/api/v1/loan/upload/enquiry")
    Single<ResponseBody> uploadGetLoanImages(@Part("finance_enquiry_id") RequestBody id,@Part MultipartBody.Part file1,@Part MultipartBody.Part file2);
    @POST("mobile/api/v1/implement/new/search/name")
    Single<ImplementList> newImpSearch(@Body Map<String,String> params);
    @POST("mobile/api/v3/user/login")
    Single<UserResponse> loginWithSocialMedia(@Body Map<String,String> params);
    @POST("mobile/api/v1/implement/new/brand/type/model/list")
    Single<com.khetigaadi.implement.model.ModelResp> getImpModels(@Body Map<String,Object> params);
    @POST("mobile/api/v1/tractor/owner/insert")
    Single<OwnerTractorResp> insertOwnerTractor(@Body Map<String,Object> params);
    @Multipart
    @POST("mobile/api/v1/tractor/owner/upload/images")
    Single<MyResponse> insertOwnerTractorImage(@Part("tractor_owner_id") RequestBody type,@Part MultipartBody.Part frontSideImage,@Part MultipartBody.Part rightSideImage);
    @Multipart
    @POST("mobile/api/v2/implement/type/rent/upload/images")
    Single<RentImpImageResp> insertRentTractorImage(@Part("rent_tractor_id") RequestBody rentTractorId,
                                                    @Part("implement_type_id1") RequestBody id1,
                                                    @Part("rate1") RequestBody rate1,
                                                    @Part("unit_id1") RequestBody ratePer1,
                                                    @Part MultipartBody.Part image1,
                                                    @Part("implement_type_id1") RequestBody id2,
                                                    @Part("rate1") RequestBody rate2,
                                                    @Part("unit_id1") RequestBody ratePer2,
                                                    @Part MultipartBody.Part image2,
                                                    @Part("implement_type_id1") RequestBody id3,
                                                    @Part("rate1") RequestBody rate3,
                                                    @Part("unit_id1") RequestBody ratePer3,
                                                    @Part MultipartBody.Part image3,
                                                    @Part("implement_type_id1") RequestBody id4,
                                                    @Part("rate1") RequestBody rate4,
                                                    @Part("unit_id1") RequestBody ratePer4,
                                                    @Part MultipartBody.Part image4,
                                                    @Part("implement_type_id1") RequestBody id5,
                                                    @Part("rate1") RequestBody rate5,
                                                    @Part("unit_id1") RequestBody ratePer5,
                                                    @Part MultipartBody.Part image5);
    @GET("mobile/api/v2/tractor")
    Single<HomeListResp> getHomeList();
    @POST("mobile/api/v1/dealer/contact")
    Single<MyResponse> getDealerDetails(@Body Map<String,Object> params);
    @POST("mobile/api/v1/tractor/old/user")
    Single<MyUsedTractorResp> getMyUploadedTractor(@Body MyUploadedTrReq params);
    @GET("mobile/api/v1/interested/buyer/old/{id}")
    Single<com.khetigaadi.user.myUsedTractor.model.InterestedBuyersResp> getInterestedBuyers(@Path("id") String id);
    @POST("mobile/api/v1/tractor/old/delete-request")
    Single<MyResponse> deleteMyUploadedTractor(@Body Map<String,Object> params);
    @POST("mobile/api/v1/tractor/old/sold/status")
    Single<MyResponse> changeSoldStatusOfTr(@Body Map<String,Object> params);
    @POST("mobile/api/v1/tractor/rent/enquiry")
    Single<MyResponse> rentTrEnquiry(@Body Map<String,Object> params);
    @POST("mobile/api/v1/tractor/new/search")
    Single<NewTrSearchByNameResp> searchNewTrByName(@Body Map<String,String> params);
    @GET("mobile/api/v1/tractor/old/search/{text}")
    Single<UsedTrByName> searchOldTrByName(@Path("text") String searchText);
    @POST("mobile/api/v1/implement/compare")
    Single<CompImpResp> getCompareImp(@Body CompImpReq compImpReq);
    @POST("mobile/api/v2/tractor/rent/insert")
    Single<RentTractorUploadResp> uploadRentTractor(@Body UploadRentTrReq compImpReq);
}

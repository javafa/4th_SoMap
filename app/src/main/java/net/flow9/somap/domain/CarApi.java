package net.flow9.somap.domain;

import net.flow9.somap.BuildConfig;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by pc on 21/11/2017.
 */

public class CarApi {

    public static List<CarData> data;

    public static void getCars(Callback callback) throws Exception{
        getCars("",callback);
    }

    // Zone 데이터를 가져오는 함수
    public static void getCars(String area, Callback callback) throws Exception{
        // 레트로핏 정의
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BuildConfig.SERVER_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = builder.build();
        // 2. 서비스 만들기 < 인터페이스로부터
        ICar service = retrofit.create(ICar.class);
        // 3. 옵저버블(Emitter) 생성
        Observable<Car> observable = service.getData(area);

        // 4. 데이터 가져오기
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                car -> {
                    if(car.isSuccess()){
                        data = car.getCarData();
                        callback.init();
                    }
                }
            );
    }

    // 인터페이스 생성
    public interface ICar {
        @GET("car/{param1}")
        Observable<Car> getData(@Path("param1") String area);
    }
    public interface Callback {
        void init();
    }
}

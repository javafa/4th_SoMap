package net.flow9.somap.domain;

import net.flow9.somap.BuildConfig;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by pc on 21/11/2017.
 */

public class ZoneApi {

    public static List<Data> data;

    // Zone 데이터를 가져오는 함수
    public static void getZones(){
        // 레트로핏 정의
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BuildConfig.SERVER_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = builder.build();
        // 2. 서비스 만들기 < 인터페이스로부터
        IZone service = retrofit.create(IZone.class);
        // 3. 옵저버블(Emitter) 생성
        Observable<Zone> observable = service.getData();

        // 4. 데이터 가져오기
        observable.subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                zone -> {
                    if(zone.isSuccess()){
                        data = zone.getData();
                    }
                }
            );
    }

    // 인터페이스 생성
    interface IZone {
        @GET("zone")
        Observable<Zone> getData();
    }
}

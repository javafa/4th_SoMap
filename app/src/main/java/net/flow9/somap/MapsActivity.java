package net.flow9.somap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.flow9.somap.domain.Data;
import net.flow9.somap.domain.ZoneApi;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ZoneApi.Callback{

    private GoogleMap mMap;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        ZoneApi.getZones(this);
    }

    public void init(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sinsa = new LatLng(37.516038, 127.019783);
        mMap.addMarker(new MarkerOptions().position(sinsa).title("Marker in Seoul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sinsa, 14));

        Observable<Data> observable = Observable.create(emitter -> {
           for(Data data :ZoneApi.data){
               Thread.sleep(1000);
               emitter.onNext(data);
           }
        });

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->{
                    LatLng marker = new LatLng(data.getLat(), data.getLng());
                    mMap.addMarker(new MarkerOptions().position(marker).title(data.getZone_name()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 14));
                });
    }
}

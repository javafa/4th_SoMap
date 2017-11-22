package net.flow9.somap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import net.flow9.somap.domain.Data;
import net.flow9.somap.domain.ZoneApi;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ZoneApi.Callback{

    private GoogleMap mMap;
    private ProgressBar progressBar;
    private ClusterManager<MarkerItem> clusterManager;

    private Map<String, LatLng> citys = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        citys.put("서울",new LatLng(37.516038, 127.019783));
        citys.put("부산",new LatLng(35.168359, 129.176855));
        citys.put("광주",new LatLng(35.154507, 126.845171));

        try{
            ZoneApi.getZones(this);
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
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

        // 1. 클러스터 매니저 초기화
        clusterManager = new ClusterManager<MarkerItem>(this, mMap);

        // * 클러스터 그룹 클릭시 처리 : 그룹 내에 있는 목록을 갱신처리...
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MarkerItem>() {
            @Override
            public boolean onClusterClick(Cluster<MarkerItem> cluster) {
                Toast.makeText(MapsActivity.this
                        , "마커좌표="+cluster.getPosition()
                        ,Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // * 단일 마커 클릭 시 처리 : 상세보기로 이동 등의 처리
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerItem>() {
            @Override
            public boolean onClusterItemClick(MarkerItem markerItem) {

                return false;
            }
        });

        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);

        setClusterOnMap();
    }

    private void setClusterOnMap(){
        clusterManager.clearItems();
        Observable<Data> observable = Observable.create(emitter -> {
            for(Data data :ZoneApi.data){
                emitter.onNext(data);
            }
            emitter.onComplete();
        });
        observable.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(data ->{
                // 2. 클러스터 매니저에 마커를 등록
                MarkerItem item = new MarkerItem(data.getLat(), data.getLng());
                item.title = data.getZone_name();
                clusterManager.addItem(item);
                // LatLng marker = new LatLng(data.getLat(), data.getLng());
                // mMap.addMarker(new MarkerOptions().position(marker).title(data.getZone_name()));
            }, e->{}, ()->{
                LatLng coord;
                if(currentArea != null && !"".equals(currentArea)) {
                    coord = citys.get(currentArea);
                }else{
                    coord = citys.get("서울");
                }
                mMap.addMarker(new MarkerOptions().position(coord).title("City : " + currentArea));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 11));
            });
    }

    public void setAll(View view){

    }
    private String currentArea = "";
    public void setSeoul(View view){
        currentArea = "서울";
        try {
            ZoneApi.getZones(currentArea, () -> {
                setClusterOnMap();
            });
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void setBusan(View view){
        currentArea = "부산";
        try {
            ZoneApi.getZones(currentArea, () -> {
                setClusterOnMap();
            });
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void setGwangju(View view){
        currentArea = "광주";
        try {
            ZoneApi.getZones(currentArea, () -> {
                setClusterOnMap();
            });
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    class MarkerItem implements ClusterItem {
        private LatLng position;
        public String title;

        public MarkerItem(double lat, double lng){
            position = new LatLng(lat, lng);
        }
        @Override
        public LatLng getPosition() {
            return position;
        }
    }
}

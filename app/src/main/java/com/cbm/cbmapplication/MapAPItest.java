package com.cbm.cbmapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cbm.cbmapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MapAPItest extends FragmentActivity implements OnMapReadyCallback {
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    private RadioGroup radioGroup;

    GoogleMap mMap;

    ArrayList<Double> lat_list;
    ArrayList<Double> lng_list;
    ArrayList<String> name_list;
    ArrayList<String> vicinity_list;
    // 지도의 표시한 마커(주변장소표시)를 관리하는 객체를 담을 리스트
    ArrayList<Marker> markers_list;

    ImageButton btn_map_goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapapitest);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        radioGroup = (RadioGroup) findViewById(R.id.grp) ;
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        btn_map_goback = (ImageButton) findViewById(R.id.btn_map_goback);

        lat_list=new ArrayList<>();
        lng_list=new ArrayList<>();
        name_list=new ArrayList<>();
        vicinity_list=new ArrayList<>();
        markers_list=new ArrayList<>();

        btn_map_goback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuChoice.class);
                startActivity(intent);
            }
        });
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            Log.i("라디로","먼가가 눌렷습니다");
            if (i == R.id.convenience) {
                System.out.println("편의점이 눌렸습니다.");
                getNearbyPlace("convenience_store");
            }
            else if (i == R.id.pharmacy) {
                System.out.println("약국 눌렸습니다.");
                getNearbyPlace("pharmacy");

            }
            else if (i == R.id.hospital) {
                System.out.println("병원 눌렸습니다.");
                getNearbyPlace("hospital");

            }
            else if (i == R.id.supermarket) {
                System.out.println("마트 눌렸습니다.");
                getNearbyPlace("supermarket");

            }
        }
    };



    //타입에 맞는 근처 장소 가져옴
    public void getNearbyPlace(String type_keyword){
        NetworkThread thread=new NetworkThread(type_keyword);
        thread.start();
    }

    //네트워크 스레드로 장소 가져옴
    class NetworkThread extends Thread{
        String type_keyword;
        public NetworkThread(String type_keyword){
            this.type_keyword=type_keyword;
        }
        @Override
        public void run() {
            try{
                //데이터를 담아놓을 리스트를 초기화한다.
                lat_list.clear();
                lng_list.clear();
                name_list.clear();
                vicinity_list.clear();

                // 접속할 페이지 주소
                String site="https://maps.googleapis.com/maps/api/place/nearbysearch/json";
                site+="?location="+currentLocation.getLatitude()+","
                        +currentLocation.getLongitude()
                        +"&radius=10000&sensor=false&language=ko"
                        +"&key=AIzaSyD8V6U_PIiws20wN9WRHMy7cXCOKcRoPfg";
                if(type_keyword!=null && type_keyword.equals("all")==false){
                    site+="&types="+type_keyword;
                }
                // 접속
                URL url=new URL(site);
                URLConnection conn=url.openConnection();
                // 스트림 추출
                InputStream is=conn.getInputStream();
                InputStreamReader isr =new InputStreamReader(is,"utf-8");
                BufferedReader br=new BufferedReader(isr);
                String str=null;
                StringBuffer buf=new StringBuffer();
                // 읽어온다
                do{
                    str=br.readLine();
                    if(str!=null){
                        buf.append(str);
                    }
                }while(str!=null);
                String rec_data=buf.toString();
                // JSON 데이터 분석
                JSONObject root=new JSONObject(rec_data);
                //status 값을 추출한다.
                String status=root.getString("status");
                // 가져온 값이 있을 경우에 지도에 표시한다.
                if(status.equals("OK")){
                    //results 배열을 가져온다
                    JSONArray results=root.getJSONArray("results");
                    // 개수만큼 반복한다.
                    for(int i=0; i<results.length() ; i++){
                        // 객체를 추출한다.(장소하나의 정보)
                        JSONObject obj1=results.getJSONObject(i);
                        // 위도 경도 추출
                        JSONObject geometry=obj1.getJSONObject("geometry");
                        JSONObject location=geometry.getJSONObject("location");
                        double lat=location.getDouble("lat");
                        double lng=location.getDouble("lng");
                        // 장소 이름 추출
                        String name=obj1.getString("name");
                        // 대략적인 주소 추출
                        String vicinity=obj1.getString("vicinity");
                        // 데이터를 담는다.
                        lat_list.add(lat);
                        lng_list.add(lng);
                        name_list.add(name);
                        vicinity_list.add(vicinity);
                    }
                    showMarker();
                }
                else{
                    Toast.makeText(getApplicationContext(),"가져온 데이터가 없습니다.",Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){e.printStackTrace();}
        }
    }

    public void showMarker(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 지도에 마커를 표시한다.
                // 지도에 표시되어있는 마커를 모두 제거한다.
                for(Marker marker : markers_list){
                    marker.remove();
                }
                markers_list.clear();
                // 가져온 데이터의 수 만큼 마커 객체를 만들어 표시한다.
                for(int i= 0 ; i< lat_list.size() ; i++){
                    // 값 추출
                    double lat= lat_list.get(i);
                    double lng=lng_list.get(i);
                    String name=name_list.get(i);
                    String vicinity=vicinity_list.get(i);
                    // 생성할 마커의 정보를 가지고 있는 객체를 생성
                    MarkerOptions options=new MarkerOptions();
                    // 위치설정
                    LatLng pos=new LatLng(lat,lng);
                    options.position(pos);
                    // 말풍선이 표시될 값 설정
                    options.title(name);
                    options.snippet(vicinity);
                    // 아이콘 설정
                    //BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
                    //options.icon(icon);
                    // 마커를 지도에 표시한다.
                    Marker marker=mMap.addMarker(options);
                    markers_list.add(marker);
                }
            }
        });
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    System.out.println("현재 위치 값 "+currentLocation.getLatitude()+", "+currentLocation.getLongitude());
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapAPItest.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.addMarker(markerOptions);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }
}
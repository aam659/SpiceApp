package com.example.spiceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.search.PlaceLink;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the MapPage class, which
 * is used to generate a map and route to a given
 * dining location.
 *
 * @author Logan Dawkins
 */

public class MapPage extends AppCompatActivity {
    // map embedded in the map fragment
    private Map map = null;
    //map fragment embedded in this activity
    private SupportMapFragment mapFragment= null;
    private List<MapObject> m_mapObjectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_page);

//        initializeMap();
//        addMarkerAtPlace();
    }

//    private void initializeMap() {
//        // Search for the map fragment to finish setup by calling init().
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
//        mapFragment.init(new OnEngineInitListener() {
//            @Override
//            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
//                if (error == OnEngineInitListener.Error.NONE) {
//                    // retrieve a reference of the map from the map fragment
//                    map = mapFragment.getMap();
//                    // Set the map center to the Vancouver region (no animation)
//                    map.setCenter(new GeoCoordinate(33.2098, -87.56592, 0.0),
//                            Map.Animation.LINEAR);
//                    // Set the zoom level to the average between min and max
//                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
//                } else {
//                    System.out.println("ERROR: Cannot initialize MapPage Fragment");
//                }
//            }
//        });
//    }
//
//    private void addMarkerAtPlace(/*PlaceLink placeLink*/) {
//            Image img = new Image();
//            try {
//                img.setImageResource(R.drawable.marker);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //cleanMap();
//            MapMarker mapMarker = new MapMarker();
//            mapMarker.setIcon(img);
//            //mapMarker.setCoordinate(new GeoCoordinate(placeLink.getPosition()));
//            //mapMarker.setCoordinate(new GeoCoordinate(33.2098, -87.56592, 0.0));
//            //map.addMapObject(mapMarker);
//            //m_mapObjectList.add(mapMarker);
//    }
//
//    private void cleanMap() {
//        if (!m_mapObjectList.isEmpty()) {
//            map.removeMapObjects(m_mapObjectList);
//            m_mapObjectList.clear();
//        }
//    }
}

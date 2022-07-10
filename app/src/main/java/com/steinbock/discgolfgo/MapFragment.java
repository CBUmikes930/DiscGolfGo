package com.steinbock.discgolfgo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment
        implements OnMapReadyCallback {

    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap googleMap;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Location currentLocation;
    Marker curLocMarker;
    String curMarkerID;
    boolean updateLocation;
    private static final int REQUEST_CODE = 101;

    float defaultZoom;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        defaultZoom = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("zoom_level", 90) / 5f;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        updateLocation = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("Stopped");
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        updateLocation = false;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        googleMap.setMapType(getMapType());
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Check for location permission and prompt if it isn't given
        if (ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        startLocationUpdates();

        // When the map is clicked, add a new hole marker and move camera to the marker
        googleMap.setOnMapClickListener(latLng -> {
            addHoleMarker(googleMap, latLng, latLng.latitude + " : " + latLng.longitude);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));
        });

        // When the button is clicked, animate camera to the current position of the user
        this.getView().findViewById(R.id.currentLocation).setOnClickListener(view -> {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currentLocation.getLatitude(),
                            currentLocation.getLongitude()), defaultZoom));
        });

        this.getView().findViewById(R.id.placeNewMarker).setOnClickListener(view -> {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            addHoleMarker(googleMap, latLng, latLng.latitude + " : " + latLng.longitude);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));
            // ((MainActivity) getContext()).dbHandler.addNewHole(new HoleInfo(latLng, latLng, 5, 4));
        });

        // When a marker is clicked, toggle the info window and zoom in on marker when toggled on
        googleMap.setOnMarkerClickListener(marker -> {
            // If the marker is currently selected
            if (curMarkerID != null && curMarkerID.equals(marker.getId())) {
                // Hide the info window and unselect the current marker
                marker.hideInfoWindow();
                curMarkerID = null;
            } else {
                // Otherwise, mark the marker as the current marker
                curMarkerID = marker.getId();
                // Zoom into the selected marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), defaultZoom));
                // Show the info window
                marker.showInfoWindow();
            }
            return true;
        });

        // When the info window is held down, prompt to delete or not
        googleMap.setOnInfoWindowLongClickListener(marker -> {
            new AlertDialog.Builder(getContext())
                    .setTitle(getContext().getString(R.string.delete_marker))
                    .setMessage(getContext().getString(R.string.delete_marker_ext))
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            marker.remove();
                        }
                    }).setNegativeButton("Cancel", null)
                    .show();
        });


        // Create a bunch of holes on the map
        List<HoleInfo> holes = new ArrayList<>();
        holes.add(new HoleInfo(34.435627632939195, -118.51841803640129, 34.434855853628136, -118.517805548691751, 1, 3));
        holes.add(new HoleInfo(34.435124912577756, -118.51743333041670, 34.435408073373340, -118.517998270690460, 2, 3));
        holes.add(new HoleInfo(34.434479778002350, -118.51773642003536, 34.435064630175150, -118.517317324876770, 3, 3));
        holes.add(new HoleInfo(34.430192396252630, -118.52607171982527, 34.429692685165726, -118.524925746023640, 10, 3));
        holes.add(new HoleInfo(34.431019526301200, -118.52568849921227, 34.430320158098570, -118.526093177497400, 11, 3));
        for (HoleInfo hole : holes) {
            addHoleMarker(googleMap, hole.getHoleLocation(),
                    "Hole " + hole.getNumber() + " | Par " + hole.getPar());
            addTeeMarker(googleMap, hole.getTeeLocation(), hole.getNumber(),
                    "Tee " + hole.getNumber() + " | Par " + hole.getPar());
            addLineForHole(googleMap, hole);
            addMapLabel(googleMap, hole);
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        updateLocation = true;

        locationRequest = LocationRequest.create()
                .setInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Move to the current location
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(loc -> {
            //curLocMarker = addMarkerAtLocation(loc, googleMap);
            googleMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), defaultZoom));
            currentLocation = loc;
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (updateLocation) {
                    for (Location loc : locationResult.getLocations()) {
                        if (curLocMarker != null)
                            curLocMarker.remove();
                        curLocMarker = addMarkerAtLocation(loc, googleMap);
                        currentLocation = loc;
                    }
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper());
    }

    private Marker addMarkerAtLocation(Location location, GoogleMap googleMap) {
        if (location != null) {
            LatLng cur = new LatLng(location.getLatitude(), location.getLongitude());
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cur, 20));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(cur);
            markerOptions.icon(colorImageAsBitmap(R.drawable.basket, R.color.dark_blue,
                    R.color.dark_blue, 10, 0.5f));
            markerOptions.anchor(0.5f, 0.5f);
            return googleMap.addMarker(markerOptions);
        }
        return null;
    }

    private Marker addHoleMarker(GoogleMap googleMap, LatLng location, String label) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);

        // Get the correctly colored icon
        markerOptions.icon(colorImageAsBitmap(R.drawable.basket, R.color.orange,
                R.color.dark_blue, 10, 1.25f));
        markerOptions.title(label);
        // Set the marker anchor point to be the center of the marker
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.zIndex(4f);

        return googleMap.addMarker(markerOptions);
    }

    private Marker addTeeMarker(GoogleMap googleMap, LatLng location, int number, String label) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);

        // Get the correctly colored icon
        markerOptions.icon(getTeeImage(number, R.color.orange, R.color.dark_blue, 1f, 48));
        markerOptions.title(label);
        // Set the marker anchor point to be the center of the marker
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.zIndex(3f);

        return googleMap.addMarker(markerOptions);
    }

    private Polyline addLineForHole(GoogleMap googleMap, HoleInfo hole) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.add(hole.teeLocation, hole.holeLocation);
        polylineOptions.color(getResources().getColor(R.color.dark_blue));
        polylineOptions.zIndex(1f);
        return googleMap.addPolyline(polylineOptions);
    }

    private GroundOverlay addMapLabel(GoogleMap googleMap, HoleInfo hole) {
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();
        groundOverlayOptions.position(hole.getMidPt(), 15f);

        // Get the angle of the text based off the two points
        float angle = hole.getBearing() - 90;
        // Adjust the angle so it isn't upside down
        if (angle < -90 || angle > 90)
            angle += 180;
        groundOverlayOptions.bearing(angle);

        // Get the label

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        String label = SP.getBoolean("is_metric", false) ? hole.getMetricDistance() + "m" : hole.getDistance() + "ft";

        groundOverlayOptions.image(getTextLabel(label, R.color.orange, R.color.dark_blue, 36, 5, 10));
        // Set the anchor to be in the middle of the label
        groundOverlayOptions.anchor(0.5f, 0.5f);
        groundOverlayOptions.zIndex(2f);

        return googleMap.addGroundOverlay(groundOverlayOptions);
    }

    private BitmapDescriptor colorImageAsBitmap(int drawable_id, int color_id, int bg_color_id, int padding, float scale) {
        // Convert drawable to a bitmap
        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawable_id).copy(Bitmap.Config.ARGB_8888, true);

        // Create a new bitmap of the size of the drawable plus the padding on all sides
        Bitmap newBitmap = Bitmap.createBitmap(bm.getWidth() + padding * 2, bm.getHeight() + padding * 2, bm.getConfig());

        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Draw a circle with background color
        paint.setColor(getResources().getColor(bg_color_id));
        canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, Math.min(canvas.getWidth() / 2f, canvas.getHeight() / 2f), paint);

        // Draw the drawable onto the new bitmap with the given color
        Paint paint2 = new Paint();
        paint2.setColorFilter(new PorterDuffColorFilter(getResources().getColor(color_id), PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bm, padding, padding, paint2);

        return BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(newBitmap, (int) (newBitmap.getWidth() * scale), (int) (newBitmap.getHeight() * scale), false));
    }

    private BitmapDescriptor getTeeImage(int number, int color_id, int bg_color_id, float scale, int textSize) {
        Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        // Create a canvas
        Canvas canvas = new Canvas(bm);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);

        // Draw circular background with background color specified
        paint.setColor(getResources().getColor(bg_color_id));
        //canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, Math.min(canvas.getWidth() / 2f, canvas.getHeight() / 2f), paint);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        // Draw the given number as a text label in the center of the circle
        paint.setColor(getResources().getColor(color_id));
        Rect bounds = new Rect();
        paint.getTextBounds(String.valueOf(number),0, String.valueOf(number).length(), bounds);
        canvas.drawText(String.valueOf(number), canvas.getWidth() / 2f, (canvas.getHeight() + bounds.height()) / 2f, paint);

        // Convert canvas to bitmap
        return BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bm, (int) (bm.getWidth() * scale), (int) (bm.getHeight() * scale), false));
    }

    private BitmapDescriptor getTextLabel(String text, int color_id, int bg_color_id, int textSize, int vPadding, int hPadding) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        Bitmap bm = Bitmap.createBitmap(bounds.width() + (hPadding * 2), bounds.height() + (vPadding * 2), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        paint.setColor(getResources().getColor(bg_color_id));
        canvas.drawRect(0, 0, bm.getWidth(), bm.getHeight(), paint);

        paint.setColor(getResources().getColor(color_id));
        canvas.drawText(text, bm.getWidth() / 2f, bm.getHeight() - vPadding, paint);

        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    private int getMapType() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        switch (SP.getString("map_style", "MAP_TYPE_HYBRID")) {
            case "MAP_TYPE_NORMAL":
                return GoogleMap.MAP_TYPE_NORMAL;
            case "MAP_TYPE_SATELLITE":
                return GoogleMap.MAP_TYPE_SATELLITE;
            case "MAP_TYPE_TERRAIN":
                return GoogleMap.MAP_TYPE_TERRAIN;
            case "MAP_TYPE_HYBRID":
                return GoogleMap.MAP_TYPE_HYBRID;
            default:
                return GoogleMap.MAP_TYPE_NONE;
        }
    }
}
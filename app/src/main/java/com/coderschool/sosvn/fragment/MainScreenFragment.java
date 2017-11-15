package com.coderschool.sosvn.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.adapter.HospitalAdapter;
import com.coderschool.sosvn.object.Hospital;
import com.coderschool.sosvn.util.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainScreenFragment extends Fragment {

    private static String[] permissionList = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;

    // Recycler view
    @BindView(R.id.rv_hospitals)
    RecyclerView rvHospitals;

    HospitalAdapter mHospitalAdapter;
    private RecyclerView.LayoutManager layoutManger;
    List<Hospital> mHospitals;

    // Firebase
    FirebaseDatabase database;
    DatabaseReference myRef;

    //   Google map fragment
    private GoogleApiClient mGoogleApiClient;
    private LatLng mCurrentLatLng;
    private long UPDATE_INTERVAL = 45 * 1000;
    private long FASTEST_INTERVAL = 15 * 1000;
    private LocationRequest mLocationRequest;
    private LocationListener mLocationListener;

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load data from the firebase
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissionList, REQUEST_ACCESS_FINE_LOCATION);
        } else {
            setHospitalData();
            setupGoogleLocationServices();
        }
    }


    private void setHospitalData() {
        mHospitals = new ArrayList<Hospital>();
        if (null != FirebaseAuth.getInstance().getCurrentUser()) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Hospital");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    mHospitals.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        Hospital firebaseObject = dataSnapshot1.getValue(Hospital.class);
                        Hospital hospital = new Hospital();
                        /// Update local object
                        hospital.setAddress(firebaseObject.getAddress());
                        hospital.setEmail(firebaseObject.getEmail());
                        hospital.setLatitude(firebaseObject.getLatitude());
                        hospital.setLongitude(firebaseObject.getLongitude());
                        hospital.setName(firebaseObject.getName());
                        hospital.setPhone(firebaseObject.getPhone());
                        hospital.setRating(firebaseObject.getRating());
                        hospital.setUrl(firebaseObject.getUrl());
                        Log.d("HD", hospital.getName().toString());
                        mHospitals.add(hospital);
                    }
                    updateListHospital();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
//                    Toast.makeText(getContext(), "Failed to load from DB", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d("HDx", String.valueOf(mHospitals.size()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setHospitalData();
                    setupGoogleLocationServices();
                    mGoogleApiClient.connect();
                } else {
                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);
                }
            }
            return;
        }
    }

    private void setupGoogleLocationServices() {
        PermissionUtils.requestLocaiton(getActivity());

        setupListener();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @SuppressWarnings("MissingPermission")
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        if (PermissionUtils.checkLocation(getContext())) {
                            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if (location != null) {
                                Log.d("XX", "current location: " + location.toString());
                                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            }
                            Log.d("XX", "Location updates");
                            startLocationUpdates();
                            updateListHospital();
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        if (i == CAUSE_SERVICE_DISCONNECTED) {
                            Toast.makeText(getContext(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
                        } else if (i == CAUSE_NETWORK_LOST) {
                            Toast.makeText(getContext(), "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getContext(), connectionResult.getErrorMessage() + "Nie dziala", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }

    private void updateListHospital() {
        if (mHospitals != null && mCurrentLatLng != null && mHospitals.size() > 0)
            mHospitalAdapter.updateLocation(mHospitals, mCurrentLatLng);
    }

    @Subscribe
    public void setupListener() {
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("XX", mCurrentLatLng.toString());
                if (mHospitalAdapter != null && mCurrentLatLng != null) {
                    mHospitalAdapter.updateLocation(mHospitals, mCurrentLatLng);
                }
            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, parent, false);
        ButterKnife.bind(this, view);


        return view;
    }


    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mHospitalAdapter = new HospitalAdapter(new ArrayList<Hospital>(), getContext(), mCurrentLatLng);
        initRecyclerView();

    }


    private void initRecyclerView() {
        layoutManger = new LinearLayoutManager(getContext());
        rvHospitals.setLayoutManager(layoutManger);
        rvHospitals.setAdapter(mHospitalAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();

    }

    @SuppressWarnings("MissingPermission")
    protected void startLocationUpdates() {
        if (PermissionUtils.checkLocation(getContext())) {
            Log.d("XX", "Location request fired.");
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(UPDATE_INTERVAL)
                    .setFastestInterval(FASTEST_INTERVAL)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, mLocationListener);
        }
    }


}
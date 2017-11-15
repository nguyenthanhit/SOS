package com.coderschool.sosvn.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coderschool.sosvn.R;
import com.coderschool.sosvn.activity.MainActivity;
import com.coderschool.sosvn.activity.ReportActivity;
import com.coderschool.sosvn.fragment.dialog.HospitalDetailDialog;
import com.coderschool.sosvn.object.Hospital;
import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

    private List<Hospital> hospitals;
    Context context;
    private LatLng currentLocation;

    public HospitalAdapter(List<Hospital> hospitals, Context context, LatLng currentLocation) {
        this.hospitals = hospitals;
        this.context = context;
        this.currentLocation = currentLocation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_hospital, parent, false);
        return new ViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Hospital item = hospitals.get(position);

        Glide.with(context)
                .load(item.getUrl())
                .centerCrop()
                .placeholder(R.drawable.p4)
                .into(holder.hospitalImageView);

        holder.titleTextView.setText(item.getName());


        String distanceStr = "";

        if (currentLocation != null ) {
          float distance = getDistance(currentLocation.latitude, currentLocation.longitude, item.getLatitude(), item.getLongitude());
            distanceStr = formatNumber(distance);
        }

        holder.distanceTextView.setText(distanceStr);
        holder.hospitalRatingBar.setRating(item.getRating());





    }

    private float getDistance(double latitude, double longitude, float latitude1, float longitude1) {

        String distanceString = "0.0 km";

        Location locationA = new Location("point A");

        locationA.setLatitude(latitude);
        locationA.setLongitude(longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(latitude1);
        locationB.setLongitude(longitude1);

        return locationA.distanceTo(locationB);
       // distanceString = formatNumber(distance);
      //  return distanceString;
    }


    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    private String formatNumber(float distance) {
        String unit = "m";
        if (distance < 1) {
            distance *= 1000;
            unit = "mm";
        } else if (distance > 1000) {
            distance /= 1000;
            unit = "km";
        }

        return String.format("%4.3f%s", distance, unit);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_hospital)
        ImageView hospitalImageView;
        @BindView(R.id.hospital_title)
        TextView titleTextView;
        @BindView(R.id.hospital_distance)
        TextView distanceTextView;
        @BindView(R.id.hospital_rating)
        RatingBar hospitalRatingBar;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        @OnClick({R.id.btn_call})
        public void call() {
            context.startActivity(new Intent(context, ReportActivity.class));
        }

        @OnClick({R.id.btn_directions})
        public void getDirections() {

            Hospital hospital = hospitals.get(getAdapterPosition());
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + String.valueOf(hospital.getLatitude())
                    + "," + String.valueOf(hospital.getLongitude()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        }

        @OnClick({R.id.iv_hospital, R.id.hospital_title})
        public void openDetailedView() {

            Hospital hospital = hospitals.get(getAdapterPosition());

            String distanceStr = "";

            if (currentLocation != null ) {
                float distance = getDistance(currentLocation.latitude, currentLocation.longitude,
                        hospital.getLatitude(), hospital.getLongitude());
                distanceStr = formatNumber(distance);
            }

            HospitalDetailDialog hospitalDetail = HospitalDetailDialog.newInstance();
            Bundle bundle = new Bundle();
            bundle.putParcelable("hospital", hospital);
            bundle.putString("distance", distanceStr);
            hospitalDetail.setArguments(bundle);

            try{
                final MainActivity activity = (MainActivity) context;

                // Return the fragment manager

                hospitalDetail.show(activity.getFragmentManager(), "");

                // If using the Support lib.
                // return activity.getSupportFragmentManager();

            } catch (ClassCastException e) {
                Log.d("TAG", "Can't get the fragment manager with this");
            }


//            Intent i = new Intent(context, HospitalDetails.class);
//            i.putExtra("hospital", hospital);
//            i.putExtra("distance", distance);
//            context.startActivity(i);
        }


    }

    public void updateLocation(List<Hospital> newHospitalList, LatLng location) {
        if(location != null)
            currentLocation = location;
        try {
            FindNearestHospital findNearestHospital = new FindNearestHospital(newHospitalList, currentLocation);
            List<Hospital> nearestList = findNearestHospital.execute(newHospitalList).get();
            if(nearestList != null && nearestList.size() > 0) {
                hospitals.clear();
                hospitals.addAll(nearestList);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    public class FindNearestHospital extends AsyncTask<List<Hospital>, Void, List<Hospital>> {

        private List<Hospital> mHospitalList;
        private LatLng mCurrentLocation;

        public FindNearestHospital(List<Hospital> hospitalList, LatLng CurrentLocation) {
            super();
            mHospitalList = hospitalList;
            mCurrentLocation = CurrentLocation;
        }

        @Override
        protected List<Hospital> doInBackground(List<Hospital>... lists) {
            mHospitalList = lists[0];
            Collections.sort(mHospitalList, new Comparator<Hospital>() {
                @Override
                public int compare(Hospital hospital, Hospital t1) {
                    float distance = getDistance(mCurrentLocation.latitude, mCurrentLocation.longitude, hospital.getLatitude(), hospital.getLongitude());
                    float distance1 = getDistance(mCurrentLocation.latitude, mCurrentLocation.longitude, t1.getLatitude(), t1.getLongitude());
                    if (distance < distance1) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
            return mHospitalList;
        }

        @Override
        protected void onPostExecute(List<Hospital> hospitals) {
            super.onPostExecute(hospitals);
        }
    }

}

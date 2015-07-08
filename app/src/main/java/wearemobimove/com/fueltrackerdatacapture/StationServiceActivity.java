package wearemobimove.com.fueltrackerdatacapture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

import wearemobimove.com.fueltrackerdatacapture.file.StationsFile;

/**
 * Created by cupidon on 7/7/15.
 */
public class StationServiceActivity extends ActionBarActivity implements View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static boolean open247 = false;
    static boolean hasCarWash = false;
    static boolean hasGas = false;
    static boolean hasShop = false;
    static boolean gps_enabled = false;
    static boolean network_enabled = false;
    static ImageView open, carWash, gas, shop;
    static RelativeLayout saveButton;
    static LocationManager locationManager;
    static Location myLocation;
    private static GoogleApiClient mGoogleApiClient;
    static LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setStationServices();
        getLocation();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setStationServices() {
        setContentView(R.layout.station_services);
        ActionBar appBar = getSupportActionBar();
        appBar.setTitle("Services");

        MainActivity.gasStation.isOpened = false;
        MainActivity.gasStation.hasCarWash = false;
        MainActivity.gasStation.hasGas = false;
        MainActivity.gasStation.hasShop = false;

        open = (ImageView) findViewById(R.id.open_icon);
        carWash = (ImageView) findViewById(R.id.carWash_icon);
        gas = (ImageView) findViewById(R.id.gas_icon);
        shop = (ImageView) findViewById(R.id.shop_icon);
        saveButton = (RelativeLayout) findViewById(R.id.save_layout);

        open.setOnClickListener(this);
        carWash.setOnClickListener(this);
        gas.setOnClickListener(this);
        shop.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (view.getClass() == ImageView.class) {
            switch (viewId) {
                case R.id.open_icon:
                    open247 = !open247;
                    break;
                case R.id.carWash_icon:
                    hasCarWash = !hasCarWash;
                    break;
                case R.id.gas_icon:
                    hasGas = !hasGas;
                    break;
                case R.id.shop_icon:
                    hasShop = !hasShop;
                    break;
            }
            setImageIcon();
        } else {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (gps_enabled) {
                Toast.makeText(this, "lat:" + myLocation.getLatitude() + "\nlng: " + myLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                MainActivity.gasStation.isOpened = open247;
                MainActivity.gasStation.hasCarWash = hasCarWash;
                MainActivity.gasStation.hasGas = hasGas;
                MainActivity.gasStation.hasShop = hasShop;
                MainActivity.gasStation.lattitude = myLocation.getLatitude();
                MainActivity.gasStation.longitude = myLocation.getLongitude();
                MainActivity.gasStation.date = new Date();
                //saveButton.setBackgroundColor(Color.rgb(15,15,15));
                Log.i("gas_station", MainActivity.gasStation.toString());
                StationsFile.stationsToCSV(MainActivity.gasStation);
                AlertDialog alert = new AlertDialog.Builder(this).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }).setMessage("Station Save successfully").setTitle("Success").show();

            } else {
                GPSDialog();
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        myLocation = location;
        Log.i("client", "location updated");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("client", "Connected");
        myLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        //  Toast.makeText(this,"connected",Toast.LENGTH_SHORT).show();
        if (myLocation != null) {
            Toast.makeText(this, "lat:" + myLocation.getLatitude() + "\nlng: " + myLocation.getLongitude(), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "location is null ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_SHORT).show();
        Log.i("client", "suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
        Log.i("client", "failed");
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    protected void startLocationUpdates() {
        LocationListener listener = new StationServiceActivity();
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,listener,listener);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    void setImageIcon() {
        if (open247)
            open.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_service_247_onstate_icon));
        else
            open.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_service_247_offstate_icon));

        if (hasCarWash)
            carWash.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_service_carwash_onstate_icon));
        else
            carWash.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_service_carwash_offstate_icon));
        if (hasGas)
            gas.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_service_gas_onstate_icon));
        else
            gas.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_service_gas_offstate_icon));
        if (hasShop)
            shop.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_service_shop_onstate_icon));
        else
            shop.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_service_shop_offstate_icon));


    }

    //get current Location


    //End getCurrentLocation()

    //Check gps availability
    private void GPSDialog() {
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("GPS network not available");
            dialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }


    abstract class GPSInterface {
        public abstract void getGPSStatus(Boolean isEnabled);
    }

    void getLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        locationManager.requestLocationUpdates(provider, 3000, 10, this);
        myLocation = locationManager.getLastKnownLocation(provider);

    }

}
/*end activity*/
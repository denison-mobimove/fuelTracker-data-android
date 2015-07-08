package wearemobimove.com.fueltrackerdatacapture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kogitune.activity_transition.ActivityTransition;

/**
 * Created by cupidon on 7/7/15.
 */
public class StationInfoActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_info);
        setStationInfo(savedInstanceState);
        ActivityTransition.with(getIntent()).to(findViewById(R.id.img_info_icon)).duration(1000).start(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setStationInfo(Bundle savedInstanceState) {
        setContentView(R.layout.station_info);
        setInfoIcon(MainActivity.selectedStation);
        ActionBar appBar = getSupportActionBar();
        appBar.setTitle(MainActivity.gasStation.brand);

        final EditText street = (EditText) findViewById(R.id.editText_street);
        final EditText town = (EditText) findViewById(R.id.editText_town);
        final EditText phone = (EditText) findViewById(R.id.editText_phone_number);
        RelativeLayout button = (RelativeLayout) findViewById(R.id.next_layout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gasStation.address_street = street.getText().toString();
                MainActivity.gasStation.address_town = town.getText().toString();
                MainActivity.gasStation.phoneNumber = phone.getText().toString();
                startActivity(new Intent(StationInfoActivity.this, StationServiceActivity.class));
            }
        });

        Runnable runn = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ((LinearLayout) findViewById(R.id.info_content)).setVisibility(LinearLayout.VISIBLE);

                    }
                });
            }
        };
        new Handler().postDelayed(runn, 1000);

    }

    private void setInfoIcon(MainActivity.STATIONS stations) {
        ImageView img = (ImageView) findViewById(R.id.img_info_icon);
        switch (stations.toString()) {
            case "SHELL":
                img.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_shell_icon));
                break;
            case "TOTAL":
                img.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_total_icon));
                break;
            case "ENGEN":
                img.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_engen_icon));
                break;
            case "INDIANOIL":
                img.setImageDrawable(getResources().getDrawable(R.drawable.fueltracker_filter_indianoil_icon));
                break;
        }
        /*end s3witch */
    }
    //End setInfoIcon


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    /*End onBackPress()*/
}
//End activity
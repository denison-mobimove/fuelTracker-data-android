package wearemobimove.com.fueltrackerdatacapture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kogitune.activity_transition.ActivityTransitionLauncher;

;import wearemobimove.com.fueltrackerdatacapture.entity.GasStation;
import wearemobimove.com.fueltrackerdatacapture.file.StationsFile;

/**
 * Created by cupidon on 7/7/15.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    public static GasStation gasStation = new GasStation();

    public static  enum STATIONS{
        SHELL,TOTAL,ENGEN,INDIANOIL
    }

    public static  STATIONS selectedStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGasStationView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setGasStationView(){
        setContentView(R.layout.gas_station);
        ActionBar appBar = getSupportActionBar();
        appBar.setTitle("Gas Stations");
        LinearLayout shell = (LinearLayout) findViewById(R.id.shell_layout);
        LinearLayout total = (LinearLayout) findViewById(R.id.total_layout);
        LinearLayout engen = (LinearLayout) findViewById(R.id.engen_layout);
        LinearLayout indianOil = (LinearLayout) findViewById(R.id.indianoil_layout);

        shell.setOnClickListener(this);
        total.setOnClickListener(this);
        engen.setOnClickListener(this);
        indianOil.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        final Intent intent= new Intent(MainActivity.this,StationInfoActivity.class);
        switch (viewId){
            case R.id.shell_layout:
                selectedStation = STATIONS.SHELL;
                ActivityTransitionLauncher.with(MainActivity.this).from((ImageView) findViewById(R.id.shell_icon)).launch(intent);
                break;
            case R.id.total_layout:
                selectedStation = STATIONS.TOTAL;
                ActivityTransitionLauncher.with(MainActivity.this).from((ImageView) findViewById(R.id.shell_icon)).launch(intent);
                break;
            case R.id.engen_layout:
                selectedStation = STATIONS.ENGEN;
                ActivityTransitionLauncher.with(MainActivity.this).from((ImageView) findViewById(R.id.shell_icon)).launch(intent);
                break;
            case R.id.indianoil_layout:
                selectedStation = STATIONS.INDIANOIL;
                ActivityTransitionLauncher.with(MainActivity.this).from((ImageView) findViewById(R.id.shell_icon)).launch(intent);
                break;
        }
        //End switch
        gasStation.brand = selectedStation.toString();
    }
    //End onClick

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {

            StationsFile.sendEmail(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
//End MainActivity

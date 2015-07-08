package wearemobimove.com.fueltrackerdatacapture.file;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;

import wearemobimove.com.fueltrackerdatacapture.MainActivity;
import wearemobimove.com.fueltrackerdatacapture.entity.GasStation;

/**
 * Created by cupidon on 7/7/15.
 */
public class StationsFile {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEWLINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "NAME,STREET,TOWN,PHONE,OPEN,CAR WASH,GAS,SHOP" +
            ",LAT,LNG,DATE";
    private  static String filelocation ="";

    public static void stationsToCSV(GasStation station) {
        String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/stations.csv";
        filelocation = csv;
        File csvFile = new File(csv);
        BufferedReader fileReader = null;

        Log.i("csv","file:"+ csv);
        try {
            FileWriter fileWriter = new FileWriter(csv,true);
            fileReader = new BufferedReader(new FileReader(csv));
            if(fileReader.readLine() == null){
                fileWriter.append(FILE_HEADER+"\n");
            }


//            CSVWriter writer = new CSVWriter(wr,'\t');


            String brand = station.brand;
            String street = station.address_street;
            String town =station.address_town;
            String phone  =station.phoneNumber;
            boolean open = station.isOpened;
            boolean carWash = station.hasCarWash;
            boolean gas = station.hasGas;
            boolean shop = station.hasShop;
            double lat = station.lattitude;
            double lng = station.longitude;
            Date date = MainActivity.gasStation.date;

            String record = brand+COMMA_DELIMITER+street+COMMA_DELIMITER+town+COMMA_DELIMITER+phone+
                    COMMA_DELIMITER+open+COMMA_DELIMITER+carWash+COMMA_DELIMITER+gas+COMMA_DELIMITER+shop
                    +COMMA_DELIMITER+lat+COMMA_DELIMITER+lng+COMMA_DELIMITER+date+NEWLINE_SEPARATOR;

            fileWriter.append(record);
            fileWriter.flush();
            fileWriter.close();
            Log.i("csv","Record added");

        }catch (Exception e){
            Log.e("csv","Error reading file",e);
        }

    }

    public static void sendEmail(Activity activity){

        try {
            String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/stations.csv";
            filelocation = csv;
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"cupidon@mokaza.com","devansh@mokaza.com","stephen@mokaza.com"});
            email.putExtra(Intent.EXTRA_SUBJECT, "Gas Stations "+new Date());
            email.putExtra(Intent.EXTRA_TEXT, "Gas stations");
            email.setType(HTTP.PLAIN_TEXT_TYPE);
            email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(csv)));
            activity.startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }catch (Exception e){
            Log.e("csv","email error",e);
        }
    }

}
//End class
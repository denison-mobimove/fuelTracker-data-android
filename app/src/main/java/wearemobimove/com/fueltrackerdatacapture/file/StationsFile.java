package wearemobimove.com.fueltrackerdatacapture.file;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetAddress;
import java.util.Date;

import wearemobimove.com.fueltrackerdatacapture.MainActivity;
import wearemobimove.com.fueltrackerdatacapture.entity.GasStation;

/**
 * Created by cupidon on 7/7/15.
 */
public class StationsFile {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEWLINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "brandKeyName,address_street,address_town,phoneNumber,phoneNumber2,isOpen,hasCarWash ,hasGas,hasShop" +
            ",latitude,longitude,date";
    private static String filelocation = "";

    public static void stationsToCSV(GasStation station) {
        String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/stations.csv";
        filelocation = csv;
        File csvFile = new File(csv);
        BufferedReader fileReader = null;

        Log.i("csv", "file:" + csv);
        try {
            FileWriter fileWriter = new FileWriter(csv, true);
            fileReader = new BufferedReader(new FileReader(csv));
            if (fileReader.readLine() == null) {
                fileWriter.append(FILE_HEADER + "\n");
            }


//            CSVWriter writer = new CSVWriter(wr,'\t');


            String brand = station.brand;
            String street = station.address_street;
            String town = station.address_town;
            String phone ;
            if(station.phoneNumber.split(",").length <2)
                phone = station.phoneNumber+COMMA_DELIMITER;
            else
                phone = station.phoneNumber;
            boolean open = station.isOpened;
            boolean carWash = station.hasCarWash;
            boolean gas = station.hasGas;
            boolean shop = station.hasShop;
            double lat = station.lattitude;
            double lng = station.longitude;
            Date date = MainActivity.gasStation.date;

            String record =brand + COMMA_DELIMITER + street + COMMA_DELIMITER + town + COMMA_DELIMITER + phone +
                    COMMA_DELIMITER + open + COMMA_DELIMITER + carWash + COMMA_DELIMITER + gas + COMMA_DELIMITER + shop
                    + COMMA_DELIMITER + lat + COMMA_DELIMITER + lng + COMMA_DELIMITER + date;

            fileWriter.append(NEWLINE_SEPARATOR);
            fileWriter.append(record);
            fileWriter.flush();
            fileWriter.close();
            Log.i("csv", "Record added");

        } catch (Exception e) {
            Log.e("csv", "Error reading file", e);
        }

    }

    public static void sendEmail(final Activity activity) {


        isInternetAvailable(new ConnectivityListener.checkInternet() {
            @Override
            public void getStatus(final boolean bool) {
                new Runnable() {

                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (bool)
                                    try {
                                        String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/stations.csv";
                                        filelocation = csv;
                                        Intent email = new Intent(Intent.ACTION_SEND);
                                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"cupidon.denison@live.com", "devansh@mokaza.com", "stephen@mokaza.com"});
                                        email.putExtra(Intent.EXTRA_SUBJECT, "Gas Stations " + new Date());
                                        email.putExtra(Intent.EXTRA_TEXT, "Gas stations");
                                        email.setType(HTTP.PLAIN_TEXT_TYPE);
                                        email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(csv)));
                                        activity.startActivity(Intent.createChooser(email, "Choose an Email client :"));
                                    } catch (Exception e) {
                                        Log.e("csv", "email error", e);
                                    }
                                else {
                                    AlertDialog alert = new AlertDialog.Builder(activity).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).setMessage("No internet connection ").setTitle("Connectivity").show();
                                }
                            }
                        });
                    }
                }.run();
            }
        });

    }
    /*End send mail*/

    public static void isInternetAvailable(final ConnectivityListener.checkInternet internet) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

                    if (ipAddr.equals("")) {
                        Log.i("internet", "inetaddress value false: " + ipAddr);
                        internet.getStatus(false);

                    } else {
                        Log.i("internet", "inetaddress value true: " + ipAddr);
                        internet.getStatus(true);
                    }

                } catch (Exception e) {
                    Log.e("internet", "connectivity error", e);
                    internet.getStatus(false);

                }
            }
        }
        ).start();

    }
    /*check internet connection*/

    public static class ConnectivityListener {
        public static abstract class checkInternet {
            public abstract void getStatus(boolean bool);
        }
    }
}
//End class
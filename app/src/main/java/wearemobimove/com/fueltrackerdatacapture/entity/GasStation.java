package wearemobimove.com.fueltrackerdatacapture.entity;

import java.util.Date;

/**
 * Created by cupidon on 7/7/15.
 */
public class GasStation {
    public String brand,address_street,address_town,phoneNumber;
    public boolean isOpened,hasCarWash,hasGas,hasShop;
    public double lattitude,longitude;
    public Date date ;


    @Override
    public String toString() {
        return "GasStation{" +
                "brand='" + brand + '\'' +
                ", address_street='" + address_street + '\'' +
                ", address_town='" + address_town + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isOpened=" + isOpened +
                ", hasCarWash=" + hasCarWash +
                ", hasGas=" + hasGas +
                ", hasShop=" + hasShop +
                ", lattitude=" + lattitude +
                ", longitude=" + longitude +
                ", date=" + date +
                '}';
    }
}
//End class

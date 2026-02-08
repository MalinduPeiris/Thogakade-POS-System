package model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
@NoArgsConstructor
public class CustomerTM {

    private String id;
    private String name;
    private LocalDate date;
    private double salary;
    private String address;
    private String city;
    private String province;
    private String postalCode;

    public CustomerTM(String id,String title , String name, LocalDate date, double salary, String address, String city, String province, String postalCode) {
        this.id = id;
        this.name = title+"."+name;
        this.date = date;
        this.salary = salary;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }
}

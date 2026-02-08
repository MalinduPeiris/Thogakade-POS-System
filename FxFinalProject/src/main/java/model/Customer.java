package model;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer {
    private String id;
    private String title;
    private String name;
    private LocalDate date;
    private double salary;
    private String address;
    private String city;
    private String province;
    private String postalCode;

}

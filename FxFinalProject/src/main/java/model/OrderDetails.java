package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetails {

    private String orderID;
    private String customerID;
    private String customerName;
    private LocalDate orderDate;
    private String orderItems;
    private double unitPrice;
    private int orderQty;
    private double totalCost;

}

package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item {
    private String id;
    private String description;
    private String packSize;
    private double unitPrice;
    private int qtyOnHand;
}

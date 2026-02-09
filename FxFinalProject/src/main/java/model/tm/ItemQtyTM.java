package model.tm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemQtyTM {

    private String id;
    private String name;
    private double unitPrice;
    private int qtyOnHand;
    private int totSoldQty;

}

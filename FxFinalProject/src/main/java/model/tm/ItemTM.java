package model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemTM {
    private String id;
    private String description;
    private String packSize;
    private double unitPrice;
    private int qtyOnHand;

}

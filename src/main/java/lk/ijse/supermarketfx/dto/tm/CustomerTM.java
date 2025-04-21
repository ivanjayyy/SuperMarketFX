package lk.ijse.supermarketfx.dto.tm;


import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class CustomerTM {
    // each column need one field

    private String customerId;
    private String name;
    private String nic;
    private String email;
    private String phone;
}

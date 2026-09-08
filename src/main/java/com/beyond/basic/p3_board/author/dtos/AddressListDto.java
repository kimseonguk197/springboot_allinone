package com.beyond.basic.p3_board.author.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddressListDto {
    private Long id;
    private String city;
    private String street;
    private String zipcode;
    private String authorEmail;
}

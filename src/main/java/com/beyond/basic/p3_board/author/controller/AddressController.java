package com.beyond.basic.p3_board.author.controller;

import com.beyond.basic.p3_board.author.dtos.AddressListDto;
import com.beyond.basic.p3_board.author.service.AddressService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/list")
    public List<AddressListDto> findAll(){
        return addressService.findAll();
    }
}

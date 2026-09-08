package com.beyond.basic.p3_board.author.service;

import com.beyond.basic.p3_board.author.domain.Address;
import com.beyond.basic.p3_board.author.dtos.AddressListDto;
import com.beyond.basic.p3_board.author.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<AddressListDto> findAll(){
//         List<Address> addressList =  addressRepository.findAll();
        List<Address> addressList =  addressRepository.findAllFetchJoin();
         List<AddressListDto> addressListDtos = new ArrayList<>();
         for (Address a : addressList){
             AddressListDto addressListDto = AddressListDto.builder().city(a.getCity())
                     .street(a.getStreet()).zipcode(a.getZipCode())
                     .authorEmail(a.getAuthor().getEmail())
                     .build();
             addressListDtos.add(addressListDto);
         }
         return addressListDtos;
    }
}

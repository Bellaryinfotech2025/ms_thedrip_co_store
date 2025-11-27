package com.thedripcostore.demo.service;

import com.thedripcostore.demo.dto.UserAddressRequestDto;
import com.thedripcostore.demo.entity.UserAddress;
import java.util.List;

public interface AddressService {
	
    UserAddress createAddress(UserAddressRequestDto dto);
    
    List<UserAddress> getAddressesByUserId(String userId);
    
    UserAddress updateAddress(Long id, UserAddressRequestDto dto);
    
    List<UserAddress> getAllAddresses();

    UserAddress getAddressByAddressId(String addressId);
    
    void deleteAddress(Long id);
}

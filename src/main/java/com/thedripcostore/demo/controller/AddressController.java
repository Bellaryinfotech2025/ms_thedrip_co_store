package com.thedripcostore.demo.controller;

import com.thedripcostore.demo.dto.UserAddressRequestDto;
import com.thedripcostore.demo.entity.UserAddress;
import com.thedripcostore.demo.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private static final Logger log = LoggerFactory.getLogger(AddressController.class);

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/address")
    public ResponseEntity<UserAddress> create(@RequestBody UserAddressRequestDto dto) {
        try {
            UserAddress created = addressService.createAddress(dto);
            log.info("Address created successfully for userId: {}", dto.getUserId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Failed to create address for userId: {}", dto.getUserId(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getAddressByUserId/{userId}")
    public ResponseEntity<List<UserAddress>> getByUser(@PathVariable String userId) {
        try {
            List<UserAddress> addresses = addressService.getAddressesByUserId(userId);
            log.info("Fetched {} addresses for userId: {}", addresses.size(), userId);
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            log.error("Failed to fetch addresses for userId: {}", userId, e);
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAddress> update(@PathVariable Long id, @RequestBody UserAddressRequestDto dto) {
        try {
            UserAddress updated = addressService.updateAddress(id, dto);
            log.info("Address updated successfully: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Failed to update address: {}", id, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getAddressAll")
    public ResponseEntity<List<UserAddress>> getAllAddresses() {
        try {
            List<UserAddress> addresses = addressService.getAllAddresses();
            log.info("Fetched all addresses, total count: {}", addresses.size());
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            log.error("Failed to fetch all addresses", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{addressId}/details")
    public ResponseEntity<UserAddress> getAddressByAddressId(@PathVariable String addressId) {
        try {
            UserAddress address = addressService.getAddressByAddressId(addressId);
            log.info("Fetched address successfully: {}", addressId);
            return ResponseEntity.ok(address);
        } catch (Exception e) {
            log.error("Failed to fetch address: {}", addressId, e);
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            log.info("Address deleted successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to delete address: {}", id, e);
            return ResponseEntity.status(500).build();
        }
    }
}

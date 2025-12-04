package com.thedripcostore.demo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thedripcostore.demo.dto.UserAddressRequestDto;
import com.thedripcostore.demo.entity.UserAddress;
import com.thedripcostore.demo.entity.UserEntity;
import com.thedripcostore.demo.repository.UserAddressRepository;
import com.thedripcostore.demo.repository.UserRepository;
import com.thedripcostore.demo.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final UserAddressRepository addressRepo;
    private final UserRepository userRepo;

    public AddressServiceImpl(UserAddressRepository addressRepo, UserRepository userRepo) {
        this.addressRepo = addressRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public UserAddress createAddress(UserAddressRequestDto dto) {
        try {
            UserEntity user = userRepo.findByUserId(dto.getUserId())
                    .orElseThrow(() -> {
                        log.warn("User not found: {}", dto.getUserId());
                        return new RuntimeException("User not found: " + dto.getUserId());
                    });

            UserAddress addr = new UserAddress();
            addr.setUser(user);
            addr.setHouseNo(dto.getHouseNo());
            addr.setStreet(dto.getStreet());
            addr.setLocality(dto.getLocality());
            addr.setCity(dto.getCity());
            addr.setState(dto.getState());
            addr.setCountry(dto.getCountry());
            addr.setPincode(dto.getPincode());
            addr.setFirstName(dto.getFirstName());
            addr.setLastName(dto.getLastName());
            addr.setEmail(dto.getEmail());
            addr.setPhone(dto.getPhone());
            addr.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false);

            // Unset other defaults if this is default
            if (addr.getIsDefault()) {
                List<UserAddress> existing = addressRepo.findByUserUserId(dto.getUserId());
                for (UserAddress e : existing) {
                    if (Boolean.TRUE.equals(e.getIsDefault())) {
                        e.setIsDefault(false);
                        addressRepo.save(e);
                    }
                }
            }

            UserAddress saved = addressRepo.save(addr);
            log.info("Address created successfully for userId: {}", dto.getUserId());
            return saved;

        } catch (Exception e) {
            log.error("Failed to create address for userId: {}", dto.getUserId(), e);
            throw new RuntimeException("Failed to create address", e);
        }
    }

    @Override
    public List<UserAddress> getAddressesByUserId(String userId) {
        try {
            List<UserAddress> addresses = addressRepo.findByUserUserId(userId);
            log.info("Fetched {} addresses for userId: {}", addresses.size(), userId);
            return addresses;
        } catch (Exception e) {
            log.error("Failed to fetch addresses for userId: {}", userId, e);
            throw new RuntimeException("Failed to fetch addresses", e);
        }
    }

    @Override
    @Transactional
    public UserAddress updateAddress(Long id, UserAddressRequestDto dto) {
        try {
            UserAddress addr = addressRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Address not found: {}", id);
                        return new RuntimeException("Address not found: " + id);
                    });

            addr.setHouseNo(dto.getHouseNo());
            addr.setStreet(dto.getStreet());
            addr.setLocality(dto.getLocality());
            addr.setCity(dto.getCity());
            addr.setState(dto.getState());
            addr.setCountry(dto.getCountry());
            addr.setPincode(dto.getPincode());
            addr.setFirstName(dto.getFirstName());
            addr.setLastName(dto.getLastName());
            addr.setEmail(dto.getEmail());
            addr.setPhone(dto.getPhone());

            if (dto.getIsDefault() != null && dto.getIsDefault()) {
                List<UserAddress> existing = addressRepo.findByUserUserId(addr.getUser().getUserId());
                for (UserAddress e : existing) {
                    if (Boolean.TRUE.equals(e.getIsDefault()) && !e.getId().equals(addr.getId())) {
                        e.setIsDefault(false);
                        addressRepo.save(e);
                    }
                }
                addr.setIsDefault(true);
            } else if (dto.getIsDefault() != null) {
                addr.setIsDefault(false);
            }

            addr.setUpdatedAt(java.time.LocalDateTime.now());
            UserAddress updated = addressRepo.save(addr);
            log.info("Address updated successfully: {}", id);
            return updated;

        } catch (Exception e) {
            log.error("Failed to update address: {}", id, e);
            throw new RuntimeException("Failed to update address", e);
        }
    }

    @Override
    public List<UserAddress> getAllAddresses() {
        try {
            List<UserAddress> addresses = addressRepo.findAll();
            log.info("Fetched all addresses, total count: {}", addresses.size());
            return addresses;
        } catch (Exception e) {
            log.error("Failed to fetch all addresses", e);
            throw new RuntimeException("Failed to fetch all addresses", e);
        }
    }

    @Override
    public UserAddress getAddressByAddressId(String addressId) {
        try {
            UserAddress address = addressRepo.findByAddressId(addressId)
                    .orElseThrow(() -> {
                        log.warn("Address not found: {}", addressId);
                        return new RuntimeException("Address not found: " + addressId);
                    });
            log.info("Fetched address: {}", addressId);
            return address;
        } catch (Exception e) {
            log.error("Failed to fetch address: {}", addressId, e);
            throw new RuntimeException("Failed to fetch address", e);
        }
    }

    @Override
    public void deleteAddress(Long id) {
        try {
            addressRepo.deleteById(id);
            log.info("Address deleted successfully: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete address: {}", id, e);
            throw new RuntimeException("Failed to delete address", e);
        }
    }
}

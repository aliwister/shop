package com.badals.shop.service;

import com.badals.shop.domain.Address;
import com.badals.shop.domain.Customer;
import com.badals.shop.repository.AddressRepository;
import com.badals.shop.service.dto.AddressDTO;
import com.badals.shop.service.mapper.AddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Address}.
 */
@Service
@Transactional
public class AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    /**
     * Save a address.
     *
     * @param addressDTO the entity to save.
     * @return the persisted entity.
     */
    public AddressDTO save(AddressDTO addressDTO) {
        log.debug("Request to save Address : {}", addressDTO);
        Address address = addressMapper.toEntity(addressDTO);

        address.setIdCountry(getCountryFromLocale(addressDTO.getCountry()));
        //addressService.getCountryFromLocale(l.getCountry())

        address = addressRepository.save(address);
        return addressMapper.toDto(address);
    }

    /**
     * Get all the addresses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AddressDTO> findAll() {
        log.debug("Request to get all Addresses");
        return addressRepository.findAll().stream()
            .map(addressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one address by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AddressDTO> findOne(Long id) {
        log.debug("Request to get Address : {}", id);
        return addressRepository.findById(id)
            .map(addressMapper::toDto);
    }

    /**
     * Delete the address by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Address : {}", id);
        addressRepository.deleteById(id);
    }

    public Boolean isAddressUsed(Long id) {
       if(addressRepository.findInOrders(id) == 0)
           return false;

       return true;
    }

    @Modifying
    public void retireAddress(Long id) {
        addressRepository.retireAddress(id);
    }

    public Long getCountryFromLocale(String country) {
        return addressRepository.findCountryByCode(country);
    }

    public void deleteAddressByFlag(Long id) {
        addressRepository.deleteAddressByFlag(id);
    }

    public List<AddressDTO> customerAddresses(Customer c) {
        return addressRepository.findAddressesByCustomerEqualsAndActiveIsTrueAndDeletedIsFalse(c).stream().map(addressMapper::toDto).collect(Collectors.toList());
    }
}

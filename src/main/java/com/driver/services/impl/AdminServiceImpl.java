package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin=new Admin(username,password);
        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {

        Admin admin=adminRepository1.findById(adminId).get();
        ServiceProvider serviceProvider=serviceProviderRepository1.findServiceProviderByName(providerName);
        admin.getServiceProviders().add(serviceProvider);
        serviceProvider.setAdmin(admin);
        admin=adminRepository1.save(admin);
        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{

        String upperCaseString = countryName.toUpperCase();
        CountryName countryName1 = findCountryByName(upperCaseString);
        if(countryName1==null){
            throw new Exception("Country not found");
        }

        Country country=new Country();
        country.setCountryName(countryName1);
        country.setCode(countryName1.toCode());
        ServiceProvider serviceProvider=serviceProviderRepository1.findById(serviceProviderId).get();

        serviceProvider.getCountryList().add(country);
        country.setServiceProvider(serviceProvider);
        countryRepository1.save(country);
        return serviceProvider;

    }

    public   CountryName findCountryByName(String name) {
        for (CountryName country : CountryName.values()) {
            if (country.name().equals(name)) {
                return country;
            }
        }
        return null;
    }
}

package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{

        User user =new User();
        user.setConnected(Boolean.FALSE);
        user.setUsername(username);
        user.setPassword(password);
        user.setMaskedIp(null);

        String upperCaseString = countryName.toUpperCase();
      Country countryName1 =  countryRepository3.findCountryByCountryName(countryName);

        if(countryName1==null){
            throw new Exception("Country not found");
        }

      /*  Country country=new Country();
        country.setCountryName(countryName1);
        country.setCode(countryName1.toCode());
        */
        user.setOriginalCountry(countryName1);

        String ip=countryName1.getCode()+"."+user.getId();
        user.setOriginalIp(ip);
        countryName1.setUser(user);
        user=userRepository3.save(user);

        return user;
    }
    public   CountryName findCountryByName(String name) {
        for (CountryName country : CountryName.values()) {
            if (country.name().equals(name)) {
                return country;
            }
        }
        return null;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {

        User user=userRepository3.findById(userId).get();
        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();

        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);

        user=userRepository3.save(user);
        return user;

    }
}

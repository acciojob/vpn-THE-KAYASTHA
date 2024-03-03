package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;


    @Autowired
    CountryRepository countryRepository;
    //Connect the user to a vpn by considering the following priority order.
    //1. If the user is already connected to any service provider, throw "Already connected" exception.
    //2. Else if the countryName corresponds to the original country of the user, do nothing. This means that the user wants to connect to its original country, for which we do not require a connection. Thus, return the user as it is.
    //3. Else, the user should be subscribed under a serviceProvider having option to connect to the given country.
    //If the connection can not be made (As user does not have a serviceProvider or serviceProvider does not have given country, throw "Unable to connect" exception.
    //Else, establish the connection where the maskedIp is "updatedCountryCode.serviceProviderId.userId" and return the updated user. If multiple service providers allow you to connect to the country, use the service provider having smallest id.

    @Override
    public User connect(int userId, String countryName) throws Exception{

        User user=userRepository2.findById(userId).get();
        if(user.getConnected()){
            throw new Exception("Already connected");
        }



        if(user.getOriginalCountry().getCountryName().name().equals(countryName)) return user;

        List<ServiceProvider> serviceProviderList=user.getServiceProviderList();
        Collections.sort(serviceProviderList, new Comparator<ServiceProvider>() {
            @Override
            public int compare(ServiceProvider sp1, ServiceProvider sp2) {
                return Integer.compare(sp1.getId(), sp2.getId());
            }
        });
        for(ServiceProvider i:serviceProviderList){

            List<Country> countryList=i.getCountryList();

            for(Country j:countryList){
                if(j.getCountryName().name().equals(countryName)){

                    String maskedId=j.getCode()+"."+i.getId()+"."+userId;

                    user.setMaskedIp(maskedId);
                    user.setConnected(Boolean.TRUE);

                    Connection connection=new Connection();
                    connection.setUser(user);
                    connection.setServiceProvider(i);
                    i.getConnectionList().add(connection);

                    connectionRepository2.save(connection);
                    user=userRepository2.save(user);

                    return user;
                }
            }

        }
        throw new Exception("Unable to connect");

    }
    @Override
    public User disconnect(int userId) throws Exception {
        //If the given user was not connected to a vpn, throw "Already disconnected" exception.
        //Else, disconnect from vpn, make masked Ip as null, update relevant attributes and return updated user.
        User user=userRepository2.findById(userId).get();

        if(user.getConnected()==Boolean.FALSE){
            throw new Exception("Already disconnected");
        }
        user.setConnected(Boolean.FALSE);
        user.setMaskedIp(null);
        user=userRepository2.save(user);
        return user;

    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
    return null;
    }
}

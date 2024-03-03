package com.driver.model;




import javax.persistence.*;

// Note: Do not write @Enumerated annotation above CountryName in this model.
@Entity
public class Country{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private CountryName countryName;
    private String code;

    public Country(){

    }
    @OneToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private ServiceProvider serviceProvider;



    public void setUser(User user) {
        this.user = user;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCountryName(CountryName countryName) {
        this.countryName = countryName;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public CountryName getCountryName() {
        return countryName;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public String getCode() {
        return code;
    }
}
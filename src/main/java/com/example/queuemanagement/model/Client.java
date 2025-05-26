package com.example.queuemanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String phone;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<ServiceType> serviceTypes;

    private LocalDateTime arrivalTime = LocalDateTime.now();
    private int queueNumber;

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Set<ServiceType> getServiceTypes() { return serviceTypes; }
    public void setServiceTypes(Set<ServiceType> serviceTypes) { this.serviceTypes = serviceTypes; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public int getQueueNumber() { return queueNumber; }
    public void setQueueNumber(int queueNumber) { this.queueNumber = queueNumber; }
}

package com.example.queuemanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class ArchivedClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String phone;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<ServiceType> serviceTypes;

    private LocalDateTime arrivalTime;
    private int queueNumber;
    private LocalDateTime servedTime;

    public ArchivedClient() {}

    public ArchivedClient(Client client, LocalDateTime servedTime) {
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.phone = client.getPhone();
        this.serviceTypes = client.getServiceTypes();
        this.arrivalTime = client.getArrivalTime();
        this.queueNumber = client.getQueueNumber();
        this.servedTime = servedTime;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public Set<ServiceType> getServiceTypes() { return serviceTypes; }
    public void setServiceTypes(Set<ServiceType> serviceTypes) { this.serviceTypes = serviceTypes; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public int getQueueNumber() { return queueNumber; }
    public LocalDateTime getServedTime() { return servedTime; }
}
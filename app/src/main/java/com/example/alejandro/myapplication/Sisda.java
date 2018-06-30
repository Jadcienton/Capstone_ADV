package com.example.alejandro.myapplication;

public class Sisda {
    private String sisda, priority,level, object, fault, status;
    private String address, number, department, city, corner, reference, lat, lng, pipeDiameter, pipeMaterial, sewerDiameter, sewerMaterial, waterMeterQuantity, socialClient, activityCustomer;
    private String codeCustomer, clientName, clientPhone, clientPhoneTwo;
    private String dateCreation, dateArrival, dateBeginning, dateHydraulic, dateDefinitive, datePavement; // TODO cambiar a tipo fecha los date
    private String arrivalUser, beginningUser, hydraulicUser, definitiveUser, pavementUser;
    private String Delay, Time;


    public Sisda(String sisda, String priority, String level, String object, String fault, String status, String address, String number, String department, String city, String corner, String reference, String lat, String lng, String pipeDiameter, String pipeMaterial, String sewerDiameter, String sewerMaterial, String waterMeterQuantity, String socialClient, String activityCustomer, String codeCustomer, String clientName, String clientPhone,String clientPhoneTwo, String dateCreation, String dateArrival, String dateBeginning, String dateHydraulic, String dateDefinitive, String datePavement, String arrivalUser, String beginningUser, String hydraulicUser, String definitiveUser, String pavementUser , String Delay, String Time) {
        this.sisda = sisda;
        this.priority = priority;
        this.level = level;
        this.object = object;
        this.fault = fault;
        this.status = status;
        this.address = address;
        this.number = number;
        this.department = department;
        this.city = city;
        this.corner = corner;
        this.reference = reference;
        this.lat = lat;
        this.lng = lng;
        this.pipeDiameter = pipeDiameter;
        this.pipeMaterial = pipeMaterial;
        this.sewerDiameter = sewerDiameter;
        this.sewerMaterial = sewerMaterial;
        this.waterMeterQuantity = waterMeterQuantity;
        this.socialClient = socialClient;
        this.activityCustomer = activityCustomer;
        this.codeCustomer = codeCustomer;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.clientPhoneTwo= clientPhoneTwo;
        this.dateCreation = dateCreation;
        this.dateArrival = dateArrival;
        this.dateBeginning = dateBeginning;
        this.dateHydraulic = dateHydraulic;
        this.dateDefinitive = dateDefinitive;
        this.datePavement = datePavement;
        this.arrivalUser = arrivalUser;
        this.beginningUser = beginningUser;
        this.hydraulicUser = hydraulicUser;
        this.definitiveUser = definitiveUser;
        this.pavementUser = pavementUser;
        this.Delay = Delay;
        this.Time = Time;
    }

    public String getSisda() {
        return sisda;
    }

    public String getPriority() {
        return priority;
    }

    public String getLevel() {
        return level;
    }

    public String getObject() {
        return object;
    }

    public String getFault() {
        return fault;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public String getDepartment() {
        return department;
    }

    public String getCity() {
        return city;
    }

    public String getCorner() {
        return corner;
    }

    public String getReference() {
        return reference;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getPipeDiameter() {
        return pipeDiameter;
    }

    public String getPipeMaterial() {
        return pipeMaterial;
    }

    public String getSewerDiameter() {
        return sewerDiameter;
    }

    public String getSewerMaterial() {
        return sewerMaterial;
    }

    public String getWaterMeterQuantity() {
        return waterMeterQuantity;
    }

    public String getSocialClient() {
        return socialClient;
    }

    public String getActivityCustomer() {
        return activityCustomer;
    }

    public String getCodeCustomer() {
        return codeCustomer;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public String getClientPhoneTwo() {
        return clientPhoneTwo;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateArrival() {
        return dateArrival;
    }

    public String getDateBeginning() {
        return dateBeginning;
    }

    public String getDateHydraulic() {
        return dateHydraulic;
    }

    public String getDateDefinitive() {
        return dateDefinitive;
    }

    public String getDatePavement() {
        return datePavement;
    }

    public String getArrivalUser() {
        return arrivalUser;
    }

    public String getBeginningUser() {
        return beginningUser;
    }

    public String getHydraulicUser() {
        return hydraulicUser;
    }

    public String getDefinitiveUser() {
        return definitiveUser;
    }

    public String getPavementUser() {
        return pavementUser;
    }
    public String getDelay() {
        return Delay;
    }
    public String getTime() {
        return Time;
    }
}
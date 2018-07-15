package com.example.alejandro.myapplication;

public class Sisda {
    private String sisda, priority,level, object, fault, status, synergiaStatus;
    private String address, number, department, city, corner, reference, pipeDiameter, pipeMaterial, sewerDiameter, sewerMaterial, waterMeterQuantity, socialClient, activityCustomer;
    private String lat, lng, latArrival, lngArrival, latBeginning, lngBeginning, latHydraulic, lngHydraulic, latPavement, lngPavement;
    private String codeCustomer, clientName, clientPhone, clientPhoneTwo;
    private String dateCreation, dateArrival, dateBeginning, dateHydraulic, dateDefinitive, datePavement, pavementRequired;
    private String arrivalUser, beginningUser, hydraulicUser, definitiveUser, pavementUser;
    private String Delay, Time;


    public Sisda(String sisda, String priority, String level, String object, String fault, String status, String address, String number, String department, String city, String corner, String reference, String lat, String lng, String pipeDiameter, String pipeMaterial, String sewerDiameter, String sewerMaterial, String waterMeterQuantity, String socialClient, String activityCustomer, String codeCustomer, String clientName, String clientPhone,String clientPhoneTwo, String dateCreation, String dateArrival, String dateBeginning, String dateHydraulic, String dateDefinitive, String datePavement, String arrivalUser, String beginningUser, String hydraulicUser, String definitiveUser, String pavementUser , String Delay, String Time,String latArrival, String lngArrival, String latBeginning, String lngBeginning, String latHydraulic, String lngHydraulic, String latPavement, String lngPavement,String synergiaStatus, String pavementRequired) {
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
        this.latArrival = latArrival;
        this.lngArrival = lngArrival;
        this.latBeginning = latBeginning;
        this.lngBeginning = lngBeginning;
        this.latHydraulic = latHydraulic;
        this.lngHydraulic = lngHydraulic;
        this.latPavement = latPavement;
        this.lngPavement = lngPavement;
        this.synergiaStatus = synergiaStatus;
        this.pavementRequired = pavementRequired;
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

    public String getLatArrival() {
        return latArrival;
    }

    public String getLngArrival() {
        return lngArrival;
    }

    public String getLatBeginning() {
        return latBeginning;
    }

    public String getLngBeginning() {
        return lngBeginning;
    }

    public String getLatHydraulic() {
        return latHydraulic;
    }

    public String getLngHydraulic() {
        return lngHydraulic;
    }

    public String getLatPavement() {
        return latPavement;
    }

    public String getLngPavement() {
        return lngPavement;
    }

    public String getSynergiaStatus() {
        return synergiaStatus;
    }

    public String getPavementRequired() {
        return pavementRequired;
    }

    public void setSisda(String sisda) {
        this.sisda = sisda;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSynergiaStatus(String synergiaStatus) {
        this.synergiaStatus = synergiaStatus;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCorner(String corner) {
        this.corner = corner;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setPipeDiameter(String pipeDiameter) {
        this.pipeDiameter = pipeDiameter;
    }

    public void setPipeMaterial(String pipeMaterial) {
        this.pipeMaterial = pipeMaterial;
    }

    public void setSewerDiameter(String sewerDiameter) {
        this.sewerDiameter = sewerDiameter;
    }

    public void setSewerMaterial(String sewerMaterial) {
        this.sewerMaterial = sewerMaterial;
    }

    public void setWaterMeterQuantity(String waterMeterQuantity) {
        this.waterMeterQuantity = waterMeterQuantity;
    }

    public void setSocialClient(String socialClient) {
        this.socialClient = socialClient;
    }

    public void setActivityCustomer(String activityCustomer) {
        this.activityCustomer = activityCustomer;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setLatArrival(String latArrival) {
        this.latArrival = latArrival;
    }

    public void setLngArrival(String lngArrival) {
        this.lngArrival = lngArrival;
    }

    public void setLatBeginning(String latBeginning) {
        this.latBeginning = latBeginning;
    }

    public void setLngBeginning(String lngBeginning) {
        this.lngBeginning = lngBeginning;
    }

    public void setLatHydraulic(String latHydraulic) {
        this.latHydraulic = latHydraulic;
    }

    public void setLngHydraulic(String lngHydraulic) {
        this.lngHydraulic = lngHydraulic;
    }

    public void setLatPavement(String latPavement) {
        this.latPavement = latPavement;
    }

    public void setLngPavement(String lngPavement) {
        this.lngPavement = lngPavement;
    }

    public void setCodeCustomer(String codeCustomer) {
        this.codeCustomer = codeCustomer;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public void setClientPhoneTwo(String clientPhoneTwo) {
        this.clientPhoneTwo = clientPhoneTwo;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateArrival(String dateArrival) {
        this.dateArrival = dateArrival;
    }

    public void setDateBeginning(String dateBeginning) {
        this.dateBeginning = dateBeginning;
    }

    public void setDateHydraulic(String dateHydraulic) {
        this.dateHydraulic = dateHydraulic;
    }

    public void setDateDefinitive(String dateDefinitive) {
        this.dateDefinitive = dateDefinitive;
    }

    public void setDatePavement(String datePavement) {
        this.datePavement = datePavement;
    }

    public void setPavementRequired(String pavementRequired) {
        this.pavementRequired = pavementRequired;
    }

    public void setArrivalUser(String arrivalUser) {
        this.arrivalUser = arrivalUser;
    }

    public void setBeginningUser(String beginningUser) {
        this.beginningUser = beginningUser;
    }

    public void setHydraulicUser(String hydraulicUser) {
        this.hydraulicUser = hydraulicUser;
    }

    public void setDefinitiveUser(String definitiveUser) {
        this.definitiveUser = definitiveUser;
    }

    public void setPavementUser(String pavementUser) {
        this.pavementUser = pavementUser;
    }

    public void setDelay(String delay) {
        Delay = delay;
    }

    public void setTime(String time) {
        Time = time;
    }
}
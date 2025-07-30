package com.headhunter.parking.Parking.constants;

public enum CatTypeVehicleENUM {

    RESIDENT("RES"),
    OFFICIAL("OFFI"),
    NOTRESIDENT("NOTRESI");

    private final String id;

    CatTypeVehicleENUM(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static CatTypeVehicleENUM fromId(String id) {
        return java.util.Arrays.stream(CatTypeVehicleENUM.values())
                .filter(tipo -> tipo.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un tipo de vehículo con id: " + id));
    }

}

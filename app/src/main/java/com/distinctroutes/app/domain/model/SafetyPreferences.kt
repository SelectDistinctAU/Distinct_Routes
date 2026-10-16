package com.distinctroutes.app.domain.model

enum class BikeType(
    val displayName: String,
    val profileName: String
) {
    STANDARD("Standard Bicycle", "bike"),
    CITY("City / Utility Bike", "bike"),
    E_BIKE("E-Bike / Pedelec", "bike"),
    MOUNTAIN("Mountain Bike (MTB)", "mtb"),
    CARGO("Cargo Bike", "cargo"),
    TANDEM("Tandem Bike", "cargo"),
    RECUMBENT("Recumbent / Velomobile", "bike"),
    WHEELCHAIR("Wheelchair / Handbike", "handbike"),
    MOBILITY_SCOOTER("Mobility Scooter", "handbike")
}

data class SafetyPreferences(
    val avoidTrafficLights: Boolean = false,
    val avoidHighways: Boolean = false,
    val avoidSteepHills: Boolean = false,
    val hasDogTrailer: Boolean = false,
    val preferGreenRoutes: Boolean = false,
    val avoidUnlitStreets: Boolean = false,
    val avoidCobblestones: Boolean = false,
    val avoidFerries: Boolean = false,
    val bikeType: BikeType = BikeType.STANDARD
) {
    fun isMobilityDevice(): Boolean {
        return bikeType == BikeType.WHEELCHAIR || bikeType == BikeType.MOBILITY_SCOOTER
    }

    fun effectiveAvoidSteepHills(): Boolean {
        return avoidSteepHills || isMobilityDevice()
    }

    fun effectiveAvoidHighways(): Boolean {
        return avoidHighways || isMobilityDevice()
    }
}
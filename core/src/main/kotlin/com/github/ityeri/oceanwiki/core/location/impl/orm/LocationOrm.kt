package com.github.ityeri.oceanwiki.core.location.impl.orm

import org.jetbrains.exposed.dao.ULongEntity
import org.jetbrains.exposed.dao.ULongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.ULongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object LocationTable : ULongIdTable("locations") {
    // In a real app, point_geometry would be a custom column type for MySQL's spatial POINT
    val latitude = double("latitude")
    val longitude = double("longitude") // This should be 'longitude'
    val fullAddress = varchar("full_address", 500)
    val province = varchar("province", 50) // address_depth1
    val city = varchar("city", 50)       // address_depth2
}

class LocationRow(id: EntityID<ULong>) : ULongEntity(id) {
    companion object : ULongEntityClass<LocationRow>(LocationTable)

    var latitude by LocationTable.latitude
    var longitude by LocationTable.longitude
    var fullAddress by LocationTable.fullAddress
    var province by LocationTable.province
    var city by LocationTable.city
}

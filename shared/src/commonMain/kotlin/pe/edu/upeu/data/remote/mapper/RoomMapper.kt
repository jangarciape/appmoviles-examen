package pe.edu.upeu.data.remote.mapper

import pe.edu.upeu.data.remote.dto.AmenityDto
import pe.edu.upeu.data.remote.dto.RoomImageDto
import pe.edu.upeu.data.remote.dto.RoomTypeDto
import pe.edu.upeu.domain.model.Amenity
import pe.edu.upeu.domain.model.RoomImage
import pe.edu.upeu.domain.model.RoomType

fun AmenityDto.toDomain(): Amenity{
    return Amenity(
        id = this.id,
        name = this.name,
        description = this.description
    )
}
fun RoomImageDto.toDomain(): RoomImage{
    return RoomImage(
        id= this.id,
        url = this.url,
        altText = this.alt_text
    )
}
fun RoomTypeDto.toDomain(): RoomType{
    return RoomType(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price_per_night.toDoubleOrNull() ?:0.0,
        capacity = this.capacity,
        amenities = this.amenities.map { it.toDomain() },
        images = this.images.map { it.toDomain() }
    )
}


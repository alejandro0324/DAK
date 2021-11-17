package application.dak.DAK.backend.zones.mappers;

import application.dak.DAK.backend.common.dto.Coordinate;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CoordinateMapper {

    @Insert("INSERT INTO T_Coordinates (lat, lng, zoneUUID) VALUES (#{coordinate.lat}, #{coordinate.lng}, #{coordinate.zoneUUID})")
    void add(@Param("coordinate") final Coordinate coordinate);

    @Select("SELECT lat, lng, zoneUUID FROM T_Coordinates WHERE zoneUUID = #{zoneUUID}")
    List<Coordinate> getCoordinates(@Param("zoneUUID") final String zoneUUID);
}

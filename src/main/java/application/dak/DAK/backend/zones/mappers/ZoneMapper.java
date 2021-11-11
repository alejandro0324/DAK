package application.dak.DAK.backend.zones.mappers;

import application.dak.DAK.backend.common.dto.Zone;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ZoneMapper {
    @Insert("INSERT INTO tZones (name, colour, isEnabled) VALUES (#{zone.name}, #{zone.colour}, 1)")
    void add(@Param("zone") final Zone checkout);

    @Update("UPDATE tZones SET isEnabled = 0 WHERE id = #{id} ")
    void remove(final Integer id);

    @Select("SELECT id, name, colour FROM item WHERE isEnabled = 1")
    List<Zone> getElements();
}

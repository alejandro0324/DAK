package application.dak.DAK.backend.zones.mappers;

import application.dak.DAK.backend.common.dto.Zone;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ZoneMapper {
    @Insert("INSERT INTO T_Zones (name, uuid, isEnabled) VALUES (#{zone.name}, #{zone.uuid}, 1)")
    void add(@Param("zone") final Zone zone);

    @Update("UPDATE T_Zones SET isEnabled = 0 WHERE uuid = #{uuid} ")
    void remove(@Param("uuid") final String uuid);

    @Select("SELECT id, name, uuid FROM T_Zones WHERE isEnabled = 1")
    List<Zone> getElements();
}

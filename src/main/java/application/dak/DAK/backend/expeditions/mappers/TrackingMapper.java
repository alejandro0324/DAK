package application.dak.DAK.backend.expeditions.mappers;

import application.dak.DAK.backend.common.models.Tracking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TrackingMapper {

    @Update("UPDATE T_Package SET state = #{state} WHERE number = #{id}")
    void updatePackage(@Param("state") final String state, @Param("id") final Integer id);

    @Update("UPDATE T_Tracking SET carID = #{tracking.carID}, stateOfTracking = #{tracking.stateOfTracking} WHERE id = #{tracking.id}")
    void updateTracking(@Param("tracking") final Tracking tracking);
}

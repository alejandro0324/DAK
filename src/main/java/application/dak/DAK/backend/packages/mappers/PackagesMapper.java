package application.dak.DAK.backend.packages.mappers;

import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.common.models.Tracking;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface PackagesMapper {

    @Select("SELECT * FROM T_Package")
    List<Package> getAllPackages();

    @Select("SELECT * FROM T_Package WHERE number LIKE #{number}")
    List<Package> getAllPackagesLike(@Param("number") final String number);

    @Insert("INSERT INTO T_Tracking(lat, lng) values (1, 1)")
    @Options(useGeneratedKeys = true, keyProperty = "Id", keyColumn = "Id")
    Integer createTracking(@Param("tracking") final Tracking tracking);

    @Insert("INSERT INTO T_DetTracking(trackingId, date, lat, lng) values (#{id}, #{date}, 1, 1)")
    void createTrackingDet(@Param("id") final Integer id, @Param("date") final Date date);

    @Insert("INSERT INTO T_Package(price, startDate, state, lat, lng, trackingId, paymentTermId, transmitterId, receiverId) values (#{pack.Price}, #{pack.StartDate}, #{pack.State}, #{pack.lat},  #{pack.lng}, #{pack.trackingId}, #{pack.paymentTermId},  #{pack.transmitterId}, #{pack.receiverId})")
    void createPackage(@Param("pack") final Package pack);
}

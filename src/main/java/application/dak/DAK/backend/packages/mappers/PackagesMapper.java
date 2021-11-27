package application.dak.DAK.backend.packages.mappers;

import application.dak.DAK.backend.common.models.Client;
import application.dak.DAK.backend.common.models.Company;
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

    @Insert("INSERT INTO T_Tracking(id, currentLat, currentLng, dateOfTracking, stateOfTracking) VALUES (#{tracking.id}, #{tracking.currentLat}, #{tracking.currentLng}, #{tracking.dateOfTracking}, #{tracking.stateOfTracking})")
    Integer createTracking(@Param("tracking") final Tracking tracking);

    @Select("SELECT * FROM V_ReadyPackages WHERE number LIKE #{number}")
    List<Package> listAllReadyPackagesLike(@Param("number") final String number);

    @Select("SELECT * FROM V_ReadyPackages")
    List<Package> listAllReadyPackages();

    @Insert("INSERT INTO T_Detail(trackingID, dateOfDetail, lat, lng, information) values (#{tracking.id}, #{tracking.dateOfTracking}, #{tracking.currentLat}, #{tracking.currentLng}, #{tracking.stateOfTracking})")
    void createTrackingDet(@Param("tracking") final Tracking tracking);

    @Insert("INSERT INTO T_Package (price, startDate, extraInfo, state, trackingId, paymentTermId, transmitterId, receiverId, lat, lng, address, weight) VALUES (#{pack.Price}, #{pack.StartDate}, #{pack.ExtraInfo}, #{pack.State}, #{pack.trackingId}, #{pack.paymentTermId}, #{pack.transmitterId}, #{pack.receiverId}, #{pack.lat}, #{pack.lng}, #{pack.address}, #{pack.weight})")
    void createPackage(@Param("pack") final Package pack);
}

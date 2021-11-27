package application.dak.DAK.backend.dashboard.mappers;

import application.dak.DAK.backend.common.models.Detail;
import application.dak.DAK.backend.common.models.Package;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DashboardMapper {

    @Select("SELECT sum(price) FROM T_Package WHERE convert(varchar(10), startDate, 102) = convert(varchar(10), getdate(), 102)")
    public String getDailyIncome();

    @Select("select * from T_Package where state = 'IN_LOCAL' or state = 'IN_TRAVEL' or state = 'IN_TRAVEL_PROBLEM'")
    public List<Package> listDashboardPackages();

    @Select("SELECT * FROM T_Detail WHERE dateOfDetail < dateadd(minute,-10,getdate())")
    public List<Detail> checkTravelProblems();

    @Update("UPDATE T_Package SET state = 'IN_TRAVEL_PROBLEM' WHERE trackingId = #{trackingId} AND state = 'IN_TRAVEL'")
    public void setTravelProblem(@Param("trackingId") final String trackingId);
}

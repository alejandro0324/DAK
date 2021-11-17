package application.dak.DAK.backend.generalConfiguration.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ConfigurationMapper {

    @Select("SELECT TripTax FROM T_GeneralConfiguration WHERE Id = 1")
    public int tripTax();

    @Update("UPDATE T_GeneralConfiguration SET TripTax = #{tripTax} WHERE Id = 1")
    public int configSave(@Param("tripTax") final Integer tripTax);
}

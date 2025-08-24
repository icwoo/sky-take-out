package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

/**
 * @Description DishFlavorMapper
 * @Author Lisheng Li
 * @Date 2025-08-09
 */
@Mapper
public interface DishFlavorMapper {
    void insertBatch(List<DishFlavor> flavors);


    /**
     * 删除口味数据
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id=#{id}")
    void deleteByDishId(String id);
}

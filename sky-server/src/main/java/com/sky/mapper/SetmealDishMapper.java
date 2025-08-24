package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description SetmealDishMapper
 * @Author Lisheng Li
 * @Date 2025-08-24
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 查找是否有关联菜品
     * @param id
     * @return
     */
    @Select("select dish_id from setmeal_dish where dish_id=#{id}")
    String getSetMealByDishId(String id);
}

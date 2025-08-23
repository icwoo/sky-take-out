package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description DishServiceImpl
 * @Author Lisheng Li
 * @Date 2025-08-09
 */
@Service//添加到容器
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired//忘记加service注解这里就会报红，显示：@Autowired需要被添加到bean里面
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品和口味数据
     *
     * @param dishDTO
     */
    @Transactional//开启事务处理，确保操作要么全成功，要么全失败。注：需要在启动类添加注解：@EnableTransactionManagement
    public void saveWithFlavor(DishDTO dishDTO) {
        log.info("新增菜品，{}", dishDTO);

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);//左边的属性拷贝给右边，相当于dish.setXX(dishDto.getXX)

        //向菜品表插入一条数据
        dishMapper.insert(dish);
        //获取insert语句增加完成后返回的主键
        Long dishId = dish.getId();

        //获取口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null && flavors.size() > 0) {

            //在插入之前遍历将口味信息添加主键id
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }

            //向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }

    }


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        //开始分页
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        Page page=dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }
}

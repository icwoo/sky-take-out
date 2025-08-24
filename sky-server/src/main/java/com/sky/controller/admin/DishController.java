package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description DishController
 * @Author Lisheng Li
 * @Date 2025-08-08
 */
@RestController
@Api("菜品相关接口")
@Slf4j
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;//没有实现类就会报红，实现这个接口就好了

    @PostMapping
    @ApiOperation("新增菜品")
        public Result save(@RequestBody DishDTO dishDTO) {//获取请求体JSON要加注解@RequestBody

        log.info("新增菜品，{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);

        return Result.success("");
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    //read 这里获取请求的参数不需要加@RequestBody注解，
    // 是因为他不是Json格式，他是在浏览器地址栏上以：XX?id=XX,name=XX这种query格式提交的
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){

        log.info("菜品分页查询：{}",dishPageQueryDTO);

        //执行查询
        PageResult pageResult= dishService.pageQuery(dishPageQueryDTO);

        //将结果返回给前端
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteDish(@RequestParam List<String> ids){

        log.info("进行删除菜品功能，ids:{}",ids);

        dishService.deleteDishBatch(ids);
        return Result.success();
    }
}

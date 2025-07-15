package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *这个@Override注解，我觉得加不加都是可以的，因为方法一般不会在接口里就实现，我猜的，就目前来说加不加都一样
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        //我写的，数据库密码手动改成md5的形式，前端输入密码，使用下面这句换成md5形式
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    public Employee save(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        //employee.setName(employeeDTO.getName());
        BeanUtils.copyProperties(employeeDTO,employee);//将employee的参数拷贝给dto

        //设置账号状态status
        employee.setStatus(StatusConstant.ENABLE);

        //设置用户密码，使用默认值123456（先md5加密后再比对
        //DigestUtils.md5DigestAsHex() 方法接受的参数类型是byte数组getbyte[]，而不是字符串，因为哈希算法本质是二进制计算
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //设置创建人and修改人的id
        //后期改成token解析获取当前人的id
        //employee.setCreateUser(20L);
        employee.setCreateUser(BaseContext.getCurrentId());//BaseContext使用threadLocal线程获取当前用户的id
        //employee.setUpdateUser(21L);
        employee.setUpdateUser(BaseContext.getCurrentId());//获取当前用户id
        employeeMapper.insert(employee);
        return employee;
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //pagehelper提供分页功能，获取页码and每页大小后，动态拼进sql语句。。应该是
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page=employeeMapper.pageQuery(employeePageQueryDTO);

        //要求返回的是PageResult，点开PageResult发现里面有tatal和records
        //意味着需要page返回带有tatal和records的内容就行
        long total = page.getTotal();
        List<Employee> records = page.getResult();//Result里面有records的内容

        return new PageResult(total,records);//用的lombook提供的构造方法创建的对象
    }

    /**
     * 启用禁用员工账号
     * 可以正常写，这里使用动态sql实现
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
/*        //new一个对象，set他的status和id，把对象传进去
        Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);
        employeeMapper.update(employee);*/

        //上面可行，下面是用builder实现，效果一样
        Employee employee=Employee.builder().status(status).id(id).build();
        employeeMapper.update(employee);
    }

    /**
     * 根据ID查询员工信息
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        Employee employee= employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //dto传给employee，因为update这个sql语句接收的是employee对象
        BeanUtils.copyProperties(employeeDTO,employee);//左边的传给右边的
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());//工具类，通过线程获取当前用户id
        employeeMapper.update(employee);
    }
}

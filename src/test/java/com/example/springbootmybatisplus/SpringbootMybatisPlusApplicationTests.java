package com.example.springbootmybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.springbootmybatisplus.bean.User;
import com.example.springbootmybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sun.nio.cs.ext.MacArabic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Phaser;

@SpringBootTest
class SpringbootMybatisPlusApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
    }

    public void testSelect() {
//        查询数据库中的所有数据，不需要where条件
        List<User> list = userMapper.selectList(null);
        for (User user:list
        ) {
            System.out.println(user);
        }
    }
    @Test//测试插入数据
    public void testInsert() {
        int result = userMapper.insert(new User(null, "范冰冰", 38, "女"));
        System.out.println(result);
    }
    @Test//测试更新数据 根据id跟新数据
    public void testUpdate() {
        int result = userMapper.updateById(new User(1, "军军", 25, "男"));
        System.out.println(result);
    }
    @Test//测试删除数据  根据id删除数据
    public void testDeleteById() {
        int result = userMapper.deleteById(8);
        System.out.println(result);
    }
    @Test//根据指定的字段查询数据
    public void testSelectByMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "大斌");
        List<User> users = userMapper.selectByMap(map);
        for (User user:users
        ) {
            System.out.println(user);
        }
    }

    @Test//模糊查询  like
    public void testSelectLike() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "大");
        queryWrapper.eq("sex", "女");
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user:users
        ) {
            System.out.println(user);
        }
    }
    @Test//between and 的用法
    public void testSelectBetweenAnd() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("age", 0, 28).eq("sex", "女");
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user:users
        ) {
            System.out.println(user);
        }
    }
    @Test//order by 的用法 gt() 大于   lt() 小于   eq()等于
    public void testSelectOrderBy() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("age", 100).orderByDesc("id","age");
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user:users
             ) {
            System.out.println(user);
        }
    }
    @Test//order by 的用法 gt() 大于   lt() 小于   eq()拼接
    //select * from user where age > 100 and sex = ("select sex from user where name='大斌'")
    public void testSelectTables() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("age",100 ).inSql("sex",
                "select sex from user where name='大斌'");
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user:users
        ) {
            System.out.println(user);
        }
    }
    @Test//（年龄<100 或者 性别为女）并且姓名为大姓
    public void testSelec() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.nested(i -> i.lt("age", 100).or().eq("sex", "女"))
                .likeRight("name", "大");
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user:users
        ) {
            System.out.println(user);
        }
    }
    @Test//select * from user where id=2,3,4,5
    public void testSelecIn() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", 2, 3, 4, 5);
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user:users
        ) {
            System.out.println(user);
        }
    }
    @Test//查询age为22岁的用户的名称和id 结果中会出现有null的字段
    public void testSelectDemo01() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name").eq("age", 22);
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user:users
        ) {
            System.out.println(user);
        }
    }
    @Test//select * from user where name='大熊' and age = null and sex = '男'
    public void testSelectAllEq() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "大熊");
        map.put("age", null);
        map.put("sex","男");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.allEq(parmas); 不会忽略null值
        queryWrapper.allEq(map, false);//false时会自动将为null的数据去除
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user:users
        ) {
            System.out.println(user);
        }
    }
    @Test//查询age为22岁的用户的名称和id  不会出现有null值的字段
    public void testSelectDemo02() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name").eq("age", 22);
        List<Map<String, Object>> users = userMapper.selectMaps(queryWrapper);
        for ( Map<String, Object> user:users
        ) {
            System.out.println(user);
        }
    }
    @Test//条件：根据性别 分组，查询每组中最大年龄和最小年龄并且展现其信息
    //select name from user group by sex having max(age) and min(age)
    public void testSelectDemo03() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("max(age)max_age", "min(age)min_age", "avg(age)avg_age", "sex")
                .groupBy("sex")
                .having("sex in({0},{1})","男","女");
        List<Map<String, Object>> users = userMapper.selectMaps(queryWrapper);
        for ( Map<String, Object> user:users
        ) {
            System.out.println(user);
        }
    }
    @Test//只返回一列数据时 使用Ojbs
    public void testSelectOjbs() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("age",100);
        List<Object> users = userMapper.selectObjs(queryWrapper);
        for ( Object user:users
        ) {
            System.out.println(user);
        }
    }
    @Test//查询年龄为21岁的记录数
    public void testSelectCount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("age", 21);
        Integer result = userMapper.selectCount(queryWrapper);
        System.out.println("年龄为21岁的记录数为="+ result);
    }
    @Test//查询name 为大斌的一个数据
    public void testSelectOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name","大斌");
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }
    @Test//将name=null的数据进行修改
    public void testUpdateNull() {
        User userUpdate = new User();
        userUpdate.setName("杨颖").setAge(30);
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.isNull("age");
        int rows = userMapper.update(userUpdate, userUpdateWrapper);
        System.out.println(rows);
    }
    @Test//批量删除
    public void testDeletes() {
        ArrayList<Integer> idList = new ArrayList<>();
        idList.add(2);
        idList.add(3);
        int rows = userMapper.deleteBatchIds(idList);
        System.out.println(rows);
    }
    @Test//根据条件删除用户信息
    public void testDeletByUser() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.isNull("name");
        int rows = userMapper.delete(userQueryWrapper);
        System.out.println(rows);
    }
    @Test//根据map条件删除数据
    public void testDeletMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "范冰冰");
        int rows = userMapper.deleteByMap(map);
        System.out.println(rows);
    }
}

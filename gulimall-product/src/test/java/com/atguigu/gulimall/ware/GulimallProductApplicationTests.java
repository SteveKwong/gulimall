package com.atguigu.gulimall.ware;

import com.atguigu.gulimall.product.GulimallProductApplication;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = GulimallProductApplication.class)
class GulimallProductApplicationTests {

    @Autowired
    private AttrGroupService attrGroupService;

    @Test
    void contextLoads() {
        List<AttrGroupEntity> list = attrGroupService.list();
        System.out.println(list);
        System.out.println("你好");
    }

}

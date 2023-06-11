package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 实现查询树形分类
     *
     * @return 返回的结果集
     */
    @Override
    public List<CategoryEntity> queryWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        // 通过递归的方式,查询到所有的子菜单
        List<CategoryEntity> level1Menu = categoryEntities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map((categoryEntity) -> {
                    // 通过方法来获取集合中每个子类中的项目
                    categoryEntity.setChildrenList(getChilrenList(categoryEntity, categoryEntities));
                    categoryEntity.setSort(categoryEntity.getSort() == null ? 0 : categoryEntity.getSort());
                    return categoryEntity;
                }).sorted(Comparator.comparingInt(CategoryEntity::getSort)).collect(Collectors.toList());
        return level1Menu;
    }

    /**
     * 该方法用来递归子项,查询每一个该项目中的子项
     *
     * @param category             父项
     * @param categoryEntities 全部的集合
     * @return 子项集合
     */
    private List<CategoryEntity> getChilrenList(CategoryEntity category, List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> childrenList = categoryEntities.stream()
                .filter(categoryEntity -> category.getCatId().equals(categoryEntity.getParentCid()))
                .map((categoryEntity) -> {
                    // 通过方法来获取集合中每个子类中的项目
                    categoryEntity.setChildrenList(getChilrenList(categoryEntity, categoryEntities));
                    categoryEntity.setSort(categoryEntity.getSort() == null ? 0 : categoryEntity.getSort());
                    return categoryEntity;
                    // fixme 标记了一个NPE 在这里,说明get不到这个sort字段
                }).sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());
        return childrenList;
    }

}
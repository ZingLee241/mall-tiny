package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.model.UmsResourceCategory;
import com.macro.mall.tiny.modules.ums.service.UmsResourceCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台资源分类管理Controller
 * Created by macro on 2020/2/5.
 */
@RestController
@Api(tags = "UmsResourceCategoryController")
@Tag(name = "UmsResourceCategoryController",description = "后台资源分类管理")
@RequestMapping("/resourceCategory")
public class UmsResourceCategoryController {
    @Resource
    private UmsResourceCategoryService resourceCategoryService;

    @ApiOperation("查询所有后台资源分类")
    @GetMapping(value = "/listAll")
    public List<UmsResourceCategory> listAll() {
        return resourceCategoryService.listAll();
    }

    @ApiOperation("添加后台资源分类")
    @PostMapping(value = "/create")
    public boolean create(@RequestBody UmsResourceCategory umsResourceCategory) {
        return resourceCategoryService.create(umsResourceCategory);
    }

    @ApiOperation("修改后台资源分类")
    @PostMapping(value = "/update/{id}")
    public boolean update(@PathVariable Long id,
                               @RequestBody UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setId(id);
        return resourceCategoryService.updateById(umsResourceCategory);
    }

    @ApiOperation("根据ID删除后台资源")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        boolean success = resourceCategoryService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }
}

package com.macro.mall.tiny.modules.ums.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.dto.UmsMenuNode;
import com.macro.mall.tiny.modules.ums.model.UmsMenu;
import com.macro.mall.tiny.modules.ums.service.UmsMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台菜单管理Controller
 * Created by macro on 2020/2/4.
 */
@RestController
@Api(tags = "UmsMenuController")
@Tag(name = "UmsMenuController",description = "后台菜单管理")
@RequestMapping("/menu")
public class UmsMenuController {

    @Resource
    private UmsMenuService menuService;

    @ApiOperation("添加后台菜单")
    @PostMapping("/create")
    public boolean create(@RequestBody UmsMenu umsMenu) {
        return  menuService.create(umsMenu);
    }

    @ApiOperation("修改后台菜单")
    @PostMapping("/update/{id}")
    public boolean update(@PathVariable Long id,
                               @RequestBody UmsMenu umsMenu) {
        return menuService.update(id, umsMenu);
    }

    @ApiOperation("根据ID获取菜单详情")
    @PostMapping("/{id}")
    public UmsMenu getItem(@PathVariable Long id) {
        return menuService.getById(id);
    }

    @ApiOperation("根据ID删除后台菜单")
    @PostMapping("/delete/{id}")
    
    public boolean delete(@PathVariable Long id) {
        return menuService.removeById(id);
    }

    @ApiOperation("分页查询后台菜单")
    @GetMapping(value = "/list/{parentId}")
    public CommonPage<UmsMenu> list(@PathVariable Long parentId,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<UmsMenu> menuList = menuService.list(parentId, pageSize, pageNum);
        return CommonPage.restPage(menuList);
    }

    @ApiOperation("树形结构返回所有菜单列表")
    @GetMapping(value = "/treeList")
    public List<UmsMenuNode> treeList() {
        return  menuService.treeList();
    }

    @ApiOperation("修改菜单显示状态")
    @PostMapping(value = "/updateHidden/{id}")
    public boolean updateHidden(@PathVariable Long id, Integer hidden) {
        return menuService.updateHidden(id, hidden);
    }
}

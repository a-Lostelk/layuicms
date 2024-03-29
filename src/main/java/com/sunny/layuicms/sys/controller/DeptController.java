package com.sunny.layuicms.sys.controller;


import java.util.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sunny.layuicms.sys.common.DataGridView;
import com.sunny.layuicms.sys.common.TreeNode;
import com.sunny.layuicms.sys.dto.ResultDTO;
import com.sunny.layuicms.sys.entity.Dept;
import com.sunny.layuicms.sys.service.DeptService;
import com.sunny.layuicms.sys.vo.DeptVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: fang
 * @Date: 2019/11/7
 * <p>
 * 登录控制器
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;


    /**
     * 加载部门管理左边的部门树的json
     */
    @RequestMapping("loadDeptManagerLeftTreeJson")
    public DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo) {
        List<Dept> list = this.deptService.list();
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Dept dept : list) {
            Boolean spread = dept.getOpen() == 1 ? true : false;
            treeNodes.add(new TreeNode(dept.getId(), dept.getPid(), dept.getTitle(), spread));
        }
        return new DataGridView(treeNodes);
    }

    /**
     * 查询
     */
    @RequestMapping("loadAllDept")
    public DataGridView loadAllDept(DeptVo deptVo) {
        IPage<Dept> page = new Page<>(deptVo.getPage(), deptVo.getLimit());
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getTitle()), "title", deptVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getAddress()), "address", deptVo.getAddress());
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getRemark()), "remark", deptVo.getRemark());
        queryWrapper.eq(deptVo.getId() != null, "id", deptVo.getId()).or().eq(deptVo.getId() != null, "pid", deptVo.getId());
        queryWrapper.orderByAsc("ordernum");
        this.deptService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 加载最大的排序码
     *
     * @return
     */
    @RequestMapping("loadDeptMaxOrderNum")
    public Map<String, Object> loadDeptMaxOrderNum() {
        Map<String, Object> map = new HashMap<String, Object>();

        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<Dept> page = new Page<>(1, 1);
        List<Dept> list = this.deptService.page(page, queryWrapper).getRecords();
        if (list.size() > 0) {
            map.put("value", list.get(0).getOrdernum() + 1);
        } else {
            map.put("value", 1);
        }
        return map;
    }


    /**
     * 添加
     *
     * @param deptVo
     * @return
     */
    @RequestMapping("addDept")
    public ResultDTO addDept(DeptVo deptVo) {
        try {
            deptVo.setCreatetime(new Date());
            this.deptService.save(deptVo);
            return ResultDTO.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.ADD_ERROR;
        }
    }


    /**
     * 修改
     *
     * @param deptVo
     * @return
     */
    @RequestMapping("updateDept")
    public ResultDTO updateDept(DeptVo deptVo) {
        try {
            this.deptService.updateById(deptVo);
            return ResultDTO.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.UPDATE_ERROR;
        }
    }


    /**
     * 查询当前的ID的部门有没有子部门
     */
    @RequestMapping("checkDeptHasChildrenNode")
    public Map<String, Object> checkDeptHasChildrenNode(DeptVo deptVo) {
        Map<String, Object> map = new HashMap<String, Object>();

        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", deptVo.getId());
        List<Dept> list = this.deptService.list(queryWrapper);
        if (list.size() > 0) {
            map.put("value", true);
        } else {
            map.put("value", false);
        }
        return map;
    }

    /**
     * 删除
     *
     * @param deptVo
     * @return
     */
    @RequestMapping("deleteDept")
    public ResultDTO deleteDept(DeptVo deptVo) {
        try {
            this.deptService.removeById(deptVo.getId());
            return ResultDTO.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.DELETE_FAILED;
        }
    }
}


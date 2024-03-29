package com.sunny.layuicms.sys.mapper;

import com.sunny.layuicms.sys.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sunny
 * @since 2019-11-14
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 查询最大的ordernum
     *
     * @return
     */
    List<Dept> selectMaxOrderNum();
}

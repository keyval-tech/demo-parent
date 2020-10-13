package com.kovizone.demo.repository;

import com.kovizone.demo.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Integer> {

    /**
     * 更新用户名
     *
     * @param id       ID
     * @param username 新用户
     * @return 更新数
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("UPDATE sys_user SET username = :username WHERE id = :id")
    Integer updateUsername(@Param("id") Integer id, @Param("username") String username);

}

package com.kovizone.demo;

import com.kovizone.demo.model.SysUser;
import com.kovizone.demo.repository.SysUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@SpringBootTest
public class ApiApplicationTests {

    @Autowired
    SysUserRepository sysUserRepository;

    @Test
    public void test() {
        System.out.println(sysUserRepository.findAll());
    }

    @Test
    public void test1() {
        SysUser probe = new SysUser();
        probe.setUsername("test");
        Example<SysUser> example = Example.of(probe);
        SysUser sysUser = sysUserRepository.findOne(example).orElse(null);
        if (sysUser != null) {
            sysUser.setEncodePassword("88888888");
            System.out.println(sysUserRepository.save(sysUser));
        }

        System.out.println(sysUserRepository.updateUsername(1, "admin"));
    }
}

package ink.squidkill.test.domain;

import ink.squidkill.domain.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/21 23:05
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleTest {

    @Resource
    RoleService roleService;


    @Test
    public void test() {
        roleService.assignRolesAndGroups("dGaWw9",false);
    }

}

package com.prateek.web.springrestdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@EnabledIf(value = "#{{'default','localmysql'}.contains(environment.getActiveProfiles()[0])}", loadContext = true)
@SpringBootTest
class SpringRestDemoApplicationTests {

	@Test
	void contextLoads() {
	}

}

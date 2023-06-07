package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ShareItTests {

	@Test
	void contextLoads() {

	}
}

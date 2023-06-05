package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.ItemService.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ShareItTests {

	private final UserService userService;

	private final ItemService itemService;

	@Test
	public void test() {
		User user = userService.createUser(User.builder()
				.name("Викинг")
				.email("viking@mail.com")
				.build());
		Assertions.assertNotNull(user);

		User newUser = userService.findUser(1L);
		Assertions.assertNotNull(newUser);

		List<User> list = userService.findAllUsers();
		Assertions.assertNotNull(list);

		User newNewUser = userService.updateUser(1L, User.builder().name("viking").build());
		Assertions.assertNotNull(newNewUser);

		ItemDto itemDto = itemService.createItem(1L, ItemDto.builder().name("оружие")
						.available(true)
						.description("могучее")
						.build());
		Assertions.assertNotNull(itemDto);

		ItemDto newItem = itemService.findItem(1L, 1L);
		Assertions.assertNotNull(newItem);

		List<ItemDto> listItem = itemService.findAllItemByUser(1L);
		Assertions.assertNotNull(listItem);

		ItemDto upItem = itemService.updateItem(1L, 1L, ItemDto.builder().name("огромное оружие").build());
		Assertions.assertNotNull(upItem);

		List<ItemDto> newList = itemService.search(1L, "оружие");
		Assertions.assertNotNull(newList);
	}

}

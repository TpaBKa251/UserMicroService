package hostel.usermicroservice.controller;

import hostel.usermicroservice.dto.request.RegisterDto;
import hostel.usermicroservice.dto.response.UserDto;
import hostel.usermicroservice.dto.response.UserShortDto;
import hostel.usermicroservice.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hostel/users")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    public UserDto createUser(@RequestBody @Valid RegisterDto body) {
        return userService.registerUser(body);
    }

    @GetMapping("/profile")
    public UserDto getProfile(Authentication authentication) {
        return userService.getUserResponseByAuthentication(authentication);
    }

    @GetMapping("/byName")
    public UserShortDto getUserByName(Authentication authentication, String name) {
        return userService.findUserByUsername(name, authentication);
    }
}

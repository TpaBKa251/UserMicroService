package hostel.usermicroservice.service.impl;

import hostel.usermicroservice.dto.request.RegisterDto;
import hostel.usermicroservice.dto.response.UserDto;
import hostel.usermicroservice.dto.response.UserShortDto;
import hostel.usermicroservice.entity.User;
import hostel.usermicroservice.jwt.JwtTokenUtils;
import hostel.usermicroservice.mapper.UserMapper;
import hostel.usermicroservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public UserDto registerUser(RegisterDto body) {
        User user = UserMapper.mapRegisterBodyToUser(body);
        userRepository.save(user);

        return UserMapper.mapUserToResponse(user);
    }

    public UserDto getUserResponseByAuthentication(Authentication authentication) {
        UUID id = jwtTokenUtils.getUserIdFromAuthentication(authentication);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));

        return UserMapper.mapUserToResponse(user);
    }

    public User getUserByAuthentication(Authentication authentication) {
        UUID id = jwtTokenUtils.getUserIdFromAuthentication(authentication);

        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));
    }

    public User getUserById(UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + uuid + " not found"));
    }

    public UserShortDto findUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));
        return UserMapper.mapUserInfoToResponse(user);
    }

    public UserShortDto findUserByUsername(String username, Authentication authentication) {
        UUID id = jwtTokenUtils.getUserIdFromAuthentication(authentication);
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));

        if (user != null) {
            String[] name = username.split(" ");

            if (name.length != 3) {
                throw new UsernameNotFoundException("Invalid username");
            }

            return UserMapper.mapUserInfoToResponse(userRepository.findByFirstNameAndLastNameAndMiddleName(name[0], name[1], name[2]).orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found")));
        }

        throw new UsernameNotFoundException("User with ID: " + id + " not found");
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        String[] name = userName.split(" ");
        return userRepository.findByFirstNameAndLastNameAndMiddleName(name[0], name[1], name[2])
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

package com.example.demo.service;

import com.example.demo.consts.UserActiveStatus;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
//@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    UserRepository userRepository;

    public User getUser(String email) {
        return userRepository.findByEmail(email)//.orElseThrow()
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER, "존재하지 않는 유저입니다."));
    }

    public User getActiveUser(String email) {
        User user = getUser(email);
        if (!user.isActiveUser())
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "비활성화 유저입니다. 다시 로그인 해주세요");
        return user;
    }

    public User addUser(String email) {
        User user = User.builder()
                .email(email)
                .userActiveStatus(UserActiveStatus.ACTIVE)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = getUser(email);
        user.deactivateUser();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void activateUser(User user) {
        user.activateUser();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER, "존재하지 않는 유저입니다."));
    }

    @Transactional // 트랜젝셕
    public User modifyUser(UserDTO userDTO) {
//        if (userDTO.getName() == null || userDTO.getName().equals(""))
        if (userDTO.getUserEntryNo() == null || userDTO.getName().equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 id가 비었습니다.");

        Optional<User> userOptional = userRepository.findById(userDTO.getUserEntryNo());
        if (!userOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다.");
        log.info("userOptional: "+userOptional);

        // 넘어온것만 수정할 수 있을까?
        User user = User.builder()
                .userEntryNo(userOptional.get().getUserEntryNo())
                .email(userOptional.get().getEmail())
                .registeredDate(userOptional.get().getRegisteredDate())
                .userActiveStatus(userOptional.get().getUserActiveStatus())
                .name(userDTO.getName())
                .phone(userDTO.getPhone())
//                . .name(memoDTO.getName())
//                .content(memoDTO.getContent())
//                .category(memoOptional.get().getCategory())
                .build();
        return userRepository.save(user);
    }

//    @Transactional
//    public Long save(UserDTO userDTO){
//        return userRepository.save(userDTO.toEntiry()).getId();
//    }

}
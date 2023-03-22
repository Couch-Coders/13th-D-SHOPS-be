package com.example.demo.service;

import java.io.IOException;
import java.util.Optional;

import com.example.demo.consts.UserActiveStatus;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Address;
import com.example.demo.entity.Company;
import com.example.demo.entity.Image;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
import com.google.cloud.storage.Bucket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private final Bucket bucket;

//    public List<User> getAll(){
//        return userRepository.findAllNotDeleted1();
//    }

    public User getUser(String email) {
        log.info("====================getUser====================");
        return userRepository.findByEmail(email)//.orElseThrow()
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER, "존재하지 않는 유저입니다."));
    }

    public User getActiveUser(String email) {
        log.info("====================getActiveUser====================");
        User user = getUser(email);
        if (!user.isActiveUser())
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "비활성화 유저입니다. 다시 로그인 해주세요");
        return user;
    }

    public User addUser(String email) {
        log.info("====================addUser====================");
        User user = User.builder()
                .email(email)
                .company(null)
                .userActiveStatus(UserActiveStatus.ACTIVE)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        log.info("====================deleteUser====================");
        User user = getUser(email);
        user.deactivateUser();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("====================UserDetails====================");
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void activateUser(User user) {
        log.info("====================activateUser====================");
        user.activateUser();
    }

    public User getUser(Long id) {
        log.info("====================getUser====================");
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER, "존재하지 않는 유저입니다."));
    }

    @Transactional // 트랜젝셕
    public User modifyUser(UserDTO userDTO) {
//        if (userDTO.getName() == null || userDTO.getName().equals(""))
        if (userDTO.getSeq() == null || userDTO.getName().equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 id가 비었습니다.");

        Optional<User> userOptional = userRepository.findById(userDTO.getSeq());
        if (!userOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다.");
        log.info("userOptional: "+userOptional);

        // company 처리
        Company existingCompany = userOptional.get().getCompany();
        Company updatedCompany = userDTO.getCompany();

        if (existingCompany == null && updatedCompany != null) {
            log.info("Company Enter1");
            // 없으면 받은거 저장
        }
        else if (existingCompany != null && updatedCompany != null) {
            // 기존것이 있으면 수정해라
            log.info("Company Enter2");
            //입력이 없으면 수정 안하게
            if(updatedCompany.getName() != null)
                existingCompany.setName(updatedCompany.getName());
            if(updatedCompany.getEmail() != null)
                existingCompany.setEmail(updatedCompany.getEmail());
            if(updatedCompany.getPhone() != null)
                existingCompany.setPhone(updatedCompany.getPhone());

            // address 처리
            Address existingAddress = existingCompany.getAddress();
            Address updatedAddress = updatedCompany.getAddress();


            if (existingAddress == null && updatedAddress != null) {
                log.info("Address Enter1");
            }
            else if (existingAddress != null && updatedAddress != null) {
                // 기존것이 있으면 수정해라
                log.info("Address Enter2");
                //입력이 없으면 수정 안하게
                if(updatedAddress.getName() != null)
                    existingAddress.setName(updatedAddress.getName());
                if(updatedAddress.getPost_code() != null)
                    existingAddress.setPost_code(updatedAddress.getPost_code());
                if(updatedAddress.getAddress() != null)
                    existingAddress.setAddress(updatedAddress.getAddress());
                if(updatedAddress.getExtra() != null)
                    existingAddress.setExtra(updatedAddress.getExtra());
                if(updatedAddress.getDetail() != null)
                    existingAddress.setDetail(updatedAddress.getDetail());
                if(updatedAddress.getLocation_x() != null)
                    existingAddress.setLocation_x(updatedAddress.getLocation_x());
                if(updatedAddress.getLocation_y() != null)
                    existingAddress.setLocation_y(updatedAddress.getLocation_y());

                existingCompany.setAddress(existingAddress);
            }

            userDTO.setCompany(existingCompany);
        }

        // user 처리
        User existingUser = userOptional.get();
        // 넘어온것만 수정할 수 있을까?
        // 성공
        // 조금더 쉬운 방법 없을까?
//        if(userDTO.getName() == null)
//            userDTO.setName(existingUser.getName());
//        if(userDTO.getPhone() == null)
//            userDTO.setPhone(existingUser.getPhone());

//        User user = User.builder()
//                .seq(userOptional.get().getSeq())
//                .email(userOptional.get().getEmail())
//                .registeredDate(userOptional.get().getRegisteredDate())
//                .userActiveStatus(userOptional.get().getUserActiveStatus())
//                .name(userDTO.getName())
//                .phone(userDTO.getPhone())
//                .company(userDTO.getCompany())
//                .build();
//        return userRepository.save(user);

        User myexistingUser = userRepository.findById(userDTO.getSeq())
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다."));

        if(userDTO.getName() != null)
            myexistingUser.setName(userDTO.getName());
        if(userDTO.getPhone() != null)
            myexistingUser.setPhone(userDTO.getPhone());
        if(userDTO.getCompany() != null)
            myexistingUser.setCompany(userDTO.getCompany());


        return userRepository.save(myexistingUser);
    }

    public Image uploadImage(Image image,byte[] files) {
        // File 저장 위치 선업
        String blob = "users/"+image.getUser_seq()+"/images/"+image.getName();
        image.setUrl(blob);
        log.info("url"+blob);

        try {
            // 이미 존재하면 파일 삭제
            if(bucket.get(blob) != null) {
                log.info("존재");
                bucket.get(blob).delete();
            }
            // 파일을 Bucket에 저장
//            bucket.create(blob,files,"image/jpg");
            bucket.create(blob,files,"multipart/form-data");
            log.info("저장");
            // DB에 유저 정보 업데이트 (Profile 이미지 위치 추가)
            image.setUrl("/users/"+image.getUrl()+"/profile");
            imageRepository.save(image);
            return image;

        } catch (CustomException e) {
            log.error(image.getUrl() + " profile upload faild", e);
            //throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "IMAGE_UPLOAD_FAILED");
        }
    }

    public byte[] getProfile(String uid, String fileName) {
        return bucket.get("users/"+uid+"/images/"+fileName).getContent();
    }
}
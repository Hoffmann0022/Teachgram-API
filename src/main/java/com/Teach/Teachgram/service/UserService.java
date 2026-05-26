package com.Teach.Teachgram.service;

import com.Teach.Teachgram.config.JwtService;
import com.Teach.Teachgram.dto.UserDTO.*;
import com.Teach.Teachgram.exception.BusinessException;
import com.Teach.Teachgram.exception.ResourceNotFoundException;
import com.Teach.Teachgram.model.User;
import com.Teach.Teachgram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    @Transactional
    public UserResponse signup(SignupRequest request) {

        if (!request.mail().contains("@")) {
            throw new BusinessException("E-mail inválido. Deve conter '@'.");
        }

        if (userRepository.existsByUserNameAndDeletedFalse(request.userName())) {
            throw new BusinessException("Username '" + request.userName() + "' já está em uso.");
        }

        if (userRepository.existsByMailAndDeletedFalse(request.mail())) {
            throw new BusinessException("E-mail '" + request.mail() + "' já está cadastrado.");
        }

        if (request.phone() != null && !request.phone().isBlank()
                && userRepository.existsByPhoneAndDeletedFalse(request.phone())) {
            throw new BusinessException("Telefone '" + request.phone() + "' já está cadastrado.");
        }

        User user = User.builder()
                .name(request.name())
                .userName(request.userName())
                .mail(request.mail())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .profileLink(request.profileLink())
                .bio(request.bio())
                .deleted(false)
                .build();

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.userName(), request.password()));
        } catch (BadCredentialsException e) {
            throw new BusinessException("Username ou senha incorretos.");
        }

        User user = userRepository.findByUserNameAndDeletedFalse(request.userName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        var userDetails = userDetailsService.loadUserByUsername(request.userName());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token, toResponse(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listAll() {
        return userRepository.findAllByDeletedFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = findActiveUser(id);
        return toResponse(user);
    }

    @Transactional
    public UserResponse update(Long id, UpdateRequest request) {
        User user = findActiveUser(id);

        if (request.userName() != null && !request.userName().equals(user.getUserName())
                && userRepository.existsByUserNameAndDeletedFalse(request.userName())) {
            throw new BusinessException("Username '" + request.userName() + "' já está em uso.");
        }

        if (request.mail() != null && !request.mail().equals(user.getMail())) {
            if (!request.mail().contains("@")) {
                throw new BusinessException("E-mail inválido. Deve conter '@'.");
            }
            if (userRepository.existsByMailAndDeletedFalse(request.mail())) {
                throw new BusinessException("E-mail '" + request.mail() + "' já está cadastrado.");
            }
        }

        if (request.phone() != null && !request.phone().equals(user.getPhone())
                && userRepository.existsByPhoneAndDeletedFalse(request.phone())) {
            throw new BusinessException("Telefone '" + request.phone() + "' já está cadastrado.");
        }

        if (request.name() != null)        user.setName(request.name());
        if (request.userName() != null)    user.setUserName(request.userName());
        if (request.mail() != null)        user.setMail(request.mail());
        if (request.phone() != null)       user.setPhone(request.phone());
        if (request.profileLink() != null) user.setProfileLink(request.profileLink());
        if (request.bio() != null)         user.setBio(request.bio());

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = findActiveUser(id);
        user.setDeleted(true);
        userRepository.save(user);
    }

    private User findActiveUser(Long id) {
        return userRepository.findById(id)
                .filter(u -> !u.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário com id " + id + " não encontrado."));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUserName(),
                user.getPhone(),
                user.getMail(),
                user.getProfileLink(),
                user.getBio(),
                user.getDeleted(),
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null,
                user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null
        );
    }
}

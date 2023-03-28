package com.example.telproject.service;

import com.example.telproject.dto.ClientDTO;
import com.example.telproject.email.EmailSender;
import com.example.telproject.entity.Client;
import com.example.telproject.entity.ConfirmationToken;
import com.example.telproject.entity.Manager;
import com.example.telproject.entity.enums.ClientStatus;
import com.example.telproject.mapper.ClientMapper;
import com.example.telproject.repository.ClientRepository;
import com.example.telproject.repository.ManagerRepository;
import com.example.telproject.security.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    private final static String USER_NOT_FOUND = "user with email %s not found";
    private final ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clientRepository.
                findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.
                        format(USER_NOT_FOUND, email)));
    }


    public List<ClientDTO> getClients() {
        return clientMapper.listToDTO(clientRepository.findAll());
    }

    public List<ClientDTO> findClientsByName(String first_name, String last_name) {
        List<ClientDTO> clients = clientMapper.listToDTO(
                clientRepository.
                        findClientByName(first_name, last_name));

        if (clients.isEmpty()) {
            throw new IllegalStateException("Users with name: " + first_name + " " + last_name + " doesn't exist in the DataBase");
        }
        return clients;
    }

    public String signUpClient(Client client) {
        Optional<Client> newClient = clientRepository.findByEmail(client.getEmail());
        if (newClient.isPresent()) {
            if (newClient.get().getStatus().equals(ClientStatus.ACTIVE)) {
                throw new IllegalStateException("User already active");
            } else {
                System.out.println("Client in sign up " + client);
                ConfirmationToken confirmationToken = new ConfirmationToken(client);
                confirmationTokenService.saveConfirmationToken(confirmationToken);
                System.out.println("\n\n\n TOKEN IN sign Up" + confirmationToken);
                return confirmationToken.getToken();
            }
        }
        String encodedPassword = bCryptPasswordEncoder.encode(client.getPassword());
        client.setPassword(encodedPassword);
        clientRepository.save(client);

        ConfirmationToken confirmationToken = new ConfirmationToken(client);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return confirmationToken.getToken();
    }
    @Transactional
    public void activeClient(String email) {
        Client client = clientRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("Tht client not found"));
        client.setStatus(ClientStatus.ACTIVE);
        clientRepository.save(client);
//        clientRepository.activeClient(ClientStatus.ACTIVE.getValue(), email);
    }

    public String register(RequestClient request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) throw new IllegalStateException("Email is not valid");

        String token;

        Optional<Client> findClientByEmail = clientRepository.findByEmail(request.getEmail());
        if (findClientByEmail.isPresent()) {
            token = signUpClient(findClientByEmail.get());
        } else {
            Manager manager = managerRepository.findById(request.getManager_id()).orElseThrow(() -> new IllegalStateException("Manager not found"));
            Client client = new Client(
                    manager,
                    request.getFirst_name(),
                    request.getLast_name(),
                    request.getEmail(),
                    request.getBirth_date(),
                    request.getPassword()
            );

            System.out.println("\n\n\n\n\n Sign up client " + client + "\n\n\n\n");
            System.out.println(client.getManager() + "\n\n\n");
            token = signUpClient(client);
        }
        String link = "http://localhost:8080/api/v1/client/confirm?token=" + token;
        emailSender.send(request.getEmail(), buildEmail(request.getFirst_name(), link));
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() -> new IllegalStateException("token not found"));
        if (confirmationToken.getConfirmed_at() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpired_at();

        if (expiredAt.isBefore(LocalDateTime.now())) throw new IllegalStateException("Token expired");

        confirmationTokenService.setConfirmedAt(token);
        activeClient(confirmationToken.getClient().getEmail());
        return "confirmed";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
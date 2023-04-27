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
import com.example.telproject.security.CheckingEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final CheckingEmail checkingEmail;
    private final ConfirmationTokenService confirmationTokenService;

    private final static String USER_NOT_FOUND = "User with %s not found";
    private final ManagerRepository managerRepository;

    /**
     * Loads a User by their email address.
     *
     * @param email the email address of the User to load.
     * @return a UserDetails object representing the User.
     * @throws UsernameNotFoundException if no User was found with the given email address.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clientRepository.
                findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.
                        format(USER_NOT_FOUND, email)));
    }


    /**
     * Retrieves a list of all Clients by page.
     *
     * @return a list of ClientDTO objects.
     */
    public Page<ClientDTO> getClientsByPage(Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(pageable);
        return clients.map(clientMapper::toDto);
    }

    /**
     * Retrieves a list of all Clients.
     *
     * @return a list of ClientDTO objects.
     */
    public List<ClientDTO> getClients() {
        return clientMapper.listToDTO(clientRepository.findAll());
    }


    /**
     * Searches for Clients with the given first and last name.
     *
     * @param first_name the first name to search for.
     * @param last_name  the last name to search for.
     * @return a list of ClientDTO objects representing the matching Clients.
     * @throws IllegalStateException if no Clients were found with the given name.
     */
    public List<ClientDTO> findClientsByName(String first_name, String last_name) {
        List<ClientDTO> clients = clientMapper.listToDTO(
                clientRepository.
                        findClientByName(first_name, last_name));

        if (clients.isEmpty()) {
            throw new IllegalStateException(String.format(USER_NOT_FOUND, "name " + first_name + " " + last_name));
        }
        return clients;
    }

    /**
     * Registers a new client by creating a new user account and generating a confirmation token for email verification.
     * <p>
     * If a client with the same email address already exists and their account is active, then an IllegalStateException is thrown.
     * <p>
     * If the account is not active, then the previous confirmation token is deleted and a new one is generated for the client.
     * <p>
     * The newly created confirmation token is returned to be sent to the client via email.
     *
     * @param client the client object containing client information to be registered
     * @return a string representation of the newly generated confirmation token
     * @throws IllegalStateException if a client with the same email address already exists and their account is active
     */
    @Transactional
    public String signUpClient(Client client) {
        Optional<Client> newClient = clientRepository.findByEmail(client.getEmail());
        if (newClient.isPresent()) {
            if (newClient.get().getStatus().equals(ClientStatus.ACTIVE)) {
                throw new IllegalStateException("Client with this email already registered");
            } else {
                confirmationTokenService.deleteConfirmationToken(client);
                ConfirmationToken confirmationToken = new ConfirmationToken(client);
                confirmationTokenService.saveConfirmationToken(confirmationToken);
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

    /**
     * Updates the status of a client's account to active, given the email address of the client.
     * If no client with the provided email address is found, an IllegalStateException is thrown.
     *
     * @param email the email address of the client whose account is to be activated
     * @throws IllegalStateException if no client with the provided email address is found
     */
    @Transactional
    public void activeClient(String email) {
        Client client = clientRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("The client not found"));
        client.setStatus(ClientStatus.ACTIVE);
        clientRepository.save(client);
    }

    /**
     * Registers a new client by creating a new user account and generating a confirmation token for email verification.
     * <p>
     * If a client with the same email address already exists and their account is not active, a new confirmation token is generated for the client.
     * <p>
     * If the client is new, a new account is created with the provided details and a confirmation token is generated.
     * <p>
     * The confirmation token is sent to the client via email.
     *
     * @param request   the client object containing client information to be registered
     * @param managerId the manager ID who is responsible for registering the client
     * @return a string representation of the newly generated confirmation token
     */
    public String register(Client request, Long managerId) {
        boolean isValidEmail = checkingEmail.test(request.getEmail());
        if (!isValidEmail) throw new IllegalStateException("Email is not valid");

        String token;

        Optional<Client> findClientByEmail = clientRepository.findByEmail(request.getEmail());
        if (findClientByEmail.isPresent()) {
            token = signUpClient(findClientByEmail.get());
        } else {
            Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new IllegalStateException("Manager not found"));
            Client client = new Client(
                    manager,
                    request.getFirst_name(),
                    request.getLast_name(),
                    request.getEmail(),
                    request.getBirth_date(),
                    request.getPassword()
            );
            token = signUpClient(client);
        }
        String link = "http://localhost:8080/api/v1/client/confirm?token=" + token;
        emailSender.send(request.getEmail(), buildEmail(request.getFirst_name(), link));
        return token;
    }

    /**
     * Confirms a client's email by validating the confirmation token provided in the request.
     * <p>
     * If the token does not exist, an IllegalStateException is thrown.
     * <p>
     * If the token has already been confirmed, an IllegalStateException is thrown.
     * <p>
     * If the token has expired, an IllegalStateException is thrown.
     * <p>
     * Otherwise, the client's account is set to active and "Confirmed" is returned.
     *
     * @param token the confirmation token provided in the request
     * @return a string indicating that the email has been confirmed
     * @throws IllegalStateException if the token does not exist, has already been confirmed, or has expired
     */
    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() -> new IllegalStateException("Token not found"));
        if (confirmationToken.getConfirmed_at() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpired_at();

        if (expiredAt.isBefore(LocalDateTime.now())) throw new IllegalStateException("Token expired");

        confirmationTokenService.setConfirmedAt(token);
        activeClient(confirmationToken.getClient().getEmail());
        return "Confirmed";
    }

    /**
     Constructs an HTML email body with a confirmation link to activate the user's account.
     @param name the name of the user to whom the email is addressed
     @param link the confirmation link to activate the user's account
     @return an HTML-formatted email body containing a confirmation link
     */
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

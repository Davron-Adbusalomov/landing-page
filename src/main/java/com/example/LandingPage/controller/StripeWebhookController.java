package com.example.LandingPage.controller;

import com.example.LandingPage.DTO.UsersDTO;
import com.example.LandingPage.config.TelegramConfig;
import com.example.LandingPage.model.*;
import com.example.LandingPage.repository.*;
import com.example.LandingPage.service.LanguageService;
import com.example.LandingPage.service.UsersService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@RestController
public class StripeWebhookController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private TelegramConfig telegramConfig;

    private static final String WEBHOOK_SECRET = "whsec_i8rLjFTm7t7wbw8XxT9KGuyA1rZkB5Zl";

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, HttpServletRequest request) {
        try {
            String sigHeader = request.getHeader("Stripe-Signature");
            Event event = Webhook.constructEvent(payload, sigHeader, WEBHOOK_SECRET);

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getData().getObject();
                Long chatId = Long.valueOf(session.getMetadata().get("chatId"));

                UsersDTO tempUser = usersService.tempUser.get(chatId);

                Users users = UsersDTO.toModel(tempUser);

                users = userRepository.save(users);

                List<Experience> experiences1 = users.getExperience();
                for (Experience experience1 : experiences1) {
                    experience1.setUsers(users);
                }
                experienceRepository.saveAll(experiences1);

                List<Language> languages = users.getLanguages();
                for (Language language : languages) {
                    language.setUsers(users);
                }
                languageRepository.saveAll(languages);

                List<Services> services = users.getServices();
                for (Services services1 : services) {
                    services1.setUsers(users);
                }
                serviceRepository.saveAll(services);

                List<Reception> receptions = users.getReception();
                for (Reception reception : receptions) {
                    reception.setUsers(users);
                }
                receptionRepository.saveAll(receptions);

                List<Speciality> specialities = users.getSpecialities();
                for (Speciality speciality : specialities) {
                    speciality.setUsers(users);
                }
                specialityRepository.saveAll(specialities);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Congratulations🎉🍾🎊 You have successfully put your info to our Landing page😍😍😍");
                telegramConfig.execute(sendMessage);
            }

            return ResponseEntity.ok().build();
        } catch (StripeException e) {
            return ResponseEntity.badRequest().build();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
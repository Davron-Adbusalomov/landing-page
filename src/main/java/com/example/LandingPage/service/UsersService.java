package com.example.LandingPage.service;

import com.example.LandingPage.DTO.*;
import com.example.LandingPage.config.TelegramConfig;
import com.example.LandingPage.model.Users;
import com.example.LandingPage.repository.ExperienceRepository;
import com.example.LandingPage.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.*;

@Service
public class UsersService {

    TelegramConfig telegramConfig = new TelegramConfig(this);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExperienceRepository experienceRepository;


    public Map<Long, Integer> userStages = new HashMap<>();
    public Map<Long, UsersDTO> tempUser = new HashMap<>();
    public Map<Long, List<ExperienceDTO>> tempExperience = new HashMap<>();
    public Map<Long, List<LanguageDTO>> tempLanguages = new HashMap<>();
    public Map<Long, List<SpecialityDTO>> tempSpecialities = new HashMap<>();
    public Map<Long, List<ReceptionDTO>> tempReception = new HashMap<>();
    public Map<Long, List<ServiceDTO>> tempService = new HashMap<>();


    public List<UsersDTO> getAllUsers(){
        List<Users> optionalUsers = userRepository.findAll();
        List<UsersDTO> usersDTOS = new ArrayList<>();
        for (Users user:optionalUsers) {
            UsersDTO usersDTO = UsersDTO.toDto(user);
            usersDTOS.add(usersDTO);
        }
        return usersDTOS;
    }

    public UsersDTO getUserDetails(Long id){
        Optional<Users> optionalUsers = userRepository.findById(id);
        if (optionalUsers.isEmpty()) throw new EntityNotFoundException("No user found with this id!");
        return UsersDTO.toDto(optionalUsers.get());
    }

    public void collectUserInput(Long chatId, String text, List<PhotoSize> photo) throws TelegramApiException, IOException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        int stage = userStages.getOrDefault(chatId, 1);

        switch (stage) {
            case 1:
                if (text.equals("/start")) {
                    sendMessage.setText("Assalomu alaykum. Welcome to our online hospital doctor registration!\nPlease enter your full name:");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 2);
                }
                break;
            case 2:
                if (text.isEmpty() || !text.matches("^[a-zA-Z\\s]*$")) {
                    sendMessage.setText("Invalid input❌ Please enter your full name:");
                    telegramConfig.execute(sendMessage);
                } else {
                    UsersDTO user = tempUser.get(chatId);
                    if (user == null) {
                        user = new UsersDTO();
                        tempUser.put(chatId, user);
                    }
                    user.setFullName(text);
                    sendMessage.setText("Let's talk about your contact numbers that clients can connect!\nEnter your phone number:");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 3);
                }
                break;
            case 3:
                if (text.isEmpty() || !text.matches("^\\d+$")) {
                    sendMessage.setText("Invalid input❌ Please reenter your phone number(it should contain only digits):");
                    telegramConfig.execute(sendMessage);
                } else {
                    tempUser.get(chatId).setPhone(text);
                    sendMessage.setText("Enter your telegram username:");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 4);
                }
                break;
            case 4:
                if (text.isEmpty() || !text.matches("^@.*$")) {
                    sendMessage.setText("Invalid input❌ Please reenter your telegram username\n(It should start with '@'):");
                    telegramConfig.execute(sendMessage);
                } else {
                    tempUser.get(chatId).setTelegram(text);
                    sendMessage.setText("Alright! Now, let's talk about your experience! In which company you worked?");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 5);
                }
                break;
            case 5:
                List<ExperienceDTO> experiences = tempExperience.get(chatId);
                if (experiences == null) {
                    experiences = new ArrayList<>();
                    tempExperience.put(chatId, experiences);
                }
                ExperienceDTO experience = new ExperienceDTO();
                experiences.add(experience);
                experience.setCompany(text);
                sendMessage.setText("Enter your start date📅");
                telegramConfig.execute(sendMessage);
                userStages.put(chatId, 6);
                break;
            case 6:
                if (!text.matches("^(0[1-9]|1[0-2])/((19|20)\\d\\d)$")) {
                    sendMessage.setText("Invalid input. Please reenter the date in MM/yyyy format:");
                    telegramConfig.execute(sendMessage);
                }
                else {
                    experiences = tempExperience.get(chatId);
                    if (!experiences.isEmpty()) {
                        experience = experiences.get(experiences.size() - 1);
                        experience.setStart_from(text);
                    }
                    sendMessage.setText("Enter your end date📅");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 7);
                }
                break;
            case 7:
                if (!text.matches("^(0[1-9]|1[0-2])/((19|20)\\d\\d)$")) {
                    sendMessage.setText("Invalid input❌ Please reenter the date in MM/yyyy format:");
                    telegramConfig.execute(sendMessage);
                }else {
                    experiences = tempExperience.get(chatId);
                    if (!experiences.isEmpty()) {
                        experience = experiences.get(experiences.size() - 1);
                        experience.setEnd_to(text);
                    }
                    tempUser.get(chatId).setExperienceDTOS(experiences);
                    sendMessage.setText("Great! Do you want to add other experience?(yes/no)");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 8);
                }
                break;
            case 8:
                if (text.equalsIgnoreCase("yes")) {
                    sendMessage.setText("Alright! Let's add another experience. In which company did you work?");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 5);
                } else if (text.equalsIgnoreCase("no")) {
                    UsersDTO user = tempUser.get(chatId);
                    if (user != null) {
                        user.setExperienceDTOS(tempExperience.get(chatId));
                    }
                    sendMessage.setText("Great! Let's proceed to the next step of the registration which is Languages you know! What is your native language?");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 9);
                }
                break;
            case 9:
                List<LanguageDTO> languages = tempLanguages.get(chatId);
                if (languages == null) {
                    languages = new ArrayList<>();
                }
                LanguageDTO languageDTO = new LanguageDTO();
                languageDTO.setLanguage(text);
                languages.add(languageDTO);
                tempUser.get(chatId).setLanguageDTOS(languages);
                sendMessage.setText("Do you want to add another language? (yes/no)");
                telegramConfig.execute(sendMessage);
                userStages.put(chatId, 10);
                break;
            case 10:
                if (text.equalsIgnoreCase("yes")) {
                    sendMessage.setText("Please enter another language:");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 9);
                } else if (text.equalsIgnoreCase("no")) {
                    sendMessage.setText("Great! Let's proceed to the next steps. What are your achievements in your career?\nCount all, please!");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 11);
                } else {
                    sendMessage.setText("Invalid input❌ Please enter 'yes' if you want to add another language or 'no' if you want to proceed to the next step.");
                    telegramConfig.execute(sendMessage);
                }
                break;
            case 11:
                if (!text.isEmpty()){
                    tempUser.get(chatId).setAchievements(text);
                    sendMessage.setText("Excellent👍! Let's continue with your education background. Where did learn your job?\nCan you enter your education degrees, please!");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 12);
                }else {
                    sendMessage.setText("Count your achievements please!");
                    telegramConfig.execute(sendMessage);
                }
                break;
            case 12:
                if (!text.isEmpty()){
                    UsersDTO user = tempUser.get(chatId);
                    user.setEducation(text);
                    tempUser.put(chatId, user); // Store the updated user back into the map
                    sendMessage.setText("Cool😎! It is turn to fill your specialities! What is your major speciality?");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 13);
                }else {
                    sendMessage.setText("Count your education places, please🏫🏫🏫");
                    telegramConfig.execute(sendMessage);
                }
                break;
            case 13:
                List<SpecialityDTO> specialityDTOS = tempSpecialities.get(chatId);
                if (specialityDTOS == null) {
                    specialityDTOS = new ArrayList<>();
                }
                SpecialityDTO specialityDTO = new SpecialityDTO();
                specialityDTO.setName(text);
                specialityDTOS.add(specialityDTO);
                tempUser.get(chatId).setSpecialityDTOS(specialityDTOS);
                sendMessage.setText("Do you want to add another speciality? (yes/no)");
                telegramConfig.execute(sendMessage);
                userStages.put(chatId, 14);
                break;
            case 14:
                if (text.equalsIgnoreCase("yes")){
                    sendMessage.setText("Add another speciality:");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 13);
                }else {
                    sendMessage.setText("Great😍! Let's talk about your reception info now.\n Enter reception place please!");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 15);
                }
                break;
            case 15:
                List<ReceptionDTO> receptionDTOS = tempReception.get(chatId);
                if (receptionDTOS == null) {
                    receptionDTOS = new ArrayList<>();
                }
                ReceptionDTO receptionDTO = new ReceptionDTO();
                receptionDTO.setAddress(text);
                receptionDTOS.add(receptionDTO);
                tempReception.put(chatId, receptionDTOS);
                sendMessage.setText("Enter target place to the destination🏫");
                telegramConfig.execute(sendMessage);
                userStages.put(chatId, 16);
                break;
            case 16:
                receptionDTOS = tempReception.get(chatId);
                if (!receptionDTOS.isEmpty()) {
                    receptionDTO = receptionDTOS.get(receptionDTOS.size() - 1);
                    receptionDTO.setTarget(text);
                }
                sendMessage.setText("Enter your reception times⏲️:");
                telegramConfig.execute(sendMessage);
                userStages.put(chatId, 17);
                break;
            case 17:
                receptionDTOS = tempReception.get(chatId);
                if (!receptionDTOS.isEmpty()) {
                    receptionDTO = receptionDTOS.get(receptionDTOS.size() - 1);
                    receptionDTO.setWork_time(text);
                }
                sendMessage.setText("Enter your reception price💰:");
                telegramConfig.execute(sendMessage);
                userStages.put(chatId, 18);
                break;
            case 18:
                receptionDTOS = tempReception.get(chatId);
                if (!receptionDTOS.isEmpty()) {
                    receptionDTO = receptionDTOS.get(receptionDTOS.size() - 1);
                    receptionDTO.setPrice(Long.valueOf(text));
                }
                tempUser.get(chatId).setReceptionDTOS(receptionDTOS);
                sendMessage.setText("Do you want to add another reception place?(yes/no)");
                telegramConfig.execute(sendMessage);
                userStages.put(chatId, 19);
                break;
            case 19:
                if (text.equalsIgnoreCase("yes")){
                    sendMessage.setText("Add another address for reception: ");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 15);
                }else {
                    sendMessage.setText("Awesome👍! We came to the stage of services you provide!\nEnter a name of the service:");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 20);
                }
                break;
            case 20:
                List<ServiceDTO> serviceDTOS = tempService.get(chatId);
                if (serviceDTOS == null) {
                    serviceDTOS = new ArrayList<>();
                }
                ServiceDTO serviceDTO = new ServiceDTO();
                serviceDTO.setName(text);
                serviceDTOS.add(serviceDTO);
                tempService.put(chatId, serviceDTOS);
                sendMessage.setText("Enter the description of the service in detail:");
                telegramConfig.execute(sendMessage);
                userStages.put(chatId, 21);
                break;
            case 21:
                serviceDTOS = tempService.get(chatId);
                if (!serviceDTOS.isEmpty()) {
                    serviceDTO = serviceDTOS.get(serviceDTOS.size() - 1);
                    serviceDTO.setDescription(text);
                }
                sendMessage.setText("Enter price of the service🤑:");
                telegramConfig.execute(sendMessage);
                userStages.put(chatId, 22);
                break;
            case 22:
                serviceDTOS = tempService.get(chatId);
                if (!serviceDTOS.isEmpty()) {
                    serviceDTO = serviceDTOS.get(serviceDTOS.size() - 1);
                    serviceDTO.setPrice(Long.valueOf(text));
                }
                tempUser.get(chatId).setServiceDTOS(serviceDTOS);
                sendMessage.setText("Do you want to add another service?(yes/no)");
                telegramConfig.execute(sendMessage);
                userStages.put(chatId, 23);
                break;
            case 23:
                if (text.equalsIgnoreCase("yes")){
                    sendMessage.setText("Enter another service name:");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 20);
                }else {
                    sendMessage.setText("Ok, you should also add profile image. Please upload!");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 24);
                } break;
            case 24:
                if (photo.isEmpty()){
                    sendMessage.setText("Error happened while photo uploading❌❌❌! Please try again!");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 24);
                } else {
                    PhotoSize myPhoto = photo.get(0);
                    String fileId = myPhoto.getFileId();
                    GetFile getFile = new GetFile();
                    getFile.setFileId(fileId);
                    File file = telegramConfig.execute(getFile);
                    java.io.File photoFile = telegramConfig.downloadFile(file);
                    byte[] fileBytes = java.nio.file.Files.readAllBytes(photoFile.toPath());
                    tempUser.get(chatId).setProfileImage(fileBytes);
                    sendMessage.setText("Your photo successfully saved✅! Here we go! We just completed registration!\n\n Now, it is turn to Payment!\n\nIn the payment stage you are required to pay the fee of the landing page. Are you ready?\n(Type YES)");
                    telegramConfig.execute(sendMessage);
                    userStages.put(chatId, 25);
                }
                break;
            case 25:
                if (text.equalsIgnoreCase("yes")) {
                    Stripe.apiKey = "sk_test_51PEhubAoBD4rVuXji4lj0nzDDzgIx00F0tuYoxgefakvyjraiowqBsOTkSXmcot9HpWfh8stLA9ZRtHJGKsVgeJN007FsYMGJK";

                    SessionCreateParams params =
                            SessionCreateParams.builder()
                                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                                    .setSuccessUrl("https://www.google.com")
                                    .setCancelUrl("https://www.google.com")
                                    .addLineItem(
                                            SessionCreateParams.LineItem.builder()
                                                    .setPrice("price_1PEiRvAoBD4rVuXjec0IXWHI")
                                                    .setQuantity(1L)
                                                    .build())
                                    .putMetadata("chatId", chatId.toString())
                                    .build();

                    try {
                        Session session = Session.create(params);
                        String checkoutUrl = session.getUrl();

                        sendMessage.setText("Please complete your payment by visiting the following URL: " + checkoutUrl);
                        telegramConfig.execute(sendMessage);
                    } catch (StripeException e) {
                        e.printStackTrace();
                        sendMessage.setText("An error occurred while creating the payment session. Please try again later.");
                        telegramConfig.execute(sendMessage);
                    }
                }
                break;
        }
    }
}

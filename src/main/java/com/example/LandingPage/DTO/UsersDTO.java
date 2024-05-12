package com.example.LandingPage.DTO;

import com.example.LandingPage.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
    private Long id;
    private String fullName;

    private String phone;

    private String telegram;

    private List<ExperienceDTO> experienceDTOS;

    private List<LanguageDTO> languageDTOS;

    private String education;

    private String achievements;

    private byte[] profileImage;

    private List<SpecialityDTO> specialityDTOS;

    private List<ReceptionDTO> receptionDTOS;

    private List<ServiceDTO> serviceDTOS;

    public static Users toModel(UsersDTO usersDTO){
        Users users = new Users();
        users.setFullName(usersDTO.getFullName());
        users.setPhone(usersDTO.getPhone());
        users.setTelegram(usersDTO.getTelegram());
        users.setAchievements(usersDTO.getAchievements());
        users.setEducation(usersDTO.getEducation());
        users.setProfileImage(usersDTO.getProfileImage());
        List<Experience> experiences = new ArrayList<>();
        for (ExperienceDTO experienceDTO: usersDTO.getExperienceDTOS()) {
            Experience experience = new Experience();
            experience.setCompany(experienceDTO.getCompany());
            experience.setEnd_to(experienceDTO.getEnd_to());
            experience.setStart_from(experienceDTO.getStart_from());
            experiences.add(experience);
        }
        users.setExperience(experiences);
        List<Language> languages = new ArrayList<>();
        for (LanguageDTO languageDTO: usersDTO.getLanguageDTOS()) {
            Language language = new Language();
            language.setLanguage(languageDTO.getLanguage());
            languages.add(language);
        }
        users.setLanguages(languages);
        List<Reception> receptions = new ArrayList<>();
        for (ReceptionDTO receptionSTO:usersDTO.getReceptionDTOS()) {
            Reception reception = new Reception();
            reception.setAddress(receptionSTO.getAddress());
            reception.setPrice(receptionSTO.getPrice());
            reception.setTarget(receptionSTO.getTarget());
            reception.setWork_time(receptionSTO.getWork_time());
            receptions.add(reception);
        }
        users.setReception(receptions);
        List<Speciality> specialityList = new ArrayList<>();
        for (SpecialityDTO speciality: usersDTO.getSpecialityDTOS()) {
            Speciality speciality1 = new Speciality();
            speciality1.setName(speciality.getName());
            specialityList.add(speciality1);
        }
        users.setSpecialities(specialityList);
        List<Services> servicesList = new ArrayList<>();
        for (ServiceDTO serviceDTO:usersDTO.getServiceDTOS()) {
            Services services = new Services();
            services.setName(serviceDTO.getName());
            services.setPrice(serviceDTO.getPrice());
            services.setDescription(serviceDTO.getDescription());
            servicesList.add(services);
        }
        users.setServices(servicesList);
        return users;
    }

    public static UsersDTO toDto(Users users){
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setId(users.getId());
        usersDTO.setFullName(users.getFullName());
        usersDTO.setPhone(users.getPhone());
        usersDTO.setTelegram(users.getTelegram());
        usersDTO.setAchievements(users.getAchievements());
        usersDTO.setEducation(users.getEducation());
        usersDTO.setProfileImage(users.getProfileImage());
        List<ExperienceDTO> experiences = new ArrayList<>();
        for (Experience experience: users.getExperience()) {
            ExperienceDTO experienceDTO = new ExperienceDTO();
            experienceDTO.setId(experience.getId());
            experienceDTO.setCompany(experience.getCompany());
            experienceDTO.setEnd_to(experience.getEnd_to());
            experienceDTO.setStart_from(experience.getStart_from());
            experiences.add(experienceDTO);
        }
        usersDTO.setExperienceDTOS(experiences);
        List<LanguageDTO> languages = new ArrayList<>();
        for (Language language: users.getLanguages()) {
            LanguageDTO languageDTO = new LanguageDTO();
            languageDTO.setId(language.getId());
            languageDTO.setLanguage(language.getLanguage());
            languages.add(languageDTO);
        }
        usersDTO.setLanguageDTOS(languages);
        List<ReceptionDTO> receptions = new ArrayList<>();
        for (Reception reception: users.getReception()) {
            ReceptionDTO receptionDTO = new ReceptionDTO();
            receptionDTO.setId(reception.getId());
            receptionDTO.setAddress(reception.getAddress());
            receptionDTO.setPrice(reception.getPrice());
            receptionDTO.setTarget(reception.getTarget());
            receptionDTO.setWork_time(reception.getWork_time());
            receptions.add(receptionDTO);
        }
        usersDTO.setReceptionDTOS(receptions);
        List<SpecialityDTO> specialities = new ArrayList<>();
        for (Speciality speciality: users.getSpecialities()) {
            SpecialityDTO specialityDTO = new SpecialityDTO();
            specialityDTO.setId(speciality.getId());
            specialityDTO.setName(speciality.getName());
            specialities.add(specialityDTO);
        }
        usersDTO.setSpecialityDTOS(specialities);
        List<ServiceDTO> services = new ArrayList<>();
        for (Services service: users.getServices()) {
            ServiceDTO serviceDTO = new ServiceDTO();
            serviceDTO.setId(service.getId());
            serviceDTO.setName(service.getName());
            serviceDTO.setPrice(service.getPrice());
            serviceDTO.setDescription(service.getDescription());
            services.add(serviceDTO);
        }
        usersDTO.setServiceDTOS(services);
        return usersDTO;
    }
}

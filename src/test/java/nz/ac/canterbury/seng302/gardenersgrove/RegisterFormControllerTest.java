package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.controller.RegisterFormController;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Gardener;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenerFormRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.FormService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenerFormService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class RegisterFormControllerTest {

    private RegisterFormController registerFormController;
    private GardenerFormService gardenerFormService;

    @BeforeEach
    public void setUp() {
        gardenerFormService = Mockito.mock(GardenerFormService.class);
        registerFormController = new RegisterFormController(gardenerFormService);
        assertTrue(true);
    }

    @Test
    void GivenValidGardenerInput_WhenUserRegisters_NewGardenerCreated() {
        registerFormController.submitForm("Kush", "Desai", LocalDate.of(2004, 1, 15),"test@gmail.com", "Password1!", "Password1!", false);
        Mockito.verify(gardenerFormService, times(1)).addGardener(Mockito.any(Gardener.class));
    }

    @Test
    void GivenInvalidFirstName_WhenUserRegisters_NewGardenerNotCreated() {
        registerFormController.submitForm("Kush1", "Desai", LocalDate.of(2004, 1, 15),"test@gmail.com", "Password1!", "Password1!", false);
        Mockito.verify(gardenerFormService, Mockito.never()).addGardener(Mockito.any(Gardener.class));
    }

    @Test
    void GivenInvalidLastName_WhenLastNameIsNotOptional_NewGardenerNotCreated() {
        registerFormController.submitForm("Kush", "Desai1", LocalDate.of(2004, 1, 15),"test@gmail.com", "Password1!", "Password1!", false);
        Mockito.verify(gardenerFormService, Mockito.never()).addGardener(Mockito.any(Gardener.class));
    }

    @Test
    void GivenInvalidLastName_WhenLastNameIsOptional_NewGardenerCreated() {
        registerFormController.submitForm("Kush", "Desai1", LocalDate.of(2004, 1, 15),"test@gmail.com", "Password1!", "Password1!", true);
        Mockito.verify(gardenerFormService, times(1)).addGardener(Mockito.any(Gardener.class));
    }

    @Test
    void GivenAgeTooLow_WhenUserRegisters_NewGardenerNotCreated() {
        registerFormController.submitForm("Kush", "Desai", LocalDate.of(2024, 1, 15),"test@gmail.com", "Password1!", "Password1!", true);
        Mockito.verify(gardenerFormService, Mockito.never()).addGardener(Mockito.any(Gardener.class));
    }

    @Test
    void GivenAgeTooHigh_WhenUserRegisters_NewGardenerNotCreated() {
        registerFormController.submitForm("Kush", "Desai", LocalDate.of(1024, 1, 15),"test@gmail.com", "Password1!", "Password1!", true);
        Mockito.verify(gardenerFormService, Mockito.never()).addGardener(Mockito.any(Gardener.class));
    }

    @Test
    void GivenPasswordsDontMatch_WhenUserRegisters_NewGardenerNotCreated() {
        registerFormController.submitForm("Kush", "Desai", LocalDate.of(2024, 1, 15),"test@gmail.com", "Password1!", "Password2!", true);
        Mockito.verify(gardenerFormService, Mockito.never()).addGardener(Mockito.any(Gardener.class));
    }

}

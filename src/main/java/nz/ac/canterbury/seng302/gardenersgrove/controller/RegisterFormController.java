package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Gardener;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenerFormService;
import nz.ac.canterbury.seng302.gardenersgrove.service.InputValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class RegisterFormController {
    Logger logger = LoggerFactory.getLogger(RegisterFormController.class);

    private final GardenerFormService gardenerFormService;
    private InputValidation inputValidator = new InputValidation();


    @Autowired
    public RegisterFormController(GardenerFormService gardenerFormService) {
        this.gardenerFormService = gardenerFormService;
    }
    /**
     * Gets form to be displayed, includes the ability to display results of previous form when linked to from POST form
     * @param firstName first name of user to be entered in the form
     * @param lastName last name of user
     * @param DoB user's date of birth
     * @param email user's email
     * @param password user's password
     * @param passwordConfirm user's confirmed password
     * @param lastNameCheck is the last name checkbox selected
     * @param model (map-like) representation of name, language and isJava boolean for use in thymeleaf
     * @return thymeleaf demoFormTemplate
     */
    @GetMapping("/register")
    public String form(@RequestParam(name="firstName", required = false, defaultValue = "") String firstName,
                       @RequestParam(name="lastName", required = false, defaultValue = "") String lastName,
                       @RequestParam(name="DoB", required = false, defaultValue = "2024-01-01") LocalDate DoB,
                       @RequestParam(name="email", required = false, defaultValue = "") String email,
                       @RequestParam(name="password", required = false, defaultValue = "") String password,
                       @RequestParam(name="passwordConfirm", required = false, defaultValue = "") String passwordConfirm,
                       @RequestParam(name="lastNameCheck", required = false) boolean lastNameCheck,
                       Model model) {
        logger.info("GET /register");

        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("DoB", DoB);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("passwordConfirm", passwordConfirm);
        return "registerTemplate";
    }

    /**
     * Posts a form response with name and favourite language
     * @param firstName first name of user
     * @param lastName last name of user
     * @param DoB user's date of birth
     * @param email user's email
     * @param password user's password
     * @param passwordConfirm user's repeated password
     * @param model (map-like) representation of name, language and isJava boolean for use in thymeleaf,
     *              with values being set to relevant parameters provided
     * @return thymeleaf demoFormTemplate
     */
    @PostMapping("/register")
    public String submitForm( @RequestParam(name="firstName") String firstName,
                              @RequestParam(name="lastName", required = false) String lastName,
                              @RequestParam(name="DoB") LocalDate DoB,
                              @RequestParam(name="email") String email,
                              @RequestParam(name="password") String password,
                              @RequestParam(name = "passwordConfirm") String passwordConfirm,
                              @RequestParam(name = "lastNameCheck", required = false) boolean lastNameCheck,
                              Model model) {
        logger.info("POST /register");
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("DoB", DoB);
        model.addAttribute("email", email);
        model.addAttribute("password", password);

        Optional<String> firstNameError = inputValidator.checkValidName(firstName, "First", false);
        model.addAttribute("firstNameValid", firstNameError.isPresent() ? firstNameError.get() : "");
        Optional<String> lastNameError = inputValidator.checkValidName(lastName, "Last", lastNameCheck);
        model.addAttribute("lastNameValid", lastNameError.isPresent() ? lastNameError.get() : "");

        model.addAttribute("DoBValid", DoB);

        Optional<String> validEmailError = inputValidator.checkValidEmail(email);
        model.addAttribute("emailValid", validEmailError.isPresent() ? validEmailError.get() : "");
        Optional<String> passwordMatchError = inputValidator.checkPasswordsMatch(password, passwordConfirm);
        model.addAttribute("passwordsMatch", passwordMatchError.isPresent() ? passwordMatchError.get() : "");
        Optional<String> passwordStrengthError = inputValidator.checkStrongPassword(password);
        model.addAttribute("passwordStrong", passwordStrengthError.isPresent() ? passwordStrengthError.get() : "");

        if (!firstNameError.isPresent() &&
        !lastNameError.isPresent() &&
        !validEmailError.isPresent() &&
        !passwordMatchError.isPresent() &&
        !passwordStrengthError.isPresent()) {
            Gardener newGardener = new Gardener(firstName, lastName, DoB, email, password);
            gardenerFormService.addGardener(newGardener);
            return "redirect:/userProfile/"+ newGardener.getId().toString();
        }

        return "registerTemplate";

    }

    /**
     * Gets all form responses
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf demoResponseTemplate
     */
    @GetMapping("/register/responses")
    public String responses(Model model) {
        logger.info("GET /form/responses");
        model.addAttribute("responses", gardenerFormService.getGardeners());
        return "registerResponsesTemplate";
    }
}

package sakura.iri2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String getMainPage(Model model) {
        return "index";
    }

    @GetMapping("/students")
    public String getStudentsPage(Model model) {
        return "studentPage";
    }

    @GetMapping("/instructors")
    public String getInstructorsPage(Model model) {
        return "instructorPage";
    }

    @GetMapping("/courses")
    public String getCoursesPage(Model model) {
        return "coursePage";
    }
}
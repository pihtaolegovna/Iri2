package sakura.iri2.controller;

import sakura.iri2.model.InstructorModel;
import sakura.iri2.service.InMemoryInstructorImpl;
import sakura.iri2.service.InstructorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InMemoryInstructorImpl instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public String getInstructorAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filterName,
            @RequestParam(required = false) String filterEmail,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<InstructorModel> instructorPage = instructorService.findAllInstructorsWithPaginationAndFilters(
                pageable, filterName, filterEmail, isDeleted, sortField, sortDirection);

        model.addAttribute("instructors", instructorPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", instructorPage.getTotalPages());
        return "instructorPage";
    }

    @GetMapping("/create")
    public String getCreateInstructor() {
        return "createInstructor";
    }

    @PostMapping("/create")
    public String postCreateInstructor(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password
    ) {
        instructorService.createInstructor(new InstructorModel(name, email, password));
        return "redirect:/instructor";
    }

    @GetMapping("/edit/{id}")
    public String getEditInstructor(@PathVariable UUID id, Model model) {
        var instructor = instructorService.findInstructorById(id);
        model.addAttribute("instructor", instructor);
        return "editInstructor";
    }

    @PostMapping("/edit/{id}")
    public String postEditInstructor(
            @PathVariable UUID id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password
    ) {
        var instructor = new InstructorModel(name, email, password);
        instructor.setId(id);
        instructorService.updateInstructor(instructor);
        return "redirect:/instructor";
    }

    // Логическое удаление
    @PostMapping("/logicdelete")
    public String logicDeleteInstructors(@RequestParam List<UUID> ids) {
        instructorService.logicDeleteInstructors(ids);
        return "redirect:/instructor";
    }

    // Физическое удаление
    @PostMapping("/physdelete")
    public String deleteInstructors(@RequestParam List<UUID> ids) {
        instructorService.deleteInstructors(ids);
        return "redirect:/instructor";
    }
}
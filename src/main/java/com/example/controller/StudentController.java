package com.example.controller;

import com.example.model.Student;
import com.example.model.StudentForm;
import com.example.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private IStudentService studentService;

    @Value("${FILE_UPLOAD}")
    private String fileUploadPath;

    @GetMapping("")
    public String index(Model model) {
        List<Student> studentList = studentService.findAll();
        model.addAttribute(studentList);
        return "/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("student", new Student());
        return "/create";
    }

    @PostMapping("/save")
    public String save(StudentForm studentForm, RedirectAttributes redirectAttributes) {
        MultipartFile file = studentForm.getImg();
        String imgFileName = file.getOriginalFilename();
        try {
            FileCopyUtils.copy(file.getBytes(), new File(fileUploadPath + imgFileName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Student student = new Student(studentForm.getName(), studentForm.getAddress(), imgFileName);
        studentService.save(student);
        redirectAttributes.addFlashAttribute("success", "Thêm " + student.getName() + " thành công");
        return "redirect:/student";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        model.addAttribute("student", studentService.findById(id));
        return "/edit";
    }

    @PostMapping("/update")
    public String update(StudentForm studentForm, RedirectAttributes redirectAttributes) {
        MultipartFile file = studentForm.getImg();
        String imgFileName = file.getOriginalFilename();
        if (!imgFileName.isEmpty()) {
            try {
                FileCopyUtils.copy(file.getBytes(), new File(fileUploadPath + imgFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Student student = new Student(studentForm.getId(), studentForm.getName(), studentForm.getAddress(),
                imgFileName.isEmpty() ? studentService.findById(studentForm.getId()).getImg() : imgFileName);
        studentService.save(student);
        redirectAttributes.addFlashAttribute("success", "Sửa " + student.getName() + " thành công");
        return "redirect:/student";
    }

    @GetMapping("/{id}/delete")
    public String delete(Model model, @PathVariable Long id) {
        model.addAttribute("student", studentService.findById(id));
        return "/delete";
    }

    @PostMapping("remove")
    public String remove(Student student, RedirectAttributes redirectAttributes) {
        studentService.remove(student.getId());
        redirectAttributes.addFlashAttribute("success", "Xoá " + student.getName() + " thành công");
        return "redirect:/student";
    }

    @GetMapping("/{id}/view")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        return "/view";
    }

    @GetMapping("/search")
    public String index(Model model, @RequestParam String q) {
        List<Student> studentList = studentService.findByName(q);
        model.addAttribute(studentList);
        return "/index";
    }
}

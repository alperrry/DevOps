package com.bigdata.second.controller;

import com.bigdata.second.entity.Member;
import com.bigdata.second.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members")
public class MemberViewController {

    private final MemberService service;

    public MemberViewController(MemberService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", service.getAllMembers());
        return "members/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("member", new Member());
        return "members/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("member") Member member,
                         BindingResult result,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "members/form";
        }
        service.createMember(member);
        flash.addFlashAttribute("successMessage", "Üye başarıyla eklendi.");
        return "redirect:/members";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("member", service.findById(id));
        return "members/form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("member") Member member,
                         BindingResult result,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "members/form";
        }
        service.update(id, member);
        flash.addFlashAttribute("successMessage", "Üye başarıyla güncellendi.");
        return "redirect:/members";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        try {
            service.delete(id);
            flash.addFlashAttribute("successMessage", "Kitap başarıyla silindi.");
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/books";
    }
}
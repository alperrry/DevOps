package com.bigdata.second.controller;

import com.bigdata.second.service.BookService;
import com.bigdata.second.service.BorrowerService;
import com.bigdata.second.service.MemberService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/borrowers")
public class BorrowerViewController {

    private final BorrowerService borrowerService;
    private final MemberService memberService;
    private final BookService bookService;

    public BorrowerViewController(BorrowerService borrowerService,
                                  MemberService memberService,
                                  BookService bookService) {
        this.borrowerService = borrowerService;
        this.memberService = memberService;
        this.bookService = bookService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("borrowers", borrowerService.getAllBorrowers());
        return "borrowers/list";
    }

    // Ödünç verme formu — select'ler için üye ve kitap listesini modele ekle
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("books", bookService.getAllBooks());
        return "borrowers/form";
    }

    // Thymeleaf form POST'u @RequestParam ile alıyoruz (BorrowRequest DTO JSON değil)
    @PostMapping("/form")
    public String create(@RequestParam Long memberId,
                         @RequestParam Long bookId,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
                         RedirectAttributes flash,
                         Model model) {
        try {
            borrowerService.borrowBook(memberId, bookId, issueDate, dueDate);
            flash.addFlashAttribute("successMessage", "Kitap başarıyla ödünç verildi.");
            return "redirect:/borrowers";
        } catch (Exception e) {
            // Hata varsa formu tekrar göster
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("members", memberService.getAllMembers());
            model.addAttribute("books", bookService.getAllBooks());
            return "borrowers/form";
        }
    }

    // İade alma
    @PostMapping("/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes flash) {
        try {
            borrowerService.returnBook(id);
            flash.addFlashAttribute("successMessage", "Kitap başarıyla iade alındı.");
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/borrowers";
    }
}

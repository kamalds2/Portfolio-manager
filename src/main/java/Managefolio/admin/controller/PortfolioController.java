package Managefolio.admin.controller;

import Managefolio.admin.model.Portfolio;
import Managefolio.admin.services.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    /**
     * List all portfolios
     */
    @GetMapping({"/", "/list"})
    public String listPortfolios(Model model) {
        List<Portfolio> portfolios = portfolioService.getAllPortfolios();
        if (portfolios == null) portfolios = new java.util.ArrayList<>();
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("hasPortfolios", !portfolios.isEmpty());
        model.addAttribute("activePortfolios", portfolioService.getActivePortfolioCount());
        model.addAttribute("totalPortfolios", portfolioService.getTotalPortfolioCount());
        model.addAttribute("viewName", "portfolio/list");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    /**
     * Show form for creating new portfolio
     */
    @GetMapping("/new")
    public String newPortfolioForm(Model model) {
        model.addAttribute("portfolio", new Portfolio());
        model.addAttribute("isEdit", false);
        model.addAttribute("viewName", "portfolio/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    /**
     * Show form for editing existing portfolio
     */
    @GetMapping("/edit/{id}")
    public String editPortfolioForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Portfolio> portfolioOpt = portfolioService.getPortfolioById(id);
        if (portfolioOpt.isPresent()) {
            model.addAttribute("portfolio", portfolioOpt.get());
            model.addAttribute("isEdit", true);
            model.addAttribute("viewName", "portfolio/form");
            model.addAttribute("isAdmin", true);
            return "layout/base";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Portfolio not found!");
            return "redirect:/admin/portfolio/list";
        }
    }

    /**
     * Save portfolio (create or update)
     */
    @PostMapping("/save")
    public String savePortfolio(@Valid @ModelAttribute Portfolio portfolio, 
                               BindingResult result, 
                               Model model, 
                               RedirectAttributes redirectAttributes) {
        
        // Check for validation errors
        if (result.hasErrors()) {
            model.addAttribute("isEdit", portfolio.getId() != null);
            model.addAttribute("viewName", "portfolio/form");
            model.addAttribute("isAdmin", true);
            return "layout/base";
        }

        // Check for duplicate portfolio code
        boolean codeExists;
        if (portfolio.getId() != null) {
            // For updates, check if code exists for other portfolios
            codeExists = portfolioService.portfolioCodeExistsForOther(portfolio.getPortfolioCode(), portfolio.getId());
        } else {
            // For new portfolios, check if code exists at all
            codeExists = portfolioService.portfolioCodeExists(portfolio.getPortfolioCode());
        }

        if (codeExists) {
            result.rejectValue("portfolioCode", "duplicate.portfolio.code", "Portfolio code already exists!");
            model.addAttribute("isEdit", portfolio.getId() != null);
            model.addAttribute("viewName", "portfolio/form");
            model.addAttribute("isAdmin", true);
            return "layout/base";
        }

        try {
            Portfolio savedPortfolio = portfolioService.savePortfolio(portfolio);
            String message = (portfolio.getId() != null) ? "Portfolio updated successfully!" : "Portfolio created successfully!";
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/admin/portfolio/list";
        } catch (Exception e) {
            result.rejectValue("portfolioName", "save.error", "Error saving portfolio: " + e.getMessage());
            model.addAttribute("isEdit", portfolio.getId() != null);
            model.addAttribute("viewName", "portfolio/form");
            model.addAttribute("isAdmin", true);
            return "layout/base";
        }
    }

    /**
     * Delete portfolio
     */
    @GetMapping("/delete/{id}")
    public String deletePortfolio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = portfolioService.deletePortfolio(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Portfolio deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete portfolio!");
        }
        return "redirect:/admin/portfolio/list";
    }

    /**
     * Toggle portfolio active status
     */
    @PostMapping("/toggle-status/{id}")
    public String togglePortfolioStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Portfolio portfolio = portfolioService.toggleActiveStatus(id);
        if (portfolio != null) {
            String status = portfolio.getActive() ? "activated" : "deactivated";
            redirectAttributes.addFlashAttribute("successMessage", "Portfolio " + status + " successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update portfolio status!");
        }
        return "redirect:/admin/portfolio/list";
    }

    /**
     * Search portfolios
     */
    @GetMapping("/search")
    public String searchPortfolios(@RequestParam(required = false) String name, Model model) {
        List<Portfolio> portfolios;
        if (name != null && !name.trim().isEmpty()) {
            portfolios = portfolioService.searchPortfoliosByName(name.trim());
            model.addAttribute("searchTerm", name.trim());
        } else {
            portfolios = portfolioService.getAllPortfolios();
        }
        
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("activePortfolios", portfolioService.getActivePortfolioCount());
        model.addAttribute("totalPortfolios", portfolioService.getTotalPortfolioCount());
        model.addAttribute("viewName", "portfolio/list");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }
}
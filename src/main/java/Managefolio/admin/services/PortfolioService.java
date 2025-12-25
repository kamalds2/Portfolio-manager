package Managefolio.admin.services;

import Managefolio.admin.model.Portfolio;
import Managefolio.admin.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    /**
     * Get all portfolios
     */
    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAllByOrderByPortfolioNameAsc();
    }

    /**
     * Get all active portfolios
     */
    public List<Portfolio> getActivePortfolios() {
        return portfolioRepository.findByActiveTrueOrderByPortfolioNameAsc();
    }

    /**
     * Get portfolio by ID
     */
    public Optional<Portfolio> getPortfolioById(Long id) {
        return portfolioRepository.findById(id);
    }

    /**
     * Get portfolio by code
     */
    public Optional<Portfolio> getPortfolioByCode(String portfolioCode) {
        return portfolioRepository.findByPortfolioCode(portfolioCode);
    }

    /**
     * Save portfolio (create or update)
     */
    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    /**
     * Delete portfolio by ID
     */
    public boolean deletePortfolio(Long id) {
        try {
            if (portfolioRepository.existsById(id)) {
                portfolioRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Toggle portfolio active status
     */
    public Portfolio toggleActiveStatus(Long id) {
        Optional<Portfolio> portfolioOpt = portfolioRepository.findById(id);
        if (portfolioOpt.isPresent()) {
            Portfolio portfolio = portfolioOpt.get();
            portfolio.setActive(!portfolio.getActive());
            return portfolioRepository.save(portfolio);
        }
        return null;
    }

    /**
     * Check if portfolio code exists
     */
    public boolean portfolioCodeExists(String portfolioCode) {
        return portfolioRepository.existsByPortfolioCode(portfolioCode);
    }

    /**
     * Check if portfolio code exists for another portfolio (for updates)
     */
    public boolean portfolioCodeExistsForOther(String portfolioCode, Long excludeId) {
        return portfolioRepository.existsByPortfolioCodeAndIdNot(portfolioCode, excludeId);
    }

    /**
     * Search portfolios by name
     */
    public List<Portfolio> searchPortfoliosByName(String name) {
        return portfolioRepository.findByPortfolioNameContainingIgnoreCase(name);
    }

    /**
     * Get total portfolio count
     */
    public long getTotalPortfolioCount() {
        return portfolioRepository.count();
    }

    /**
     * Get active portfolio count
     */
    public long getActivePortfolioCount() {
        return portfolioRepository.findByActiveTrue().size();
    }
}
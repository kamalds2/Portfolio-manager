package Managefolio.admin.repository;

import Managefolio.admin.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    
    /**
     * Find portfolio by portfolio code
     */
    Optional<Portfolio> findByPortfolioCode(String portfolioCode);
    
    /**
     * Check if portfolio code exists (for validation)
     */
    boolean existsByPortfolioCode(String portfolioCode);
    
    /**
     * Check if portfolio code exists excluding current portfolio (for updates)
     */
    boolean existsByPortfolioCodeAndIdNot(String portfolioCode, Long id);
    
    /**
     * Find all active portfolios
     */
    List<Portfolio> findByActiveTrue();
    
    /**
     * Find portfolios by name containing (case insensitive)
     */
    @Query("SELECT p FROM Portfolio p WHERE LOWER(p.portfolioName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Portfolio> findByPortfolioNameContainingIgnoreCase(String name);
    
    /**
     * Find all portfolios ordered by name
     */
    List<Portfolio> findAllByOrderByPortfolioNameAsc();
    
    /**
     * Find all active portfolios ordered by name
     */
    List<Portfolio> findByActiveTrueOrderByPortfolioNameAsc();
}
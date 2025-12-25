package Managefolio.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tbl_portfolios")
@EntityListeners(AuditingEntityListener.class)
public class Portfolio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Portfolio name is required")
    @Size(max = 100, message = "Portfolio name must not exceed 100 characters")
    @Column(name = "portfolio_name", nullable = false)
    private String portfolioName;
    
    @NotBlank(message = "Portfolio link is required")
    @Size(max = 500, message = "Portfolio link must not exceed 500 characters")
    @Column(name = "portfolio_link", nullable = false)
    private String portfolioLink;
    
    @NotBlank(message = "Portfolio code is required")
    @Size(max = 10, message = "Portfolio code must not exceed 10 characters")
    @Column(name = "portfolio_code", nullable = false, unique = true)
    private String portfolioCode;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_active")
    private Boolean active = true;
    
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;
    
    // Default constructor
    public Portfolio() {}
    
    // Constructor with required fields
    public Portfolio(String portfolioName, String portfolioLink, String portfolioCode) {
        this.portfolioName = portfolioName;
        this.portfolioLink = portfolioLink;
        this.portfolioCode = portfolioCode;
        this.active = true;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPortfolioName() {
        return portfolioName;
    }
    
    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }
    
    public String getPortfolioLink() {
        return portfolioLink;
    }
    
    public void setPortfolioLink(String portfolioLink) {
        this.portfolioLink = portfolioLink;
    }
    
    public String getPortfolioCode() {
        return portfolioCode;
    }
    
    public void setPortfolioCode(String portfolioCode) {
        this.portfolioCode = portfolioCode;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    @Override
    public String toString() {
        return "Portfolio{" +
                "id=" + id +
                ", portfolioName='" + portfolioName + '\'' +
                ", portfolioCode='" + portfolioCode + '\'' +
                ", active=" + active +
                '}';
    }
}
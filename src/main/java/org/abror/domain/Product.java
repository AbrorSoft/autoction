package org.abror.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.abror.domain.enumeration.AuctionCategory;
import org.abror.domain.enumeration.Classification;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "auction_category", nullable = false)
    private AuctionCategory auctionCategory;

    @NotNull
    @Column(name = "lot_number", nullable = false, unique = true)
    private Long lotNumber;

    @NotNull
    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "image_key")
    private String imageKey;

    @NotNull
    @Column(name = "produced_year", nullable = false)
    private Instant producedYear;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "classification", nullable = false)
    private Classification classification;

    @NotNull
    @Column(name = "estimated_price", nullable = false)
    private Double estimatedPrice;

    @Column(name = "description")
    private String description;

    @Column(name = "auction_date")
    private Instant auctionDate;

    @Column(name = "additional_information")
    private String additionalInformation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuctionCategory getAuctionCategory() {
        return this.auctionCategory;
    }

    public Product auctionCategory(AuctionCategory auctionCategory) {
        this.setAuctionCategory(auctionCategory);
        return this;
    }

    public void setAuctionCategory(AuctionCategory auctionCategory) {
        this.auctionCategory = auctionCategory;
    }

    public Long getLotNumber() {
        return this.lotNumber;
    }

    public Product lotNumber(Long lotNumber) {
        this.setLotNumber(lotNumber);
        return this;
    }

    public void setLotNumber(Long lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public Product authorName(String authorName) {
        this.setAuthorName(authorName);
        return this;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public Instant getProducedYear() {
        return this.producedYear;
    }

    public Product producedYear(Instant producedYear) {
        this.setProducedYear(producedYear);
        return this;
    }

    public void setProducedYear(Instant producedYear) {
        this.producedYear = producedYear;
    }

    public Classification getClassification() {
        return this.classification;
    }

    public Product classification(Classification classification) {
        this.setClassification(classification);
        return this;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Double getEstimatedPrice() {
        return this.estimatedPrice;
    }

    public Product estimatedPrice(Double estimatedPrice) {
        this.setEstimatedPrice(estimatedPrice);
        return this;
    }

    public void setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getAuctionDate() {
        return this.auctionDate;
    }

    public Product auctionDate(Instant auctionDate) {
        this.setAuctionDate(auctionDate);
        return this;
    }

    public void setAuctionDate(Instant auctionDate) {
        this.auctionDate = auctionDate;
    }

    public String getAdditionalInformation() {
        return this.additionalInformation;
    }

    public Product additionalInformation(String additionalInformation) {
        this.setAdditionalInformation(additionalInformation);
        return this;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", auctionCategory='" + getAuctionCategory() + "'" +
            ", lotNumber=" + getLotNumber() +
            ", authorName='" + getAuthorName() + "'" +
            ", imageKey='" + getImageKey() + "'" +
            ", producedYear='" + getProducedYear() + "'" +
            ", classification='" + getClassification() + "'" +
            ", estimatedPrice=" + getEstimatedPrice() +
            ", description='" + getDescription() + "'" +
            ", auctionDate='" + getAuctionDate() + "'" +
            ", additionalInformation='" + getAdditionalInformation() + "'" +
            "}";
    }
}

package org.abror.service.dto;

import jakarta.validation.constraints.NotNull;
import org.abror.domain.enumeration.AuctionCategory;
import org.abror.domain.enumeration.Classification;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * A DTO for the {@link org.abror.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private AuctionCategory auctionCategory;

    @NotNull
    private Long lotNumber;

    @NotNull
    private String authorName;

    private String imageKey;

    private byte[] imageFile;

    @NotNull
    private Instant producedYear;

    @NotNull
    private Classification classification;

    @NotNull
    private Double estimatedPrice;

    private String description;

    private Instant auctionDate;

    private Map<String, Object> additionalInformation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuctionCategory getAuctionCategory() {
        return auctionCategory;
    }

    public void setAuctionCategory(AuctionCategory auctionCategory) {
        this.auctionCategory = auctionCategory;
    }

    public Long getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(Long lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getAuthorName() {
        return authorName;
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

    public byte[] getImageFile() {
        return imageFile;
    }

    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }

    public Instant getProducedYear() {
        return producedYear;
    }

    public void setProducedYear(Instant producedYear) {
        this.producedYear = producedYear;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getAuctionDate() {
        return auctionDate;
    }

    public void setAuctionDate(Instant auctionDate) {
        this.auctionDate = auctionDate;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", auctionCategory='" + getAuctionCategory() + "'" +
            ", lotNumber=" + getLotNumber() +
            ", authorName='" + getAuthorName() + "'" +
            ", producedYear='" + getProducedYear() + "'" +
            ", imageKey='" + getImageKey() + "'" +
            ", classification='" + getClassification() + "'" +
            ", estimatedPrice=" + getEstimatedPrice() +
            ", description='" + getDescription() + "'" +
            ", auctionDate='" + getAuctionDate() + "'" +
            ", additionalInformation='" + getAdditionalInformation() + "'" +
            "}";
    }
}

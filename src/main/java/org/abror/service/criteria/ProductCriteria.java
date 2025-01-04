package org.abror.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.abror.domain.enumeration.AuctionCategory;
import org.abror.domain.enumeration.Classification;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.abror.domain.Product} entity. This class is used
 * in {@link org.abror.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AuctionCategory
     */
    public static class AuctionCategoryFilter extends Filter<AuctionCategory> {

        public AuctionCategoryFilter() {}

        public AuctionCategoryFilter(AuctionCategoryFilter filter) {
            super(filter);
        }

        @Override
        public AuctionCategoryFilter copy() {
            return new AuctionCategoryFilter(this);
        }
    }

    /**
     * Class for filtering Classification
     */
    public static class ClassificationFilter extends Filter<Classification> {

        public ClassificationFilter() {}

        public ClassificationFilter(ClassificationFilter filter) {
            super(filter);
        }

        @Override
        public ClassificationFilter copy() {
            return new ClassificationFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private AuctionCategoryFilter auctionCategory;

    private LongFilter lotNumber;

    private StringFilter authorName;

    private InstantFilter producedYear;

    private ClassificationFilter classification;

    private DoubleFilter estimatedPrice;

    private StringFilter description;

    private InstantFilter auctionDate;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.auctionCategory = other.optionalAuctionCategory().map(AuctionCategoryFilter::copy).orElse(null);
        this.lotNumber = other.optionalLotNumber().map(LongFilter::copy).orElse(null);
        this.authorName = other.optionalAuthorName().map(StringFilter::copy).orElse(null);
        this.producedYear = other.optionalProducedYear().map(InstantFilter::copy).orElse(null);
        this.classification = other.optionalClassification().map(ClassificationFilter::copy).orElse(null);
        this.estimatedPrice = other.optionalEstimatedPrice().map(DoubleFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.auctionDate = other.optionalAuctionDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public AuctionCategoryFilter getAuctionCategory() {
        return auctionCategory;
    }

    public Optional<AuctionCategoryFilter> optionalAuctionCategory() {
        return Optional.ofNullable(auctionCategory);
    }

    public AuctionCategoryFilter auctionCategory() {
        if (auctionCategory == null) {
            setAuctionCategory(new AuctionCategoryFilter());
        }
        return auctionCategory;
    }

    public void setAuctionCategory(AuctionCategoryFilter auctionCategory) {
        this.auctionCategory = auctionCategory;
    }

    public LongFilter getLotNumber() {
        return lotNumber;
    }

    public Optional<LongFilter> optionalLotNumber() {
        return Optional.ofNullable(lotNumber);
    }

    public LongFilter lotNumber() {
        if (lotNumber == null) {
            setLotNumber(new LongFilter());
        }
        return lotNumber;
    }

    public void setLotNumber(LongFilter lotNumber) {
        this.lotNumber = lotNumber;
    }

    public StringFilter getAuthorName() {
        return authorName;
    }

    public Optional<StringFilter> optionalAuthorName() {
        return Optional.ofNullable(authorName);
    }

    public StringFilter authorName() {
        if (authorName == null) {
            setAuthorName(new StringFilter());
        }
        return authorName;
    }

    public void setAuthorName(StringFilter authorName) {
        this.authorName = authorName;
    }

    public InstantFilter getProducedYear() {
        return producedYear;
    }

    public Optional<InstantFilter> optionalProducedYear() {
        return Optional.ofNullable(producedYear);
    }

    public InstantFilter producedYear() {
        if (producedYear == null) {
            setProducedYear(new InstantFilter());
        }
        return producedYear;
    }

    public void setProducedYear(InstantFilter producedYear) {
        this.producedYear = producedYear;
    }

    public ClassificationFilter getClassification() {
        return classification;
    }

    public Optional<ClassificationFilter> optionalClassification() {
        return Optional.ofNullable(classification);
    }

    public ClassificationFilter classification() {
        if (classification == null) {
            setClassification(new ClassificationFilter());
        }
        return classification;
    }

    public void setClassification(ClassificationFilter classification) {
        this.classification = classification;
    }

    public DoubleFilter getEstimatedPrice() {
        return estimatedPrice;
    }

    public Optional<DoubleFilter> optionalEstimatedPrice() {
        return Optional.ofNullable(estimatedPrice);
    }

    public DoubleFilter estimatedPrice() {
        if (estimatedPrice == null) {
            setEstimatedPrice(new DoubleFilter());
        }
        return estimatedPrice;
    }

    public void setEstimatedPrice(DoubleFilter estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getAuctionDate() {
        return auctionDate;
    }

    public Optional<InstantFilter> optionalAuctionDate() {
        return Optional.ofNullable(auctionDate);
    }

    public InstantFilter auctionDate() {
        if (auctionDate == null) {
            setAuctionDate(new InstantFilter());
        }
        return auctionDate;
    }

    public void setAuctionDate(InstantFilter auctionDate) {
        this.auctionDate = auctionDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(auctionCategory, that.auctionCategory) &&
            Objects.equals(lotNumber, that.lotNumber) &&
            Objects.equals(authorName, that.authorName) &&
            Objects.equals(producedYear, that.producedYear) &&
            Objects.equals(classification, that.classification) &&
            Objects.equals(estimatedPrice, that.estimatedPrice) &&
            Objects.equals(description, that.description) &&
            Objects.equals(auctionDate, that.auctionDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            auctionCategory,
            lotNumber,
            authorName,
            producedYear,
            classification,
            estimatedPrice,
            description,
            auctionDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAuctionCategory().map(f -> "auctionCategory=" + f + ", ").orElse("") +
            optionalLotNumber().map(f -> "lotNumber=" + f + ", ").orElse("") +
            optionalAuthorName().map(f -> "authorName=" + f + ", ").orElse("") +
            optionalProducedYear().map(f -> "producedYear=" + f + ", ").orElse("") +
            optionalClassification().map(f -> "classification=" + f + ", ").orElse("") +
            optionalEstimatedPrice().map(f -> "estimatedPrice=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalAuctionDate().map(f -> "auctionDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

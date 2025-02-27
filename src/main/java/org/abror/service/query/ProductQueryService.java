package org.abror.service.query;

import org.abror.domain.*;
import org.abror.repository.ProductRepository;
import org.abror.service.criteria.ProductCriteria;
import org.abror.service.dto.ProductDTO;
import org.abror.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page).map(productMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getAuctionCategory() != null) {
                specification = specification.and(buildSpecification(criteria.getAuctionCategory(), Product_.auctionCategory));
            }
            if (criteria.getLotNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLotNumber(), Product_.lotNumber));
            }
            if (criteria.getAuthorName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthorName(), Product_.authorName));
            }
            if (criteria.getProducedYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProducedYear(), Product_.producedYear));
            }
            if (criteria.getClassification() != null) {
                specification = specification.and(buildSpecification(criteria.getClassification(), Product_.classification));
            }
            if (criteria.getEstimatedPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstimatedPrice(), Product_.estimatedPrice));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Product_.description));
            }
            if (criteria.getAuctionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAuctionDate(), Product_.auctionDate));
            }
        }
        return specification;
    }
}

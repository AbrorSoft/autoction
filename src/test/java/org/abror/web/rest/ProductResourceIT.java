package org.abror.web.rest;

import static org.abror.domain.ProductAsserts.*;
import static org.abror.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.abror.IntegrationTest;
import org.abror.domain.Product;
import org.abror.domain.enumeration.AuctionCategory;
import org.abror.domain.enumeration.Classification;
import org.abror.repository.ProductRepository;
import org.abror.service.dto.ProductDTO;
import org.abror.service.mapper.ProductMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final AuctionCategory DEFAULT_AUCTION_CATEGORY = AuctionCategory.PAINTINGS;
    private static final AuctionCategory UPDATED_AUCTION_CATEGORY = AuctionCategory.DRAWINGS;

    private static final Long DEFAULT_LOT_NUMBER = 1L;
    private static final Long UPDATED_LOT_NUMBER = 2L;
    private static final Long SMALLER_LOT_NUMBER = 1L - 1L;

    private static final String DEFAULT_AUTHOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_PRODUCED_YEAR = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PRODUCED_YEAR = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Classification DEFAULT_CLASSIFICATION = Classification.LANDSCAPE;
    private static final Classification UPDATED_CLASSIFICATION = Classification.SEASCAPE;

    private static final Double DEFAULT_ESTIMATED_PRICE = 1D;
    private static final Double UPDATED_ESTIMATED_PRICE = 2D;
    private static final Double SMALLER_ESTIMATED_PRICE = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_AUCTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_AUCTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    private Product insertedProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity() {
        return new Product()
            .auctionCategory(DEFAULT_AUCTION_CATEGORY)
            .lotNumber(DEFAULT_LOT_NUMBER)
            .authorName(DEFAULT_AUTHOR_NAME)
            .producedYear(DEFAULT_PRODUCED_YEAR)
            .classification(DEFAULT_CLASSIFICATION)
            .estimatedPrice(DEFAULT_ESTIMATED_PRICE)
            .description(DEFAULT_DESCRIPTION)
            .auctionDate(DEFAULT_AUCTION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity() {
        return new Product()
            .auctionCategory(UPDATED_AUCTION_CATEGORY)
            .lotNumber(UPDATED_LOT_NUMBER)
            .authorName(UPDATED_AUTHOR_NAME)
            .producedYear(UPDATED_PRODUCED_YEAR)
            .classification(UPDATED_CLASSIFICATION)
            .estimatedPrice(UPDATED_ESTIMATED_PRICE)
            .description(UPDATED_DESCRIPTION)
            .auctionDate(UPDATED_AUCTION_DATE);
    }

    @BeforeEach
    public void initTest() {
        product = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProduct != null) {
            productRepository.delete(insertedProduct);
            insertedProduct = null;
        }
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        var returnedProductDTO = om.readValue(
            restProductMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductDTO.class
        );

        // Validate the Product in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProduct = productMapper.toEntity(returnedProductDTO);
        assertProductUpdatableFieldsEquals(returnedProduct, getPersistedProduct(returnedProduct));

        insertedProduct = returnedProduct;
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAuctionCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setAuctionCategory(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLotNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setLotNumber(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAuthorNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setAuthorName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProducedYearIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setProducedYear(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClassificationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setClassification(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstimatedPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setEstimatedPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].auctionCategory").value(hasItem(DEFAULT_AUCTION_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].lotNumber").value(hasItem(DEFAULT_LOT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].authorName").value(hasItem(DEFAULT_AUTHOR_NAME)))
            .andExpect(jsonPath("$.[*].producedYear").value(hasItem(DEFAULT_PRODUCED_YEAR.toString())))
            .andExpect(jsonPath("$.[*].classification").value(hasItem(DEFAULT_CLASSIFICATION.toString())))
            .andExpect(jsonPath("$.[*].estimatedPrice").value(hasItem(DEFAULT_ESTIMATED_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].auctionDate").value(hasItem(DEFAULT_AUCTION_DATE.toString())));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.auctionCategory").value(DEFAULT_AUCTION_CATEGORY.toString()))
            .andExpect(jsonPath("$.lotNumber").value(DEFAULT_LOT_NUMBER.intValue()))
            .andExpect(jsonPath("$.authorName").value(DEFAULT_AUTHOR_NAME))
            .andExpect(jsonPath("$.producedYear").value(DEFAULT_PRODUCED_YEAR.toString()))
            .andExpect(jsonPath("$.classification").value(DEFAULT_CLASSIFICATION.toString()))
            .andExpect(jsonPath("$.estimatedPrice").value(DEFAULT_ESTIMATED_PRICE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.auctionDate").value(DEFAULT_AUCTION_DATE.toString()));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByAuctionCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where auctionCategory equals to
        defaultProductFiltering("auctionCategory.equals=" + DEFAULT_AUCTION_CATEGORY, "auctionCategory.equals=" + UPDATED_AUCTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByAuctionCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where auctionCategory in
        defaultProductFiltering(
            "auctionCategory.in=" + DEFAULT_AUCTION_CATEGORY + "," + UPDATED_AUCTION_CATEGORY,
            "auctionCategory.in=" + UPDATED_AUCTION_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllProductsByAuctionCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where auctionCategory is not null
        defaultProductFiltering("auctionCategory.specified=true", "auctionCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByLotNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lotNumber equals to
        defaultProductFiltering("lotNumber.equals=" + DEFAULT_LOT_NUMBER, "lotNumber.equals=" + UPDATED_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllProductsByLotNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lotNumber in
        defaultProductFiltering("lotNumber.in=" + DEFAULT_LOT_NUMBER + "," + UPDATED_LOT_NUMBER, "lotNumber.in=" + UPDATED_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllProductsByLotNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lotNumber is not null
        defaultProductFiltering("lotNumber.specified=true", "lotNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByLotNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lotNumber is greater than or equal to
        defaultProductFiltering("lotNumber.greaterThanOrEqual=" + DEFAULT_LOT_NUMBER, "lotNumber.greaterThanOrEqual=" + UPDATED_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllProductsByLotNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lotNumber is less than or equal to
        defaultProductFiltering("lotNumber.lessThanOrEqual=" + DEFAULT_LOT_NUMBER, "lotNumber.lessThanOrEqual=" + SMALLER_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllProductsByLotNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lotNumber is less than
        defaultProductFiltering("lotNumber.lessThan=" + UPDATED_LOT_NUMBER, "lotNumber.lessThan=" + DEFAULT_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllProductsByLotNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where lotNumber is greater than
        defaultProductFiltering("lotNumber.greaterThan=" + SMALLER_LOT_NUMBER, "lotNumber.greaterThan=" + DEFAULT_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllProductsByAuthorNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where authorName equals to
        defaultProductFiltering("authorName.equals=" + DEFAULT_AUTHOR_NAME, "authorName.equals=" + UPDATED_AUTHOR_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByAuthorNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where authorName in
        defaultProductFiltering("authorName.in=" + DEFAULT_AUTHOR_NAME + "," + UPDATED_AUTHOR_NAME, "authorName.in=" + UPDATED_AUTHOR_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByAuthorNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where authorName is not null
        defaultProductFiltering("authorName.specified=true", "authorName.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByAuthorNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where authorName contains
        defaultProductFiltering("authorName.contains=" + DEFAULT_AUTHOR_NAME, "authorName.contains=" + UPDATED_AUTHOR_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByAuthorNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where authorName does not contain
        defaultProductFiltering("authorName.doesNotContain=" + UPDATED_AUTHOR_NAME, "authorName.doesNotContain=" + DEFAULT_AUTHOR_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProducedYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where producedYear equals to
        defaultProductFiltering("producedYear.equals=" + DEFAULT_PRODUCED_YEAR, "producedYear.equals=" + UPDATED_PRODUCED_YEAR);
    }

    @Test
    @Transactional
    void getAllProductsByProducedYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where producedYear in
        defaultProductFiltering(
            "producedYear.in=" + DEFAULT_PRODUCED_YEAR + "," + UPDATED_PRODUCED_YEAR,
            "producedYear.in=" + UPDATED_PRODUCED_YEAR
        );
    }

    @Test
    @Transactional
    void getAllProductsByProducedYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where producedYear is not null
        defaultProductFiltering("producedYear.specified=true", "producedYear.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByClassificationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where classification equals to
        defaultProductFiltering("classification.equals=" + DEFAULT_CLASSIFICATION, "classification.equals=" + UPDATED_CLASSIFICATION);
    }

    @Test
    @Transactional
    void getAllProductsByClassificationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where classification in
        defaultProductFiltering(
            "classification.in=" + DEFAULT_CLASSIFICATION + "," + UPDATED_CLASSIFICATION,
            "classification.in=" + UPDATED_CLASSIFICATION
        );
    }

    @Test
    @Transactional
    void getAllProductsByClassificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where classification is not null
        defaultProductFiltering("classification.specified=true", "classification.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByEstimatedPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where estimatedPrice equals to
        defaultProductFiltering("estimatedPrice.equals=" + DEFAULT_ESTIMATED_PRICE, "estimatedPrice.equals=" + UPDATED_ESTIMATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByEstimatedPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where estimatedPrice in
        defaultProductFiltering(
            "estimatedPrice.in=" + DEFAULT_ESTIMATED_PRICE + "," + UPDATED_ESTIMATED_PRICE,
            "estimatedPrice.in=" + UPDATED_ESTIMATED_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByEstimatedPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where estimatedPrice is not null
        defaultProductFiltering("estimatedPrice.specified=true", "estimatedPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByEstimatedPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where estimatedPrice is greater than or equal to
        defaultProductFiltering(
            "estimatedPrice.greaterThanOrEqual=" + DEFAULT_ESTIMATED_PRICE,
            "estimatedPrice.greaterThanOrEqual=" + UPDATED_ESTIMATED_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByEstimatedPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where estimatedPrice is less than or equal to
        defaultProductFiltering(
            "estimatedPrice.lessThanOrEqual=" + DEFAULT_ESTIMATED_PRICE,
            "estimatedPrice.lessThanOrEqual=" + SMALLER_ESTIMATED_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByEstimatedPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where estimatedPrice is less than
        defaultProductFiltering("estimatedPrice.lessThan=" + UPDATED_ESTIMATED_PRICE, "estimatedPrice.lessThan=" + DEFAULT_ESTIMATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByEstimatedPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where estimatedPrice is greater than
        defaultProductFiltering(
            "estimatedPrice.greaterThan=" + SMALLER_ESTIMATED_PRICE,
            "estimatedPrice.greaterThan=" + DEFAULT_ESTIMATED_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description equals to
        defaultProductFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description in
        defaultProductFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description contains
        defaultProductFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain
        defaultProductFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByAuctionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where auctionDate equals to
        defaultProductFiltering("auctionDate.equals=" + DEFAULT_AUCTION_DATE, "auctionDate.equals=" + UPDATED_AUCTION_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByAuctionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where auctionDate in
        defaultProductFiltering(
            "auctionDate.in=" + DEFAULT_AUCTION_DATE + "," + UPDATED_AUCTION_DATE,
            "auctionDate.in=" + UPDATED_AUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductsByAuctionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where auctionDate is not null
        defaultProductFiltering("auctionDate.specified=true", "auctionDate.specified=false");
    }

    private void defaultProductFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductShouldBeFound(shouldBeFound);
        defaultProductShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].auctionCategory").value(hasItem(DEFAULT_AUCTION_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].lotNumber").value(hasItem(DEFAULT_LOT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].authorName").value(hasItem(DEFAULT_AUTHOR_NAME)))
            .andExpect(jsonPath("$.[*].producedYear").value(hasItem(DEFAULT_PRODUCED_YEAR.toString())))
            .andExpect(jsonPath("$.[*].classification").value(hasItem(DEFAULT_CLASSIFICATION.toString())))
            .andExpect(jsonPath("$.[*].estimatedPrice").value(hasItem(DEFAULT_ESTIMATED_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].auctionDate").value(hasItem(DEFAULT_AUCTION_DATE.toString())));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .auctionCategory(UPDATED_AUCTION_CATEGORY)
            .lotNumber(UPDATED_LOT_NUMBER)
            .authorName(UPDATED_AUTHOR_NAME)
            .producedYear(UPDATED_PRODUCED_YEAR)
            .classification(UPDATED_CLASSIFICATION)
            .estimatedPrice(UPDATED_ESTIMATED_PRICE)
            .description(UPDATED_DESCRIPTION)
            .auctionDate(UPDATED_AUCTION_DATE);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductToMatchAllProperties(updatedProduct);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .producedYear(UPDATED_PRODUCED_YEAR)
            .authorName(UPDATED_AUTHOR_NAME)
            .classification(UPDATED_CLASSIFICATION)
            .lotNumber(UPDATED_LOT_NUMBER)
            .auctionCategory(UPDATED_AUCTION_CATEGORY)
            .estimatedPrice(UPDATED_ESTIMATED_PRICE)
            .description(UPDATED_DESCRIPTION)
            .auctionDate(UPDATED_AUCTION_DATE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduct, product), getPersistedProduct(product));
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .auctionCategory(UPDATED_AUCTION_CATEGORY)
            .lotNumber(UPDATED_LOT_NUMBER)
            .authorName(UPDATED_AUTHOR_NAME)
            .producedYear(UPDATED_PRODUCED_YEAR)
            .classification(UPDATED_CLASSIFICATION)
            .estimatedPrice(UPDATED_ESTIMATED_PRICE)
            .description(UPDATED_DESCRIPTION)
            .auctionDate(UPDATED_AUCTION_DATE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(partialUpdatedProduct, getPersistedProduct(partialUpdatedProduct));
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Product getPersistedProduct(Product product) {
        return productRepository.findById(product.getId()).orElseThrow();
    }

    protected void assertPersistedProductToMatchAllProperties(Product expectedProduct) {
        assertProductAllPropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }

    protected void assertPersistedProductToMatchUpdatableProperties(Product expectedProduct) {
        assertProductAllUpdatablePropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }
}

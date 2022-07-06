package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Userk;
import com.mycompany.myapp.repository.UserkRepository;
import com.mycompany.myapp.service.UserkService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserkResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserkResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/userks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserkRepository userkRepository;

    @Mock
    private UserkRepository userkRepositoryMock;

    @Mock
    private UserkService userkServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserkMockMvc;

    private Userk userk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Userk createEntity(EntityManager em) {
        Userk userk = new Userk().username(DEFAULT_USERNAME).email(DEFAULT_EMAIL).firstname(DEFAULT_FIRSTNAME).lastname(DEFAULT_LASTNAME);
        return userk;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Userk createUpdatedEntity(EntityManager em) {
        Userk userk = new Userk().username(UPDATED_USERNAME).email(UPDATED_EMAIL).firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME);
        return userk;
    }

    @BeforeEach
    public void initTest() {
        userk = createEntity(em);
    }

    @Test
    @Transactional
    void createUserk() throws Exception {
        int databaseSizeBeforeCreate = userkRepository.findAll().size();
        // Create the Userk
        restUserkMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userk))
            )
            .andExpect(status().isCreated());

        // Validate the Userk in the database
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeCreate + 1);
        Userk testUserk = userkList.get(userkList.size() - 1);
        assertThat(testUserk.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserk.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserk.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testUserk.getLastname()).isEqualTo(DEFAULT_LASTNAME);
    }

    @Test
    @Transactional
    void createUserkWithExistingId() throws Exception {
        // Create the Userk with an existing ID
        userk.setId(1L);

        int databaseSizeBeforeCreate = userkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserkMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Userk in the database
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userkRepository.findAll().size();
        // set the field null
        userk.setUsername(null);

        // Create the Userk, which fails.

        restUserkMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userk))
            )
            .andExpect(status().isBadRequest());

        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserks() throws Exception {
        // Initialize the database
        userkRepository.saveAndFlush(userk);

        // Get all the userkList
        restUserkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userk.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserksWithEagerRelationshipsIsEnabled() throws Exception {
        when(userkServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserkMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userkServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userkServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserkMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userkServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUserk() throws Exception {
        // Initialize the database
        userkRepository.saveAndFlush(userk);

        // Get the userk
        restUserkMockMvc
            .perform(get(ENTITY_API_URL_ID, userk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userk.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME));
    }

    @Test
    @Transactional
    void getNonExistingUserk() throws Exception {
        // Get the userk
        restUserkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserk() throws Exception {
        // Initialize the database
        userkRepository.saveAndFlush(userk);

        int databaseSizeBeforeUpdate = userkRepository.findAll().size();

        // Update the userk
        Userk updatedUserk = userkRepository.findById(userk.getId()).get();
        // Disconnect from session so that the updates on updatedUserk are not directly saved in db
        em.detach(updatedUserk);
        updatedUserk.username(UPDATED_USERNAME).email(UPDATED_EMAIL).firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME);

        restUserkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserk.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserk))
            )
            .andExpect(status().isOk());

        // Validate the Userk in the database
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeUpdate);
        Userk testUserk = userkList.get(userkList.size() - 1);
        assertThat(testUserk.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserk.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserk.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUserk.getLastname()).isEqualTo(UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void putNonExistingUserk() throws Exception {
        int databaseSizeBeforeUpdate = userkRepository.findAll().size();
        userk.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userk.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Userk in the database
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserk() throws Exception {
        int databaseSizeBeforeUpdate = userkRepository.findAll().size();
        userk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Userk in the database
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserk() throws Exception {
        int databaseSizeBeforeUpdate = userkRepository.findAll().size();
        userk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserkMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userk))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Userk in the database
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserkWithPatch() throws Exception {
        // Initialize the database
        userkRepository.saveAndFlush(userk);

        int databaseSizeBeforeUpdate = userkRepository.findAll().size();

        // Update the userk using partial update
        Userk partialUpdatedUserk = new Userk();
        partialUpdatedUserk.setId(userk.getId());

        partialUpdatedUserk.lastname(UPDATED_LASTNAME);

        restUserkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserk.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserk))
            )
            .andExpect(status().isOk());

        // Validate the Userk in the database
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeUpdate);
        Userk testUserk = userkList.get(userkList.size() - 1);
        assertThat(testUserk.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserk.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserk.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testUserk.getLastname()).isEqualTo(UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void fullUpdateUserkWithPatch() throws Exception {
        // Initialize the database
        userkRepository.saveAndFlush(userk);

        int databaseSizeBeforeUpdate = userkRepository.findAll().size();

        // Update the userk using partial update
        Userk partialUpdatedUserk = new Userk();
        partialUpdatedUserk.setId(userk.getId());

        partialUpdatedUserk.username(UPDATED_USERNAME).email(UPDATED_EMAIL).firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME);

        restUserkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserk.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserk))
            )
            .andExpect(status().isOk());

        // Validate the Userk in the database
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeUpdate);
        Userk testUserk = userkList.get(userkList.size() - 1);
        assertThat(testUserk.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserk.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserk.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUserk.getLastname()).isEqualTo(UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void patchNonExistingUserk() throws Exception {
        int databaseSizeBeforeUpdate = userkRepository.findAll().size();
        userk.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userk.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Userk in the database
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserk() throws Exception {
        int databaseSizeBeforeUpdate = userkRepository.findAll().size();
        userk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Userk in the database
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserk() throws Exception {
        int databaseSizeBeforeUpdate = userkRepository.findAll().size();
        userk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserkMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userk))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Userk in the database
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserk() throws Exception {
        // Initialize the database
        userkRepository.saveAndFlush(userk);

        int databaseSizeBeforeDelete = userkRepository.findAll().size();

        // Delete the userk
        restUserkMockMvc
            .perform(delete(ENTITY_API_URL_ID, userk.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Userk> userkList = userkRepository.findAll();
        assertThat(userkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

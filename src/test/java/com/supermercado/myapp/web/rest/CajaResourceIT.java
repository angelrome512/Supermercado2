package com.supermercado.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.supermercado.myapp.IntegrationTest;
import com.supermercado.myapp.domain.Caja;
import com.supermercado.myapp.repository.CajaRepository;
import com.supermercado.myapp.service.dto.CajaDTO;
import com.supermercado.myapp.service.mapper.CajaMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CajaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CajaResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_SALDO_INICIAL = 1D;
    private static final Double UPDATED_SALDO_INICIAL = 2D;

    private static final Double DEFAULT_TOTAL_EFECTIVO = 1D;
    private static final Double UPDATED_TOTAL_EFECTIVO = 2D;

    private static final Double DEFAULT_TOTAL_TARJETA = 1D;
    private static final Double UPDATED_TOTAL_TARJETA = 2D;

    private static final Double DEFAULT_SALDO_TOTAL = 1D;
    private static final Double UPDATED_SALDO_TOTAL = 2D;

    private static final String ENTITY_API_URL = "/api/cajas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CajaRepository cajaRepository;

    @Autowired
    private CajaMapper cajaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCajaMockMvc;

    private Caja caja;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caja createEntity(EntityManager em) {
        Caja caja = new Caja()
            .fecha(DEFAULT_FECHA)
            .saldoInicial(DEFAULT_SALDO_INICIAL)
            .totalEfectivo(DEFAULT_TOTAL_EFECTIVO)
            .totalTarjeta(DEFAULT_TOTAL_TARJETA)
            .saldoTotal(DEFAULT_SALDO_TOTAL);
        return caja;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caja createUpdatedEntity(EntityManager em) {
        Caja caja = new Caja()
            .fecha(UPDATED_FECHA)
            .saldoInicial(UPDATED_SALDO_INICIAL)
            .totalEfectivo(UPDATED_TOTAL_EFECTIVO)
            .totalTarjeta(UPDATED_TOTAL_TARJETA)
            .saldoTotal(UPDATED_SALDO_TOTAL);
        return caja;
    }

    @BeforeEach
    public void initTest() {
        caja = createEntity(em);
    }

    @Test
    @Transactional
    void createCaja() throws Exception {
        int databaseSizeBeforeCreate = cajaRepository.findAll().size();
        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);
        restCajaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaDTO)))
            .andExpect(status().isCreated());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeCreate + 1);
        Caja testCaja = cajaList.get(cajaList.size() - 1);
        assertThat(testCaja.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testCaja.getSaldoInicial()).isEqualTo(DEFAULT_SALDO_INICIAL);
        assertThat(testCaja.getTotalEfectivo()).isEqualTo(DEFAULT_TOTAL_EFECTIVO);
        assertThat(testCaja.getTotalTarjeta()).isEqualTo(DEFAULT_TOTAL_TARJETA);
        assertThat(testCaja.getSaldoTotal()).isEqualTo(DEFAULT_SALDO_TOTAL);
    }

    @Test
    @Transactional
    void createCajaWithExistingId() throws Exception {
        // Create the Caja with an existing ID
        caja.setId(1L);
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        int databaseSizeBeforeCreate = cajaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCajaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSaldoInicialIsRequired() throws Exception {
        int databaseSizeBeforeTest = cajaRepository.findAll().size();
        // set the field null
        caja.setSaldoInicial(null);

        // Create the Caja, which fails.
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        restCajaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaDTO)))
            .andExpect(status().isBadRequest());

        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCajas() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        // Get all the cajaList
        restCajaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caja.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].saldoInicial").value(hasItem(DEFAULT_SALDO_INICIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].totalEfectivo").value(hasItem(DEFAULT_TOTAL_EFECTIVO.doubleValue())))
            .andExpect(jsonPath("$.[*].totalTarjeta").value(hasItem(DEFAULT_TOTAL_TARJETA.doubleValue())))
            .andExpect(jsonPath("$.[*].saldoTotal").value(hasItem(DEFAULT_SALDO_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    void getCaja() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        // Get the caja
        restCajaMockMvc
            .perform(get(ENTITY_API_URL_ID, caja.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caja.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.saldoInicial").value(DEFAULT_SALDO_INICIAL.doubleValue()))
            .andExpect(jsonPath("$.totalEfectivo").value(DEFAULT_TOTAL_EFECTIVO.doubleValue()))
            .andExpect(jsonPath("$.totalTarjeta").value(DEFAULT_TOTAL_TARJETA.doubleValue()))
            .andExpect(jsonPath("$.saldoTotal").value(DEFAULT_SALDO_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCaja() throws Exception {
        // Get the caja
        restCajaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCaja() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();

        // Update the caja
        Caja updatedCaja = cajaRepository.findById(caja.getId()).get();
        // Disconnect from session so that the updates on updatedCaja are not directly saved in db
        em.detach(updatedCaja);
        updatedCaja
            .fecha(UPDATED_FECHA)
            .saldoInicial(UPDATED_SALDO_INICIAL)
            .totalEfectivo(UPDATED_TOTAL_EFECTIVO)
            .totalTarjeta(UPDATED_TOTAL_TARJETA)
            .saldoTotal(UPDATED_SALDO_TOTAL);
        CajaDTO cajaDTO = cajaMapper.toDto(updatedCaja);

        restCajaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cajaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
        Caja testCaja = cajaList.get(cajaList.size() - 1);
        assertThat(testCaja.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testCaja.getSaldoInicial()).isEqualTo(UPDATED_SALDO_INICIAL);
        assertThat(testCaja.getTotalEfectivo()).isEqualTo(UPDATED_TOTAL_EFECTIVO);
        assertThat(testCaja.getTotalTarjeta()).isEqualTo(UPDATED_TOTAL_TARJETA);
        assertThat(testCaja.getSaldoTotal()).isEqualTo(UPDATED_SALDO_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cajaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCajaWithPatch() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();

        // Update the caja using partial update
        Caja partialUpdatedCaja = new Caja();
        partialUpdatedCaja.setId(caja.getId());

        partialUpdatedCaja.fecha(UPDATED_FECHA);

        restCajaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaja.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaja))
            )
            .andExpect(status().isOk());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
        Caja testCaja = cajaList.get(cajaList.size() - 1);
        assertThat(testCaja.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testCaja.getSaldoInicial()).isEqualTo(DEFAULT_SALDO_INICIAL);
        assertThat(testCaja.getTotalEfectivo()).isEqualTo(DEFAULT_TOTAL_EFECTIVO);
        assertThat(testCaja.getTotalTarjeta()).isEqualTo(DEFAULT_TOTAL_TARJETA);
        assertThat(testCaja.getSaldoTotal()).isEqualTo(DEFAULT_SALDO_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateCajaWithPatch() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();

        // Update the caja using partial update
        Caja partialUpdatedCaja = new Caja();
        partialUpdatedCaja.setId(caja.getId());

        partialUpdatedCaja
            .fecha(UPDATED_FECHA)
            .saldoInicial(UPDATED_SALDO_INICIAL)
            .totalEfectivo(UPDATED_TOTAL_EFECTIVO)
            .totalTarjeta(UPDATED_TOTAL_TARJETA)
            .saldoTotal(UPDATED_SALDO_TOTAL);

        restCajaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaja.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaja))
            )
            .andExpect(status().isOk());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
        Caja testCaja = cajaList.get(cajaList.size() - 1);
        assertThat(testCaja.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testCaja.getSaldoInicial()).isEqualTo(UPDATED_SALDO_INICIAL);
        assertThat(testCaja.getTotalEfectivo()).isEqualTo(UPDATED_TOTAL_EFECTIVO);
        assertThat(testCaja.getTotalTarjeta()).isEqualTo(UPDATED_TOTAL_TARJETA);
        assertThat(testCaja.getSaldoTotal()).isEqualTo(UPDATED_SALDO_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cajaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cajaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cajaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cajaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaja() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        int databaseSizeBeforeDelete = cajaRepository.findAll().size();

        // Delete the caja
        restCajaMockMvc
            .perform(delete(ENTITY_API_URL_ID, caja.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

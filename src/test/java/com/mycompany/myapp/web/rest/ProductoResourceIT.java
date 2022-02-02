package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.domain.enumeration.TipoIva;
import com.mycompany.myapp.domain.enumeration.TipoProducto;
import com.mycompany.myapp.repository.ProductoRepository;
import com.mycompany.myapp.service.dto.ProductoDTO;
import com.mycompany.myapp.service.mapper.ProductoMapper;
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
 * Integration tests for the {@link ProductoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Double DEFAULT_PRECIO_BASE = 1D;
    private static final Double UPDATED_PRECIO_BASE = 2D;

    private static final Double DEFAULT_PRECIO_TOTAL = 1D;
    private static final Double UPDATED_PRECIO_TOTAL = 2D;

    private static final TipoIva DEFAULT_TIPO_IVA = TipoIva.A;
    private static final TipoIva UPDATED_TIPO_IVA = TipoIva.B;

    private static final TipoProducto DEFAULT_TIPO_PRODUCTO = TipoProducto.FRUTAS;
    private static final TipoProducto UPDATED_TIPO_PRODUCTO = TipoProducto.VERDURAS;

    private static final String ENTITY_API_URL = "/api/productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductoMockMvc;

    private Producto producto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createEntity(EntityManager em) {
        Producto producto = new Producto()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .cantidad(DEFAULT_CANTIDAD)
            .precioBase(DEFAULT_PRECIO_BASE)
            .precioTotal(DEFAULT_PRECIO_TOTAL)
            .tipoIva(DEFAULT_TIPO_IVA)
            .tipoProducto(DEFAULT_TIPO_PRODUCTO);
        return producto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createUpdatedEntity(EntityManager em) {
        Producto producto = new Producto()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .cantidad(UPDATED_CANTIDAD)
            .precioBase(UPDATED_PRECIO_BASE)
            .precioTotal(UPDATED_PRECIO_TOTAL)
            .tipoIva(UPDATED_TIPO_IVA)
            .tipoProducto(UPDATED_TIPO_PRODUCTO);
        return producto;
    }

    @BeforeEach
    public void initTest() {
        producto = createEntity(em);
    }

    @Test
    @Transactional
    void createProducto() throws Exception {
        int databaseSizeBeforeCreate = productoRepository.findAll().size();
        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);
        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isCreated());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeCreate + 1);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testProducto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProducto.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testProducto.getPrecioBase()).isEqualTo(DEFAULT_PRECIO_BASE);
        assertThat(testProducto.getPrecioTotal()).isEqualTo(DEFAULT_PRECIO_TOTAL);
        assertThat(testProducto.getTipoIva()).isEqualTo(DEFAULT_TIPO_IVA);
        assertThat(testProducto.getTipoProducto()).isEqualTo(DEFAULT_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void createProductoWithExistingId() throws Exception {
        // Create the Producto with an existing ID
        producto.setId(1L);
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        int databaseSizeBeforeCreate = productoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = productoRepository.findAll().size();
        // set the field null
        producto.setCodigo(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = productoRepository.findAll().size();
        // set the field null
        producto.setNombre(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = productoRepository.findAll().size();
        // set the field null
        producto.setCantidad(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioBaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = productoRepository.findAll().size();
        // set the field null
        producto.setPrecioBase(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductos() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].precioBase").value(hasItem(DEFAULT_PRECIO_BASE.doubleValue())))
            .andExpect(jsonPath("$.[*].precioTotal").value(hasItem(DEFAULT_PRECIO_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].tipoIva").value(hasItem(DEFAULT_TIPO_IVA.toString())))
            .andExpect(jsonPath("$.[*].tipoProducto").value(hasItem(DEFAULT_TIPO_PRODUCTO.toString())));
    }

    @Test
    @Transactional
    void getProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get the producto
        restProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, producto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(producto.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.precioBase").value(DEFAULT_PRECIO_BASE.doubleValue()))
            .andExpect(jsonPath("$.precioTotal").value(DEFAULT_PRECIO_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.tipoIva").value(DEFAULT_TIPO_IVA.toString()))
            .andExpect(jsonPath("$.tipoProducto").value(DEFAULT_TIPO_PRODUCTO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProducto() throws Exception {
        // Get the producto
        restProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeUpdate = productoRepository.findAll().size();

        // Update the producto
        Producto updatedProducto = productoRepository.findById(producto.getId()).get();
        // Disconnect from session so that the updates on updatedProducto are not directly saved in db
        em.detach(updatedProducto);
        updatedProducto
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .cantidad(UPDATED_CANTIDAD)
            .precioBase(UPDATED_PRECIO_BASE)
            .precioTotal(UPDATED_PRECIO_TOTAL)
            .tipoIva(UPDATED_TIPO_IVA)
            .tipoProducto(UPDATED_TIPO_PRODUCTO);
        ProductoDTO productoDTO = productoMapper.toDto(updatedProducto);

        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testProducto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProducto.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testProducto.getPrecioBase()).isEqualTo(UPDATED_PRECIO_BASE);
        assertThat(testProducto.getPrecioTotal()).isEqualTo(UPDATED_PRECIO_TOTAL);
        assertThat(testProducto.getTipoIva()).isEqualTo(UPDATED_TIPO_IVA);
        assertThat(testProducto.getTipoProducto()).isEqualTo(UPDATED_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void putNonExistingProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeUpdate = productoRepository.findAll().size();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .precioTotal(UPDATED_PRECIO_TOTAL)
            .tipoProducto(UPDATED_TIPO_PRODUCTO);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testProducto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProducto.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testProducto.getPrecioBase()).isEqualTo(DEFAULT_PRECIO_BASE);
        assertThat(testProducto.getPrecioTotal()).isEqualTo(UPDATED_PRECIO_TOTAL);
        assertThat(testProducto.getTipoIva()).isEqualTo(DEFAULT_TIPO_IVA);
        assertThat(testProducto.getTipoProducto()).isEqualTo(UPDATED_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void fullUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeUpdate = productoRepository.findAll().size();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .cantidad(UPDATED_CANTIDAD)
            .precioBase(UPDATED_PRECIO_BASE)
            .precioTotal(UPDATED_PRECIO_TOTAL)
            .tipoIva(UPDATED_TIPO_IVA)
            .tipoProducto(UPDATED_TIPO_PRODUCTO);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testProducto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProducto.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testProducto.getPrecioBase()).isEqualTo(UPDATED_PRECIO_BASE);
        assertThat(testProducto.getPrecioTotal()).isEqualTo(UPDATED_PRECIO_TOTAL);
        assertThat(testProducto.getTipoIva()).isEqualTo(UPDATED_TIPO_IVA);
        assertThat(testProducto.getTipoProducto()).isEqualTo(UPDATED_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void patchNonExistingProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeDelete = productoRepository.findAll().size();

        // Delete the producto
        restProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, producto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

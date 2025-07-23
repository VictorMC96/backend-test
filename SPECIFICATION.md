# Especificación Técnica - Sistema de Gestión de Estacionamiento

## 📋 Resumen Ejecutivo

Sistema Spring Boot para gestionar el acceso y cobro de vehículos en un estacionamiento de pago con diferentes tipos de usuarios: oficiales, residentes y no residentes.

## 🏗️ Arquitectura del Sistema

### Estructura de Packages Refactorizada

```
com.neology.estacionamiento/
├── application/
│   ├── dto/
│   ├── service/
│   └── usecase/
├── domain/
│   ├── model/
│   ├── repository/
│   └── service/
├── infrastructure/
│   ├── persistence/
│   ├── configuration/
│   └── exception/
└── web/
    ├── controller/
    ├── request/
    └── response/
```

## 🎯 Casos de Uso y APIs REST

### 1. Gestión de Vehículos

| Método | Endpoint | Descripción | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/vehiculos/oficial` | Alta vehículo oficial | `{placa: string}` |
| POST | `/api/vehiculos/residente` | Alta vehículo residente | `{placa: string}` |
| GET | `/api/vehiculos/{placa}` | Consultar vehículo | - |
| GET | `/api/vehiculos/tipo/{tipo}` | Listar por tipo | - |

### 2. Control de Acceso

| Método | Endpoint | Descripción | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/estacionamiento/entrada` | Registrar entrada | `{placa: string}` |
| POST | `/api/estacionamiento/salida` | Registrar salida | `{placa: string}` |
| GET | `/api/estacionamiento/activos` | Vehículos en estacionamiento | - |

### 3. Facturación y Reportes

| Método | Endpoint | Descripción | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/facturacion/nuevo-mes` | Iniciar nuevo mes | - |
| GET | `/api/facturacion/residentes` | Reporte pagos residentes | - |
| GET | `/api/facturacion/residente/{placa}` | Deuda específica | - |

### 4. Consultas y Estadísticas

| Método | Endpoint | Descripción | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/reportes/estancias/{placa}` | Historial estancias | - |
| GET | `/api/reportes/ingresos/{fecha}` | Ingresos del día | - |

## 🗂️ Modelo de Datos

### Entidades JPA

#### 1. Vehiculo
```java
@Entity
@Table(name = "vehiculos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_vehiculo")
public abstract class Vehiculo {
    @Id
    private String placa;
    
    @Enumerated(EnumType.STRING)
    private TipoVehiculo tipo;
    
    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta;
    
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL)
    private List<Estancia> estancias;
    
    // Método abstracto para cálculo de tarifa
    public abstract BigDecimal calcularImporte(int minutos);
}
```

#### 2. VehiculoOficial
```java
@Entity
@DiscriminatorValue("OFICIAL")
public class VehiculoOficial extends Vehiculo {
    @Override
    public BigDecimal calcularImporte(int minutos) {
        return BigDecimal.ZERO; // No pagan
    }
}
```

#### 3. VehiculoResidente  
```java
@Entity
@DiscriminatorValue("RESIDENTE")
public class VehiculoResidente extends Vehiculo {
    
    @Column(name = "tiempo_acumulado_mes")
    private int tiempoAcumuladoMes = 0;
    
    private static final BigDecimal TARIFA_POR_MINUTO = new BigDecimal("0.05");
    
    @Override
    public BigDecimal calcularImporte(int minutos) {
        return TARIFA_POR_MINUTO.multiply(new BigDecimal(minutos));
    }
}
```

#### 4. VehiculoNoResidente
```java
@Entity
@DiscriminatorValue("NO_RESIDENTE")
public class VehiculoNoResidente extends Vehiculo {
    
    private static final BigDecimal TARIFA_POR_MINUTO = new BigDecimal("0.50");
    
    @Override
    public BigDecimal calcularImporte(int minutos) {
        return TARIFA_POR_MINUTO.multiply(new BigDecimal(minutos));
    }
}
```

#### 5. Estancia
```java
@Entity
@Table(name = "estancias")
public class Estancia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placa_vehiculo")
    private Vehiculo vehiculo;
    
    @Column(name = "fecha_entrada", nullable = false)
    private LocalDateTime fechaEntrada;
    
    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;
    
    @Column(name = "minutos_estancia")
    private Integer minutosEstancia;
    
    @Column(name = "importe_pagado", precision = 10, scale = 2)
    private BigDecimal importePagado;
    
    @Enumerated(EnumType.STRING)
    private EstadoEstancia estado;
}
```

## 🔧 Servicios de Dominio

### 1. EstacionamientoService
```java
@Service
public class EstacionamientoService {
    public void registrarEntrada(String placa);
    public BigDecimal registrarSalida(String placa);
    public List<Vehiculo> obtenerVehiculosActivos();
    public void iniciarNuevoMes();
}
```

### 2. VehiculoService  
```java
@Service
public class VehiculoService {
    public VehiculoOficial registrarVehiculoOficial(String placa);
    public VehiculoResidente registrarVehiculoResidente(String placa);
    public Optional<Vehiculo> buscarPorPlaca(String placa);
    public List<Vehiculo> buscarPorTipo(TipoVehiculo tipo);
}
```

### 3. FacturacionService
```java
@Service
public class FacturacionService {
    public ReporteResidentes generarReporteResidentes();
    public BigDecimal calcularDeudaResidente(String placa);
    public void limpiarDatosInicioMes();
}
```

## 🧪 Estrategia de Testing

### Estructura de Tests
```
src/test/java/com/neology/estacionamiento/
├── unit/
│   ├── domain/model/
│   ├── application/service/
│   └── web/controller/
├── integration/
│   ├── repository/
│   └── api/
└── architecture/
    └── ArchitectureTest.java
```

### Tests Unitarios Críticos

#### 1. VehiculoTest
```java
@ExtendWith(MockitoExtension.class)
class VehiculoTest {
    
    @Test
    void vehiculoOficial_noPagaImporte() {
        VehiculoOficial oficial = new VehiculoOficial();
        oficial.setPlaca("GOV123");
        
        BigDecimal importe = oficial.calcularImporte(120);
        
        assertThat(importe).isEqualTo(BigDecimal.ZERO);
    }
    
    @Test
    void vehiculoResidente_calculaTarifaCorrecta() {
        VehiculoResidente residente = new VehiculoResidente();
        residente.setPlaca("RES456");
        
        BigDecimal importe = residente.calcularImporte(100); // 100 min
        
        assertThat(importe).isEqualTo(new BigDecimal("5.00"));
    }
    
    @Test
    void vehiculoNoResidente_calculaTarifaCorrecta() {
        VehiculoNoResidente noResidente = new VehiculoNoResidente();
        noResidente.setPlaca("VIS789");
        
        BigDecimal importe = noResidente.calcularImporte(60); // 60 min
        
        assertThat(importe).isEqualTo(new BigDecimal("30.00"));
    }
}
```

#### 2. EstacionamientoServiceTest
```java
@ExtendWith(MockitoExtension.class)
class EstacionamientoServiceTest {
    
    @Mock
    private VehiculoRepository vehiculoRepository;
    
    @Mock
    private EstanciaRepository estanciaRepository;
    
    @InjectMocks
    private EstacionamientoService estacionamientoService;
    
    @Test
    void registrarEntrada_vehiculoExistente_creaEstancia() {
        // Given
        String placa = "ABC123";
        VehiculoOficial vehiculo = new VehiculoOficial();
        vehiculo.setPlaca(placa);
        when(vehiculoRepository.findByPlaca(placa)).thenReturn(Optional.of(vehiculo));
        
        // When
        estacionamientoService.registrarEntrada(placa);
        
        // Then
        ArgumentCaptor<Estancia> estanciaCaptor = ArgumentCaptor.forClass(Estancia.class);
        verify(estanciaRepository).save(estanciaCaptor.capture());
        
        Estancia estancia = estanciaCaptor.getValue();
        assertThat(estancia.getVehiculo()).isEqualTo(vehiculo);
        assertThat(estancia.getFechaEntrada()).isNotNull();
        assertThat(estancia.getEstado()).isEqualTo(EstadoEstancia.ACTIVA);
    }
    
    @Test
    void registrarSalida_vehiculoNoResidente_calculaImporteCorrectamente() {
        // Given
        String placa = "VIS123";
        VehiculoNoResidente vehiculo = new VehiculoNoResidente();
        vehiculo.setPlaca(placa);
        
        Estancia estanciaActiva = new Estancia();
        estanciaActiva.setVehiculo(vehiculo);
        estanciaActiva.setFechaEntrada(LocalDateTime.now().minusHours(2)); // 2 horas = 120 min
        estanciaActiva.setEstado(EstadoEstancia.ACTIVA);
        
        when(vehiculoRepository.findByPlaca(placa)).thenReturn(Optional.of(vehiculo));
        when(estanciaRepository.findByVehiculoPlacaAndEstado(placa, EstadoEstancia.ACTIVA))
            .thenReturn(Optional.of(estanciaActiva));
        
        // When
        BigDecimal importe = estacionamientoService.registrarSalida(placa);
        
        // Then
        assertThat(importe).isEqualTo(new BigDecimal("60.00")); // 120 min * 0.50
        
        ArgumentCaptor<Estancia> estanciaCaptor = ArgumentCaptor.forClass(Estancia.class);
        verify(estanciaRepository).save(estanciaCaptor.capture());
        
        Estancia estanciaGuardada = estanciaCaptor.getValue();
        assertThat(estanciaGuardada.getEstado()).isEqualTo(EstadoEstancia.FINALIZADA);
        assertThat(estanciaGuardada.getFechaSalida()).isNotNull();
        assertThat(estanciaGuardada.getImportePagado()).isEqualTo(new BigDecimal("60.00"));
    }
}
```

#### 3. EstacionamientoControllerTest
```java
@WebMvcTest(EstacionamientoController.class)
class EstacionamientoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EstacionamientoService estacionamientoService;
    
    @Test
    void registrarEntrada_vehiculoValido_retorna200() throws Exception {
        // Given
        EntradaRequest request = new EntradaRequest();
        request.setPlaca("ABC123");
        
        // When & Then
        mockMvc.perform(post("/api/estacionamiento/entrada")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Entrada registrada exitosamente"))
                .andExpect(jsonPath("$.placa").value("ABC123"));
        
        verify(estacionamientoService).registrarEntrada("ABC123");
    }
    
    @Test
    void registrarSalida_vehiculoNoResidente_retornaImporte() throws Exception {
        // Given
        SalidaRequest request = new SalidaRequest();
        request.setPlaca("VIS123");
        when(estacionamientoService.registrarSalida("VIS123"))
            .thenReturn(new BigDecimal("25.50"));
        
        // When & Then
        mockMvc.perform(post("/api/estacionamiento/salida")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa").value("VIS123"))
                .andExpected(jsonPath("$.importeAPagar").value(25.50))
                .andExpect(jsonPath("$.tipoVehiculo").value("NO_RESIDENTE"));
    }
}
```

### Tests de Integración

#### EstacionamientoIntegrationTest
```java
@SpringBootTest
@Testcontainers
class EstacionamientoIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");
    
    @Autowired
    private EstacionamientoService estacionamientoService;
    
    @Autowired
    private VehiculoService vehiculoService;
    
    @Test
    void flujoCompleto_vehiculoNoResidente() {
        // Given - Registrar entrada
        String placa = "TEST123";
        estacionamientoService.registrarEntrada(placa);
        
        // When - Registrar salida después de tiempo
        BigDecimal importe = estacionamientoService.registrarSalida(placa);
        
        // Then
        assertThat(importe).isGreaterThan(BigDecimal.ZERO);
    }
}
```

## 📊 Diagramas Conceptuales

### Diagrama de Clases (Simplificado)
```mermaid
classDiagram
    class Vehiculo {
        <<abstract>>
        -String placa
        -TipoVehiculo tipo
        -LocalDateTime fechaAlta
        -List~Estancia~ estancias
        +calcularImporte(int minutos)* BigDecimal
    }
    
    class VehiculoOficial {
        +calcularImporte(int minutos) BigDecimal
    }
    
    class VehiculoResidente {
        -int tiempoAcumuladoMes
        +calcularImporte(int minutos) BigDecimal
    }
    
    class VehiculoNoResidente {
        +calcularImporte(int minutos) BigDecimal
    }
    
    class Estancia {
        -Long id
        -LocalDateTime fechaEntrada
        -LocalDateTime fechaSalida
        -Integer minutosEstancia
        -BigDecimal importePagado
        -EstadoEstancia estado
    }
    
    Vehiculo <|-- VehiculoOficial
    Vehiculo <|-- VehiculoResidente
    Vehiculo <|-- VehiculoNoResidente
    Vehiculo ||--o{ Estancia
```

## 🔄 Plan de Refactorización

### Fase 1: Reestructuración Base (1 hora)
1. ✅ Refactorizar packages según arquitectura hexagonal
2. ✅ Crear entidades JPA base (Vehiculo, Estancia)
3. ✅ Implementar repositorios básicos
4. ✅ Configurar base de datos H2/PostgreSQL

### Fase 2: Lógica de Negocio (1.5 horas)
1. ✅ Implementar jerarquía de vehículos
2. ✅ Desarrollar servicios de dominio
3. ✅ Implementar casos de uso principales
4. ✅ Cálculos de tarifas y tiempo

### Fase 3: APIs y Testing (0.5 horas)
1. ✅ Crear controladores REST
2. ✅ Implementar DTOs de request/response
3. ✅ Tests unitarios críticos
4. ✅ Validación y manejo de errores

## 🛡️ Consideraciones de Calidad

### Principios SOLID Aplicados
- **SRP**: Cada clase tiene una sola responsabilidad
- **OCP**: Extensible para nuevos tipos de vehículos  
- **LSP**: Subtipos de Vehiculo son intercambiables
- **ISP**: Interfaces específicas por funcionalidad
- **DIP**: Dependencias hacia abstracciones

### Patrones Utilizados
- **Strategy**: Para cálculo de tarifas por tipo
- **Factory**: Para creación de vehículos
- **Repository**: Para abstracción de persistencia
- **DTO**: Para transferencia de datos

### Métricas de Calidad Esperadas
- **Cobertura de tests**: > 80%
- **Complejidad ciclomática**: < 10 por método
- **Deuda técnica**: < 5%
- **Duplicación de código**: < 3%

## 🚀 Comandos de Ejecución

```bash
# Ejecutar aplicación
mvn spring-boot:run

# Ejecutar tests
mvn test

# Generar reporte de cobertura
mvn jacoco:report

# Análisis de calidad de código
mvn sonar:sonar

# Generar documentación API
mvn spring-boot:run & curl http://localhost:8080/v3/api-docs
```

---

**Esta especificación garantiza un código limpio, testeable y extensible que cumple con los requerimientos del ejercicio técnico.** 
# Spring_Security_Thymeleaf

El proyecto simula el proceso de Generación de Boletas para una Ferretería. Los usuarios, al iniciar sesión (o crear una cuenta), son asignados a diferentes roles según su posición en la empresa. Los empleados estándar pueden generar boletas al seleccionar los productos con sus respectivas cantidades, lo que culmina en la creación automática de un archivo PDF listo para imprimir.

Por otro lado, los usuarios con el rol de ADMIN disponen de privilegios adicionales. Tienen acceso al manejo completo de los datos de los productos, lo que incluye operaciones de Crear, Leer, Actualizar y Eliminar (CRUD), permitiendo editar, agregar y eliminar productos de la base de datos de la ferretería.

Este sistema ofrece una solución integral que agiliza la generación de boletas para los empleados y facilita la gestión eficiente de los productos para los administradores, optimizando así las operaciones diarias de la ferretería.

## Instalación y Configuración

Para ejecutar el proyecto localmente, necesitarás tener instalado lo siguiente:

1. **Java Development Kit (JDK)** - Versión 17.
2. **Maven** - Herramienta de gestión de proyectos Java.

El proyecto hace uso de las siguientes herramientas y librerías:

- Spring Boot 3.2.0
- Thymeleaf
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Web
- iText 7 Core
- MySQL Connector/J
- Lombok
- Spring Boot Starter Validation (Hibernate)

## Configuración de la Base de Datos

En el archivo `application.yml`, encontrarás la configuración de la conexión a la base de datos. Puedes modificar los siguientes campos según tus credenciales y configuración específica de tu base de datos:

```yaml
spring:
  datasource:
    url: jdbc:mysql://MYSQLHOST:MYSQLPORT/MYSQLDATABASE
    username: MYSQLUSER
    password: MYSQLPASSWORD
```

## Funcionalidades Principales:

Descripción detallada de las funcionalidades principales del proyecto, incluyendo cómo acceder a ellas y su utilidad.

## Tecnologías Utilizadas:

### Thymeleaf:

Se utiliza Thymeleaf en los archivos HTML para la integración de datos en las vistas:

Archivo: `login.html`

```html
<!-- Enlace a la librería Thymeleaf -->
<html lang="en" xmlns:th="https://www.thymeleaf.org">
  <!-- Uso de Thymeleaf para configurar la acción del formulario -->
  <form class="space-y-4 md:space-y-6" th:action="@{/login}" method="post">
    <!-- Uso de Thymeleaf para manejar atributos y mostrar datos -->
    <label
      for="username"
      class="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
      >Your email</label
    >
    <input
      type="email"
      name="username"
      id="username"
      th:placeholder="name@company.com"
      required
    />
    <!-- ... -->
    <label
      for="password"
      class="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
      >Password</label
    >
    <input
      type="password"
      name="password"
      id="password"
      th:placeholder="••••••••"
      required
    />
    <!-- ... -->
    <button
      type="submit"
      class="w-full text-white bg-primary-600 hover:bg-primary-700 ..."
    >
      Ingresar
    </button>
  </form>
</html>
```

### Hibernate Validator

Se utiliza Hibernate Validator en las entidades para aplicar restricciones de validación:

Archivo: `Customer.java`

```java
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customer")
public class Customer {
    // ... otros atributos
    @Column(name = "email")
    @NotEmpty(message = "El email no puede estar vacio")
    @Email(message = "El email debe ser valido")
    private String email;
    // ... otros atributos
}
```

### Spring Security

Se utiliza Spring Security para configurar la autenticación y la autorización:
Se configura el archivo para definir las reglas de seguridad. En este fragmento de código, se establecen las directivas de autorización con HTTP, permitiendo el acceso a ciertas rutas como `/`, `register`, `/profile`, entre otras. Además, se especifica el formulario de inicio de sesión en `/login`, el procesamiento de la solicitud de inicio de sesión en la misma URL y la redirección a `/profile` tras un inicio de sesión exitoso. Por último, se habilita la funcionalidad de cierre de sesión en `/logout`, con redirección a la página de inicio de sesión.

Archivo: `SpringSecurityConfig.java`

```java
@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {
    // ... otros métodos y configuraciones
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuración de autorización con HTTP
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(httpRequest ->
            // Configuración de rutas y permisos específicos
            httpRequest.requestMatchers("/", "register", "/profile**", "/agregateProduct**",
                    "/newProduct**", "/editProduct**")
                    .permitAll()
                    .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/profile")
                    .permitAll())
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .permitAll());
        return http.build();
    }
    // ... otros métodos
}
```

### Spring Data JPA

Spring Data JPA se utiliza para manejar la persistencia de datos:

Archivo: `Boleta.java`

```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "boleta_id")
    private List<ProductoDetalle> detalles;
    private Double precioTotal;
    // ... otros atributos
}
```

## Spring Boot Starter Web

Spring Boot Starter Web es una dependencia que proporciona características para construir aplicaciones web. Aquí hay un ejemplo de cómo se usa:

Archivo: `CustomerController.java`

```java
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping("/register")
    public String createCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.createCustomer(customer);
        return "redirect:/profile";
    }
    // ... otros métodos del controlador
}
```

## Lombok

Lombok es una biblioteca que reduce la verbosidad del código al generar automáticamente métodos como getters, setters, constructores, etc. Aquí hay un ejemplo de cómo se utiliza:

Archivo: `Customer.java`

```java
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue
    private Long customerId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Transient
    private String password;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserAccount userAccount;
    // ... otros atributos y métodos
}
```

## Estructura del Proyecto:

En la estructura del proyecto, cada directorio contiene archivos relacionados con funcionalidades específicas del sistema, como autenticación (`auth`), gestión de boletas (`boleta`), configuración (`config`), clientes (`customer`), productos (`product`), seguridad (`security`), y archivos de recursos HTML para las vistas en templates. Además, el archivo de configuración principal del proyecto se encuentra en application.yml dentro del directorio resources.

- `src/main/java/com/tecsup/ferreteria/`
  - `auth/`
    - `Authority.java`
    - `Role.java`
    - `RoleRepository.java`
    - `RoleService.java`
    - `RoleServiceImpl.java`
  - `boleta/`
    - `Boleta.java`
    - `BoletaController.java`
    - `BoletaRepository.java`
    - `BoletaService.java`
    - `BoletaServiceImpl.java`
    - `ProductoDetalle.java`
  - `config/`
    - `SpringSecurityConfig.java`
  - `customer/`
    - `Customer.java`
    - `CustomerController.java`
    - `CustomerRepository.java`
    - `CustomerService.java`
    - `CustomerServiceImpl.java`
  - `product/`
    - `Product.java`
    - `ProductController.java`
    - `ProductDTO.java`
    - `ProductRepository.java`
    - `ProductService.java`
    - `ProductServiceImpl.java`
  - `security/`
    - `AccountService.java`
    - `UserAccount.java`
    - `UserAccountController.java`
    - `UserAccountDetails.java`
    - `UserAccountPasswordEncoder.java`
    - `UserAccountRepository.java`
    - `UserAccountService.java`
  - `SpringSecurityApplication.java` (main)
- `src/main/resources/`
  - `templates/`
    - `agregateProduct.html`
    - `chooseProducts.html`
    - `descargarBoleta.html`
    - `editProduct.html`
    - `login.html`
    - `products.html`
    - `profile.html`
    - `register.html`
  - `application.yml`

## Capturas de Pantalla:

### Menú de Login

[![Menú de Login](https://i.postimg.cc/7L2zy1yb/login.png)](https://postimg.cc/f3Myc9yh)

### Menú de Register

[![Menú de Register](https://i.postimg.cc/qMvC7HdP/register.png)](https://postimg.cc/3WzwL6N1)

### Menú Inicial de USER

[![Menú Inicial de USER](https://i.postimg.cc/RhX6TXwp/home.png)](https://postimg.cc/zHHGzwtT)

### Menú Inicial buscando Producto

[![Menú Inicial buscando Producto](https://i.postimg.cc/0j8KmLKJ/home-buscando.png)](https://postimg.cc/zbcfY2jJ)

### Menú para Escoger los productos a Comprar

[![Menú para Escoger los productos a Comprar](https://i.postimg.cc/PxnwfLty/choose-Product.png)](https://postimg.cc/GBz2qmTT)

### Menú para Confirmar Venta y Generar Boleta

[![Menú para Confirmar Venta y Generar Boleta](https://i.postimg.cc/DwDGDjHt/descargando-Boleta.png)](https://postimg.cc/3kXW0BKB)

### PDF Resultante de Boleta

[![PDF Resultante de Boleta](https://i.postimg.cc/xT6MGTRj/boleta-PDF.png)](https://postimg.cc/gn6xmpQ1)

### Menú Inicial de ADMIN

[![Menú Inicial de ADMIN](https://i.postimg.cc/qRcCNkqT/home-ADMIN.png)](https://postimg.cc/JDnhgCj6)

### Menú de Administración de Productos

[![Menú de Administración de Productos](https://i.postimg.cc/L4T1R6Gk/products.png)](https://postimg.cc/ThKwJGdh)

### Menú para Agregar un Nuevo Producto

[![Menú para Agregar un Nuevo Producto](https://i.postimg.cc/tCqFVVFm/add-Product.png)](https://postimg.cc/z3Qysvtn)

### Menú para Editar un Producto

[![Menú para Editar un Producto](https://i.postimg.cc/y8W9L4Qm/edit-Product.png)](https://postimg.cc/TKMpPSX1)

## Licencia:

Este proyecto está licenciado bajo [Creative Commons Atribución-NoComercial-CompartirIgual 4.0 Internacional](http://creativecommons.org/licenses/by-nc-sa/4.0/):

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">
  <img alt="Licencia Creative Commons" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" />
</a>

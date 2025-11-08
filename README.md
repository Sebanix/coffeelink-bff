# ☕ CoffeeLink - BFF (El Perro de Seguridad - guau guau)

Este es el segundo servicio de CoffeeLink.

El objetivo de este servicio es separar la lógica de negocio de todo lo relacionado con la seguridad.

## ¿Qué hace este servicio?

* **Maneja el Login:** Es el único que sabe cómo validar usuarios (`/api/login`).
* **Crea los "Pases de Acceso" (JWT):** Cuando un usuario inicia sesión, este servicio le da un token (JWT) para demostrar quién es.
* **Revisa los Pases:** Implementé un "Filtro" que revisa el token en cada petición. Si no tienes un token, o no es válido, no te deja pasar.
* **Protege las Rutas por Rol:** Aquí es donde defino que solo los "admin" pueden crear productos y solo los "cliente" pueden comprar.


Toperdito implementar Spring Security:
1.  Que el `SecurityConfig` le diera permiso a `/api/login`.
2.  Que el `AuthController` usara el `PasswordEncoder` para revisar el hash.
3.  Que el `JwtService` creara el token.

Chato jsjsjs

## Tecnologías
* Java + Spring Boot
* Spring Security
* JWT (JSON Web Tokens)
* RestTemplate (enlazarlo con el otro (servicio) proyecto java)
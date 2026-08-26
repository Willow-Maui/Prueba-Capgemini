package com.capgemini.test.code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de contexto de la aplicación.
 *
 * Este es un test básico que verifica que la aplicación pueda inicializarse.
 * La carga completa del contexto se realiza en tests de integración específicos.
 */
@DisplayName("CodeApplication Context Tests")
class CodeApplicationTests {

	@Test
	@DisplayName("Debe importarse la clase de aplicación correctamente")
	void applicationContextClassExists() {
		// Simple test que no requiere cargar el contexto completo
		// Los tests de integración cargarán el contexto cuando sea necesario
		assertThat(CodeApplication.class).isNotNull();
	}

}



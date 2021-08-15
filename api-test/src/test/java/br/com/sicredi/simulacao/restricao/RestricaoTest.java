package br.com.sicredi.simulacao.restricao;

import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestricaoTest {
	private String uri      = "http://localhost:8080/api/v1/";
	private String endpoint = "restricoes/";
	
	@Test
	public void testGetRestricaoCpfSemRestricao()
	{

		//cpf que será validado
		String cpf = "180750417";
		
		//Buscar restrição
		given()
		.when()
			.get(uri + endpoint +cpf)
		.then()
			.statusCode(204);	
	}
		
	@Test
	public void testGetRestricaoCpfComRestricao()
	{
		/*
	    CPFs com restrição:
		97093236014
		60094146012
		84809766080
		62648716050
		26276298085
		01317496094
		55856777050
		19626829001
		24094592008
		58063164083
		 */
		
		//cpf que será validado
		String cpf = "97093236014";
		
		//Buscar restrição
		given()
		.when()
			.get(uri + endpoint +cpf)
		.then()
			.statusCode(200)
			.body("mensagem", is("O CPF " + cpf + " possui restrição"));
					
				
	}
}

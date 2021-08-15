package br.com.sicredi.simulacao.simulacao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AlterarSimulacaoTest {

	private static String uri            = "http://localhost:8080/api/v1/";
	private static String endpoint       = "simulacoes/";
	private static String cpf            = "748456782";
	private static String cpfAlterar     = "234911121";
	private static String cpfInexistente = "446551687";
	
	@BeforeClass
	public static void criarSimulacoes()
	{
		//body da criacao
		String bodyCreateS1 = "{\n"
				+ "  \"nome\": \"José Fulano\",\n"
				+ "  \"cpf\": " + cpf + ",\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao para alteracoes
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreateS1)
		.when()
			.post(uri + endpoint)
		.then();
		
		//body da criacao
		String bodyCreateS2 = "{\n"
				+ "  \"nome\": \"José Siclano\",\n"
				+ "  \"cpf\": " + cpfAlterar + ",\n"
				+ "  \"email\": \"jsc@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao para testar cpf duplicado
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreateS2)
		.when()
			.post(uri + endpoint)
		.then();
	}
	
	@Test
	public void alterarDadosdaSimulacao()
	{
		
		//body da alteracao
		String bodyUpdate = "{\n"
				+ "  \"nome\": \"José Joseano\",\n"
				+ "  \"email\": \"jta@email.com\",\n"
				+ "  \"valor\": 4500,\n"
				+ "  \"parcelas\": 5,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		given()
			.contentType(ContentType.JSON)
			.body(bodyUpdate)
		.when()
			.put(uri + endpoint + cpf)
		.then()
			.statusCode(200)
			.body("nome", is("José Joseano"))
			.body("cpf", is(cpf))
			.body("email", is("jta@email.com"))
			.body("valor", is(4500))
			.body("parcelas", is(5))
			.body("seguro", is(false));
			
	}
	

	@Test
	public void alterarSimulacaoComEmailInvalido()
	{
		String validacaoEmail = "E-mail deve ser um e-mail válido";
		
		//body da alteracao
		String bodyUpdate = "{\n"
				+ "  \"email\": \"jta@email\",\n"
				+ "  \"parcelas\": 10,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		String erro = 
			given()
				.contentType(ContentType.JSON)
				.body(bodyUpdate)
			.when()
				.put(uri + endpoint + cpf)
			.then()
				.statusCode(400)
				.extract()
					.path("erros.email");
		
		Assert.assertEquals(validacaoEmail, erro);
	}

	@Test
	public void alterarrSimulacaoComCPFEmFormatoErrado()
	{
		//não existe mensagem de erro		
		//body da alteracao
		String bodyUpdate = "{\n"
				+ "  \"cpf\": 745.315.658-43,\n"
				+ "  \"parcelas\": 5,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		given()
			.contentType(ContentType.JSON)
			.body(bodyUpdate)
		.when()
			.put(uri + endpoint + cpf)
		.then()
			.statusCode(400);
		
	}
	
	
	@Test
	public void alterarSimulacaoComValorAbaixoDe1000()
	{
		//não existe mensagem de erro		
		//body da alteracao
		String bodyUpdate = "{\n"
				+ "  \"valor\": 900,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		given()
			.contentType(ContentType.JSON)
			.body(bodyUpdate)
		.when()
			.put(uri + endpoint + cpf)
		.then()
			.statusCode(400);
	}
	
	@Test
	public void alterarSimulacaoComValorAcimaDe40000()
	{
		String validacaoValor = "Valor deve ser menor ou igual a R$ 40.000";
		
		//body da alteracao
		String bodyUpdate = "{\n"
				+ "  \"valor\": 900,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		String erro = 
			given()
				.contentType(ContentType.JSON)
				.body(bodyUpdate)
			.when()
				.put(uri + endpoint + cpf)
			.then()
				.statusCode(400)
				.extract()
					.path("erros.valor");
		
		Assert.assertEquals(validacaoValor, erro);
	}
	
	@Test
	public void alterarSimulacaoComParcelasMenorQue2()
	{
		String validacaoParcelas = "Parcelas deve ser igual ou maior que 2";
		
		//body da alteracao
		String bodyUpdate = "{\n"
				+ "  \"valor\": 900,\n"
				+ "  \"parcelas\": 0,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		String erro = 
			given()
				.contentType(ContentType.JSON)
				.body(bodyUpdate)
			.when()
				.post(uri + endpoint)
			.then()
				.statusCode(400)
				.extract()
					.path("erros.parcelas");
		
		Assert.assertEquals(validacaoParcelas, erro);
	}
	
	@Test
	public void alterarSimulacaoComParcelasMaiorQue48()
	{
		//não existe mensagem de erro
		//body da alteracao
		String bodyUpdate = "{\n"
				+ "  \"email\": \"jfa@email.com\",\n"
				+ "  \"valor\": 900,\n"
				+ "  \"parcelas\": 50,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//alterar simulacao
			given()
				.contentType(ContentType.JSON)
				.body(bodyUpdate)
			.when()
				.put(uri + endpoint + cpf)
			.then()
				.statusCode(400);
	}
	
	@Test
	public void alterarSimulacaoParaCpfJaExistente()
	{
		String validacaoCpfDuplicado = "CPF duplicado";
		
		//body da alteracao
		String bodyUpdate = "{\n"
				+ "  \"cpf\": " + cpf +",\n"
				+ "  \"email\": \"jta@email.com\",\n"
				+ "  \"valor\": 4500,\n"
				+ "  \"parcelas\": 5,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		String erro = 
				given()
					.contentType(ContentType.JSON)
					.body(bodyUpdate)
				.when()
					.put(uri + endpoint + cpfAlterar)
				.then()
					.statusCode(400)
					.extract()
						.path("mensagem");
			
			Assert.assertEquals(validacaoCpfDuplicado, erro);
	}
	
	@Test
	public void alterarSimulacaoParaCpfInexistente()
	{
		String validacaoCpfInexistente = "CPF " + cpfInexistente + " não encontrado";
		
		//body da alteracao
		String bodyUpdate = "{\n"
				+ "  \"email\": \"jta@email.com\",\n"
				+ "  \"parcelas\": 5,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		String erro = 
				given()
					.contentType(ContentType.JSON)
					.body(bodyUpdate)
				.when()
					.put(uri + endpoint + cpfInexistente)
				.then()
					.statusCode(404)
					.extract()
						.path("mensagem");
			
			Assert.assertEquals(validacaoCpfInexistente, erro);
	}
}

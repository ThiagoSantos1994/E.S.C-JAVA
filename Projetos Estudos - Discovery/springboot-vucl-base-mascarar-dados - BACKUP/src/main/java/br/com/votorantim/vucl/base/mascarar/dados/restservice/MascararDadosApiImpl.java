package br.com.votorantim.vucl.base.mascarar.dados.restservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.votorantim.vucl.base.mascarar.dados.model.DadosRequest;
import br.com.votorantim.vucl.base.mascarar.dados.model.DadosResponse;
import br.com.votorantim.vucl.base.mascarar.dados.processor.MascararDados;

@RestController
@RequestMapping("/api")
public class MascararDadosApiImpl {

	MascararDados dados = new MascararDados();

//	@PostMapping("/mascararDados")
//	public @ResponseBody ArrayList<DadosResponse> mascararDados(@RequestBody String json) {
//		
//		TypeToken tt = new TypeToken<ArrayList<DadosRequest>>() {};
//		Gson gson = new Gson();
//
//		ArrayList<DadosRequest> listJson = gson.fromJson(json, tt.getType());
//		return dados.MascararDados(listJson);
//	}
	
//	@Autowired
//	MascararDados dados;
	
	@PostMapping("/mascararDados")
	public @ResponseBody ArrayList<DadosResponse> mascararDados(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException {
		List<DadosRequest> requests = fromJSON(json);
		return dados.MascararDados(requests);
	}

	public static List<DadosRequest> fromJSON(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		DadosRequest[] array = mapper.readValue(json, DadosRequest[].class);
		List<DadosRequest> lista = Arrays.asList(array);
		return lista;
	} 
}

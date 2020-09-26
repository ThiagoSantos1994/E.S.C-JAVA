package br.esc.software.commons;

import static br.esc.software.commons.GlobalUtils.LogInfo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectParser {

	private Gson gson = null;
	
	public ObjectParser() {
		GsonBuilder builder = new GsonBuilder();
		gson = builder.create();
	}
	
	public String parser(Object origem) {
		
		LogInfo("Realizando a conversão do objeto para JSON");
		
		String parser = gson.toJson(origem);
		
		LogInfo("<<INICIO>> Refatorando parserResponse (Retirando acentos)");
		
		ByteBuffer buffer = StandardCharsets.UTF_8.encode(parser); 
		 
		String utf8String = StandardCharsets.UTF_8.decode(buffer).toString();
		
		LogInfo("<<FIM>> Parser refatorado com sucesso");
		
		return utf8String;
	}

}

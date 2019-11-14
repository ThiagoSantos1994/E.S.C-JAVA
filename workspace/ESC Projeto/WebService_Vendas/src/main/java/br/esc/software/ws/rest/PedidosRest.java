package br.esc.software.ws.rest;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.esc.software.exception.InsertException;
import br.esc.software.model.DetalhesPedido;
import br.esc.software.model.Pedido;
import br.esc.software.processor.PedidosBusiness;

@RestController
public class PedidosRest {
	/**
	 * @author Thiago Santos
	 * @since 09/2019
	 * @category WebService JSON
	 * 
	 * Orquestrador serviço Vendas -> Retorno JSON (REST)
	 * 
	 */
	
	@Autowired
	public PedidosBusiness pedidosBusiness;

	@GetMapping("/pedidos/{idPedido}")
	public @ResponseBody ArrayList<Pedido> pedidosPorPedidoId(@PathVariable("idPedido") int idPedido) {
		return pedidosBusiness.getPedido(idPedido);
	}
	
	@GetMapping("/pedidos/clientes/{idCliente}")
	public ArrayList<Pedido> buscarPedidosPorClienteID(@PathVariable("idCliente") int idCliente) {
		return pedidosBusiness.getPedidoCliente(idCliente);
	}
	
	@GetMapping("/pedidos/detalhes/{idPedido}")
	public ArrayList<DetalhesPedido> buscarDetalhesPedidos(@PathVariable("idPedido") int idPedido){
		return pedidosBusiness.getDetalhesPedido(idPedido);
	}
	
	@PostMapping("/pedidos/{nomeDoCliente}")
	public Boolean save(@PathVariable("nomeDoCliente") String nomeDoCliente) throws InsertException {
		return pedidosBusiness.salvarPedidos(nomeDoCliente);
	}

	@DeleteMapping("/pedidos/{idPedido}")
	public Boolean delete(@PathVariable("idPedido") int idPedido) {
		return pedidosBusiness.excluirPedidos(idPedido);
	}

	@PutMapping("/pedidos/{idPedido}/{nomeDoCliente}")
	public Boolean update(@PathVariable("idPedido") int idPedido, @PathVariable("nomeDoCliente") String nomeDoCliente) {
		return pedidosBusiness.atualizarPedidos(idPedido, nomeDoCliente);
	}
	
	
}

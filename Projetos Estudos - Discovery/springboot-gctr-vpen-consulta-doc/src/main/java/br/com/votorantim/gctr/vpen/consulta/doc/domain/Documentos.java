/**
 * 
 */
package br.com.votorantim.gctr.vpen.consulta.doc.domain;

/**
 * @author resource.mlima
 *
 */
public class Documentos {

	private Integer codigoDocumento = null;

	private String nomeDocumento = null;

	private String indicadorObrigatoriedade = null;

	private String indicadorAtivo = null;

	/**
	 * @return the codigoDocumento
	 */
	public Integer getCodigoDocumento() {
		return codigoDocumento;
	}

	/**
	 * @param codigoDocumento the codigoDocumento to set
	 */
	public void setCodigoDocumento(Integer codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	/**
	 * @return the nomeDocumento
	 */
	public String getNomeDocumento() {
		return nomeDocumento;
	}

	/**
	 * @param nomeDocumento the nomeDocumento to set
	 */
	public void setNomeDocumento(String nomeDocumento) {
		this.nomeDocumento = nomeDocumento;
	}

	/**
	 * @return the indicadorObrigatoriedade
	 */
	public String getIndicadorObrigatoriedade() {
		return indicadorObrigatoriedade;
	}

	/**
	 * @param indicadorObrigatoriedade the indicadorObrigatoriedade to set
	 */
	public void setIndicadorObrigatoriedade(String indicadorObrigatoriedade) {
		this.indicadorObrigatoriedade = indicadorObrigatoriedade;
	}

	/**
	 * @return the indicadorAtivo
	 */
	public String getIndicadorAtivo() {
		return indicadorAtivo;
	}

	/**
	 * @param indicadorAtivo the indicadorAtivo to set
	 */
	public void setIndicadorAtivo(String indicadorAtivo) {
		this.indicadorAtivo = indicadorAtivo;
	}

}
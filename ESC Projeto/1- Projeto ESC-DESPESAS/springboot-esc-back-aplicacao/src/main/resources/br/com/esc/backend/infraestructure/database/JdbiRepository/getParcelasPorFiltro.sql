SELECT
	id_DespesaParcelada,
	id_Parcelas,
	nr_Parcela,
	ds_dataVencimento,
	ds_Observacoes,
	tp_Baixado,
	id_DetalheDespesa,
	id_Despesa,
	tp_Quitado,
	id_Funcionario,
	tp_ParcelaAdiada,
	tp_ParcelaAmortizada,
	vl_Parcela,
	vl_Desconto
FROM
	tbd_Parcelas
WHERE
    (:idDespesaParcelada IS NULL OR id_DespesaParcelada = :idDespesaParcelada)
    AND (:idParcela IS NULL OR id_Parcelas = :idParcela)
    AND (:tpBaixado IS NULL OR tp_Baixado = :tpBaixado)
    AND id_Funcionario = :idFuncionario
ORDER BY
    id_Parcelas
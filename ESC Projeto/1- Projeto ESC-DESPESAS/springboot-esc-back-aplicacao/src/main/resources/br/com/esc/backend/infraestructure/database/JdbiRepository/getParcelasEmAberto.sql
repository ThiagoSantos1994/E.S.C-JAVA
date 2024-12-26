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
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Parcelas >= (SELECT MAX(id_Parcelas) FROM tbd_Parcelas WHERE id_DespesaParcelada = :idDespesaParcelada AND tp_Baixado = 'S' AND id_Funcionario = :idFuncionario)
    AND id_Funcionario = :idFuncionario
ORDER BY
    id_Parcelas
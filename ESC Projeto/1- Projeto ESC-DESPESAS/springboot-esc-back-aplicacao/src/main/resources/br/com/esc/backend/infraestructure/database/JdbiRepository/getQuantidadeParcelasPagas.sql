SELECT
    COUNT(nr_Parcela)
FROM
	tbd_Parcelas
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND tp_Baixado = 'S'
    AND tp_ParcelaAdiada = 'N'
    AND id_Funcionario = :idFuncionario
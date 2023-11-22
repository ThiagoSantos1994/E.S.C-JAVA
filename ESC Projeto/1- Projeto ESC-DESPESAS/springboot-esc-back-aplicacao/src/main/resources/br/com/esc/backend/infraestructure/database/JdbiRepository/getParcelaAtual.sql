SELECT TOP 1
    nr_Parcela
FROM
	tbd_Parcelas
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND tp_Baixado = 'N'
    AND tp_ParcelaAdiada = 'N'
    AND id_Funcionario = :idFuncionario


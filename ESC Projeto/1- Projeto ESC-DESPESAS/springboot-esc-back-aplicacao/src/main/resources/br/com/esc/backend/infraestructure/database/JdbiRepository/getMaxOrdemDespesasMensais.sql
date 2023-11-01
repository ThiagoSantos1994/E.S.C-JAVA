SELECT
	ISNULL(MAX(id_OrdemExibicao),0) + 1
FROM
    tbd_DespesaMensal
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario